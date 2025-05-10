package br.wesley.webflux_spring_lab.service;

import br.wesley.webflux_spring_lab.dto.ConsultaDTO;
import br.wesley.webflux_spring_lab.dto.ConsultaResponseDTO;
import br.wesley.webflux_spring_lab.model.Consulta;
import br.wesley.webflux_spring_lab.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ConsultaService {

    private final ConsultaRepository repository;

    public ConsultaService(ConsultaRepository repository) {
        this.repository = repository;
    }

    // Marcar nova consulta
    public Mono<ConsultaResponseDTO> marcarConsulta(ConsultaDTO dto) {
        Consulta nova = new Consulta();
        nova.setPaciente(dto.getPaciente());
        nova.setMedico(dto.getMedico());
        nova.setDataHora(dto.getDataHora());
        nova.setConfirmada(dto.getConfirmada()); // respeita valor enviado

        return repository.save(nova)
                .map(this::toResponse);
    }

    public Flux<ConsultaResponseDTO> mostrarTodas() {
        return repository.findAll()
                .map(this::toResponse);
    }

    public Mono<ConsultaResponseDTO> mostrarPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse);
    }

    public Flux<ConsultaResponseDTO> mostrarPorPaciente(String nome) {
        return repository.findByPacienteContainingIgnoreCase(nome)
                .map(this::toResponse);
    }

    public Flux<ConsultaResponseDTO> buscarPorMedico(String nome) {
        return repository.findByMedicoContainingIgnoreCase(nome)
                .map(this::toResponse);
    }

    public Flux<ConsultaResponseDTO> buscarPorData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        return repository.findByDataHoraBetween(inicio, fim)
                .map(this::toResponse);
    }

    public Flux<ConsultaResponseDTO> buscarPorConfirmada(Boolean status) {
        return repository.findByConfirmada(status)
                .map(this::toResponse);
    }

    public Mono<ConsultaResponseDTO> atualizarData(Long id, LocalDateTime novaData) {
        return repository.findById(id)
                .flatMap(consulta -> {
                    consulta.setDataHora(novaData);
                    return repository.save(consulta);
                })
                .map(this::toResponse);
    }

    public Mono<Void> desmarcarConsulta(Long id) {
        return repository.deleteById(id);
    }

    public Flux<ConsultaResponseDTO> buscarPorTodos(String paciente, String medico, LocalDateTime dataHora) {
        return repository.findByPacienteAndMedicoAndDataHora(paciente, medico, dataHora)
                .map(this::toResponse);
    }

    private ConsultaResponseDTO toResponse(Consulta consulta) {
        ConsultaResponseDTO dto = new ConsultaResponseDTO();
        dto.setId(consulta.getId());
        dto.setPaciente(consulta.getPaciente());
        dto.setMedico(consulta.getMedico());
        dto.setDataHora(consulta.getDataHora());
        dto.setConfirmada(consulta.getConfirmada());
        return dto;
    }
}
