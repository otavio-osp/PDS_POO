package br.ufv.ccf313.projeto.test;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.inimigo.LixoGarrafa;
import br.ufv.ccf313.projeto.model.inimigo.LixoPlastico;
import br.ufv.ccf313.projeto.model.inimigo.LixoRede;
import br.ufv.ccf313.projeto.model.mapa.Celula;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaDeCouro;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaDePente;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaVerde;
import br.ufv.ccf313.projeto.service.GerenciadorCombate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GerenciadorCombateTest {

    private GerenciadorCombate combate;
    private Mapa mapa;
    private Jogador jogador;

    @BeforeEach
    public void setUp() {
        combate = new GerenciadorCombate();
        // Mapa 6x6
        mapa = new Mapa(6, 6);
        // Caminho do rio em formato de zigue-zague:
        // (0,0) -> (0,1) -> (0,2) -> (1,2) -> (2,2) -> (2,1) -> (2,0)
        mapa.adicionarCelulaCaminho(0, 0); // idx 0 (nascente)
        mapa.adicionarCelulaCaminho(0, 1); // idx 1
        mapa.adicionarCelulaCaminho(0, 2); // idx 2
        mapa.adicionarCelulaCaminho(1, 2); // idx 3
        mapa.adicionarCelulaCaminho(2, 2); // idx 4
        mapa.adicionarCelulaCaminho(2, 1); // idx 5
        mapa.adicionarCelulaCaminho(2, 0); // idx 6 (praia/chegada)

        jogador = new Jogador("Lethicia", 10, 100);
    }

    @Test
    public void testDeteccaoAlcanceManhattan() {
        TartarugaVerde tartaruga = new TartarugaVerde(1, 1); // Torre em (coluna=1, linha=1), alcance=2
        mapa.colocarTartaruga(1, 1, tartaruga);
        Celula celulaTorre = mapa.getCelula(1, 1);

        List<InimigoLixo> inimigos = new ArrayList<>();
        // Inimigo 1: em (col=2, linha=1) -> dist = |2-1| + |1-1| = 1 (no alcance)
        LixoPlastico inRange1 = new LixoPlastico(2, 1);
        // Inimigo 2: em (col=2, linha=2) -> dist = |2-1| + |2-1| = 2 (no alcance)
        LixoGarrafa inRange2 = new LixoGarrafa(2, 2);
        // Inimigo 3: em (col=5, linha=5) -> dist = |5-1| + |5-1| = 8 (fora do alcance)
        LixoRede outRange = new LixoRede(5, 5);

        inimigos.add(inRange1);
        inimigos.add(inRange2);
        inimigos.add(outRange);

        List<InimigoLixo> detectados = combate.inimigosNoAlcance(tartaruga, celulaTorre, inimigos, mapa);

        assertEquals(2, detectados.size());
        assertTrue(detectados.contains(inRange1));
        assertTrue(detectados.contains(inRange2));
        assertFalse(detectados.contains(outRange));
    }

    @Test
    public void testSelecaoInimigoMaisAvancadoNoCaminho() {
        // Tartaruga em (1, 1) com alcance 2
        TartarugaVerde tartaruga = new TartarugaVerde(1, 1);
        mapa.colocarTartaruga(1, 1, tartaruga);

        // Inimigo A em (col=2, linha=0) -> é a célula (0, 2), índice 2 do caminho
        LixoPlastico inimigoMenosAvancado = new LixoPlastico(2, 0);
        // Inimigo B em (col=1, linha=2) -> é a célula (2, 1), índice 5 do caminho
        LixoGarrafa inimigoMaisAvancado = new LixoGarrafa(1, 2);

        List<InimigoLixo> candidatos = new ArrayList<>();
        candidatos.add(inimigoMenosAvancado);
        candidatos.add(inimigoMaisAvancado);

        InimigoLixo alvoEscolhido = combate.inimigoMaisAvancado(candidatos, mapa);

        assertNotNull(alvoEscolhido);
        assertSame(inimigoMaisAvancado, alvoEscolhido, "Deve escolher o inimigo mais adiantado no rio em direção à praia");
    }

    @Test
    public void testMultiplosAtaquesPorVelocidade() {
        // TartarugaVerde: dano = 15, velocidade = 2 (desfere 2 ataques por turno)
        TartarugaVerde tartaruga = new TartarugaVerde(1, 1);
        mapa.colocarTartaruga(1, 1, tartaruga);

        // Lixo com 30 de vida máxima. 2 ataques de 15 devem destruí-lo nesta rodada.
        LixoGarrafa inimigo = new LixoGarrafa(2, 1); // vida = 30
        List<InimigoLixo> inimigosAtivos = new ArrayList<>();
        inimigosAtivos.add(inimigo);

        combate.processarCombate(mapa, inimigosAtivos, jogador);

        assertTrue(inimigo.estaDestruido());
        assertEquals(0, inimigosAtivos.size(), "Inimigo destruído deve ser removido da lista de ativos");
        assertEquals(15, jogador.getScore(), "Score deve aumentar pelo valor da recompensa (15)");
        assertEquals(115, jogador.getDinheiro(), "Dinheiro deve aumentar em 15 conchas");
    }

    @Test
    public void testAtaqueSimultaneoNaMesmaCelula() {
        // TartarugaDeCouro: dano = 60, velocidade = 1
        TartarugaDeCouro tartaruga = new TartarugaDeCouro(1, 1);
        mapa.colocarTartaruga(1, 1, tartaruga);

        // Dois inimigos ocupando exatamente a mesma célula (col=2, linha=1)
        LixoPlastico inimigo1 = new LixoPlastico(2, 1); // vida = 50
        LixoRede inimigo2 = new LixoRede(2, 1);         // vida = 150

        List<InimigoLixo> inimigosAtivos = new ArrayList<>();
        inimigosAtivos.add(inimigo1);
        inimigosAtivos.add(inimigo2);

        combate.processarCombate(mapa, inimigosAtivos, jogador);

        // O inimigo 1 tinha 50 de vida e tomou 60 -> foi destruído
        assertTrue(inimigo1.estaDestruido());
        // O inimigo 2 tinha 150 de vida e tomou 60 -> sobrevive com 90
        assertFalse(inimigo2.estaDestruido());
        assertEquals(90, inimigo2.getVidaAtual());

        // Inimigo 1 destruído foi removido; Inimigo 2 sobrevive na lista
        assertEquals(1, inimigosAtivos.size());
        assertTrue(inimigosAtivos.contains(inimigo2));
        assertEquals(10, jogador.getScore(), "Recompensa do LixoPlastico (10) creditada");
    }

    @Test
    public void testTrocaDeAlvoSePrimeiroMorreNoPrimeiroAtaque() {
        // TartarugaVerde: dano = 15, velocidade = 2
        TartarugaVerde tartaruga = new TartarugaVerde(1, 1);
        mapa.colocarTartaruga(1, 1, tartaruga);

        // Inimigo 1 mais avançado com vida baixa (10 HP restante)
        LixoGarrafa maisAvancado = new LixoGarrafa(1, 2); // índice 5 no caminho
        maisAvancado.receberDano(20); // vida cai de 30 para 10

        // Inimigo 2 menos avançado com vida cheia (50 HP)
        LixoPlastico menosAvancado = new LixoPlastico(2, 1); // índice 3 no caminho

        List<InimigoLixo> inimigosAtivos = new ArrayList<>();
        inimigosAtivos.add(maisAvancado);
        inimigosAtivos.add(menosAvancado);

        combate.processarCombate(mapa, inimigosAtivos, jogador);

        // Primeiro ataque (15 dano) abateu o inimigo 1
        assertTrue(maisAvancado.estaDestruido());

        // Segundo ataque (15 dano) atingiu o inimigo 2: 50 - 15 = 35 HP
        assertEquals(35, menosAvancado.getVidaAtual());
        assertFalse(menosAvancado.estaDestruido());
        assertTrue(inimigosAtivos.contains(menosAvancado));
    }

    @Test
    public void testInimigoForaDoAlcanceNaoRecebeDano() {
        TartarugaVerde tartaruga = new TartarugaVerde(0, 0); // alcance 2
        mapa.colocarTartaruga(0, 0, tartaruga);

        LixoPlastico longe = new LixoPlastico(5, 5); // fora de alcance
        List<InimigoLixo> inimigosAtivos = new ArrayList<>();
        inimigosAtivos.add(longe);

        combate.processarCombate(mapa, inimigosAtivos, jogador);

        assertEquals(50, longe.getVidaAtual());
        assertFalse(longe.estaDestruido());
        assertEquals(1, inimigosAtivos.size());
    }

    @Test
    public void testProcessarInimigosChegaramAoFimDescontaVidaDoJogador() {
        // Fim do caminho é (col=0, linha=2), pois adicionamos (2, 0) como última célula
        LixoRede lixoNaPraia = new LixoRede(0, 2); // dano = 2
        List<InimigoLixo> inimigosAtivos = new ArrayList<>();
        inimigosAtivos.add(lixoNaPraia);

        assertEquals(10, jogador.getVida());

        combate.processarInimigosChegaramAoFim(mapa, inimigosAtivos, jogador);

        assertEquals(8, jogador.getVida(), "Dano do lixo (2) descontado da vida do jogador");
        assertTrue(lixoNaPraia.estaDestruido());
        assertEquals(0, inimigosAtivos.size(), "Lixo que atingiu a praia é removido");
    }

    @Test
    public void testIdentificarCelulasRioNoAlcance() {
        TartarugaDePente tartaruga = new TartarugaDePente(1, 1); // alcance 2
        mapa.colocarTartaruga(1, 1, tartaruga);
        Celula celulaTorre = mapa.getCelula(1, 1);

        List<Celula> celulasRio = combate.getCelulasRioNoAlcance(tartaruga, celulaTorre, mapa);

        // Caminho tem células: (0,0), (0,1), (0,2), (1,2), (2,2), (2,1), (2,0)
        // Distância Manhattan da torre (1, 1) até cada uma:
        // (0,0) -> |0-1| + |0-1| = 2 <= 2 (SIM)
        // (0,1) -> |1-1| + |0-1| = 1 <= 2 (SIM)
        // (0,2) -> |2-1| + |0-1| = 2 <= 2 (SIM)
        // (1,2) -> |2-1| + |1-1| = 1 <= 2 (SIM)
        // (2,2) -> |2-1| + |2-1| = 2 <= 2 (SIM)
        // (2,1) -> |1-1| + |2-1| = 1 <= 2 (SIM)
        // (2,0) -> |0-1| + |2-1| = 2 <= 2 (SIM)
        // Todas as 7 células do zigue-zague estão a até 2 passos de Manhattan de (1,1)!
        assertEquals(7, celulasRio.size());
    }
}
