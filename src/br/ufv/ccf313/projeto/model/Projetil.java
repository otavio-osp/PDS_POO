package br.ufv.ccf313.projeto.model;

/**
 * Representa um projétil disparado por uma torre tartaruga em direção a um
 * inimigo.
 * Herda de Entidade para participar do ciclo de atualização do GameLoop.
 * 
 * @author Tomás
 */
public class Projetil extends Entidade {

    // Velocidade de deslocamento do projétil (células por tick)
    private final double velocidade;

    // Dano causado ao colidir com um inimigo
    private final int dano;

    // Inimigo-alvo que este projétil persegue
    private final InimigoLixo alvo;

    // Tipo visual/comportamental do projétil (ex: "bolha", "nadadeira",
    // "jato_dagua")
    private final String tipo;

    /**
     * Cria um novo projétil a partir de uma posição de origem em direção a um alvo.
     *
     * @param posX       Posição X de origem (coluna da torre que disparou).
     * @param posY       Posição Y de origem (linha da torre que disparou).
     * @param velocidade Velocidade de deslocamento em células por tick.
     * @param dano       Dano causado ao atingir o alvo.
     * @param alvo       Inimigo que será perseguido.
     * @param tipo       Identificador do tipo do projétil (para uso visual/futuro).
     */
    public Projetil(int posX, int posY, double velocidade, int dano, InimigoLixo alvo, String tipo) {
        super(posX, posY);
        this.velocidade = velocidade;
        this.dano = dano;
        this.alvo = alvo;
        this.tipo = tipo;
    }

    /**
     * Atualiza a posição do projétil, movendo-o em direção ao alvo.
     * Utiliza lógica de perseguição:
     * calcula a direção até o alvo e avança na velocidade configurada.
     * Se atingir o alvo (distância menor que a velocidade), aplica o dano e se
     * desativa.
     */
    @Override
    public void atualizar() {
        if (!isAtiva() || alvo == null || !alvo.isAtiva()) {
            desativar(); // Alvo sumiu, projétil se perde
            return;
        }

        double dx = alvo.getPosX() - this.getPosX();
        double dy = alvo.getPosY() - this.getPosY();
        double distancia = Math.sqrt(dx * dx + dy * dy);

        // Chegou ao alvo
        if (distancia < velocidade) {
            alvo.receberDano(dano);
            desativar();
            return;
        }

        // Move em direção ao alvo
        double proporcao = velocidade / distancia;
        setPosX((int) Math.round(getPosX() + dx * proporcao));
        setPosY((int) Math.round(getPosY() + dy * proporcao));
    }

    // Getters

    public double getVelocidade() {
        return velocidade;
    }

    public int getDano() {
        return dano;
    }

    public InimigoLixo getAlvo() {
        return alvo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Projetil[" + tipo + "] -> Dano: " + dano
                + " | Vel: " + velocidade
                + " | Alvo: " + (alvo != null ? alvo.getClass().getSimpleName() : "nenhum");
    }
}
