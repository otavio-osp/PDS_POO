package br.ufv.ccf313.projeto.model.tartaruga;

import br.ufv.ccf313.projeto.model.Entidade;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;

// Classe pai de todas as tartarugas (torres).
// Cada tartaruga filha define seu proprio custo, alcance, dano e velocidade de ataque.
public abstract class TorreTartaruga extends Entidade {

    private String nome;
    private int custo;
    private int alcance; // quantidade de celulas que a tartaruga alcanca
    private int dano; // dano causado por ataque direto no inimigo
    private int velocidade; // quantos ataques da antes do inimigo andar uma celula

    public TorreTartaruga(int posX, int posY, String nome, int custo, int alcance, int dano, int velocidade) {
        super(posX, posY);
        this.nome = nome;
        this.custo = custo;
        this.alcance = alcance;
        this.dano = dano;
        this.velocidade = velocidade;
    }

    // Cada tartaruga filha implementa como aplica o dano no inimigo
    public abstract void atacar(InimigoLixo alvo);

    // Aqui entrarao os metodos de upgrade que alteram dano, alcance e velocidade
    // Exemplo: public void aplicarUpgrade(int bonusDano, int bonusAlcance, int
    // bonusVelocidade)

    public String getNome() {
        return nome;
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

    // setters para upgrades alterarem os atributos
    public void setDano(int dano) {
        this.dano = dano;
    }

    public void setAlcance(int alcance) {
        this.alcance = alcance;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    @Override
    public String toString() {
        return nome + " [Custo: " + custo + " | Dano: " + dano + " | Alcance: " + alcance + " | Velocidade: "
                + velocidade + "]";
    }
}
