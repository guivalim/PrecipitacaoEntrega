package br.edu.precipitacao.exception;

public class AppException extends RuntimeException {

    private final String tipo;

    public AppException(String tipo, String mensagem) {
        super(mensagem);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public static AppException arquivoNaoEncontrado(String caminho) {
        return new AppException("ARQUIVO_NAO_ENCONTRADO",
                "Arquivo nao encontrado: " + caminho + "\nVerifique se o caminho esta correto.");
    }

    public static AppException arquivoVazio(String caminho) {
        return new AppException("ARQUIVO_VAZIO",
                "O arquivo nao possui dados: " + caminho + "\nVerifique se o CSV tem linhas alem do cabecalho.");
    }

    public static AppException cabecalhoInvalido(String esperado, String encontrado) {
        return new AppException("CABECALHO_INVALIDO",
                "Cabecalho invalido.\n  Esperado:   " + esperado + "\n  Encontrado: " + encontrado);
    }

    public static AppException dataInvalida(int linha, String valor) {
        return new AppException("DATA_INVALIDA",
                "Data invalida na linha " + linha + ": '" + valor + "'\nFormatos aceitos: dd/MM/yyyy ou yyyy-MM-dd");
    }

    public static AppException valorInvalido(int linha, String valor) {
        return new AppException("VALOR_INVALIDO",
                "Valor invalido na linha " + linha + ": '" + valor + "'\nDeve ser um numero maior ou igual a zero.");
    }

    public static AppException semDados(int mes, int ano) {
        return new AppException("SEM_DADOS",
                "Nenhum dado encontrado para o periodo: " + mes + "/" + ano);
    }
}
