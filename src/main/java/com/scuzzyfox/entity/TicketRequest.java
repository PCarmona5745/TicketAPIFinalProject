package com.scuzzyfox.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TicketRequest {

	@NotNull
	@Length(max = 50)
	@Pattern(regexp = "[\\w\\s]*")
	@Schema(example = "Ticket Title")
	private String ticketTitle;
	

	@Length(max = 1000)
	
	@Schema(example = "The ticket body")
	private String ticketBody;
	

	@NotNull
	@Length(max = 15)
	@Pattern(regexp = "[\\w\\s]*")
	@Schema(example = "scuzzyfox")
	private String username;
	

}
