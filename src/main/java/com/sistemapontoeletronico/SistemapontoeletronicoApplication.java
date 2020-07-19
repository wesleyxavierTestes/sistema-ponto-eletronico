package com.sistemapontoeletronico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.sistemapontoeletronico.domain.entities")
@EnableJpaRepositories("com.sistemapontoeletronico.infra.repositorys")
@SpringBootApplication
public class SistemapontoeletronicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemapontoeletronicoApplication.class, args);
	}
}
