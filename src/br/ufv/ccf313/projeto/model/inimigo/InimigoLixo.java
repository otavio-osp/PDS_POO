package br.ufv.ccf313.projeto.model.inimigo;

import br.ufv.ccf313.projeto.model.Entidade;
import java.util.List;

//  classe pai de todos os tipos de lixo 

// cada inimigo filho define seus proprios atributos de vida, velocidade, recompensa e dano

public abstract class InimigoLixo extends Entidade{

    private int vidaMaxima;
    private int vidaAtual;
    private int velocidade;  // quantas celulas anda por rodada (0 = leva 2 rodadas para andar 1)
    private int recompensa;  // XP/dinheiro liberado ao jogador quando este inimigo morre
    private int dano;        // dano descontado da vida do jogador se este inimigo passar
    private int contadorTurnos;
    private int indiceCaminho;
    private List<int[]> caminho;

    public InimigoLixo(int posX, int posY, int vidaMaxima, int velocidade, int recompensa, int dano){
        super(posX, posY);
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.velocidade = velocidade;
        this.recompensa = recompensa;
        this.dano = dano;
        this.contadorTurnos = 0;
        this.indiceCaminho = 0;
    }

    public void definirCaminho(List<int[]> caminho) {
        this.caminho = caminho;
        if (caminho != null && !caminho.isEmpty()) {
            setPosX(caminho.get(0)[0]);
            setPosY(caminho.get(0)[1]);
        }
    }

    @Override
    public void atualizar(){
        if (!isAtiva() || caminho == null || caminho.isEmpty()){
            return;
        }

        int passos = this.velocidade;
        if (passos == 0){
            contadorTurnos++;
            if (contadorTurnos<2){
                return;
            }
            contadorTurnos = 0;
            passos = 1;
        }

        indiceCaminho += passos;

        if (indiceCaminho >= caminho.size()){
            indiceCaminho = caminho.size()-1;
        }

        int[] novaPos = caminho.get(indiceCaminho);
        setPosX(novaPos[0]);
        setPosY(novaPos[1]);
    }

    // o caminho do lixo acabou (deve ser desativado) 
    public boolean chegouAoFim() {
        return caminho != null && !caminho.isEmpty() && indiceCaminho >= caminho.size() - 1;
    }

    public void receberDano(int dano) {
        if (isAtiva() && dano > 0) {
            this.vidaAtual -= dano;
            if (this.vidaAtual <= 0) {
                this.vidaAtual = 0;
                desativar();
                // quando chega aqui, devemos chamar no modulo da classe jogador "jogador.registrarInimigoDestruido(this.getRecompensa());"
                // nao chamar aqui diretamente pois este modulo nao tem acesso a classe jogador
            }
        }
    }


    // retorna true se o inimigo foi destruido
    public boolean estaDestruido() {
        return !isAtiva() || this.vidaAtual <= 0;
    }
    public int getVidaMaxima(){ 
        return vidaMaxima; 
    }
    public int getVidaAtual(){ 
        return vidaAtual; 
    }
    public int getVelocidade(){ 
        return velocidade; 
    }
    public int getRecompensa(){ 
        return recompensa; 
    }
    public int getDano(){ 
        return dano; 
    }

    
    public void aplicarBustDeWave(int bonusVida, int bonusVelocidade){
        this.vidaMaxima += bonusVida;
        this.vidaAtual = this.vidaMaxima;
        this.velocidade += bonusVelocidade;
    }
}
