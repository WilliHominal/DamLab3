package com.warh.damlab3.model;

import java.util.Date;
import java.util.Objects;

public class RecordatorioModel {
    private String texto;
    private Date fecha;

    public RecordatorioModel(final String texto, final Date fecha) {
        this.texto = texto;
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }
    public void setTexto(final String texto) {
        this.texto = texto;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(final Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !getClass().equals(other.getClass())) {
            return false;
        }
        final RecordatorioModel that = (RecordatorioModel) other;
        return Objects.equals(this.texto, that.texto) && Objects.equals(this.fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texto) + Objects.hash(fecha);
    }
}
