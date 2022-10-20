package com.scuzzyfox.user.controller;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.scuzzyfox.entity.User;
import com.scuzzyfox.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DefaultUserController implements UserController {
	
	@Autowired
	UserService userService;

	@Override
	public User fetchAUser(@Length(max = 15) String username) {
		return userService.fetchAUser(username);
	}

	@Override
	public User makeAUser(@Length(max = 15) String username) {
		return userService.createAUser(username);
	}

	

	@Override
	public User deleteAUser(@Length(max = 15) String username) {
		return userService.deleteAUser(username);
	}

	@Override
	public User updateAUsername(@Length(max = 15) String username, @Length(max = 15) String newUsername) {
		return userService.updateAUser(username, newUsername);
	}

}
