package br.ufv.ccf313.projeto.test;

import br.ufv.ccf313.projeto.model.RegistroRanking;
import br.ufv.ccf313.projeto.service.GerenciadorRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GerenciadorRankingTest {
    private GerenciadorRanking ranking;

    @BeforeEach
    public void setUp() {
        ranking = new GerenciadorRanking();
    }

    @Test
    public void testAdicionarEOrdenarRanking() {
        ranking.adicionarRegistro("Jogador A", 1500, 5);
        ranking.adicionarRegistro("Jogador B", 3000, 10);
        ranking.adicionarRegistro("Jogador C", 500, 2);

        List<RegistroRanking> top = ranking.getTopRegistros(3);
        
        assertEquals(3, top.size());
        assertEquals("Jogador B", top.get(0).getNomeJogador());
        assertEquals(3000, top.get(0).getPontuacaoTotal());
        assertEquals("Jogador C", top.get(2).getNomeJogador());
    }

    @Test
    public void testBuscarRegistroExistenteENaoExistente() {
        ranking.adicionarRegistro("Lethicia", 2500, 8);

        RegistroRanking encontrado = ranking.buscarRegistro("Lethicia");
        assertNotNull(encontrado);
        assertEquals(2500, encontrado.getPontuacaoTotal());

        RegistroRanking naoExiste = ranking.buscarRegistro("Inexistente");
        assertNull(naoExiste);
    }

    @Test
    public void testAtualizarRegistro() {
        ranking.adicionarRegistro("Pedro", 1000, 4);

        boolean atualizou = ranking.atualizarRegistro("Pedro", 3500, 10);
        assertTrue(atualizou);

        RegistroRanking atualizado = ranking.buscarRegistro("Pedro");
        assertEquals(3500, atualizado.getPontuacaoTotal());
        assertEquals(10, atualizado.getOndasSobrevividas());

        boolean falha = ranking.atualizarRegistro("Ninguem", 500, 1);
        assertFalse(falha);
    }

    @Test
    public void testRemoverRegistro() {
        ranking.adicionarRegistro("Tomas", 2000, 6);
        assertEquals(1, ranking.totalRegistros());

        boolean removeu = ranking.removerRegistro("Tomas");
        assertTrue(removeu);
        assertEquals(0, ranking.totalRegistros());
        assertNull(ranking.buscarRegistro("Tomas"));

        boolean removeuInexistente = ranking.removerRegistro("Tomas");
        assertFalse(removeuInexistente);
    }

    @Test
    public void testNomeInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ranking.adicionarRegistro("", 100, 1);
        });
        assertEquals("O nome do jogador não pode ser vazio.", exception.getMessage());
    }
}
