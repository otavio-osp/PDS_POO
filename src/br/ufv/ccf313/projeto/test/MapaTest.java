package br.ufv.ccf313.projeto.test;

import br.ufv.ccf313.projeto.exception.PosicaoInvalidaException;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.model.mapa.TipoCelula;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaDeCouro;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaVerde;
import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapaTest {

    private Mapa mapaPadrao;
    private Mapa mapaCustom;

    @BeforeEach
    public void setUp() {
        mapaPadrao = new Mapa();
        mapaCustom = new Mapa(6, 8);
    }

    @Test
    public void testInicializacaoMapaPadrao() {
        assertEquals(Mapa.LINHAS_PADRAO, mapaPadrao.getLinhas());
        assertEquals(Mapa.COLUNAS_PADRAO, mapaPadrao.getColunas());

        assertNotNull(mapaPadrao.getCaminho());
        assertEquals(27, mapaPadrao.getCaminho().size());

        int[] inicio = mapaPadrao.getPosicaoInicial();
        assertNotNull(inicio);
        assertEquals(2, inicio[0]);
        assertEquals(0, inicio[1]);

        int[] fim = mapaPadrao.getPosicaoFinal();
        assertNotNull(fim);
        assertEquals(7, fim[0]);
        assertEquals(13, fim[1]);

        assertEquals(TipoCelula.CAMINHO, mapaPadrao.getCelula(2, 0).getTipo());
        assertEquals(TipoCelula.LIVRE, mapaPadrao.getCelula(1, 1).getTipo());
        assertEquals(TipoCelula.BLOQUEADA, mapaPadrao.getCelula(0, 0).getTipo());
    }

    @Test
    public void testColocarTartarugaEmSlotFixo() {
        TorreTartaruga torre = new TartarugaVerde(1, 1);

        assertTrue(mapaPadrao.isPosicaoValidaParaTorre(1, 1));
        assertTrue(mapaPadrao.colocarTartaruga(1, 1, torre));
        assertEquals(TipoCelula.OCUPADA, mapaPadrao.getCelula(1, 1).getTipo());

        assertFalse(mapaPadrao.colocarTartaruga(1, 1, new TartarugaDeCouro(1, 1)));

        TorreTartaruga removida = mapaPadrao.removerTartaruga(1, 1);
        assertNotNull(removida);
        assertEquals(torre, removida);
        assertEquals(TipoCelula.LIVRE, mapaPadrao.getCelula(1, 1).getTipo());
    }

    @Test
    public void testBloqueioEmCenarioInacessivel() {
        assertFalse(mapaPadrao.isPosicaoValidaParaTorre(0, 0));
        assertFalse(mapaPadrao.colocarTartaruga(0, 0, new TartarugaVerde(0, 0)));

        assertThrows(PosicaoInvalidaException.class, () -> {
            mapaPadrao.posicionarTartaruga(0, 0, new TartarugaVerde(0, 0));
        });
    }

    @Test
    public void testColocarTartarugaEmCaminhoFalha() {
        TorreTartaruga torre = new TartarugaVerde(2, 0);
        assertFalse(mapaPadrao.isPosicaoValidaParaTorre(2, 0));
        assertFalse(mapaPadrao.colocarTartaruga(2, 0, torre));

        assertThrows(PosicaoInvalidaException.class, () -> {
            mapaPadrao.posicionarTartaruga(2, 0, torre);
        });
    }

    @Test
    public void testGetCelulasComTartaruga() {
        mapaPadrao.colocarTartaruga(1, 1, new TartarugaVerde(1, 1));
        mapaPadrao.colocarTartaruga(1, 3, new TartarugaDeCouro(1, 3));

        var celulasOcupadas = mapaPadrao.getCelulasComTartaruga();
        assertEquals(2, celulasOcupadas.size());
    }

    @Test
    public void testDimensaoInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new Mapa(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Mapa(5, -1));
    }
}
