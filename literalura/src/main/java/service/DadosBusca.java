package service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dto.DadosLivro;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosBusca(@JsonAlias("results") List<DadosLivro> resultados) {}
