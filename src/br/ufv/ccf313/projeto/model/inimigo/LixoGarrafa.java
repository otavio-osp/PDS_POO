package br.ufv.ccf313.projeto.model.inimigo;

// Lixo de garrafa: rapido mas com pouca vida
// BRUNA: ajustar os valores de vida, velocidade, recompensa e dano conforme balanceamento
public class LixoGarrafa extends InimigoLixo {

    private static final int VIDA = 30;
    private static final int VELOCIDADE = 2; // anda 2 celulas por rodada
    private static final int RECOMPENSA = 15;
    private static final int DANO = 1;

    public LixoGarrafa(int posX, int posY) {
        super(posX, posY, VIDA, VELOCIDADE, RECOMPENSA, DANO);
    }

    @Override
    public String toString() {
        return "LixoGarrafa [Vida: " + getVidaAtual() + "/" + getVidaMaxima() + "]";
    }
}
