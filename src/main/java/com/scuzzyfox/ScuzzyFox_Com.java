package com.scuzzyfox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = { ComponentScanMarker.class })
public class ScuzzyFox_Com {

	public static void main(String[] args) {
		SpringApplication.run(ScuzzyFox_Com.class, args);
	}

}
