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
public class User {
	
	@Schema(example = "1")
	private Long userId;
	@Schema(example="scuzzyfox")
	private String username;

}
