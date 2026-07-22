package com.unamba.apilibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.unamba.apilibrary.repository.RepositoryUser;
import com.unamba.apilibrary.entity.EntityUser;

@SpringBootApplication
public class ApilibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApilibraryApplication.class, args);
	}

    @Bean
    public CommandLineRunner dataLoader(RepositoryUser repo) {
        return args -> {
            EntityUser admin = repo.findByEmail("admin@unamba.edu.pe").orElse(null);
            if (admin != null) {
                admin.setPassword("$2a$10$r40efuVFIhb9fb4F98VMlOY.TE9ypDXhX9Ge.N/kMolfhNDhrlgLO");
                repo.save(admin);
            }
        };
    }
}
