package team.epl.monkeytype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MonkeytypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonkeytypeApplication.class, args);
	}

}
