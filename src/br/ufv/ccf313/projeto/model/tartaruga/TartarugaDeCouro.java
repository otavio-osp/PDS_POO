package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga de Couro: alto dano, longo alcance, ataque lento
public class TartarugaDeCouro extends TorreTartaruga {

    private static final int CUSTO = 200;
    private static final int ALCANCE = 4;
    private static final int DANO = 60;
    private static final int VELOCIDADE = 1;

    public TartarugaDeCouro(int posX, int posY) {
        super(posX, posY, "Tartaruga-de-Couro", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    // Métodos de evolução
    public void setDano() {
        super.setDano((int) (getDano() * 1.5)); // Aumenta o dano em 50%;
    }

    public void setVelocidade() {
        super.setVelocidade((int) (getVelocidade() * 1.20)); // Aumenta a velocidade em 20%
    }

    public void setAlcance() {
        super.setAlcance(getAlcance() + 1); // Aumenta o alcance em 1
    }

    public void setCusto() {
        super.setCusto(getCusto() + 50); // Aumenta o custo em 50
    }

    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva())
            return;
        alvo.receberDano(getDano());
    }

    @Override
    public String toString() {
        return "TartarugaDeCouro em (" + getPosX() + ", " + getPosY() + ") | " + super.toString();
    }
}
