package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.Address;
import com.app.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepository;
	
	public Address save(Address address){
		return addressRepository.save(address);
	}
	
	public Address findOne(int id){
		return addressRepository.findOne(id);
	}
	

}
