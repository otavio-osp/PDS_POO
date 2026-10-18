package br.ufv.ccf313.projeto.model;

/**
 * Torre tartaruga do tipo Tartaruga-de-Couro (Dermochelys coriacea).
 * É a tartaruga mais robusta e cara do jogo, com alto dano e longo alcance,
 * mas com velocidade de ataque mais lenta.
 *
 * Ataque: Dispara um projétil de alto impacto contra o inimigo mais próximo.
 * 
 * @author Tomás
 */
public class TartarugaDeCouro extends TorreTartaruga {

    // Custo de compra (em conchas)
    private static final int CUSTO = 200;

    // Raio de alcance (em células do grid)
    private static final double ALCANCE = 4.0;

    // Dano por projétil disparado
    private static final int DANO = 60;

    // Ataques por segundo
    private static final double VELOCIDADE_ATAQUE = 0.8;

    // Velocidade de deslocamento do projétil disparado
    private static final double VELOCIDADE_PROJETIL = 2.5;

    /**
     * Cria uma Tartaruga-de-Couro na posição indicada do grid.
     *
     * @param posX Coluna do grid onde a torre será posicionada.
     * @param posY Linha do grid onde a torre será posicionada.
     */
    public TartarugaDeCouro(int posX, int posY) {
        super(posX, posY,
                "Tartaruga-de-Couro",
                CUSTO,
                ALCANCE,
                DANO,
                VELOCIDADE_ATAQUE);
    }

    /**
     * @param alvo O inimigo-alvo dentro do alcance da torre.
     */
    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva())
            return;

        // Cria o projétil na posição atual da torre
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
