package com.scuzzyfox.ticket.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.scuzzyfox.entity.Category;
import com.scuzzyfox.entity.Comment;
import com.scuzzyfox.entity.Status;
import com.scuzzyfox.entity.Ticket;
import com.scuzzyfox.entity.TicketRequest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;

@Validated
@RequestMapping("/ticket")
@OpenAPIDefinition(info = @Info(title = "Create ticket service"), servers = {
		@Server(url = "http://scuzzyfox.com", description = "Public server") })

public interface TicketController {
	@Operation(summary = "Create a ticket using a json ticket request object", description = "Returns the created Ticket", responses = {
			@ApiResponse(responseCode = "201", description = "The created Ticket is returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "A Ticket component was not found with the input criteria", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) })

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	Ticket createTicket(@Valid @RequestBody TicketRequest ticketRequest);

	@Operation(summary = "Retrieve a list of tickets made by a particular user", description = "Returns the list of tickets", responses = {
			@ApiResponse(responseCode = "200", description = "the list of tickets is returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "No tickets were found with the given username", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) })

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	List<Ticket> fetchTicketsByUser(@Length(max = 15) String username);
	
	@Operation(summary = "Retrieve a list of comments on a ticket", description = "Returns the list of comments", responses = {
			@ApiResponse(responseCode = "200", description = "the list of comments is returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "No tickets were found with the given ID", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name="ticketId", allowEmptyValue = false, required = false, description="The ticketId that you want to look at comments for.")})
	
	@GetMapping("/{ticketId}/comments")
	@ResponseStatus(code = HttpStatus.OK)
	List<Comment> fetchCommentsOnTicket(Long ticketId);
	
	@Operation(summary = "Retrieve a list of tickets belonging to a category", description = "Returns the list of tickets", responses = {
			@ApiResponse(responseCode = "200", description = "the list of tickets is returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "No categories were found with the given parameters", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name="categoryName", allowEmptyValue = false, required = false, description="The category that you want")})
	
	@GetMapping("/category/{categoryName}")
	@ResponseStatus(code = HttpStatus.OK)
	List<Ticket> fetchTicketsByCategory(String categoryName);
	
	@Operation(summary = "Attach a category to an existing ticket", description = "Returns the updated ticket", responses = {
			@ApiResponse(responseCode = "201", description = "the category attached is returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "No ticket and/or category was found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name="ticketId", allowEmptyValue = false, required = false, description="The ticketId that you want to update"),
					@Parameter(name="categoryName", allowEmptyValue = false, required = false, description="the category you want to attach to the ticket")
					
			})

	@PutMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	Category giveExistingTicketACategory(Long ticketId, String categoryName);
	
	
	@Operation(summary = "Add a comment to an existing ticket", description = "Returns the posted comment", responses = {
			@ApiResponse(responseCode = "201", description = "the comment added is returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "No ticket and was found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name="ticketId", allowEmptyValue = false, required = false, description="The ticketId that you want to update"),
					@Parameter(name="comment", allowEmptyValue = false, required = false, description="the comment text you want to add."),
					@Parameter(name="username", allowEmptyValue = false, required = false, description="your username")
					
			})
	
	@PutMapping("/comment")
	@ResponseStatus(code = HttpStatus.CREATED)
	Comment addCommentToTicket(Long ticketId, String comment, String username);
	
	@Operation(summary = "update an existing ticket's status", description = "Returns the changed ticket", responses = {
			@ApiResponse(responseCode = "201", description = "the status is updated and ticket is returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class))),
			@ApiResponse(responseCode = "400", description = "The request parameters are invalid", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "No ticket and was found", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500", description = "An unplanned error occured.", content = @Content(mediaType = "application/json")) }, parameters = {
					@Parameter(name="ticketId", allowEmptyValue = false, required = false, description="The ticketId that you want to update"),
					@Parameter(name="status", allowEmptyValue = false, required = false, description="the status you want to put on the ticket(PENDING, ACTIVE, CLOSED, RESOLVED)",schema=@Schema(implementation = Status.class))
					
			})
	
	@PutMapping("/status")
	@ResponseStatus(code = HttpStatus.CREATED)
	Ticket changeTicketStatus(Long ticketId, Status status);
	
	

}
