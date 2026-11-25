package br.ufv.ccf313.projeto.test;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.inimigo.LixoGarrafa;
import br.ufv.ccf313.projeto.model.inimigo.LixoPlastico;
import br.ufv.ccf313.projeto.model.inimigo.LixoRede;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaDeCouro;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaDePente;
import br.ufv.ccf313.projeto.model.tartaruga.TartarugaVerde;
import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;
import br.ufv.ccf313.projeto.service.CollisionService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o CollisionService conforme os requisitos do
 * "Plano de Execução do Protótipo Jogável".
 */
public class CollisionServiceTest {

    private CollisionService collisionService;
    private Jogador jogador;

    @BeforeEach
    public void setUp() {
        collisionService = new CollisionService();
        jogador = new Jogador("Lethicia", 10, 100);
    }

    @Test
    public void testCalcularDistanciaEuclidianaComMathHypot() {
        TorreTartaruga torre = new TartarugaVerde(1, 1);
        InimigoLixo inimigo = new LixoPlastico(4, 5); // deltaX = 3, deltaY = 4 -> hypot = 5.0

        double distancia = collisionService.calcularDistancia(torre, inimigo);
        assertEquals(5.0, distancia, 0.0001, "Math.hypot(3, 4) deve resultar exatamente em 5.0");
    }

    @Test
    public void testEstaNoAlcance() {
        // TartarugaVerde possui alcance 2
        TorreTartaruga torre = new TartarugaVerde(2, 2);

        InimigoLixo inRange = new LixoPlastico(2, 4);  // dist = 2.0 <= 2 (dentro)
        InimigoLixo outRange = new LixoPlastico(2, 5); // dist = 3.0 > 2 (fora)

        assertTrue(collisionService.estaNoAlcance(torre, inRange));
        assertFalse(collisionService.estaNoAlcance(torre, outRange));

        // Inimigo destruído não deve ser considerado no alcance
        inRange.receberDano(inRange.getVidaMaxima());
        assertFalse(collisionService.estaNoAlcance(torre, inRange));
    }

    @Test
    public void testEncontrarPrimeiroAlvoNoAlcance() {
        TorreTartaruga torre = new TartarugaVerde(1, 1); // alcance 2

        InimigoLixo longe = new LixoPlastico(10, 10);
        InimigoLixo perto1 = new LixoPlastico(1, 2); // dist 1.0 (dentro)
        InimigoLixo perto2 = new LixoGarrafa(1, 3);  // dist 2.0 (dentro)

        List<InimigoLixo> lista = List.of(longe, perto1, perto2);

        InimigoLixo alvo = collisionService.encontrarPrimeiroAlvoNoAlcance(torre, lista);
        assertNotNull(alvo);
        assertSame(perto1, alvo, "Deve retornar o primeiro alvo da fila que estiver dentro do alcance");
    }

    @Test
    public void testAtaqueAplicaDanoDiretoERegistraParaExibicaoVisual() {
        // TartarugaDeCouro: alcance 4, dano 60, velocidade 1
        TorreTartaruga torre = new TartarugaDeCouro(2, 2);
        InimigoLixo inimigo = new LixoRede(2, 3); // vida 150, dist 1.0

        List<TorreTartaruga> torres = List.of(torre);
        List<InimigoLixo> inimigos = new ArrayList<>(List.of(inimigo));

        collisionService.verificarColisoesEAtaques(torres, inimigos);

        // Aplicação direta de dano: 150 - 60 = 90
        assertEquals(90, inimigo.getVidaAtual());

        // Registro do alvo atingido para a tela (g.drawLine)
        List<CollisionService.RegistroAtaque> ataques = collisionService.getAtaquesDoTurno();
        assertEquals(1, ataques.size());
        assertSame(torre, ataques.get(0).getTorre());
        assertSame(inimigo, ataques.get(0).getAlvo());
        assertEquals(1.0, ataques.get(0).getDistancia(), 0.0001);

        assertSame(inimigo, collisionService.getAlvoAtingido(torre));
    }

    @Test
    public void testMultiplosAtaquesPorVelocidadeDaTartaruga() {
        // TartarugaVerde tem velocidade = 2, dano = 15
        TorreTartaruga torre = new TartarugaVerde(1, 1);
        InimigoLixo inimigo = new LixoGarrafa(1, 2); // vida 30

        List<TorreTartaruga> torres = List.of(torre);
        List<InimigoLixo> inimigos = new ArrayList<>(List.of(inimigo));

        collisionService.verificarColisoesEAtaques(torres, inimigos);

        // 2 ataques de 15 = 30 dano -> inimigo destruído
        assertTrue(inimigo.estaDestruido());
    }

    @Test
    public void testLimpezaEResolucaoCreditaConchasEDescontaVida() {
        InimigoLixo morto = new LixoPlastico(0, 0); // recompensa = 10
        morto.receberDano(morto.getVidaMaxima());

        InimigoLixo chegouAoFim = new LixoRede(0, 0); // dano = 2
        // Simula caminho completo para o lixo
        chegouAoFim.definirCaminho(List.of(new int[]{0, 0}));
        assertTrue(chegouAoFim.chegouAoFim());

        InimigoLixo vivo = new LixoGarrafa(1, 1);

        List<InimigoLixo> ativos = new ArrayList<>(List.of(morto, chegouAoFim, vivo));

        assertEquals(100, jogador.getDinheiro());
        assertEquals(10, jogador.getVida());

        collisionService.processarLimpezaEResolucao(ativos, jogador);

        // Conchas e score creditados pelo inimigo destruído
        assertEquals(110, jogador.getDinheiro());
        assertEquals(10, jogador.getScore());

        // Vida descontada pelo inimigo que chegou ao fim
        assertEquals(8, jogador.getVida());

        // Apenas o inimigo vivo e no meio do trajeto permanece na lista
        assertEquals(1, ativos.size());
        assertTrue(ativos.contains(vivo));
    }

    @Test
    public void testIntegracaoComMapaReal() throws Exception {
        Mapa mapa = new Mapa(); // mapa 10x14 com caminho e slots
        TartarugaVerde torre = new TartarugaVerde(1, 1);
        mapa.posicionarTartaruga(1, 1, torre); // slot (1, 1)

        InimigoLixo lixo = new LixoPlastico(0, 0);
        lixo.definirCaminho(mapa.getCaminho()); // no mapa padrão, caminho inicia em (linha=2, coluna=0)

        List<InimigoLixo> inimigos = new ArrayList<>(List.of(lixo));

        // Torre em (col=1, linha=1). Lixo em (col=0, linha=2).
        // deltaX = 1, deltaY = 1 -> dist = sqrt(2) ~= 1.414 <= alcance (2)
        collisionService.verificarColisoesEAtaques(mapa, inimigos);

        assertEquals(2, collisionService.getAtaquesDoTurno().size()); // TartarugaVerde possui velocidade = 2 (2 ataques)
        assertEquals(50 - 30, lixo.getVidaAtual()); // 2 ataques de 15 = 30 dano
    }
}
