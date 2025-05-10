package br.wesley.webflux.service;

import br.wesley.webflux.dto.ConsultaDTO;
import br.wesley.webflux.dto.ConsultaResponseDTO;
import br.wesley.webflux.model.Consulta;
import br.wesley.webflux.repository.ConsultaRepository;
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
        nova.setConfirmada(false);

        return repository.save(nova)
                .map(this::toResponse);
    }

    // Mostrar todas as consultas
    public Flux<ConsultaResponseDTO> mostrarTodas() {
        return repository.findAll()
                .map(this::toResponse);
    }

    // Buscar por ID
    public Mono<ConsultaResponseDTO> mostrarPorId(Long id) {
        return repository.findById(id)
                .map(this::toResponse);
    }

    // Buscar por nome do paciente
    public Flux<ConsultaResponseDTO> mostrarPorPaciente(String nome) {
        return repository.findByPacienteContainingIgnoreCase(nome)
                .map(this::toResponse);
    }

    // Buscar por médico
    public Flux<ConsultaResponseDTO> buscarPorMedico(String nome) {
        return repository.findByMedicoContainingIgnoreCase(nome)
                .map(this::toResponse);
    }

    // Buscar por data (apenas por dia)
    public Flux<ConsultaResponseDTO> buscarPorData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        return repository.findByDataHoraBetween(inicio, fim)
                .map(this::toResponse);
    }

    // Buscar por status de confirmação
    public Flux<ConsultaResponseDTO> buscarPorConfirmada(Boolean status) {
        return repository.findByConfirmada(status)
                .map(this::toResponse);
    }

    // Atualizar data de uma consulta
    public Mono<ConsultaResponseDTO> atualizarData(Long id, LocalDateTime novaData) {
        return repository.findById(id)
                .flatMap(consulta -> {
                    consulta.setDataHora(novaData);
                    return repository.save(consulta);
                })
                .map(this::toResponse);
    }

    // Desmarcar consulta
    public Mono<Void> desmarcarConsulta(Long id) {
        return repository.deleteById(id);
    }

    // Buscar por todos os atributos combinados
    public Flux<ConsultaResponseDTO> buscarPorTodos(String paciente, String medico, LocalDateTime dataHora) {
        return repository.findByPacienteAndMedicoAndDataHora(paciente, medico, dataHora)
                .map(this::toResponse);
    }

    // Conversão para ResponseDTO
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
