package com.example.usarcamera.classes;

public class Historico {
    private String nomeRemedio, principioAtivo;
    private int idRemedio;

    public Historico(String nomeRemedio, String principioAtivo, int idRemedio) {
        this.nomeRemedio = nomeRemedio;
        this.principioAtivo = principioAtivo;
        this.idRemedio = idRemedio;
    }

    public Historico() { }

    public String getNomeRemedio() {
        return nomeRemedio;
    }

    public void setNomeRemedio(String nomeRemedio) {
        this.nomeRemedio = nomeRemedio;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public void setPrincipioAtivo(String principioAtivo) {
        this.principioAtivo = principioAtivo;
    }

    public int getIdRemedio() {
        return idRemedio;
    }


}
