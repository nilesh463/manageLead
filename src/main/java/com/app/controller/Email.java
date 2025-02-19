package com.app.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ContactRequestDTO;
import com.app.dto.Responce;
import com.app.utilityServices.EmailService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contact-us")
@Validated
public class Email {

	@Autowired
	EmailService emailService;

	@GetMapping("/")
	public ResponseEntity<Responce> submitContactForm(@Valid @RequestBody ContactRequestDTO request)
			throws UnsupportedEncodingException, MessagingException {

		emailService.contactUs(request);
		Responce response = new Responce();
		response.setStatus(200L);
		response.setMessage("Contact form submitted successfully!");
		return ResponseEntity.ok(response);
	}
}
