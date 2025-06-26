package br.edu.imepac.atendimento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "br.edu.imepac.atendimento",
        "br.edu.imepac.comum.exceptions",
        "br.edu.imepac.comum.services"
})
@EnableJpaRepositories(basePackages = {
        "br.edu.imepac.atendimento.repositories",
        "br.edu.imepac.comum.repositories"
})
@EntityScan(basePackages = {
        "br.edu.imepac.comum.models"
})
public class ClinicaMedicaAtendimentoApp {
    public static void main(String[] args) {
        SpringApplication.run(ClinicaMedicaAtendimentoApp.class, args);
    }
}