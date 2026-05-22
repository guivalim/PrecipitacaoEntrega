package br.edu.precipitacao.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Record {

    private LocalDate data;
    private double precipitacao;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Record(LocalDate data, double precipitacao) {
        this.data = data;
        this.precipitacao = precipitacao;
    }

    public LocalDate getData() {
        return data;
    }

    public double getPrecipitacao() {
        return precipitacao;
    }

    public int getAno() {
        return data.getYear();
    }

    public int getMes() {
        return data.getMonthValue();
    }

    public int getDia() {
        return data.getDayOfMonth();
    }

    @Override
    public String toString() {
        return data.format(FORMATO) + " -> " + precipitacao + " mm";
    }
}
