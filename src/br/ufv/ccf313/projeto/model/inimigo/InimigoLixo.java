package br.ufv.ccf313.projeto.model.inimigo;

import br.ufv.ccf313.projeto.model.Entidade;

// Classe pai de todos os tipos de lixo (inimigos).
// Cada inimigo filho define seus proprios atributos de vida, velocidade, recompensa e dano.
// BRUNA: criar as classes filhas concretas (ex: LixoPlastico, LixoGarrafa, LixoRede)
public abstract class InimigoLixo extends Entidade {

    private int vidaMaxima;
    private int vidaAtual;
    private int velocidade;  // quantas celulas anda por rodada (0 = leva 2 rodadas para andar 1)
    private int recompensa;  // XP/dinheiro liberado ao jogador quando este inimigo morre
    private int dano;        // dano descontado da vida do jogador se este inimigo passar

    public InimigoLixo(int posX, int posY, int vidaMaxima, int velocidade, int recompensa, int dano) {
        super(posX, posY);
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;
        this.recompensa = recompensa;
        this.dano = dano;
    }

    // Aplica dano neste inimigo.
    // Chamado pela logica de combate (Leticia).
    // Quando vidaAtual chegar a 0, chamar jogador.registrarInimigoDestruido(recompensa)
    public void receberDano(int dano) {
        if (isAtiva() && dano > 0) {
            this.vidaAtual -= dano;
            if (this.vidaAtual <= 0) {
                this.vidaAtual = 0;
                desativar();
                // INTEGRACAO PENDENTE (Leticia - Combate):
                // Quando chegar aqui, chamar no modulo de combate:
                //   jogador.registrarInimigoDestruido(this.getRecompensa());
                // Nao chamar aqui diretamente pois este modulo nao tem acesso ao Jogador.
            }
        }
    }

    // Retorna true se o inimigo foi destruido (vida zerada)
    public boolean estaDestruido() {
        return !isAtiva() || this.vidaAtual <= 0;
    }

    // BRUNA: logica de movimento pelo caminho do mapa sera implementada nas classes filhas
    // ou no GerenciadorOndas (Helena), de acordo com o combinado entre as duas

    public int getVidaMaxima() { return vidaMaxima; }
    public int getVidaAtual() { return vidaAtual; }
    public int getVelocidade() { return velocidade; }
    public int getRecompensa() { return recompensa; }
    public int getDano() { return dano; }

    // BRUNA: upgrades por wave aumentam os atributos do inimigo
    // Helena define o valor do bust, Bruna aplica aqui
    public void aplicarBustDeWave(int bonusVida, int bonusVelocidade) {
        this.vidaMaxima += bonusVida;
        this.vidaAtual = this.vidaMaxima;
        this.velocidade += bonusVelocidade;
    }
}
