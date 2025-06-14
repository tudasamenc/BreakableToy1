package breakableToy;
import breakableToy.Task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class BreakableToy1Application {
	private static final Logger log = LoggerFactory.getLogger(BreakableToy1Application.class);

	public static void main(String[] args) {

		SpringApplication.run(BreakableToy1Application.class, args);
	}
	@Bean
	CommandLineRunner runner(){
		return args -> {
		};
	}
}
