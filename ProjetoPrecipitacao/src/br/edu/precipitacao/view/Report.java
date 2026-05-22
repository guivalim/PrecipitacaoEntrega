package br.edu.precipitacao.view;

import br.edu.precipitacao.model.Record;
import br.edu.precipitacao.service.Statistics;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Report {

    private Statistics statistics;

    public Report(Statistics statistics) {
        this.statistics = statistics;
    }

    public void exibirRelatorio(List<Record> registros, int ano) {
        exibirCabecalho(ano, registros.size());
        exibirTotalPorMes(registros, ano);
        exibirDiaMaiorMenor(registros, ano);
        exibirMesMaiorMenor(registros, ano);
        exibirMediaAnual(registros, ano);
        exibirMediaPorMes(registros, ano);
        exibirTop10(registros, ano);
        exibirRodape();
    }

    private void exibirCabecalho(int ano, int totalRegistros) {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  Relatorio de precipitação - ACARAU/CE - " + ano);
        System.out.println("  Total de registros: " + totalRegistros + " dias");
        System.out.println("=".repeat(55));
    }

    private void exibirTotalPorMes(List<Record> registros, int ano) {
        System.out.println("\n Total por mês:");
        System.out.println("    " + "-".repeat(35));

        Map<Integer, Double> totais = statistics.totalPorTodosMeses(registros, ano);
        double totalGeral = 0;

        for (Map.Entry<Integer, Double> entrada : totais.entrySet()) {
            int mes = entrada.getKey();
            double total = entrada.getValue();
            totalGeral += total;
            System.out.printf("    %-12s - %8.1f mm%n", nomeMes(mes), total);
        }

        System.out.println("    " + "-".repeat(35));
        System.out.printf("    %-12s = %8.1f mm%n", "Total Anual", totalGeral);
    }

    private void exibirDiaMaiorMenor(List<Record> registros, int ano) {
        System.out.println("\n Dia de maior precipitação:");
        try {
            System.out.println("    " + statistics.diaMaiorPrecipitacao(registros, ano));
        } catch (Exception e) {
            System.out.println("    Sem dados para este periodo.");
        }

        System.out.println("\n Dia de menor precipitação:");
        try {
            System.out.println("    " + statistics.diaMenorPrecipitacao(registros, ano));
        } catch (Exception e) {
            System.out.println("    Nenhum dia com chuva registrado.");
        }
    }

    private void exibirMesMaiorMenor(List<Record> registros, int ano) {
        Map<Integer, Double> totais = statistics.totalPorTodosMeses(registros, ano);

        System.out.println("\n Mês de maior precipitação:");
        try {
            int mes = statistics.mesMaiorPrecipitacao(registros, ano);
            System.out.printf("    %s (%.1f mm)%n", nomeMes(mes), totais.get(mes));
        } catch (Exception e) {
            System.out.println("    Sem dados.");
        }

        System.out.println("\n Mês de menor precipitação:");
        try {
            int mes = statistics.mesMenorPrecipitacao(registros, ano);
            System.out.printf("    %s (%.1f mm)%n", nomeMes(mes), totais.get(mes));
        } catch (Exception e) {
            System.out.println("    Sem dados.");
        }
    }

    private void exibirMediaAnual(List<Record> registros, int ano) {
        System.out.println("\n Média de precipitação do ano:");
        System.out.printf("    %.2f mm/dia%n", statistics.mediaDiaria(registros, ano));
    }

    private void exibirMediaPorMes(List<Record> registros, int ano) {
        System.out.println("\nMédia por mês:");
        System.out.println("    " + "-".repeat(35));

        Map<Integer, Double> medias = statistics.mediaTodosMeses(registros, ano);
        for (Map.Entry<Integer, Double> entrada : medias.entrySet()) {
            System.out.printf("    %-12s | %7.2f mm/dia%n", nomeMes(entrada.getKey()), entrada.getValue());
        }
    }

    private void exibirTop10(List<Record> registros, int ano) {
        System.out.println("\n Top 10 dias de maior precipitação:");
        System.out.println("    " + "-".repeat(35));

        List<Record> top10 = statistics.topNDias(registros, ano, 10);

        if (top10.isEmpty()) {
            System.out.println("    Sem dados suficientes.");
            return;
        }

        for (int i = 0; i < top10.size(); i++) {
            System.out.printf("    %2do  %s%n", i + 1, top10.get(i));
        }
    }

    private void exibirRodape() {
        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("  Fonte: FUNCEME - Posto 2 - Acarau/CE");
        System.out.println("=".repeat(55));
        System.out.println();
    }

    private String nomeMes(int mes) {
        return Month.of(mes).getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
    }
}
