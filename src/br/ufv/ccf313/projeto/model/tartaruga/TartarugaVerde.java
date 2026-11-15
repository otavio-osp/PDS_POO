package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga equilibrada: custo baixo, stats medianos — boa opcao inicial
// TOM: definir os valores finais de balanceamento
public class TartarugaVerde extends TorreTartaruga {

    private static final int CUSTO = 50;
    private static final int ALCANCE = 2;
    private static final int DANO = 15;
    private static final int VELOCIDADE = 2;

    public TartarugaVerde(int posX, int posY) {
        super(posX, posY, "Tartaruga-Verde", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva()) return;
        alvo.receberDano(getDano());
    }

    @Override
    public String toString() {
        return "TartarugaVerde em (" + getPosX() + ", " + getPosY() + ") | " + super.toString();
    }
}
