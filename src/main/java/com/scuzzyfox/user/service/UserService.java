package com.scuzzyfox.user.service;

import org.springframework.stereotype.Service;

import com.scuzzyfox.entity.TicketRequest;
import com.scuzzyfox.entity.User;

@Service
public interface UserService {
	
	User fetchAUser(String username);
	User updateAUser(String username, String newUsername);
	User deleteAUser(String username);
	User createAUser(String username);
	
	public User createUserIfOneDoesNotExist(TicketRequest ticketRequest);
	public User createUserIfOneDoesNotExist(String username);

}
