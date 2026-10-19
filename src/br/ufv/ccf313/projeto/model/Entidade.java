package br.ufv.ccf313.projeto.model;

public abstract class Entidade {

    private int posX;
    private int posY;
    private boolean ativa;

    public Entidade() {
        this(0, 0);
    }

    public Entidade(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.ativa = true;
    }

    public void atualizar() {
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setPosicao(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void ativar() {
        this.ativa = true;
    }

    public void desativar() {
        this.ativa = false;
    }
}
