package com.example.usarcamera.classes;

import androidx.annotation.NonNull;

public class Remedio {
    private int id;
    private String bula, resumoBula, principioAtivo;

    public Remedio(int id, String bula, String resumoBula, String principioAtivo) {
        this.id = id;
        this.bula = bula;
        this.resumoBula = resumoBula;
        this.principioAtivo = principioAtivo;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Princípio-Ativo: " + principioAtivo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBula() {
        return bula;
    }

    public void setBula(String bula) {
        this.bula = bula;
    }

    public String getResumoBula() {
        return resumoBula;
    }

    public void setResumoBula(String resumoBula) {
        this.resumoBula = resumoBula;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public void setPrincipioAtivo(String principioAtivo) {
        this.principioAtivo = principioAtivo;
    }
}
