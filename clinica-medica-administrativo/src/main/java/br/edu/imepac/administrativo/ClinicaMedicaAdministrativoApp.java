package br.edu.imepac.administrativo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"br.edu.imepac"})
@EnableJpaRepositories(basePackages = {"br.edu.imepac.comum.repositories"})
@EntityScan(basePackages = {"br.edu.imepac.comum.models"})
public class ClinicaMedicaAdministrativoApp {
    public static void main(String[] args) {
        SpringApplication.run(ClinicaMedicaAdministrativoApp.class, args);
    }
}