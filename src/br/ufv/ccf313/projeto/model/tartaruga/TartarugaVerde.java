package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga Verde: custo baixo, stats medianos
public class TartarugaVerde extends TorreTartaruga {

    private static final int CUSTO = 50;
    private static final int ALCANCE = 2;
    private static final int DANO = 15;
    private static final int VELOCIDADE = 2;

    public TartarugaVerde(int posX, int posY) {
        super(posX, posY, "Tartaruga-Verde", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    // Métodos de evolução
    public void setDano() {
        super.setDano((int) (getDano() * 1.20)); // Aumenta o dano em 20%;
    }

    public void setVelocidade() {
        super.setVelocidade((int) (getVelocidade() * 1.25)); // Aumenta a velocidade em 25%
    }

    public void setAlcance() {
        super.setAlcance(getAlcance() + 1); // Aumenta o alcance em 1
    }

    public void setCusto() {
        super.setCusto(getCusto() + 20); // Aumenta o custo em 20
    }

    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva())
            return;
        alvo.receberDano(getDano());
    }

    @Override
    public String toString() {
        return "TartarugaVerde em (" + getPosX() + ", " + getPosY() + ") | " + super.toString();
    }
}
