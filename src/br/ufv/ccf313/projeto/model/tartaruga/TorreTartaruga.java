package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.Entidade;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Classe pai de todas as tartarugas (torres).
public abstract class TorreTartaruga extends Entidade {

    public static final int NIVEL_MAXIMO = 3;

    private String nome;
    private int custo;
    private int alcance;
    private int dano;   
    private int velocidade; // quantos ataques da antes do inimigo andar uma celula
    private int nivel; // nivel atual da tartaruga

    public TorreTartaruga(int posX, int posY, String nome, int custo, int alcance, int dano, int velocidade) {
        super(posX, posY);
        this.nome = nome;
        this.custo = custo;
        this.alcance = alcance;
        this.dano = dano;
        this.velocidade = velocidade;
        this.nivel = 1;
    }

    // Cada tartaruga filha implementa como aplica o dano no inimigo
    public abstract void atacar(InimigoLixo alvo);

    // Aplica a melhoria de atributos conforme a especie e o nivel atual.
    public abstract void evoluir();

    // Retorna o custo em conchas para evoluir para o proximo nivel.
    public abstract int getCustoUpgrade();

    // Verifica se a tartaruga ainda pode ser evoluida
    public boolean podeEvoluir() {
        return this.nivel < NIVEL_MAXIMO;
    }

    public String getNome() {
        return nome;
    }

    public int getCustoCompra() {
        return custo;
    }

    public int getCusto() {
        return custo;
    }

    public int getAlcance() {
        return alcance;
    }

    public int getDano() {
        return dano;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getNivel() {
        return nivel;
    }

    public int getNivelMaximo() {
        return NIVEL_MAXIMO;
    }

    // setters
    public void setCusto(int custo) {
        this.custo = custo;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public void setAlcance(int alcance) {
        this.alcance = alcance;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    // Incrementa o nivel da tartaruga. Chamado pelas subclasses dentro de evoluir().
    protected void incrementarNivel() {
        if (this.nivel < NIVEL_MAXIMO) {
            this.nivel++;
        }
    }

    @Override
    public String toString() {
        return nome + " [Nv." + nivel + " | Custo: " + custo + " | Dano: " + dano + " | Alcance: " + alcance
                + " | Velocidade: " + velocidade + "]";
    }
}
