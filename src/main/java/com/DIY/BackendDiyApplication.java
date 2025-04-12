package com.DIY;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendDiyApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		System.setProperty("gemini.model-name", dotenv.get("GEMINI_MODEL_NAME"));
		SpringApplication.run(BackendDiyApplication.class, args);
	}

}
