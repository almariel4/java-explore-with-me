package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EwmMainService {
	public static void main(String[] args) {
		SpringApplication.run(EwmMainService.class, args);
		String name = System.getProperty("user.name");
		System.out.println(name);
	}

}
