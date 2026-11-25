package br.ufv.ccf313.projeto.model.mapa;

import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;

// Representa uma celula individual da grade do mapa.
// Cada celula tem uma posicao (linha, coluna), um tipo e pode ou nao conter uma tartaruga.
public class Celula {

    private int linha;
    private int coluna;
    private TipoCelula tipo;
    private TorreTartaruga tartaruga; // null quando celula esta livre

    public Celula(int linha, int coluna, TipoCelula tipo) {
        this.linha = linha;
        this.coluna = coluna;
        this.tipo = tipo;
        this.tartaruga = null;
    }

    // Posiciona uma tartaruga nesta celula e marca como OCUPADA.
    // Chamado por Mapa.colocarTartaruga() apos verificar disponibilidade e dinheiro do jogador.
    // Retorna false se a celula nao estiver LIVRE.
    public boolean colocarTartaruga(TorreTartaruga torre) {
        if (this.tipo != TipoCelula.LIVRE) return false;
        this.tartaruga = torre;
        this.tipo = TipoCelula.OCUPADA;
        if (torre != null) {
            torre.setPosicao(coluna, linha);
        }
        return true;
    }

    // Remove a tartaruga desta celula e volta o tipo para LIVRE.
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
