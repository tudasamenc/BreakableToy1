package com.example.demo;


import com.example.demo.Task.Taskk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.config.Task;

import java.time.LocalDateTime;

@SpringBootApplication
public class BreakableToy1Application {
	private static final Logger log = LoggerFactory.getLogger(BreakableToy1Application.class);

	public static void main(String[] args) {

		SpringApplication.run(BreakableToy1Application.class, args);
		//log.info("Application started successfully!");
		//LinkedList<ToDo> tasks = new LinkedList<>();
	}
	@Bean
	CommandLineRunner runner(){
		return args -> {
			Taskk task = new Taskk(1,"Tarea 1",false,2, LocalDateTime.now());
			log.info("Task: "+ task);
		};
	}
}
