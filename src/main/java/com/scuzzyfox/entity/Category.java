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
public class Category {
	
	@Schema(example="2")
	private Long categoryId;
	
	@Schema(example="client_devices")
	private String categoryName;
}
