package br.edu.precipitacao.reader;

import br.edu.precipitacao.exception.AppException;
import br.edu.precipitacao.model.Record;
import br.edu.precipitacao.util.Validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    private int totalLinhas;
    private int totalIgnoradas;
    private int totalValidos;

    public List<Record> lerCSV(String caminho) {
        totalLinhas = 0;
        totalIgnoradas = 0;
        totalValidos = 0;

        Validator.validarArquivo(caminho);

        List<Record> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String cabecalho = br.readLine();
            totalLinhas++;
            Validator.validarCabecalho(cabecalho, caminho);

            String linha;
            int numLinha = 1;

            while ((linha = br.readLine()) != null) {
                totalLinhas++;
                numLinha++;

                String[] partes = Validator.splitLinha(linha, numLinha);
                if (partes == null) {
                    totalIgnoradas++;
                    continue;
                }

                LocalDate data = Validator.parseData(partes[0], numLinha);
                double precipitacao = Validator.parsePrecipitacao(partes[1], numLinha);

                lista.add(new Record(data, precipitacao));
                totalValidos++;
            }

        } catch (IOException e) {
            throw new AppException("ERRO_LEITURA", "Erro ao ler o arquivo: " + caminho + "\n" + e.getMessage());
        }

        if (lista.isEmpty()) {
            throw AppException.arquivoVazio(caminho);
        }

        return lista;
    }

    public static String buscarCSV(String nomeArquivo) {
        String[] candidatos = {
                "src" + File.separator + nomeArquivo,
                nomeArquivo,
                "resources" + File.separator + nomeArquivo
        };

        for (String caminho : candidatos) {
            if (new File(caminho).exists()) {
                return caminho;
            }
        }

        return "src" + File.separator + nomeArquivo;
    }

    public String getResumoLeitura() {
        return totalLinhas + " linha(s) lida(s), " + totalValidos + " registro(s) valido(s), " + totalIgnoradas + " ignorada(s).";
    }
}
