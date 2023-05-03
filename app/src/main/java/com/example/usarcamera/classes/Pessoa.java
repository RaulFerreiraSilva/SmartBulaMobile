package com.example.usarcamera.classes;

public class Pessoa {
    private String nome, sobreNome, senha, email, dataNasc;

    public Pessoa(String nome, String sobreNome, String senha, String email, String dataNasc) {
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.senha = senha;
        this.email = email;
        this.dataNasc = dataNasc;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }
}
