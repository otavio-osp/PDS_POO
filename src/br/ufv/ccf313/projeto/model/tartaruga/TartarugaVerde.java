package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga Verde: custo baixo, stats medianos, evolucao balanceada
// Nivel 1 (base): Custo 50, Dano 15, Alcance 2, Velocidade 2
// Nivel 2:        +5 dano, +1 velocidade
// Nivel 3:        +5 dano, +1 alcance
public class TartarugaVerde extends TorreTartaruga {

    private static final int CUSTO = 50;
    private static final int ALCANCE = 2;
    private static final int DANO = 15;
    private static final int VELOCIDADE = 2;
    private static final int CUSTO_UPGRADE = 30; // ~60% do custo base

    public TartarugaVerde(int posX, int posY) {
        super(posX, posY, "Tartaruga-Verde", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    @Override
    public void evoluir() {
        if (!podeEvoluir()) return;

        int nivelAtual = getNivel();

        if (nivelAtual == 1) {
            // Nivel 1 -> 2: +5 dano, +1 velocidade
            setDano(getDano() + 5);
            setVelocidade(getVelocidade() + 1);
        } else if (nivelAtual == 2) {
            // Nivel 2 -> 3: +5 dano, +1 alcance
            setDano(getDano() + 5);
            setAlcance(getAlcance() + 1);
        }

        incrementarNivel();
    }

    @Override
    public int getCustoUpgrade() {
        return CUSTO_UPGRADE;
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
