package br.ufv.ccf313.projeto.model;

/**
 * Torre tartaruga do tipo Tartaruga-de-Pente (Eretmochelys imbricata).
 * É uma tartaruga ágil e de custo médio, especializada em ataques rápidos
 * com projéteis mais fracos, mas com alta cadência de disparo.
 * Ideal para combater múltiplos inimigos fracos.
 *
 * Ataque especial: "Rajada de Nadadeiras" — a cada 5 ataques consecutivos,
 * o próximo projétil causa dano dobrado (bônus crítico).
 *
 * @author Tomás
 */
public class TartarugaDePente extends TorreTartaruga {

    // Custo de compra (em conchas)
    private static final int CUSTO = 100;

    // Raio de alcance (em células do grid)
    private static final double ALCANCE = 2.5;

    // Dano base por projétil
    private static final int DANO_BASE = 20;

    // Ataques por segundo (alta cadência)
    private static final double VELOCIDADE_ATAQUE = 2.0;

    // Velocidade de deslocamento do projétil
    private static final double VELOCIDADE_PROJETIL = 3.0;

    // A cada ATAQUES_PARA_CRITICO ataques, o próximo é crítico (dano dobrado)
    private static final int ATAQUES_PARA_CRITICO = 5;

    // Contador interno de ataques realizados desde o último crítico
    private int contadorAtaques;

    /**
     * Cria uma Tartaruga-de-Pente na posição indicada do grid.
     *
     * @param posX Coluna do grid onde a torre será posicionada.
     * @param posY Linha do grid onde a torre será posicionada.
     */
    public TartarugaDePente(int posX, int posY) {
        super(posX, posY,
                "Tartaruga-de-Pente",
                CUSTO,
                ALCANCE,
                DANO_BASE,
                VELOCIDADE_ATAQUE);
        this.contadorAtaques = 0;
    }

    /**
     * Implementação do ataque da Tartaruga-de-Pente.
     * Dispara projéteis rápidos do tipo "nadadeira".
     * A cada {@code ATAQUES_PARA_CRITICO} ataques, o próximo é crítico (dano x2).
     *
     * @param alvo O inimigo-alvo dentro do alcance da torre.
     */
    @Override
    public void atacar(InimigoLixo alvo) {
        if (alvo == null || !alvo.isAtiva())
            return;

        contadorAtaques++;

        // Verifica se é um ataque crítico
        boolean ehCritico = (contadorAtaques % ATAQUES_PARA_CRITICO == 0);
        int danoFinal = ehCritico ? getDano() * 2 : getDano();
        String tipoProjetil = ehCritico ? "nadadeira_critica" : "nadadeira";

        // Cria e dispara o projétil
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

    /**
     * Retorna o número de ataques realizados desde o início do jogo.
     * Útil para a interface gráfica exibir progresso até o próximo crítico.
     *
     * @return Número total de ataques realizados.
     */
    public int getContadorAtaques() {
        return contadorAtaques;
    }

    /**
     * Retorna quantos ataques faltam para o próximo crítico.
     *
     * @return Número de ataques restantes para o próximo crítico.
     */
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
