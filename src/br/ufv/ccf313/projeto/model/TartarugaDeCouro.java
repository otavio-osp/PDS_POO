package br.ufv.ccf313.projeto.model;

/**
 * Tartaruga-de-Couro (Dermochelys coriacea).
 * Torre robusta de alto dano e longo alcance, com cadência mais lenta.
 *
 * @author Tomás
 */
public class TartarugaDeCouro extends TorreTartaruga {

    private static final int CUSTO = 200;
    private static final double ALCANCE = 4.0;
    private static final int DANO = 60;
    private static final double VELOCIDADE_ATAQUE = 0.8;
    private static final double VELOCIDADE_PROJETIL = 2.5;

    public TartarugaDeCouro(int posX, int posY) {
        super(posX, posY,
                "Tartaruga-de-Couro",
                CUSTO,
                ALCANCE,
                DANO,
                VELOCIDADE_ATAQUE);
    }

    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva())
            return;

        Projetil projetil = new Projetil(
                getPosX(),
                getPosY(),
                VELOCIDADE_PROJETIL,
                getDano(),
                alvo,
                "jato_dagua");

        dispararProjetil(projetil);
        reiniciarCooldown();
    }

    @Override
    public String toString() {
        return "TartarugaDeCouro em (" + getPosX() + ", " + getPosY() + ") | "
                + super.toString();
    }
}
