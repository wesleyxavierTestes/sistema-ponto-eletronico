package com.sistemapontoeletronico;

import com.sistemapontoeletronico.utils.DateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@ComponentScan({
		"com.sistemapontoeletronico.infra.data",
		"com.sistemapontoeletronico.domain.services",
		"com.sistemapontoeletronico.controllers"
})
@EntityScan("com.sistemapontoeletronico.domain.entities")
@EnableJpaRepositories("com.sistemapontoeletronico.infra.repositorys")
@SpringBootApplication
public class SistemapontoeletronicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemapontoeletronicoApplication.class, args);
	}
}
