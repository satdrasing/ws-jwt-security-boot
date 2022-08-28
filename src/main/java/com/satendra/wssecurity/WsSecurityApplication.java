package com.satendra.wssecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WsSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsSecurityApplication.class, args);
	}

}
