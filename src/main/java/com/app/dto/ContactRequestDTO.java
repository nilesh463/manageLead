package com.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ContactRequestDTO {

	@NotBlank(message = "Full Name is required")
	private String fullName;

	@NotBlank(message = "Email ID is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Phone Number is required")
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
	private String phoneNumber;

	@NotBlank(message = "Message is required")
	private String message;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ContactRequestDTO [fullName=" + fullName + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", message=" + message + "]";
	}

}
