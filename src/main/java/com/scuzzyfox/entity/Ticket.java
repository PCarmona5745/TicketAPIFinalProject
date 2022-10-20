package com.scuzzyfox.entity;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
	
	@Schema(example="2")
	private Long ticketId;
	
	@Schema(example="Title of the ticket")
	private String ticketTitle;
	
	@Schema(example="Body of the ticket")
	private String ticketBody;
	
	@Schema(example="2019-05-11T04:07:37.191Z")
	private LocalDateTime created;
	
	@Schema(example="PENDING")
	private Status status;
	
	@Schema
	private User user;
}
