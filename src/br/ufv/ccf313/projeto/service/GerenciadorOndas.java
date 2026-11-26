package br.ufv.ccf313.projeto.service;

import br.ufv.ccf313.projeto.exception.WaveInvalidaException;
import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.inimigo.LixoGarrafa;
import br.ufv.ccf313.projeto.model.inimigo.LixoPlastico;
import br.ufv.ccf313.projeto.model.inimigo.LixoRede;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * GerenciadorOndas - Controla o ciclo de waves, fila de spawn (Queue),
 * vinculacao ao caminho do mapa e escalonamento progressivo de inimigos.
 *
 * @author Helena Santos Perez
 */
public class GerenciadorOndas {

    private int waveAtual;
    private static final int TOTAL_WAVES = 10;
    private static final int INTERVALO_TICKS_SPAWN = 2;

    private Queue<InimigoLixo> filaSpawn;
    private int contadorTicks;
    private boolean waveEmAndamento;

    public GerenciadorOndas() {
        this.waveAtual = 1;
        this.filaSpawn = new LinkedList<>();
        this.contadorTicks = 0;
        this.waveEmAndamento = false;
    }

    /**
     * Prepara a fila de spawn (Queue) da onda atual, vincula cada inimigo
     * ao caminho do Mapa e aplica o escalonamento progressivo de dificuldade.
     */
    public void prepararWave(Mapa mapa) throws WaveInvalidaException {
        if (mapa == null || mapa.getCaminho() == null || mapa.getCaminho().isEmpty()) {
            throw new WaveInvalidaException("Mapa ou caminho invalido ao preparar a wave.");
        }
        if (waveAtual < 1 || waveAtual > TOTAL_WAVES) {
            throw new WaveInvalidaException("Numero de wave invalido: " + waveAtual);
        }

        filaSpawn.clear();
        contadorTicks = 0;
        int[] inicio = mapa.getPosicaoInicial();
        int posX = (inicio != null) ? inicio[1] : 0;
        int posY = (inicio != null) ? inicio[0] : 0;
        int quantidade = 3 + (waveAtual * 2);

        for (int i = 0; i < quantidade; i++) {
            InimigoLixo inimigo = criarInimigoPorWave(i, posX, posY);
            inimigo.definirCaminho(mapa.getCaminho());
            aplicarBustDeWave(inimigo);
            filaSpawn.offer(inimigo);
        }

        this.waveEmAndamento = true;
    }

    /**
     * Etapa 1 do Game Loop: libera o proximo inimigo da fila para a lista
     * de inimigos ativos no mapa a cada intervalo de ticks.
     */
    public InimigoLixo processarSpawnTick(List<InimigoLixo> inimigosAtivos) {
        if (filaSpawn.isEmpty()) {
            return null;
        }

        contadorTicks++;
        if (contadorTicks >= INTERVALO_TICKS_SPAWN) {
            contadorTicks = 0;
            InimigoLixo proximo = filaSpawn.poll();
            if (proximo != null && inimigosAtivos != null) {
                inimigosAtivos.add(proximo);
            }
            return proximo;
        }
        return null;
    }

    /**
     * Verifica se a onda terminou: fila de spawn vazia E nenhum inimigo ativo no mapa.
     */
    public boolean isWaveFinalizada(List<InimigoLixo> inimigosAtivos) {
        if (!filaSpawn.isEmpty()) {
            return false;
        }
        if (inimigosAtivos != null) {
            for (InimigoLixo inimigo : inimigosAtivos) {
                if (inimigo != null && !inimigo.estaDestruido() && !inimigo.chegouAoFim()) {
                    return false;
                }
            }
        }
        waveEmAndamento = false;
        return true;
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

    // Mantem compatibilidade com chamadas diretas que geram a lista completa de uma vez
    public List<InimigoLixo> gerarInimigos(int posInicialX, int posInicialY) throws WaveInvalidaException {
        if (waveAtual < 1 || waveAtual > TOTAL_WAVES) {
            throw new WaveInvalidaException("Numero de wave invalido: " + waveAtual);
        }

        List<InimigoLixo> inimigos = new ArrayList<>();
        int quantidade = 3 + (waveAtual * 2);

        for (int i = 0; i < quantidade; i++) {
            InimigoLixo inimigo = criarInimigoPorWave(i, posInicialX, posInicialY);
            aplicarBustDeWave(inimigo);
            inimigos.add(inimigo);
        }
        return inimigos;
    }

    private InimigoLixo criarInimigoPorWave(int indice, int x, int y) {
        if (waveAtual <= 3) {
            return (indice % 4 == 3) ? new LixoGarrafa(x, y) : new LixoPlastico(x, y);
        } else if (waveAtual <= 7) {
            int tipo = indice % 4;
            if (tipo == 0 || tipo == 1) return new LixoPlastico(x, y);
            if (tipo == 2) return new LixoGarrafa(x, y);
            return new LixoRede(x, y);
        } else {
            int tipo = indice % 3;
            if (tipo == 0) return new LixoGarrafa(x, y);
            if (tipo == 1) return new LixoRede(x, y);
            return new LixoPlastico(x, y);
        }
    }

    private void aplicarBustDeWave(InimigoLixo inimigo) {
        int bonusVida = (waveAtual - 1) * 15;
        int bonusVelocidade = (waveAtual - 1) / 3;
        inimigo.aplicarBustDeWave(bonusVida, bonusVelocidade);
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
        return waveAtual >= TOTAL_WAVES && isWaveFinalizada(inimigos);
    }

    public int getWaveAtual() { return waveAtual; }
    public int getTotalWaves() { return TOTAL_WAVES; }
    public int getRestantesNaFila() { return filaSpawn.size(); }
    public boolean isWaveEmAndamento() { return waveEmAndamento; }
}