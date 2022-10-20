package com.scuzzyfox.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	
	@Schema(example="3")
	private Long commentId;
	
	@Schema(example="I took a look at the coffee machine. Awaiting a new one to be shipped.")
	private String commentText;
	
	@Schema(example="2")
	private Long ticketFK;
	
	@Schema(example="scuzzyfox")
	private Long userFK;
	
	@Schema(example="7")
	private int commentOrder;
}
