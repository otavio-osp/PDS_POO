package br.ufv.ccf313.projeto.model;

// Classe abstrata que representa um inimigo (lixo marinho) que percorre o caminho do mapa.
// Será implementada em detalhes pela Bruna (GarrafaPet, RedeFantasma, Microplastico).
// Este arquivo serve como contrato mínimo para que as torres possam referenciar os inimigos.

public abstract class InimigoLixo extends Entidade {

    private int vidaMaxima;
    private int vidaAtual;
    private double velocidade; // Velocidade de movimento em células/segundo
    private int recompensa; // Conchas concedidas ao ser destruído

    /**
     * Construtor de InimigoLixo.
     *
     * @param posX       Coluna inicial no grid.
     * @param posY       Linha inicial no grid.
     * @param vidaMaxima Pontos de vida máximos.
     * @param velocidade Velocidade de deslocamento.
     * @param recompensa Conchas ganhas ao destruir este inimigo.
     */
    public InimigoLixo(int posX, int posY, int vidaMaxima, double velocidade, int recompensa) {
        super(posX, posY);
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;
        this.recompensa = recompensa;
    }

    /**
     * Aplica dano a este inimigo.
     * Se a vida chegar a zero ou menos, o inimigo é desativado.
     *
     * @param dano Quantidade de dano recebida.
     */
    public void receberDano(int dano) {
        this.vidaAtual -= dano;
        if (this.vidaAtual <= 0) {
            this.vidaAtual = 0;
            desativar();
        }
    }

    /**
     * Verifica se o inimigo foi destruído.
     *
     * @return true se a vida atual for zero.
     */
    public boolean estaDestruido() {
        return this.vidaAtual <= 0;
    }

    // Getters

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getVidaAtual() {
        return vidaAtual;
    }

    public double getVelocidade() {
        return velocidade;
    }

    public int getRecompensa() {
        return recompensa;
    }
}
