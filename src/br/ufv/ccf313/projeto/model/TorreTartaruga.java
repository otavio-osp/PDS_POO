package br.ufv.ccf313.projeto.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que representa uma Torre Tartaruga posicionada no mapa.
 * Herda de Entidade e define os atributos e comportamentos comuns a todas as
 * torres.
 */
public abstract class TorreTartaruga extends Entidade {

    private final String nome;
    private final int custo; // Custo em conchas para comprar a torre
    private final double alcance; // Raio de alcance no grid
    private final int dano; // Dano causado por projétil/ataque
    private final double velocidadeAtaque; // Ataques por segundo

    // Contador interno de tempo para controlar cadência de ataque
    private double tempoCooldown;

    // Lista de projéteis disparados por esta torre, para ser processada pelo
    // CollisionService
    private final List<Projetil> projetisAtivos;

    /**
     * Construtor de TorreTartaruga.
     *
     * @param posX             Coluna no grid.
     * @param posY             Linha no grid.
     * @param nome             Nome da espécie de tartaruga.
     * @param custo            Custo em conchas para comprar.
     * @param alcance          Raio de alcance (em células do grid).
     * @param dano             Dano por ataque.
     * @param velocidadeAtaque Ataques por segundo.
     */
    public TorreTartaruga(int posX, int posY, String nome, int custo,
            double alcance, int dano, double velocidadeAtaque) {
        super(posX, posY);
        this.nome = nome;
        this.custo = custo;
        this.alcance = alcance;
        this.dano = dano;
        this.velocidadeAtaque = velocidadeAtaque;
        this.tempoCooldown = 0.0;
        this.projetisAtivos = new ArrayList<>();
    }

    /**
     * Lógica de ataque de cada tartaruga.
     * Cada subclasse define como ataca.
     *
     * @param alvo O inimigo-alvo a ser atacado.
     */
    public abstract void atacar(InimigoLixo alvo);

    /**
     * Atualização por ciclo do GameLoop. Decrementa o cooldown de ataque.
     */
    @Override
    public void atualizar() {
        if (tempoCooldown > 0) {
            // A cada ciclo, reduz o cooldown
            tempoCooldown -= velocidadeAtaque;
        }
    }

    /**
     * Verifica se a torre pode atacar novamente (cooldown zerado).
     *
     * @return true se pode atacar.
     */
    public boolean podeatAcar() {
        return tempoCooldown <= 0;
    }

    /**
     * Reinicia o cooldown após um ataque.
     */
    public void reiniciarCooldown() {
        // Cooldown em ticks = 60 ticks/s dividido pela velocidade de ataque
        this.tempoCooldown = 60.0 / velocidadeAtaque;
    }

    /**
     * Adiciona um projétil disparado por esta torre à lista de ativos.
     *
     * @param projetil Projétil criado pelo método atacar().
     */
    protected void dispararProjetil(Projetil projetil) {
        projetisAtivos.add(projetil);
    }

    /**
     * Remove projéteis inativos.
     */
    public void limparProjetisInativos() {
        projetisAtivos.removeIf(p -> !p.isAtiva());
    }

    // Getters

    public String getNome() {
        return nome;
    }

    public int getCusto() {
        return custo;
    }

    public double getAlcance() {
        return alcance;
    }

    public int getDano() {
        return dano;
    }

    public double getVelocidadeAtaque() {
        return velocidadeAtaque;
    }

    public List<Projetil> getProjetisAtivos() {
        return projetisAtivos;
    }

    @Override
    public String toString() {
        return nome + " [Custo: " + custo + " conchas | Dano: " + dano
                + " | Alcance: " + alcance + " | Vel. Ataque: " + velocidadeAtaque + "/s]";
    }
}
