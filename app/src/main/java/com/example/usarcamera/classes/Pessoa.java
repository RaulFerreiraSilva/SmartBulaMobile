package com.example.usarcamera.classes;

public class Pessoa {
    private String nome, sobreNome, dataNasc, email, senha;

    public Pessoa(String nome, String sobreNome, String dataNasc, String email, String senha) {
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.senha = senha;
    }

    public Pessoa(){}


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
