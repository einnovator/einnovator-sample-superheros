package org.einnovator.sample.superheros;

import org.einnovator.sample.superheros.config.AppConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SuperherosApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SuperherosApplication.class).profiles(AppConfig.getProfiles()).run(args);
	}
	

}
