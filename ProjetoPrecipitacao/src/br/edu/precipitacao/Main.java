package br.edu.precipitacao;

import br.edu.precipitacao.exception.AppException;
import br.edu.precipitacao.model.Record;
import br.edu.precipitacao.reader.Reader;
import br.edu.precipitacao.service.Statistics;
import br.edu.precipitacao.view.Report;

import java.util.List;

public class Main {

    private static final String NOME_ARQUIVO = "precipitacao_acarau_2025.csv";
    private static final int ANO = 2025;

    public static void main(String[] args) {
        String caminho = args.length > 0 ? args[0] : Reader.buscarCSV(NOME_ARQUIVO);

        System.out.println();
        System.out.println("Arquivo: " + caminho);
        System.out.println("Ano: " + ANO);

        try {
            Reader reader = new Reader();
            List<Record> registros = reader.lerCSV(caminho);
            System.out.println("Status: " + reader.getResumoLeitura());

            Statistics statistics = new Statistics();
            Report report = new Report(statistics);
            report.exibirRelatorio(registros, ANO);

        } catch (AppException e) {
            System.out.println();
            System.out.println("ERRO [" + e.getTipo() + "]");
            for (String linha : e.getMessage().split("\n")) {
                System.out.println("  " + linha);
            }
            System.out.println();
            System.out.println("Como usar:");
            System.out.println("  Coloque o arquivo CSV dentro da pasta src/ do projeto");
            System.out.println("  Ou informe o caminho completo como argumento:");
            System.out.println("  Exemplo: java Main C:\\Users\\voce\\Downloads\\arquivo.csv");
            System.out.println();
            System.out.println("Formato esperado do CSV:");
            System.out.println("  data;precipitacao");
            System.out.println("  01/01/2025;0.0");
            System.out.println("  15/01/2025;192.0");
            System.exit(1);

        } catch (Exception e) {
            System.out.println();
            System.out.println("ERRO INESPERADO: " + e.getMessage());
            System.exit(2);
        }
    }
}
