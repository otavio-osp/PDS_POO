package br.ufv.ccf313.projeto.service;

import br.ufv.ccf313.projeto.exception.WaveInvalidaException;
import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.inimigo.LixoGarrafa;
import br.ufv.ccf313.projeto.model.inimigo.LixoPlastico;
import br.ufv.ccf313.projeto.model.inimigo.LixoRede;
import java.util.ArrayList;
import java.util.List;

/**
 * GerenciadorOndas - Mantem compatibilidade com chamadas legadas de geracao
 * direta de ondas e escalonamento progressivo de inimigos.
 *
 * @author Helena Santos Perez
 */
public class GerenciadorOndas {

    private int waveAtual;
    private static final int TOTAL_WAVES = 10;

    public GerenciadorOndas() {
        this.waveAtual = 1;
    }

    public void avancarWave() throws WaveInvalidaException {
        if (waveAtual >= TOTAL_WAVES) {
            throw new WaveInvalidaException("O jogo ja atingiu a ultima wave (" + TOTAL_WAVES + ").");
        }
        this.waveAtual++;
    }

    public void avancarWave(Jogador jogador) throws WaveInvalidaException {
        avancarWave();
        if (jogador != null) {
            jogador.avancarWave();
        }
    }

    public List<InimigoLixo> gerarInimigos(int posInicialX, int posInicialY) throws WaveInvalidaException {
        if (waveAtual < 1 || waveAtual > TOTAL_WAVES) {
            throw new WaveInvalidaException("Numero de wave invalido: " + waveAtual);
        }

        List<InimigoLixo> inimigos = new ArrayList<>();
        int quantidade = 3 + (waveAtual * 2);

        for (int i = 0; i < quantidade; i++) {
            InimigoLixo inimigo;
            if (waveAtual <= 3) {
                inimigo = (i % 4 == 3)
                        ? new LixoGarrafa(posInicialX, posInicialY)
                        : new LixoPlastico(posInicialX, posInicialY);
            } else if (waveAtual <= 7) {
                int tipo = i % 4;
                if (tipo == 0 || tipo == 1) inimigo = new LixoPlastico(posInicialX, posInicialY);
                else if (tipo == 2) inimigo = new LixoGarrafa(posInicialX, posInicialY);
                else inimigo = new LixoRede(posInicialX, posInicialY);
            } else {
                int tipo = i % 3;
                if (tipo == 0) inimigo = new LixoGarrafa(posInicialX, posInicialY);
                else if (tipo == 1) inimigo = new LixoRede(posInicialX, posInicialY);
                else inimigo = new LixoPlastico(posInicialX, posInicialY);
            }
            inimigos.add(inimigo);
        }

        aplicarBustDeWave(inimigos);
        return inimigos;
    }

    private void aplicarBustDeWave(List<InimigoLixo> inimigos) {
        int bonusVida = (waveAtual - 1) * 15;
        int bonusVelocidade = (waveAtual - 1) / 3;
        for (InimigoLixo inimigo : inimigos) {
            inimigo.aplicarBustDeWave(bonusVida, bonusVelocidade);
        }
    }

    public boolean waveCompleta(List<InimigoLixo> inimigos) {
        if (inimigos == null || inimigos.isEmpty()) {
            return true;
        }
        for (InimigoLixo inimigo : inimigos) {
            if (inimigo != null && !inimigo.estaDestruido() && inimigo.isAtiva()) {
                return false;
            }
        }
        return true;
    }

    public boolean jogoCompleto(List<InimigoLixo> inimigos) {
        return waveAtual >= TOTAL_WAVES && waveCompleta(inimigos);
    }

    public int getWaveAtual() { return waveAtual; }
    public int getTotalWaves() { return TOTAL_WAVES; }
}