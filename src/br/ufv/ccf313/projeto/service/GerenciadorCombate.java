package br.ufv.ccf313.projeto.service;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.mapa.Celula;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;
import java.util.ArrayList;
import java.util.List;

/**
 * GerenciadorCombate - Módulo de Combate, Detecção de Alcance & Resolução de Dano.
 * 
 * Responsável por:
 * 1. Integrar Mapa, Tartarugas e Inimigos (Lixo).
 * 2. Identificar células e inimigos dentro do alcance (distância Manhattan).
 * 3. Selecionar o lixo mais avançado no caminho em direção à praia.
 * 4. Desferir a quantidade de ataques correspondente à velocidade da tartaruga.
 * 5. Aplicar dano em área por célula (inimigos na mesma célula são atacados juntos).
 * 6. Creditar pontuação e recompensas no Jogador ao abater inimigos.
 * 7. Processar lixos que alcançam a praia, descontando vida do Jogador.
 * 
 * @author Lethícia Gonçalves da Silva
 */
public class GerenciadorCombate {

    /**
     * Executa a fase de combate da rodada para todas as tartarugas ativas no mapa.
     * 
     * Ordem de resolução:
     * 1. Tartarugas ativas atacam os alvos prioritários no alcance.
     * 2. Inimigos na mesma célula recebem dano em conjunto.
     * 3. Se um inimigo for abatido, credita XP/conchas ao Jogador.
     * 4. Inimigos destruídos são removidos da lista de ativos.
     *
     * @param mapa O mapa do jogo contendo as células e o caminho.
     * @param inimigosAtivos Lista de inimigos atualmente vivos e ativos.
     * @param jogador O jogador atual (para receber pontuação e recompensas).
     */
    public void processarCombate(Mapa mapa, List<InimigoLixo> inimigosAtivos, Jogador jogador) {
        if (mapa == null || inimigosAtivos == null || inimigosAtivos.isEmpty()) {
            return;
        }

        List<Celula> celulasComTorre = mapa.getCelulasComTartaruga();
        if (celulasComTorre == null || celulasComTorre.isEmpty()) {
            return;
        }

        for (Celula celula : celulasComTorre) {
            TorreTartaruga torre = celula.getTartaruga();
            if (torre == null || !torre.isAtiva()) {
                continue;
            }

            // Sincroniza a posição da tartaruga com a coordenada da célula
            torre.setPosicao(celula.getColuna(), celula.getLinha());

            // A velocidade da tartaruga determina quantos ataques ela desfere por rodada
            int quantidadeAtaques = Math.max(1, torre.getVelocidade());

            for (int ataque = 0; ataque < quantidadeAtaques; ataque++) {
                List<InimigoLixo> candidatos = inimigosNoAlcance(torre, celula, inimigosAtivos, mapa);
                if (candidatos.isEmpty()) {
                    break; // Nenhum inimigo no alcance nesta iteração de ataque
                }

                InimigoLixo alvo = inimigoMaisAvancado(candidatos, mapa);
                if (alvo == null) {
                    break;
                }

                // Dano em área na célula: todos os lixos na mesma célula do alvo recebem o ataque juntos
                List<InimigoLixo> alvosNaMesmaCelula = inimigosNaMesmaCelula(alvo, inimigosAtivos);
                for (InimigoLixo inimigo : alvosNaMesmaCelula) {
                    if (inimigo.estaDestruido()) {
                        continue;
                    }

                    torre.atacar(inimigo);

                    // Se a vida zerar, registra a recompensa no jogador imediatamente
                    if (inimigo.estaDestruido() && jogador != null) {
                        jogador.registrarInimigoDestruido(inimigo.getRecompensa());
                    }
                }
            }
        }

        // Lixos derrotados são removidos da lista de ativos
        removerInimigosDestruidos(inimigosAtivos);
    }

