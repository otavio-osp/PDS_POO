package br.ufv.ccf313.projeto.model.inimigo;

// lixo rede de pesca: lento e com muita vida
public class LixoRede extends InimigoLixo {

    private static final int VIDA = 150;
    private static final int VELOCIDADE = 0; // leva 2 rodadas para andar 1 celula
    private static final int RECOMPENSA = 30;
    private static final int DANO = 2;       // é a que causa mais dano ao jogador se passar

    public LixoRede(int posX, int posY) {
        super(posX, posY, VIDA, VELOCIDADE, RECOMPENSA, DANO);
    }

    @Override
    public String toString() {
        return "LixoRede [Vida: " + getVidaAtual() + "/" + getVidaMaxima() + "]";
    }
}
