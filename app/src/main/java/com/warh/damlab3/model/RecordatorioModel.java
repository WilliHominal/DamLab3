package com.warh.damlab3.model;

import android.support.annotation.NonNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName="RECORDATORIO")
public class RecordatorioModel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name="ID")
    private int id;

    @ColumnInfo(name="TEXTO")
    private String texto;

    @ColumnInfo(name="FECHA")
    private Date fecha;

    public RecordatorioModel () { }

    public RecordatorioModel(final String texto, final Date fecha) {
        this.texto = texto;
        this.fecha = fecha;
    }

    public RecordatorioModel(int id, final String texto, final Date fecha) {
        this.id = id;
        this.texto = texto;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(final int id) { this.id = id; }
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
