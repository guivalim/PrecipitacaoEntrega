package br.edu.precipitacao.util;

import br.edu.precipitacao.exception.AppException;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

    public static final String SEPARADOR = ";";
    public static final String CABECALHO_ESPERADO = "data;precipitacao";
    public static final int COLUNAS_MINIMAS = 2;

    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Validator() {}

    public static void validarArquivo(String caminho) {
        File arquivo = new File(caminho);
        if (!arquivo.exists() || !arquivo.isFile()) {
            throw AppException.arquivoNaoEncontrado(caminho);
        }
    }

    public static void validarCabecalho(String cabecalhoLido, String caminho) {
        if (cabecalhoLido == null) {
            throw AppException.arquivoVazio(caminho);
        }
        if (!cabecalhoLido.trim().toLowerCase().equals(CABECALHO_ESPERADO)) {
            throw AppException.cabecalhoInvalido(CABECALHO_ESPERADO, cabecalhoLido.trim());
        }
    }

    public static LocalDate parseData(String texto, int numLinha) {
        String limpo = texto.trim();
        try {
            return LocalDate.parse(limpo, FORMATO_BR);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(limpo, FORMATO_ISO);
            } catch (DateTimeParseException e2) {
                throw AppException.dataInvalida(numLinha, texto.trim());
            }
        }
    }

    public static double parsePrecipitacao(String texto, int numLinha) {
        try {
            double valor = Double.parseDouble(texto.trim().replace(",", "."));
            if (valor < 0) {
                throw AppException.valorInvalido(numLinha, texto.trim() + " (valor negativo)");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw AppException.valorInvalido(numLinha, texto.trim());
        }
    }

    public static String[] splitLinha(String linha, int numLinha) {
        if (linha == null) return null;
        String limpa = linha.trim();
        if (limpa.isEmpty() || limpa.startsWith("#")) return null;
        String[] partes = limpa.split(SEPARADOR, -1);
        if (partes.length < COLUNAS_MINIMAS) {
            throw new AppException("COLUNA_FALTANDO",
                    "Linha " + numLinha + " tem apenas " + partes.length + " coluna(s). Conteudo: '" + limpa + "'");
        }
        return partes;
    }
}
