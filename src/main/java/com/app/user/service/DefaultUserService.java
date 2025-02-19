package com.app.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.app.dto.UserRegisteredDTO;
import com.app.modal.User;


public interface DefaultUserService extends UserDetailsService{

	User save(UserRegisteredDTO userRegisteredDTO);
	
}
