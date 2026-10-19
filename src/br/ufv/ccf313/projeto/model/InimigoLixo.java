package br.ufv.ccf313.projeto.model;

public abstract class InimigoLixo extends Entidade {

    private int vidaMaxima;
    private int vidaAtual;
    private double velocidade; // velocidade do movimento (células/segundo)
    private int recompensa; // conchas concedidas ao ser destruído

    
    // construtor da subclasse 
    public InimigoLixo(int posX, int posY, int vidaMaxima, double velocidade, int recompensa){
        super(posX, posY); 
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;
        this.recompensa = recompensa;
    }


    // aplica dano
    public void receberDano(int dano){
        if (super.isAtiva() && dano > 0){
            this.vidaAtual -= dano;
            if (this.vidaAtual <= 0){
                desativar();
            }
        }    
    }

    
    // verifica se o inimigo está ativo no mapa
    public boolean estaDestruido() {
        return !isAtiva() || this.vidaAtual <= 0;
    }

    
     // métodos de acesso
    public int getVidaMaxima(){
        return vidaMaxima;
    }
    public int getVidaAtual(){
        return vidaAtual;
    }
    public double getVelocidade(){
        return velocidade;
    }
    public int getRecompensa(){
        return recompensa;
    }
    
}
