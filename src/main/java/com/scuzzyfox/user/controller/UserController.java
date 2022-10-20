package com.scuzzyfox.user.controller;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.scuzzyfox.entity.User;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;

@Validated
@RequestMapping("/user")
@OpenAPIDefinition(info = @Info(title = "User management service"), servers = {
		@Server(url = "http://scuzzyfox.com", description = "Public server") })
public interface UserController {
	
	
	
	
	@Operation(summary = "Retrieves a user based on case-sensitive username", description = "Returns a user as json", responses = {
			@ApiResponse(responseCode = "200", description = "success: the User is returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "No Users were found with the input criteria", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name = "username", allowEmptyValue = false, required = false, description = "the username string in question (case sensitive)")})

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	User fetchAUser(@Length(max = 15)  @RequestParam(required = true) String username);
	
	@Operation(summary = "Creates a user based on case-sensitive username", description = "Returns the created user as json", responses = {
			@ApiResponse(responseCode = "201", description = "success: the User was created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name = "username", allowEmptyValue = false, required = false, description = "the username string in question (case sensitive)")})

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	User makeAUser(@Length(max = 15) @RequestParam(required = true) String username);
	
	@Operation(summary = "Updates a user's username based on case-sensitive username", description = "Returns the updated user as json", responses = {
			@ApiResponse(responseCode = "201", description = "success: the username was created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name = "username", allowEmptyValue = false, required = false, description = "the username string in question (case sensitive)"), @Parameter(name="newUsername", allowEmptyValue = false, required = false, description="the new username")})
	
	@PutMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	User updateAUsername(@Length(max = 15) @RequestParam(required = true) String username, @Length(max = 15) @RequestParam(required = true) String newUsername);
	
	
	@Operation(summary = "Deletes a user based on case-sensitive username. THIS WILL DELETE ALL USER'S TICKETS", description = "Returns the deleted user as json", responses = {
			@ApiResponse(responseCode = "201", description = "success: the User was created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name = "username", allowEmptyValue = false, required = false, description = "the username string in question (case sensitive)")})
	
	@DeleteMapping
	@ResponseStatus(code=HttpStatus.OK)
	User deleteAUser(@Length(max = 15) @RequestParam(required = true) String username);
}
