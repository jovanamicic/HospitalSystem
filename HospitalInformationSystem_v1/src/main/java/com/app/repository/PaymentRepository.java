package com.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	
	Page<Payment> findAll(Pageable page);
}
