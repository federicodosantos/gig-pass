package com.ddosantos.gig.pass;

import com.ddosantos.gig.pass.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GigPassApplication {

	public static void main(String[] args) {
		SpringApplication.run(GigPassApplication.class, args);
	}

	@Bean
	public TokenProvider tokenProvider() {
		return new TokenProvider();
	}
}
