package br.edu.imepac.agendamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "br.edu.imepac.agendamento",
        "br.edu.imepac.comum"
})
@EnableJpaRepositories(basePackages = {"br.edu.imepac.comum.repositories"})
@EntityScan(basePackages = {"br.edu.imepac.comum.models"})
public class ClinicaMedicaAgendamentoApp {
    public static void main(String[] args) {
        SpringApplication.run(ClinicaMedicaAgendamentoApp.class, args);
    }
}