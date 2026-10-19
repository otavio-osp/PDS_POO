package br.ufv.ccf313.projeto.model;

// Torre ágil de custo médio com alta cadência de disparo.
// A cada 5 ataques consecutivos, o próximo causa dano dobrado (crítico).
public class TartarugaDePente extends TorreTartaruga {

    private static final int CUSTO = 100;
    private static final double ALCANCE = 2.5;
    private static final int DANO_BASE = 20;
    private static final double VELOCIDADE_ATAQUE = 2.0;
    private static final double VELOCIDADE_PROJETIL = 3.0;
    private static final int ATAQUES_PARA_CRITICO = 5;

    private int contadorAtaques;

    public TartarugaDePente(int posX, int posY) {
        super(posX, posY,
                "Tartaruga-de-Pente",
                CUSTO,
                ALCANCE,
                DANO_BASE,
                VELOCIDADE_ATAQUE);
        this.contadorAtaques = 0;
    }

    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva())
            return;

        contadorAtaques++;

        boolean ehCritico = (contadorAtaques % ATAQUES_PARA_CRITICO == 0);
        int danoFinal = ehCritico ? getDano() * 2 : getDano();
        String tipoProjetil = ehCritico ? "nadadeira_critica" : "nadadeira";

        Projetil projetil = new Projetil(
                getPosX(),
                getPosY(),
                VELOCIDADE_PROJETIL,
                danoFinal,
                alvo,
                tipoProjetil);

        dispararProjetil(projetil);
        reiniciarCooldown();
    }

    public int getContadorAtaques() {
        return contadorAtaques;
    }

    public int getAtaquesParaProximoCritico() {
        return ATAQUES_PARA_CRITICO - (contadorAtaques % ATAQUES_PARA_CRITICO);
    }

    @Override
    public String toString() {
        return "TartarugaDePente em (" + getPosX() + ", " + getPosY() + ") | "
                + super.toString()
                + " | Crítico em: " + getAtaquesParaProximoCritico() + " ataque(s)";
    }
}
