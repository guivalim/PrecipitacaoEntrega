package br.edu.precipitacao.service;

import br.edu.precipitacao.exception.AppException;
import br.edu.precipitacao.model.Record;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {

    public double totalPorMes(List<Record> registros, int mes, int ano) {
        validarLista(registros);
        validarMes(mes);
        return registros.stream()
                .filter(r -> r.getMes() == mes && r.getAno() == ano)
                .mapToDouble(Record::getPrecipitacao)
                .sum();
    }

    public Map<Integer, Double> totalPorTodosMeses(List<Record> registros, int ano) {
        validarLista(registros);
        Map<Integer, Double> totais = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            totais.put(m, totalPorMes(registros, m, ano));
        }
        return totais;
    }

    public Record diaMaiorPrecipitacao(List<Record> registros, int ano) {
        validarLista(registros);
        return registros.stream()
                .filter(r -> r.getAno() == ano)
                .max(Comparator.comparingDouble(Record::getPrecipitacao))
                .orElseThrow(() -> AppException.semDados(0, ano));
    }

    public Record diaMenorPrecipitacao(List<Record> registros, int ano) {
        validarLista(registros);
        return registros.stream()
                .filter(r -> r.getAno() == ano && r.getPrecipitacao() > 0)
                .min(Comparator.comparingDouble(Record::getPrecipitacao))
                .orElseThrow(() -> AppException.semDados(0, ano));
    }

    public int mesMaiorPrecipitacao(List<Record> registros, int ano) {
        validarLista(registros);
        return totalPorTodosMeses(registros, ano)
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> AppException.semDados(0, ano));
    }

    public int mesMenorPrecipitacao(List<Record> registros, int ano) {
        validarLista(registros);
        return totalPorTodosMeses(registros, ano)
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> AppException.semDados(0, ano));
    }

    public double mediaDiaria(List<Record> registros, int ano) {
        validarLista(registros);
        return registros.stream()
                .filter(r -> r.getAno() == ano)
                .mapToDouble(Record::getPrecipitacao)
                .average()
                .orElse(0.0);
    }

    public double mediaPorMes(List<Record> registros, int mes, int ano) {
        validarLista(registros);
        validarMes(mes);
        return registros.stream()
                .filter(r -> r.getMes() == mes && r.getAno() == ano)
                .mapToDouble(Record::getPrecipitacao)
                .average()
                .orElse(0.0);
    }

    public Map<Integer, Double> mediaTodosMeses(List<Record> registros, int ano) {
        validarLista(registros);
        Map<Integer, Double> medias = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            medias.put(m, mediaPorMes(registros, m, ano));
        }
        return medias;
    }

    public List<Record> topNDias(List<Record> registros, int ano, int n) {
        validarLista(registros);
        if (n < 1) {
            throw new AppException("VALOR_INVALIDO", "O valor de N deve ser maior que zero. Valor recebido: " + n);
        }
        return registros.stream()
                .filter(r -> r.getAno() == ano)
                .sorted(Comparator.comparingDouble(Record::getPrecipitacao).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    private void validarLista(List<Record> registros) {
        if (registros == null || registros.isEmpty()) {
            throw new AppException("LISTA_VAZIA", "A lista de registros esta vazia.");
        }
    }

    private void validarMes(int mes) {
        if (mes < 1 || mes > 12) {
            throw new AppException("MES_INVALIDO", "Mes invalido: " + mes + ". Deve ser entre 1 e 12.");
        }
    }
}
