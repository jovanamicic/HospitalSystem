package com.app.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.PaymentDTO;
import com.app.model.Payment;
import com.app.model.Person;
import com.app.security.TokenUtils;
import com.app.service.PaymentService;
import com.app.service.PersonService;

@RestController
@RequestMapping(value = "payments")
public class PaymentController {

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_NUMBER = 0;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PersonService personService;

	@Autowired
	private TokenUtils tokenUtils;

	/**
	 * Function saves new payment in database.
	 * 
	 * @param token
	 * @param dto
	 * @return
	 */
	@PreAuthorize("hasAuthority('Submit_payment')")
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Void> savePayment(@RequestHeader("X-Auth-Token") String token, @RequestBody PaymentDTO dto) {

		if (dto == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		String username = tokenUtils.getUsernameFromToken(token);
		Person manager = personService.findByUsername(username);

		Payment p = new Payment();
		p.setAccount(dto.getAccount());
		p.setAmount(dto.getAmount());
		p.setCurrency(dto.getCurrency());
		p.setRecipient(dto.getRecipient());
		p.setDate(new Date());
		p.setManager(manager);
		p = paymentService.save(p);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * Function returns all payments pageable.
	 * @param token
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_all_payments')")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Page<Payment>> getAll(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {
		
		Page<Payment> payments = paymentService.findAllPage(page);
		
		return new ResponseEntity<>(payments, HttpStatus.OK);
	}

}
