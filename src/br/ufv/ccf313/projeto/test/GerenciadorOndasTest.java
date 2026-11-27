package br.ufv.ccf313.projeto.test;

import br.ufv.ccf313.projeto.exception.WaveInvalidaException;
import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.service.GerenciadorOndas;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitarios para o GerenciadorOndas
 *
 * @author Helena Santos Perez
 */
public class GerenciadorOndasTest {

    private GerenciadorOndas gerenciadorOndas;
    private Mapa mapa;
    private Jogador jogador;

    @BeforeEach
    public void setUp() {
        gerenciadorOndas = new GerenciadorOndas();
        mapa = new Mapa();
        jogador = new Jogador("Helena", 10, 100);
    }

    @Test
    public void testEstadoInicial() {
        assertEquals(1, gerenciadorOndas.getWaveAtual());
        assertEquals(10, gerenciadorOndas.getTotalWaves());
        assertEquals(0, gerenciadorOndas.getRestantesNaFila());
        assertFalse(gerenciadorOndas.isWaveEmAndamento());
    }

    @Test
    public void testPrepararWaveCriaFilaEVinculaCaminhoDoMapa() throws WaveInvalidaException {
        gerenciadorOndas.prepararWave(mapa);

        // Wave 1 gera 3 + (1 * 2) = 5 inimigos na fila
        assertEquals(5, gerenciadorOndas.getRestantesNaFila());
        assertTrue(gerenciadorOndas.isWaveEmAndamento());

        List<InimigoLixo> ativos = new ArrayList<>();
        assertNull(gerenciadorOndas.processarSpawnTick(ativos)); // Tick 1: aguarda intervalo
        InimigoLixo primeiro = gerenciadorOndas.processarSpawnTick(ativos); // Tick 2: libera 1o inimigo

        assertNotNull(primeiro);
        assertEquals(1, ativos.size());
        assertEquals(4, gerenciadorOndas.getRestantesNaFila());

        // Verifica se o inimigo foi posicionado na entrada do rio do Mapa (col=0, linha=2)
        int[] inicioMapa = mapa.getPosicaoInicial();
        assertEquals(inicioMapa[1], primeiro.getPosX());
        assertEquals(inicioMapa[0], primeiro.getPosY());
    }

    @Test
    public void testPrepararWaveComMapaInvalidoLancaExcecao() {
        assertThrows(WaveInvalidaException.class, () -> {
            gerenciadorOndas.prepararWave(null);
        });

        Mapa mapaSemCaminho = new Mapa(5, 5);
        assertThrows(WaveInvalidaException.class, () -> {
            gerenciadorOndas.prepararWave(mapaSemCaminho);
        });
    }

    @Test
    public void testEscalonamentoDeDificuldadePorWave() throws WaveInvalidaException {
        List<InimigoLixo> wave1 = gerenciadorOndas.gerarInimigos(0, 2);
        int vidaWave1 = wave1.get(0).getVidaMaxima();

        gerenciadorOndas.avancarWave(jogador);
        assertEquals(2, gerenciadorOndas.getWaveAtual());
        assertEquals(2, jogador.getWaveAtual());

        List<InimigoLixo> wave2 = gerenciadorOndas.gerarInimigos(0, 2);
        int vidaWave2 = wave2.get(0).getVidaMaxima();

        // Na wave 2 o inimigo recebe +15 de vida de bust
        assertTrue(vidaWave2 > vidaWave1);
        assertEquals(vidaWave1 + 15, vidaWave2);
    }

    @Test
    public void testDeteccaoDeFimDaWave() throws WaveInvalidaException {
        gerenciadorOndas.prepararWave(mapa);
        List<InimigoLixo> ativos = new ArrayList<>();

        // Ainda ha inimigos na fila -> wave nao acabou
        assertFalse(gerenciadorOndas.isWaveFinalizada(ativos));

        // Esvazia toda a fila de spawn (5 inimigos * 2 ticks = 10 ticks)
        for (int tick = 0; tick < 10; tick++) {
            gerenciadorOndas.processarSpawnTick(ativos);
        }
        assertEquals(0, gerenciadorOndas.getRestantesNaFila());
        assertEquals(5, ativos.size());

        // Fila vazia, mas inimigos ainda vivos no rio -> wave nao acabou
        assertFalse(gerenciadorOndas.isWaveFinalizada(ativos));

        // Destroi todos os inimigos ativos
        for (InimigoLixo inimigo : ativos) {
            inimigo.receberDano(inimigo.getVidaMaxima());
        }

        // Fila vazia E nenhum inimigo ativo -> wave finalizada
        assertTrue(gerenciadorOndas.isWaveFinalizada(ativos));
        assertFalse(gerenciadorOndas.isWaveEmAndamento());
    }

    @Test
    public void testVitoriaNaUltimaWaveEExcecaoAoUltrapassarLimite() throws WaveInvalidaException {
        for (int w = 1; w < 10; w++) {
            gerenciadorOndas.avancarWave(jogador);
        }
        assertEquals(10, gerenciadorOndas.getWaveAtual());

        // Tentar avancar alem da wave 10 deve lancar WaveInvalidaException
        assertThrows(WaveInvalidaException.class, () -> {
            gerenciadorOndas.avancarWave(jogador);
        });

        // Com a wave 10 finalizada e lista vazia, jogoCompleto deve ser true
        assertTrue(gerenciadorOndas.jogoCompleto(new ArrayList<>()));
    }
}
