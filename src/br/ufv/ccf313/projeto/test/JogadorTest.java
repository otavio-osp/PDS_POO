package br.ufv.ccf313.projeto.test;

import br.ufv.ccf313.projeto.model.Jogador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JogadorTest {

    private Jogador jogador;

    @BeforeEach
    public void setUp() {
        jogador = new Jogador("Otavio");
    }

    @Test
    public void testInicializacaoValoresPadrao() {
        assertEquals("Otavio", jogador.getNome());
        assertEquals(Jogador.VIDA_PADRAO, jogador.getVida());
        assertEquals(Jogador.DINHEIRO_PADRAO, jogador.getDinheiro());
        assertEquals(0, jogador.getScore());
        assertEquals(1, jogador.getWaveAtual());
        assertTrue(jogador.estaVivo());
    }

    @Test
    public void testInicializacaoCustomizada() {
        Jogador custom = new Jogador("Heroi", 20, 300);
        assertEquals("Heroi", custom.getNome());
        assertEquals(20, custom.getVida());
        assertEquals(300, custom.getDinheiro());
    }

    @Test
    public void testNomeInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Jogador(null));
        assertThrows(IllegalArgumentException.class, () -> new Jogador("   "));
    }

    @Test
    public void testReceberDanoEGameOver() {
        jogador.receberDano(5);
        assertEquals(10, jogador.getVida());
        assertTrue(jogador.estaVivo());

        jogador.receberDano(10);
        assertEquals(0, jogador.getVida());
        assertFalse(jogador.estaVivo());

        jogador.receberDano(10);
        assertEquals(0, jogador.getVida());
    }

    @Test
    public void testRegistrarInimigoDestruido() {
        jogador.registrarInimigoDestruido(50);
        assertEquals(50, jogador.getScore());
        assertEquals(150 + 50, jogador.getDinheiro());
    }

    @Test
    public void testGastarDinheiro() {
        assertTrue(jogador.gastarDinheiro(100));
        assertEquals(50, jogador.getDinheiro());

        assertFalse(jogador.gastarDinheiro(60));
        assertEquals(50, jogador.getDinheiro());
    }

    @Test
    public void testAdicionarDinheiro() {
        jogador.adicionarDinheiro(25);
        assertEquals(175, jogador.getDinheiro());
    }

    @Test
    public void testAvancarWave() {
        assertEquals(1, jogador.getWaveAtual());
        jogador.avancarWave();
        assertEquals(2, jogador.getWaveAtual());
    }
}
