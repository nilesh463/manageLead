package com.app.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.dto.Responce;
import com.app.dto.UserDto;
import com.app.modal.User;

public interface UserService {

	void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

	void saveMasterUser(UserDto userDto);

	Page<UserDto> findAllUsers(int page, int size);

	User edit(Long id);

	String updateUser(Long id, User user);

	Responce deleteUser(Long userId);

	void saveUser(User user);
	
	Responce deleteUserByEmail(String userId);

	boolean isUserLoggedIn(String email);
	
}
