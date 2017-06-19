package com.maliciousServer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value= "attack")
public class AttackController {
	
	@RequestMapping(value="/dos", method = RequestMethod.GET)
	public void dosAttack() throws InterruptedException{
		while (true) {
			Thread.sleep(Long.MAX_VALUE);
		}
	}

}
