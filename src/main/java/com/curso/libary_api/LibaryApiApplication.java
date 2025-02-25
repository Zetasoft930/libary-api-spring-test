package com.curso.libary_api;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableAdminServer
@SpringBootApplication
public class LibaryApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(LibaryApiApplication.class, args);
	}

}
