package br.ufv.ccf313.projeto.model;

// Projétil disparado por uma torre em direção a um inimigo.
// Persegue o alvo e aplica dano ao colidir.

public class Projetil extends Entidade {

    private final double velocidade;
    private final int dano;
    private final InimigoLixo alvo;
    private final String tipo;

    public Projetil(int posX, int posY, double velocidade, int dano, InimigoLixo alvo, String tipo) {
        super(posX, posY);
        this.velocidade = velocidade;
        this.dano = dano;
        this.alvo = alvo;
        this.tipo = tipo;
    }

    @Override
    public void atualizar() {
        if (!isAtiva() || alvo == null || !alvo.isAtiva()) {
            desativar();
            return;
        }

        double dx = alvo.getPosX() - this.getPosX();
        double dy = alvo.getPosY() - this.getPosY();
        double distancia = Math.sqrt(dx * dx + dy * dy);

        if (distancia < velocidade) {
            alvo.receberDano(dano);
            desativar();
            return;
        }

        double proporcao = velocidade / distancia;
        setPosX((int) Math.round(getPosX() + dx * proporcao));
        setPosY((int) Math.round(getPosY() + dy * proporcao));
    }

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
