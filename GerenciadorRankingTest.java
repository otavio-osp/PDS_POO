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
        ranking.adicionarRegistro("Jogador B", 3000, 10); // Maior pontuação
        ranking.adicionarRegistro("Jogador C", 500, 2);

        List<RegistroRanking> top = ranking.getTopRegistros(3);
        
        // Verifica se o Jogador B ficou em primeiro lugar
        assertEquals("Jogador B", top.get(0).getNomeJogador());
        assertEquals(3000, top.get(0).getPontuacaoTotal());
        
        // Verifica se o Jogador C ficou em último
        assertEquals("Jogador C", top.get(2).getNomeJogador());
    }

    @Test
    public void testNomeInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ranking.adicionarRegistro("", 100, 1);
        });
        assertEquals("O nome do jogador não pode ser vazio.", exception.getMessage());
    }
}