    /**
     * Retorna todos os inimigos vivos dentro do alcance de uma tartaruga.
     * Utiliza a distância Manhattan: |x1 - x2| + |y1 - y2| <= alcance.
     * 
     * @param torre A tartaruga atacante.
     * @param celulaTorre A célula onde a tartaruga está posicionada.
     * @param inimigos Lista de inimigos ativos.
     * @param mapa O mapa do jogo.
     * @return Lista de inimigos dentro do raio de alcance.
     */
    public List<InimigoLixo> inimigosNoAlcance(TorreTartaruga torre, Celula celulaTorre,
                                              List<InimigoLixo> inimigos, Mapa mapa) {
        List<InimigoLixo> resultado = new ArrayList<>();
        if (torre == null || inimigos == null) {
            return resultado;
        }

        int torreX = celulaTorre != null ? celulaTorre.getColuna() : torre.getPosX();
        int torreY = celulaTorre != null ? celulaTorre.getLinha() : torre.getPosY();
        int alcance = torre.getAlcance();

        for (InimigoLixo inimigo : inimigos) {
            if (inimigo == null || inimigo.estaDestruido() || !inimigo.isAtiva()) {
                continue;
            }

            int distancia = calcularDistanciaManhattan(inimigo.getPosX(), inimigo.getPosY(), torreX, torreY);
            if (distancia <= alcance) {
                resultado.add(inimigo);
            }
        }
        return resultado;
    }

    /**
     * Retorna o inimigo mais avançado em direção à praia (maior progresso no caminho do rio).
     * 
     * @param candidatos Lista de inimigos dentro do alcance.
     * @param mapa O mapa do jogo com o trajeto do rio.
     * @return O inimigo prioritário para ser atacado.
     */
    public InimigoLixo inimigoMaisAvancado(List<InimigoLixo> candidatos, Mapa mapa) {
        if (candidatos == null || candidatos.isEmpty()) {
            return null;
        }

        InimigoLixo maisAvancado = null;
        int maiorProgresso = Integer.MIN_VALUE;
        double menorDistanciaFim = Double.MAX_VALUE;

        int[] posFinal = mapa != null ? mapa.getPosicaoFinal() : null;

        for (InimigoLixo inimigo : candidatos) {
            if (inimigo == null || inimigo.estaDestruido()) {
                continue;
            }

            int progresso = obterProgressoNoCaminho(inimigo, mapa);

            if (progresso >= 0) {
                // Progresso baseado no índice do caminho: quanto maior, mais perto da praia
                if (progresso > maiorProgresso) {
                    maiorProgresso = progresso;
                    maisAvancado = inimigo;
                }
            } else if (maiorProgresso == Integer.MIN_VALUE && posFinal != null) {
                // Fallback secundário caso a posição do inimigo não esteja cadastrada no caminho
                double dist = calcularDistanciaManhattan(inimigo.getPosX(), inimigo.getPosY(), posFinal[1], posFinal[0]);
                if (dist < menorDistanciaFim) {
                    menorDistanciaFim = dist;
                    maisAvancado = inimigo;
                }
            } else if (maisAvancado == null) {
                maisAvancado = inimigo;
            }
        }

        return maisAvancado;
    }

