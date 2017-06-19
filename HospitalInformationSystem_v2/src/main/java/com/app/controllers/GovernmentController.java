package com.app.controllers;

import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Examination;
import com.app.model.Operation;
import com.app.service.ExaminationService;
import com.app.service.OperationService;

@RestController
@RequestMapping(value = "government")

public class GovernmentController {
	
	private final String KEYSTORE_PATH = "./keystores/government_server_keystore.keystore";  
	private final String KEYSTORE_PASS = "govgov";
	private final String SECRET = "Neka nasa strasna tajna :D";
	
	@Autowired
	OperationService operationService;
	
	@Autowired
	ExaminationService examinationService;
	
	@PreAuthorize("permitAll()")
	@RequestMapping(value="/operations", method = RequestMethod.GET)
	public ResponseEntity<List<Operation>> get(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			HttpServletRequest request) throws Exception {
		
		KeyPair kp = getKeyPair();
		PublicKey publicKey = kp.getPublic();
		
		String signatureAsString = request.getHeader("X-Signature");
		byte[] signature = Base64.decodeBase64(signatureAsString);
		boolean valid = verify(SECRET.getBytes(), signature, publicKey);

		if (!valid)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if (name == null)
			name = "%"; //any name
		else
			name = "%" + name + "%";
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 
		
		List<Operation> retVal = new ArrayList<>();
		
		//get all by name (if name is null, return all)
		if (startDate == null && endDate == null)  
			retVal = operationService.findByName(name);
		
		//get all by date between
		else if (startDate != null && endDate != null) 
			try {
				retVal = operationService.findByDate(dt.parse(startDate), dt.parse(endDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		//get all after date
		else if (startDate != null && endDate == null) 
			try {
				retVal = operationService.findByDateAfter(dt.parse(startDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		//get all before date
		else if (startDate == null && endDate != null) 
			try {
				retVal = operationService.findByDateBefore(dt.parse(endDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		return new ResponseEntity<List<Operation>>(retVal, HttpStatus.OK);
	}
	
	@PreAuthorize("permitAll()")
	@RequestMapping(value="/examinations", method = RequestMethod.GET)
	public ResponseEntity<List<Examination>> getExaminations(
			@RequestParam(value = "diagnosis", required = false) String diagnosis,
			HttpServletRequest request) throws Exception {
		
		KeyPair kp = getKeyPair();
		PublicKey publicKey = kp.getPublic();
		
		String signatureAsString = request.getHeader("X-Signature");
		byte[] signature = Base64.decodeBase64(signatureAsString);
		boolean valid = verify(SECRET.getBytes(), signature, publicKey);

		if (!valid)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		List<Examination> retVal = new ArrayList<>();
		
		if (diagnosis == null || diagnosis.isEmpty())
			retVal = examinationService.findAll();
		else
			retVal = examinationService.findByDiagnosis(diagnosis);
		
		
		return new ResponseEntity<List<Examination>>(retVal, HttpStatus.OK);
	}
	
	
	public KeyPair getKeyPair() throws Exception {
	    FileInputStream is = new FileInputStream(KEYSTORE_PATH);

	    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
	    keystore.load(is, KEYSTORE_PASS.toCharArray());

	    String alias = "gov_serv";

	    Key key = keystore.getKey(alias, KEYSTORE_PASS.toCharArray());
	    
	    if (key instanceof PrivateKey) {
	      // Get certificate of public key
	      Certificate cert = keystore.getCertificate(alias);

	      // Get public key
	      PublicKey publicKey = cert.getPublicKey();

	      // Return a key pair
	      return new KeyPair(publicKey, (PrivateKey) key);
	    }
	    return null;
	  }
	
	private boolean verify(byte[] data, byte[] signature, PublicKey publicKey) {
		try {
			Signature sig = Signature.getInstance("SHA1withRSA");
			sig.initVerify(publicKey);
			sig.update(data);
			return sig.verify(signature);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return false;
	}
	

}
