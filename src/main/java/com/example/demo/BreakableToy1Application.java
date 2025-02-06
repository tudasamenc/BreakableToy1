package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BreakableToy1Application {

	public static void main(String[] args) {

		SpringApplication.run(BreakableToy1Application.class, args);
		LinkedList<ToDo> tasks = new LinkedList<>();
	}

}
