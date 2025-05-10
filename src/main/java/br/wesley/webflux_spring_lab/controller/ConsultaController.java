package br.wesley.webflux_spring_lab.controller;

import br.wesley.webflux_spring_lab.dto.ConsultaDTO;
import br.wesley.webflux_spring_lab.dto.ConsultaResponseDTO;
import br.wesley.webflux_spring_lab.service.ConsultaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    // POST /consultas
    @PostMapping
    public Mono<ConsultaResponseDTO> marcarConsulta(@RequestBody ConsultaDTO dto) {
        return service.marcarConsulta(dto);
    }

    // GET /consultas
    @GetMapping
    public Flux<ConsultaResponseDTO> mostrarTodas() {
        return service.mostrarTodas();
    }

    // GET /consultas/{id}
    @GetMapping("/{id}")
    public Mono<ConsultaResponseDTO> mostrarPorId(@PathVariable Long id) {
        return service.mostrarPorId(id);
    }

    // GET /consultas/paciente/{nome}
    @GetMapping("/paciente/{nome}")
    public Flux<ConsultaResponseDTO> mostrarPorPaciente(@PathVariable String nome) {
        return service.mostrarPorPaciente(nome);
    }

    // GET /consultas/medico/{nome}
    @GetMapping("/medico/{nome}")
    public Flux<ConsultaResponseDTO> buscarPorMedico(@PathVariable String nome) {
        return service.buscarPorMedico(nome);
    }

    // GET /consultas/data/{data}
    @GetMapping("/data/{data}")
    public Flux<ConsultaResponseDTO> buscarPorData(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return service.buscarPorData(data);
    }

    // GET /consultas/confirmada/{status}
    @GetMapping("/confirmada/{status}")
    public Flux<ConsultaResponseDTO> buscarPorConfirmada(@PathVariable Boolean status) {
        return service.buscarPorConfirmada(status);
    }

    // PUT /consultas/{id}/data
    @PutMapping("/{id}/data")
    public Mono<ConsultaResponseDTO> atualizarData(@PathVariable Long id,
                                                   @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime novaData) {
        return service.atualizarData(id, novaData);
    }

    // DELETE /consultas/{id}
    @DeleteMapping("/{id}")
    public Mono<Void> desmarcarConsulta(@PathVariable Long id) {
        return service.desmarcarConsulta(id);
    }

    // GET /consultas/filtro
    @GetMapping("/filtro")
    public Flux<ConsultaResponseDTO> buscarPorTodos(
            @RequestParam String paciente,
            @RequestParam String medico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        return service.buscarPorTodos(paciente, medico, dataHora);
    }
}
