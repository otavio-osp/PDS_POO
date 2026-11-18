package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Tartaruga de Couro: alto custo, dano massivo, longo alcance, ataque lento
// Nivel 1 (base): Custo 200, Dano 60, Alcance 4, Velocidade 1
// Nivel 2:        +30 dano
// Nivel 3:        +30 dano, +1 alcance
public class TartarugaDeCouro extends TorreTartaruga {

    private static final int CUSTO = 200;
    private static final int ALCANCE = 4;
    private static final int DANO = 60;
    private static final int VELOCIDADE = 1;
    private static final int CUSTO_UPGRADE = 120; // ~60% do custo base

    public TartarugaDeCouro(int posX, int posY) {
        super(posX, posY, "Tartaruga-de-Couro", CUSTO, ALCANCE, DANO, VELOCIDADE);
    }

    @Override
    public void evoluir() {
        if (!podeEvoluir()) return;

        int nivelAtual = getNivel();

        if (nivelAtual == 1) {
            // Nivel 1 -> 2: +30 dano bruto
            setDano(getDano() + 30);
        } else if (nivelAtual == 2) {
            // Nivel 2 -> 3: +30 dano, +1 alcance
            setDano(getDano() + 30);
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
        return "TartarugaDeCouro em (" + getPosX() + ", " + getPosY() + ") | " + super.toString();
    }
}
