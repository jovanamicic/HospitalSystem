package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.RoleMember;
import com.app.repository.RoleMemberRepository;

@Service
public class RoleMemberService {

	@Autowired
	private RoleMemberRepository roleMemberRepository;

	public RoleMember save(RoleMember rm) {
		return roleMemberRepository.save(rm);
		
	}
}
