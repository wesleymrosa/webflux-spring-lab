package br.wesley.webflux_spring_lab.repository;

import br.wesley.webflux_spring_lab.model.Consulta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ConsultaRepository extends ReactiveCrudRepository<Consulta, Long> {

    Flux<Consulta> findByPacienteContainingIgnoreCase(String paciente);

    Flux<Consulta> findByMedicoContainingIgnoreCase(String medico);

    Flux<Consulta> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    Flux<Consulta> findByConfirmada(Boolean confirmada);

    Flux<Consulta> findByPacienteAndMedico(String paciente, String medico);

    Flux<Consulta> findByPacienteAndConfirmada(String paciente, Boolean confirmada);

    Flux<Consulta> findByPacienteAndMedicoAndDataHora(String paciente, String medico, LocalDateTime dataHora);
}

