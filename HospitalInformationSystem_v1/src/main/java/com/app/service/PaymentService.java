package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.model.Payment;
import com.app.repository.PaymentRepository;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository paymentRepository;

	public Payment save(Payment p) {
		return paymentRepository.save(p);
	}
	
	public Page<Payment> findAllPage(Pageable page){
		return paymentRepository.findAll(page);
	}

}
