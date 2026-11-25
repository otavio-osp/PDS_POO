package br.ufv.ccf313.projeto.model;

// RESPONSABILIDADE: Otavio
// Esta classe representa o estado do jogador durante uma partida.
// Ela deve ser instanciada no inicio de cada partida e descartada ao fim.
// Ao Game Over, os dados desta classe sao passados para GerenciadorRanking.salvarPartida().
public class Jogador {

    public static final int VIDA_PADRAO = 15;
    public static final int DINHEIRO_PADRAO = 150;

    // Nome digitado pelo jogador antes de comecar a partida
    private String nome;

    // Quantidade de inimigos que podem chegar ao fim do mapa antes do jogador perder.
    // Cada inimigo que passa desconta 1 de vida (ou o valor do atributo dano do inimigo).
    // Quando vida chegar a 0, o jogo termina com derrota.
    private int vida;

    // Quantidade de dinheiro disponivel para comprar tartarugas ou upgrades.
    // Aumenta quando um inimigo e destruido (valor = recompensa do inimigo).
    // Diminui quando o jogador compra uma tartaruga ou upgrade.
    private int dinheiro;

    // Soma total de XP acumulado durante a partida.
    // Aumenta sempre que um inimigo e destruido (valor = recompensa do inimigo).
    // Este valor e salvo no ranking ao fim da partida.
    private int score;

    // Numero da wave atual em que o jogador se encontra.
    // Comeca em 1 e e incrementado pela logica de ondas (Helena).
    // Este valor e salvo no ranking ao fim da partida.
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

    // Chamado pela logica de combate (Leticia) quando um inimigo e destruido.
    // Soma o XP do inimigo ao score e libera o dinheiro da recompensa para o jogador.
    // Exemplo de uso: jogador.registrarInimigoDestruido(inimigo.getRecompensa());
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

    // Chamado pela logica de ondas (Helena) ao iniciar uma nova wave.
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
