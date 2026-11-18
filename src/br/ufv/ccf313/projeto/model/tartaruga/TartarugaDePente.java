package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga de Pente: custo medio, ataque rapido, foco em velocidade/cadencia
// Nivel 1 (base): Custo 100, Dano 20, Alcance 2, Velocidade 3
// Nivel 2:        +1 velocidade, +5 dano
// Nivel 3:        +1 velocidade, +1 alcance
public class TartarugaDePente extends TorreTartaruga {

    private static final int CUSTO = 100;
    private static final int ALCANCE = 2;
    private static final int DANO = 20;
    private static final int VELOCIDADE = 3;
    private static final int CUSTO_UPGRADE = 60; // ~60% do custo base

    public TartarugaDePente(int posX, int posY) {
        super(posX, posY, "Tartaruga-de-Pente", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    @Override
    public void evoluir() {
        if (!podeEvoluir()) return;

        int nivelAtual = getNivel();

        if (nivelAtual == 1) {
            // Nivel 1 -> 2: +1 velocidade, +5 dano
            setVelocidade(getVelocidade() + 1);
            setDano(getDano() + 5);
        } else if (nivelAtual == 2) {
            // Nivel 2 -> 3: +1 velocidade, +1 alcance
            setVelocidade(getVelocidade() + 1);
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
        return "TartarugaDePente em (" + getPosX() + ", " + getPosY() + ") | " + super.toString();
    }
}
