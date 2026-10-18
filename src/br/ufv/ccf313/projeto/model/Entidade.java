package br.ufv.ccf313.projeto.model;

// Classe abstrata base para todas as entidades do jogo.

public abstract class Entidade {

    // Posição da entidade no grid do mapa (coluna e linha)
    private int posX;
    private int posY;
    private boolean ativa;

    /**
     * Construtor base da entidade.
     * 
     * @param posX Posição horizontal (coluna) no grid.
     * @param posY Posição vertical (linha) no grid.
     */
    public Entidade(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.ativa = true;
    }

    // Método de atualização chamado a cada ciclo do GameLoop.
    // Cada subclasse define seu próprio comportamento de atualização.
    public abstract void atualizar();

    // Getters e Setters

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

    public boolean isAtiva() {
        return ativa;
    }

    // Desativa a entidade (marca para remoção do jogo).
    public void desativar() {
        this.ativa = false;
    }
}
