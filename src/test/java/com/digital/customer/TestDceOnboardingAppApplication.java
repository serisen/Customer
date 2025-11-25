package com.digital.customer;

import org.springframework.boot.SpringApplication;

public class TestDceOnboardingAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(DceOnboardingAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
