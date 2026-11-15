package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga robusta: alto dano, longo alcance, ataque lento
public class TartarugaDeCouro extends TorreTartaruga {

    private static final int CUSTO = 200;
    private static final int ALCANCE = 4;
    private static final int DANO = 60;
    private static final int VELOCIDADE = 1;

    public TartarugaDeCouro(int posX, int posY) {
        super(posX, posY, "Tartaruga-de-Couro", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva()) return;
        alvo.receberDano(getDano());
    }

    @Override
    public String toString() {
        return "TartarugaDeCouro em (" + getPosX() + ", " + getPosY() + ") | " + super.toString();
    }
}