    /**
     * Retorna o índice de progresso de um inimigo na lista ordenada do caminho do mapa.
     * O índice 0 é a nascente (spawn) e o último índice é a praia (destino).
     * 
     * @param inimigo O inimigo a ser verificado.
     * @param mapa O mapa contendo a rota.
     * @return O índice no caminho, ou -1 se não for encontrado.
     */
    public int obterProgressoNoCaminho(InimigoLixo inimigo, Mapa mapa) {
        if (inimigo == null || mapa == null || mapa.getCaminho() == null) {
            return -1;
        }

        List<int[]> caminho = mapa.getCaminho();
        // Busca do final para o início para priorizar o maior avanço em caso de sobreposição
        for (int i = caminho.size() - 1; i >= 0; i--) {
            int[] pos = caminho.get(i);
            // pos[0] = linha (Y), pos[1] = coluna (X)
            if (pos[0] == inimigo.getPosY() && pos[1] == inimigo.getPosX()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retorna todos os inimigos que ocupam a mesma célula da matriz do inimigo de referência.
     * Usado para garantir que todos os inimigos na mesma célula sofram o dano do ataque juntos.
     * 
     * @param referencia O inimigo principal selecionado como alvo.
     * @param todos A lista com todos os inimigos ativos.
     * @return Lista de inimigos na mesma coordenada (posX, posY).
     */
    public List<InimigoLixo> inimigosNaMesmaCelula(InimigoLixo referencia, List<InimigoLixo> todos) {
        List<InimigoLixo> resultado = new ArrayList<>();
        if (referencia == null || todos == null) {
            return resultado;
        }

        int targetX = referencia.getPosX();
        int targetY = referencia.getPosY();

        for (InimigoLixo inimigo : todos) {
            if (inimigo != null && !inimigo.estaDestruido() && inimigo.isAtiva()
                    && inimigo.getPosX() == targetX
                    && inimigo.getPosY() == targetY) {
                resultado.add(inimigo);
            }
        }
        return resultado;
    }

    /**
     * Retorna quais células do rio (caminho) estão dentro do alcance Manhattan da tartaruga.
     * Atende ao item de identificar células do rio ao alcance para mira frontal e exibição visual.
     * 
     * @param torre A tartaruga.
     * @param celulaTorre A célula onde a tartaruga está posicionada.
     * @param mapa O mapa do jogo.
     * @return Lista de células do rio alcançáveis.
     */
    public List<Celula> getCelulasRioNoAlcance(TorreTartaruga torre, Celula celulaTorre, Mapa mapa) {
        List<Celula> resultado = new ArrayList<>();
        if (torre == null || celulaTorre == null || mapa == null || mapa.getCaminho() == null) {
            return resultado;
        }

        int alcance = torre.getAlcance();
        int torreX = celulaTorre.getColuna();
        int torreY = celulaTorre.getLinha();

        for (int[] pos : mapa.getCaminho()) {
            int dist = calcularDistanciaManhattan(pos[1], pos[0], torreX, torreY);
            if (dist <= alcance) {
                Celula celula = mapa.getCelula(pos[0], pos[1]);
                if (celula != null) {
                    resultado.add(celula);
                }
            }
        }
        return resultado;
    }

    /**
     * Remove com segurança todos os inimigos destruídos da lista de inimigos ativos.
     * 
     * @param inimigosAtivos Lista mutável de inimigos.
     */
    public void removerInimigosDestruidos(List<InimigoLixo> inimigosAtivos) {
        if (inimigosAtivos == null) {
            return;
        }
        try {
            inimigosAtivos.removeIf(InimigoLixo::estaDestruido);
        } catch (UnsupportedOperationException e) {
            // Caso seja passada uma lista não modificável
        }
    }

    /**
     * Verifica os lixos que alcançaram a praia (posição final do caminho).
     * Aplica dano à vida do Jogador, desativa os lixos e os remove da lista de ativos.
     * 
     * @param mapa O mapa do jogo.
     * @param inimigosAtivos Lista de inimigos ativos.
     * @param jogador O jogador da partida.
     */
    public void processarInimigosChegaramAoFim(Mapa mapa, List<InimigoLixo> inimigosAtivos, Jogador jogador) {
        if (mapa == null || inimigosAtivos == null || inimigosAtivos.isEmpty()) {
            return;
        }

        int[] posicaoFinal = mapa.getPosicaoFinal();
        if (posicaoFinal == null) {
            return;
        }

        int finalY = posicaoFinal[0]; // linha
        int finalX = posicaoFinal[1]; // coluna

        for (InimigoLixo inimigo : inimigosAtivos) {
            if (inimigo == null || inimigo.estaDestruido()) {
                continue;
            }

            if (inimigo.getPosX() == finalX && inimigo.getPosY() == finalY) {
                if (jogador != null) {
                    jogador.receberDano(inimigo.getDano());
                }
                inimigo.desativar();
            }
        }

        removerInimigosDestruidos(inimigosAtivos);
    }

    /**
     * Executa o ciclo completo de resolução de combate da rodada:
     * 1. Ataque das tartarugas nos inimigos ao alcance.
     * 2. Remoção de inimigos destruídos e pontuação no jogador.
     * 3. Verificação de inimigos que alcançaram a praia e aplicação de dano ao jogador.
     * 
     * @param mapa O mapa do jogo.
     * @param inimigosAtivos Lista de inimigos ativos.
     * @param jogador O jogador da partida.
     */
    public void executarFaseCombateEResolucao(Mapa mapa, List<InimigoLixo> inimigosAtivos, Jogador jogador) {
        processarCombate(mapa, inimigosAtivos, jogador);
        processarInimigosChegaramAoFim(mapa, inimigosAtivos, jogador);
    }

    /**
     * Calcula a distância Manhattan entre dois pontos discretos (x1, y1) e (x2, y2).
     */
    public int calcularDistanciaManhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
