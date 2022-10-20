package com.scuzzyfox.user.dao;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.scuzzyfox.entity.User;

@Component
public interface UserDao {
	
	Optional<User> fetchUser(Long userId);

	Optional<User> fetchUser(String username);
	
	Optional<User> fetchUser(User user);
	
	User updateUsername(String username, String newUsername);
	User deleteUser(String username);
	User saveUser(String username);
}
