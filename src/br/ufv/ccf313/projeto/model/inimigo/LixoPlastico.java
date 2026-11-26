package br.ufv.ccf313.projeto.model.inimigo;

// lixo plástico: inimigo basico, velocidade normal e dano e vida também
public class LixoPlastico extends InimigoLixo {

    private static final int VIDA = 60;
    private static final int VELOCIDADE = 1; // anda 1 celula por rodada
    private static final int RECOMPENSA = 10; // XP/dinheiro ao morrer
    private static final int DANO = 1;        // dano ao jogador se passar

    public LixoPlastico(int posX, int posY) {
        super(posX, posY, VIDA, VELOCIDADE, RECOMPENSA, DANO);
    }

    @Override
    public String toString() {
        return "LixoPlastico [Vida: " + getVidaAtual() + "/" + getVidaMaxima() + "]";
    }
}
