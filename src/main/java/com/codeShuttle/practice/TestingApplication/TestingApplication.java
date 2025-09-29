package com.codeShuttle.practice.TestingApplication;

import com.codeShuttle.practice.TestingApplication.services.CustomProfileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class TestingApplication implements CommandLineRunner {

	private final CustomProfileService customProfileService;

	@Value("${global.name}")
	private String valueFromEnv;

	@Value("${DB_HOST_URL}")
	private String connectionUrl;

	@Value("${DB_PASSWORD}")
	private String dbpass;

	@Value("${DB_USERNAME}")
	private String dbuser;

	public static void main(String[] args) {
		SpringApplication.run(TestingApplication.class, args);
	}



	public void run(String... args) throws Exception {
		System.out.println(customProfileService.getData());
		System.out.println(valueFromEnv);
		System.out.println(dbuser);
		System.out.println(dbpass);
		System.out.println(connectionUrl);

	}
}
