package br.ufv.ccf313.projeto.model.mapa;

import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;

public class Celula {

    private int linha;
    private int coluna;
    private TipoCelula tipo;
    private TorreTartaruga tartaruga;

    public Celula(int linha, int coluna, TipoCelula tipo) {
        this.linha = linha;
        this.coluna = coluna;
        this.tipo = tipo;
        this.tartaruga = null;
    }

    public boolean colocarTartaruga(TorreTartaruga torre) {
        if (this.tipo != TipoCelula.LIVRE) return false;
        this.tartaruga = torre;
        this.tipo = TipoCelula.OCUPADA;
        return true;
    }

    public TorreTartaruga removerTartaruga() {
        TorreTartaruga removida = this.tartaruga;
        this.tartaruga = null;
        this.tipo = TipoCelula.LIVRE;
        return removida;
    }

    public boolean estaLivre() { return this.tipo == TipoCelula.LIVRE; }
    public boolean eCaminho() { return this.tipo == TipoCelula.CAMINHO; }
    public boolean estaOcupada() { return this.tipo == TipoCelula.OCUPADA; }
    public boolean estaBloqueada() { return this.tipo == TipoCelula.BLOQUEADA; }

    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    public TipoCelula getTipo() { return tipo; }
    public TorreTartaruga getTartaruga() { return tartaruga; }

    @Override
    public String toString() {
        return "Celula(" + linha + ", " + coluna + ") [" + tipo + (tartaruga != null ? " - " + tartaruga.getNome() : "") + "]";
    }
}
