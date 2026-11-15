package br.ufv.ccf313.projeto.model;

// RESPONSABILIDADE: Otavio
// Esta classe representa o estado do jogador durante uma partida.
// Ela deve ser instanciada no inicio de cada partida e descartada ao fim.
// Ao Game Over, os dados desta classe sao passados para GerenciadorRanking.salvarPartida().
public class Jogador {

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

    // OTAVIO: inicializar o jogador com nome, vida inicial e dinheiro inicial.
    // Os valores iniciais de vida e dinheiro serao definidos pelo balanceamento do jogo.
    public Jogador(String nome, int vidaInicial, int dinheiroInicial) {
        this.nome = nome;
        this.vida = vidaInicial;
        this.dinheiro = dinheiroInicial;
        this.score = 0;
        this.waveAtual = 1;
    }

    // OTAVIO: chamado pela logica de combate (Leticia) quando um inimigo e destruido.
    // Soma o XP do inimigo ao score e libera o dinheiro da recompensa para o jogador.
    // Exemplo de uso: jogador.registrarInimigoDestruido(inimigo.getRecompensa());
    public void registrarInimigoDestruido(int recompensa) {
        this.score += recompensa;
        this.dinheiro += recompensa;
    }

    // OTAVIO: chamado quando um inimigo chega ao fim do mapa sem ser destruido.
    // Desconta o dano do inimigo da vida do jogador.
    // Exemplo de uso: jogador.receberDano(inimigo.getDano());
    public void receberDano(int dano) {
        this.vida -= dano;
        if (this.vida < 0) {
            this.vida = 0;
        }
    }

    // OTAVIO: chamado ao tentar comprar uma tartaruga ou upgrade.
    // Verifica se ha dinheiro suficiente antes de descontar.
    // Retorna true se a compra foi realizada, false se nao havia dinheiro.
    // Exemplo de uso: if (jogador.gastarDinheiro(custo)) { colocarTartaruga(); }
    public boolean gastarDinheiro(int custo) {
        if (this.dinheiro >= custo) {
            this.dinheiro -= custo;
            return true;
        }
        return false;
    }

    // OTAVIO: chamado pela logica de ondas (Helena) ao iniciar uma nova wave.
    public void avancarWave() {
        this.waveAtual++;
    }

    // OTAVIO: retorna true se o jogador ainda esta vivo (vida > 0).
    // Usado pelo GameLoop para verificar condicao de derrota.
    public boolean estaVivo() {
        return this.vida > 0;
    }

    public String getNome() { return nome; }
    public int getVida() { return vida; }
    public int getDinheiro() { return dinheiro; }
    public int getScore() { return score; }
    public int getWaveAtual() { return waveAtual; }
}
