package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga agil: custo medio, alcance curto, ataque rapido
// TOM: o ataque critico a cada 5 ataques pode entrar como upgrade ou comportamento base — a definir
public class TartarugaDePente extends TorreTartaruga {

    private static final int CUSTO = 100;
    private static final int ALCANCE = 2;
    private static final int DANO = 20;
    private static final int VELOCIDADE = 3;

    public TartarugaDePente(int posX, int posY) {
        super(posX, posY, "Tartaruga-de-Pente", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva()) return;
        alvo.receberDano(getDano());
    }

    @Override
    public String toString() {
        return "TartarugaDePente em (" + getPosX() + ", " + getPosY() + ") | " + super.toString();
    }
}
