package com.example.usarcamera.classes;

public class Alergia {
    private int id_Alergia;
    private String tipo_Alergia;

    public Alergia(int id_Alergia, String tipo_Alergia) {
        this.id_Alergia = id_Alergia;
        this.tipo_Alergia = tipo_Alergia;
    }

    public int getId_Alergia() {
        return id_Alergia;
    }

    public String getTipo_Alergia() {
        return tipo_Alergia;
    }
}
