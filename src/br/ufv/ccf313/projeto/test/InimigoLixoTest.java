package br.ufv.ccf313.projeto.test;

import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.inimigo.LixoGarrafa;
import br.ufv.ccf313.projeto.model.inimigo.LixoPlastico;
import br.ufv.ccf313.projeto.model.inimigo.LixoRede;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// classe de teste para o lixo 
public class InimigoLixoTest {

    private List<int[]> caminhoTeste;


    // o caminho que será usado no teste para o lixo
    @BeforeEach
    public void setUp() {
        caminhoTeste = List.of(
            new int[]{2, 0}, // inicio da posicao -> (X=0, Y=2)
            new int[]{2, 1}, 
            new int[]{2, 2},
            new int[]{3, 2}  
        );
    }


    // definindo caminhos no mapa!!
    @Test
    public void testDefinirCaminhoAlinhaCoordenadasComMapa() {
        InimigoLixo garrafa = new LixoGarrafa(0, 0);
        garrafa.definirCaminho(caminhoTeste);

        assertEquals(0, garrafa.getPosX());
        assertEquals(2, garrafa.getPosY());
        assertFalse(garrafa.chegouAoFim());
    }


    // teste da movimentação do lixo no mapa, segundo o caminho do teste 
    @Test
    public void testMovimentacaoEChegadaAoFim() {
        InimigoLixo plastico = new LixoPlastico(0, 0);
        plastico.definirCaminho(caminhoTeste);

        for (int i = 0; i < 5; i++) {
            plastico.atualizar();
        }

        assertEquals(2, plastico.getPosX());
        assertEquals(3, plastico.getPosY());
        assertTrue(plastico.chegouAoFim());
    }


    // testando as velocidades de cada tipo de inimigo diferente
    @Test
    public void testInimigoLentoVelocidadeZeroAndaACadaDoisTurnos() {
        // LixoRede-> velo é 0, ou seja, 1 passo a cada 2 turnos
        InimigoLixo rede = new LixoRede(0, 0);
        rede.definirCaminho(caminhoTeste);

        if (rede.getVelocidade() == 0) {
            // turno 1 está parada ainda
            rede.atualizar();
            assertEquals(0, rede.getPosX());
            assertEquals(2, rede.getPosY());

            // turno 2 anda 1 celula
            rede.atualizar();
            assertEquals(1, rede.getPosX());
            assertEquals(2, rede.getPosY());
        }
    }


    // inimigo sofre um ataque da tartaruga 
    @Test
    public void testReceberDanoEDestruicao() {
        InimigoLixo garrafa = new LixoGarrafa(0, 0);
        int vidaInicial = garrafa.getVidaMaxima();

        garrafa.receberDano(10);
        assertEquals(vidaInicial - 10, garrafa.getVidaAtual());
        assertFalse(garrafa.estaDestruido());
        assertTrue(garrafa.isAtiva());

        // dano que o mata
        garrafa.receberDano(vidaInicial + 50);
        assertEquals(0, garrafa.getVidaAtual());
        assertTrue(garrafa.estaDestruido());
        assertFalse(garrafa.isAtiva());
    }


    // teste das ondas
    @Test
    public void testAplicarBustDeWave() {
        InimigoLixo garrafa = new LixoGarrafa(0, 0);
        int vidaAntes = garrafa.getVidaMaxima();
        int velAntes = garrafa.getVelocidade();

        garrafa.aplicarBustDeWave(25, 1);

        assertEquals(vidaAntes + 25, garrafa.getVidaMaxima());
        assertEquals(vidaAntes + 25, garrafa.getVidaAtual());
        assertEquals(velAntes + 1, garrafa.getVelocidade());
    }


    // conexão com o mapa real do otavio
    @Test
    public void testIntegracaoComCaminhoRealDoMapa() {
        Mapa mapa = new Mapa();
        InimigoLixo garrafa = new LixoGarrafa(0, 0);
        garrafa.definirCaminho(mapa.getCaminho());

        assertEquals(0, garrafa.getPosX());
        assertEquals(2, garrafa.getPosY());

        // execução dos turnos
        for (int i = 0; i < 30; i++) {
            garrafa.atualizar();
        }


        assertTrue(garrafa.chegouAoFim());
        assertEquals(13, garrafa.getPosX());
        assertEquals(7, garrafa.getPosY());
    }


    // teste extra para analisar os diferentes atributos das classes filhas
    @Test
    public void testAtributosIniciaisDasSubclasses() {
        InimigoLixo garrafa = new LixoGarrafa(0, 0);
        assertEquals(30, garrafa.getVidaMaxima());
        assertEquals(2, garrafa.getVelocidade());
        assertEquals(15, garrafa.getRecompensa());
        assertEquals(1, garrafa.getDano());

        InimigoLixo plastico = new LixoPlastico(0, 0);
        assertEquals(50, plastico.getVidaMaxima());
        assertEquals(1, plastico.getVelocidade());
        assertEquals(10, plastico.getRecompensa());
        assertEquals(1, plastico.getDano());

        InimigoLixo rede = new LixoRede(0, 0);
        assertEquals(150, rede.getVidaMaxima());
        assertEquals(0, rede.getVelocidade());
        assertEquals(30, rede.getRecompensa());
        assertEquals(2, rede.getDano());
    }
}