package br.ufv.ccf313.projeto.service;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.inimigo.LixoGarrafa;
import br.ufv.ccf313.projeto.model.inimigo.LixoPlastico;
import br.ufv.ccf313.projeto.model.inimigo.LixoRede;
import java.util.ArrayList;
import java.util.List;

// HELENA: controla as ondas de inimigos do jogo.
// Decide quantos e quais inimigos aparecem em cada wave,
// aplica os busts progressivos e verifica quando a wave terminou.
public class GerenciadorOndas {

    private int waveAtual;
    private static final int TOTAL_WAVES = 10; // HELENA: definir quantas waves no jogo

    public GerenciadorOndas() {
        this.waveAtual = 1;
    }

    // Avanca para a proxima wave.
    // Chamado quando a wave atual e concluida (todos inimigos destruidos ou passaram).
    // Chamado pela logica principal do jogo (Carol/GameLoop).
    public void avancarWave() {
        this.waveAtual++;
    }

    // Gera a lista de inimigos para a wave atual.
    // HELENA: definir a composicao de cada wave (quais tipos e quantidades).
    // Por agora, a logica e simples: aumenta a quantidade a cada wave.
    // Mais tarde pode adicionar aleatoriedade de tipos.
    public List<InimigoLixo> gerarInimigos(int posInicialX, int posInicialY) {
        List<InimigoLixo> inimigos = new ArrayList<>();
        int quantidade = waveAtual * 3; // HELENA: ajustar formula de quantidade

        for (int i = 0; i < quantidade; i++) {
            // HELENA: definir a distribuicao de tipos por wave
            // Por agora, distribui ciclicamente entre os 3 tipos
            int tipo = i % 3;
            if (tipo == 0) inimigos.add(new LixoPlastico(posInicialX, posInicialY));
            else if (tipo == 1) inimigos.add(new LixoGarrafa(posInicialX, posInicialY));
            else inimigos.add(new LixoRede(posInicialX, posInicialY));
        }

        aplicarBustDeWave(inimigos);
        return inimigos;
    }

    // Aplica o bust (melhoria progressiva) nos inimigos de acordo com a wave atual.
    // HELENA: definir os valores de bonus de vida e velocidade por wave.
    // A cada wave os inimigos ficam mais fortes.
    private void aplicarBustDeWave(List<InimigoLixo> inimigos) {
        int bonusVida = (waveAtual - 1) * 10;      // HELENA: ajustar formula
        int bonusVelocidade = (waveAtual - 1) / 3; // HELENA: ajustar formula
        for (InimigoLixo inimigo : inimigos) {
            inimigo.aplicarBustDeWave(bonusVida, bonusVelocidade);
        }
    }

    // Retorna true se todos os inimigos da wave foram destruidos ou passaram.
    // Chamado a cada ciclo para saber quando iniciar a proxima wave.
    public boolean waveCompleta(List<InimigoLixo> inimigos) {
        for (InimigoLixo inimigo : inimigos) {
            if (!inimigo.estaDestruido() && inimigo.isAtiva()) return false;
        }
        return true;
    }

    // Retorna true se o jogo chegou na ultima wave e ela foi completada.
    public boolean jogoCompleto(List<InimigoLixo> inimigos) {
        return waveAtual >= TOTAL_WAVES && waveCompleta(inimigos);
    }

    public int getWaveAtual() { return waveAtual; }
    public int getTotalWaves() { return TOTAL_WAVES; }
}
