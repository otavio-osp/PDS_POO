package br.ufv.ccf313.projeto.model;

public class Jogador {

    public static final int VIDA_PADRAO = 15;
    public static final int DINHEIRO_PADRAO = 150;

    private String nome;
    private int vida;
    private int dinheiro;
    private int score;
    private int waveAtual;

    public Jogador(String nome) {
        this(nome, VIDA_PADRAO, DINHEIRO_PADRAO);
    }

    public Jogador(String nome, int vidaInicial, int dinheiroInicial) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do jogador não pode ser nulo ou vazio.");
        }
        if (vidaInicial < 0) {
            throw new IllegalArgumentException("Vida inicial não pode ser negativa.");
        }
        if (dinheiroInicial < 0) {
            throw new IllegalArgumentException("Dinheiro inicial não pode ser negativo.");
        }
        this.nome = nome.trim();
        this.vida = vidaInicial;
        this.dinheiro = dinheiroInicial;
        this.score = 0;
        this.waveAtual = 1;
    }

    public void registrarInimigoDestruido(int recompensa) {
        if (recompensa > 0) {
            this.score += recompensa;
            this.dinheiro += recompensa;
        }
    }

    public void receberDano(int dano) {
        if (dano > 0) {
            this.vida -= dano;
            if (this.vida < 0) {
                this.vida = 0;
            }
        }
    }

    public boolean gastarDinheiro(int custo) {
        if (custo < 0) {
            return false;
        }
        if (this.dinheiro >= custo) {
            this.dinheiro -= custo;
            return true;
        }
        return false;
    }

    public void adicionarDinheiro(int quantia) {
        if (quantia > 0) {
            this.dinheiro += quantia;
        }
    }

    public void avancarWave() {
        this.waveAtual++;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getDinheiro() { return dinheiro; }
    public int getScore() { return score; }
    public int getWaveAtual() { return waveAtual; }

    @Override
    public String toString() {
        return "Jogador: " + nome + " | Vidas: " + vida + " | Conchas: " + dinheiro 
                + " | Score: " + score + " | Wave: " + waveAtual;
    }
}
