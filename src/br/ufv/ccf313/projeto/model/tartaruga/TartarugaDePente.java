package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga Pente: custo medio, alcance curto, ataque rapido
// O ataque critico a cada 5 ataques pode entrar como upgrade
public class TartarugaDePente extends TorreTartaruga {

    private static final int CUSTO = 100;
    private static final int ALCANCE = 2;
    private static final int DANO = 20;
    private static final int VELOCIDADE = 3;

    public TartarugaDePente(int posX, int posY) {
        super(posX, posY, "Tartaruga-de-Pente", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    // Métodos de evolução
    public void setDano() {
        super.setDano((int) (getDano() * 1.35)); // Aumenta o dano em 20%;
    }

    public void setVelocidade() {
        super.setVelocidade((int) (getVelocidade() * 1.5)); // Aumenta a velocidade em 50%
    }

    public void setAlcance() {
        super.setAlcance(getAlcance() + 1); // Aumenta o alcance em 1
    }

    public void setCusto() {
        super.setCusto(getCusto() + 35); // Aumenta o custo em 35
    }

    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva())
            return;
        alvo.receberDano(getDano());
    }

    @Override
    public String toString() {
        return "TartarugaDePente em (" + getPosX() + ", " + getPosY() + ") | " + super.toString();
    }
}
