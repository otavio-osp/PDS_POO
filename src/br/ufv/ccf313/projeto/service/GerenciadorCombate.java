package br.ufv.ccf313.projeto.service;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.mapa.Celula;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;
import java.util.ArrayList;
import java.util.List;

// LETICIA: logica de combate entre tartarugas e inimigos.
// Esta classe e chamada a cada rodada do jogo para verificar quais tartarugas
// podem atacar e quais inimigos estao dentro do alcance.
public class GerenciadorCombate {

    // Percorre todas as tartarugas posicionadas no mapa.
    // Para cada tartaruga, encontra o inimigo mais avancado dentro do seu alcance
    // e aplica o dano. Se o inimigo morrer, registra a recompensa no jogador.
    // Chamado pelo GameLoop (ou equivalente) a cada ciclo de jogo.
    // LETICIA: implementar esta logica.
    public void processarCombate(Mapa mapa, List<InimigoLixo> inimigosAtivos, Jogador jogador) {
        List<Celula> celulasComTorre = mapa.getCelulasComTartaruga();

        for (Celula celula : celulasComTorre) {
            TorreTartaruga torre = celula.getTartaruga();

            List<InimigoLixo> candidatos = inimigosNoAlcance(torre, celula, inimigosAtivos, mapa);

            if (candidatos.isEmpty()) continue;

            InimigoLixo alvo = inimigoMaisAvancado(candidatos, mapa);

            if (alvo != null) {
                // LETICIA: a tartaruga ataca todos os inimigos na mesma celula do alvo
                // (comportamento: 2 inimigos na mesma celula levam o ataque juntos)
                List<InimigoLixo> nasMesmaCelula = inimigosNaMesmaCelula(alvo, inimigosAtivos);
                for (InimigoLixo inimigo : nasMesmaCelula) {
                    torre.atacar(inimigo);
                    if (inimigo.estaDestruido()) {
                        jogador.registrarInimigoDestruido(inimigo.getRecompensa());
                    }
                }
            }
        }
    }

    // Retorna os inimigos dentro do alcance (em celulas) de uma tartaruga.
    // O alcance e calculado pela distancia no caminho: a tartaruga alcanca
    // os inimigos que estao em celulas a ate torre.getAlcance() passos de distancia dela.
    // LETICIA: definir como medir a distancia (posicao no grid ou indice no caminho).
    private List<InimigoLixo> inimigosNoAlcance(TorreTartaruga torre, Celula celulaTorre,
                                                  List<InimigoLixo> inimigos, Mapa mapa) {
        List<InimigoLixo> resultado = new ArrayList<>();
        int alcance = torre.getAlcance();

        for (InimigoLixo inimigo : inimigos) {
            if (inimigo.estaDestruido()) continue;
            int distancia = Math.abs(inimigo.getPosX() - celulaTorre.getColuna())
                          + Math.abs(inimigo.getPosY() - celulaTorre.getLinha());
            if (distancia <= alcance) {
                resultado.add(inimigo);
            }
        }
        return resultado;
    }

    // Retorna o inimigo mais proximo do fim do caminho (mais avancado na fila).
    // Prioridade de ataque: o que esta mais perto de causar dano ao jogador.
    // LETICIA: usar o indice do inimigo no caminho para determinar quem esta mais avancado.
    private InimigoLixo inimigoMaisAvancado(List<InimigoLixo> candidatos, Mapa mapa) {
        InimigoLixo maisAvancado = null;
        int[] posicaoFinal = mapa.getPosicaoFinal();
        if (posicaoFinal == null) return null;

        double menorDistanciaAoFim = Double.MAX_VALUE;

        for (InimigoLixo inimigo : candidatos) {
            double dist = Math.sqrt(
                Math.pow(inimigo.getPosX() - posicaoFinal[1], 2) +
                Math.pow(inimigo.getPosY() - posicaoFinal[0], 2)
            );
            if (dist < menorDistanciaAoFim) {
                menorDistanciaAoFim = dist;
                maisAvancado = inimigo;
            }
        }
        return maisAvancado;
    }

    // Retorna todos os inimigos que estao na mesma celula do inimigo de referencia.
    // Comportamento definido: 2 na mesma celula levam o ataque juntos.
    private List<InimigoLixo> inimigosNaMesmaCelula(InimigoLixo referencia, List<InimigoLixo> todos) {
        List<InimigoLixo> resultado = new ArrayList<>();
        for (InimigoLixo inimigo : todos) {
            if (!inimigo.estaDestruido()
                    && inimigo.getPosX() == referencia.getPosX()
                    && inimigo.getPosY() == referencia.getPosY()) {
                resultado.add(inimigo);
            }
        }
        return resultado;
    }
}
