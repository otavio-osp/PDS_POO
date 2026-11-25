package br.ufv.ccf313.projeto.service;

import br.ufv.ccf313.projeto.model.Jogador;
import br.ufv.ccf313.projeto.model.inimigo.InimigoLixo;
import br.ufv.ccf313.projeto.model.mapa.Celula;
import br.ufv.ccf313.projeto.model.mapa.Mapa;
import br.ufv.ccf313.projeto.model.tartaruga.TorreTartaruga;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CollisionService - Responsável pela lógica de combate, detecção de alcance
 * e aplicação direta de dano das tartarugas contra os lixos marinhos.
 */
public class CollisionService {
    /**
     * Representa o registro de um ataque desferido por uma tartaruga a um lixo.
     * Utilizado pela interface gráfica para desenhar a linha reta (g.drawLine).
     */
    public static class RegistroAtaque {
        private final TorreTartaruga torre;
        private final InimigoLixo alvo;
        private final double distancia;

        public RegistroAtaque(TorreTartaruga torre, InimigoLixo alvo, double distancia) {
            this.torre = torre;
            this.alvo = alvo;
            this.distancia = distancia;
        }

        public TorreTartaruga getTorre() {
            return torre;
        }

        public InimigoLixo getAlvo() {
            return alvo;
        }

        public double getDistancia() {
            return distancia;
        }

        @Override
        public String toString() {
            return "Ataque [" + torre.getNome() + " em (" + torre.getPosX() + "," + torre.getPosY() +
                   ") -> " + alvo.getClass().getSimpleName() + " em (" + alvo.getPosX() + "," + alvo.getPosY() +
                   ") | Distancia: " + String.format("%.2f", distancia) + "]";
        }
    }

    private final List<RegistroAtaque> ataquesDoTurno = new ArrayList<>();
    private final Map<TorreTartaruga, InimigoLixo> alvosAtingidos = new HashMap<>();

    /**
     * Executa a Etapa 3 do Game Loop: percorre a lista de tartarugas e a lista de lixos ativos,
     * verifica o alcance e aciona o ataque direto no primeiro alvo ao alcance de cada tartaruga.
     * 
     * @param tartarugas Lista de tartarugas ativas no mapa.
     * @param inimigosAtivos Lista de lixos atualmente ativos no rio.
     */
    public void verificarColisoesEAtaques(List<TorreTartaruga> tartarugas, List<InimigoLixo> inimigosAtivos) {
        limparAtaquesDoTurno();

        if (tartarugas == null || inimigosAtivos == null || tartarugas.isEmpty() || inimigosAtivos.isEmpty()) {
            return;
        }

        for (TorreTartaruga torre : tartarugas) {
            if (torre == null || !torre.isAtiva()) {
                continue;
            }

            int ataquesRestantes = Math.max(1, torre.getVelocidade());

            for (int a = 0; a < ataquesRestantes; a++) {
                InimigoLixo alvo = encontrarPrimeiroAlvoNoAlcance(torre, inimigosAtivos);
                if (alvo != null) {
                    double distancia = calcularDistancia(torre, alvo);

                    // Aplica dano direto conforme especificado no plano do protótipo
                    alvo.receberDano(torre.getDano());

                    // Registra o ataque para exibição visual na tela (g.drawLine)
                    registrarAtaque(torre, alvo, distancia);

                    // Se houver mais de um lixo na mesma célula, aplica dano simultâneo
                    aplicarDanoEmAreaMesmaCelula(torre, alvo, inimigosAtivos);
                } else {
                    break; // Nenhum inimigo no alcance nesta rodada de ataque
                }
            }
        }
    }

    /**
     * Sobrecarga conveniente que extrai as tartarugas diretamente do Mapa.
     * 
     * @param mapa Mapa do jogo contendo as células com tartaruga.
     * @param inimigosAtivos Lista de lixos ativos no rio.
     */
    public void verificarColisoesEAtaques(Mapa mapa, List<InimigoLixo> inimigosAtivos) {
        if (mapa == null) {
            limparAtaquesDoTurno();
            return;
        }

        List<TorreTartaruga> tartarugas = extrairTartarugasDoMapa(mapa);
        verificarColisoesEAtaques(tartarugas, inimigosAtivos);
    }

    /**
     * Alias para compatibilidade com a nomenclatura de processamento do GameLoop.
     */
    public void processarAtaques(List<TorreTartaruga> tartarugas, List<InimigoLixo> inimigosAtivos) {
        verificarColisoesEAtaques(tartarugas, inimigosAtivos);
    }

    /**
     * Sobrecarga de processarAtaques recebendo o Mapa.
     */
    public void processarAtaques(Mapa mapa, List<InimigoLixo> inimigosAtivos) {
        verificarColisoesEAtaques(mapa, inimigosAtivos);
    }

    /**
     * Calcula a distância euclidiana entre a tartaruga e o lixo utilizando:
     * Math.hypot(inimigo.getPosX() - torre.getPosX(), inimigo.getPosY() - torre.getPosY()).
     * 
     * @param torre A tartaruga defensora.
     * @param inimigo O lixo invasor.
     * @return A distância euclidiana calculada.
     */
    public double calcularDistancia(TorreTartaruga torre, InimigoLixo inimigo) {
        if (torre == null || inimigo == null) {
            return Double.MAX_VALUE;
        }
        return Math.hypot(inimigo.getPosX() - torre.getPosX(), inimigo.getPosY() - torre.getPosY());
    }

    /**
     * Verifica se o inimigo está dentro do raio de alcance da tartaruga.
     * 
     * @param torre A tartaruga defensora.
     * @param inimigo O lixo invasor.
     * @return true se a distância for menor ou igual a torre.getAlcance(), false caso contrário.
     */
    public boolean estaNoAlcance(TorreTartaruga torre, InimigoLixo inimigo) {
        if (torre == null || inimigo == null || inimigo.estaDestruido() || !inimigo.isAtiva()) {
            return false;
        }
        return calcularDistancia(torre, inimigo) <= torre.getAlcance();
    }

    /**
     * Busca o primeiro lixo ativo dentro do raio de ação (alcance) da tartaruga.
     * A lista de inimigos ativos preserva a ordem do caminho/rio.
     * 
     * @param torre A tartaruga que irá atacar.
     * @param inimigosAtivos Lista de lixos presentes no jogo.
     * @return O primeiro inimigo dentro do alcance, ou null se nenhum estiver ao alcance.
     */
    public InimigoLixo encontrarPrimeiroAlvoNoAlcance(TorreTartaruga torre, List<InimigoLixo> inimigosAtivos) {
        if (torre == null || inimigosAtivos == null) {
            return null;
        }

        for (InimigoLixo inimigo : inimigosAtivos) {
            if (inimigo != null && !inimigo.estaDestruido() && inimigo.isAtiva()) {
                if (estaNoAlcance(torre, inimigo)) {
                    return inimigo;
                }
            }
        }
        return null;
    }

    /**
     * Retorna todos os inimigos ativos que estão dentro do alcance da tartaruga.
     * 
     * @param torre A tartaruga.
     * @param inimigosAtivos Lista de inimigos ativos.
     * @return Lista de inimigos no alcance.
     */
    public List<InimigoLixo> listarInimigosNoAlcance(TorreTartaruga torre, List<InimigoLixo> inimigosAtivos) {
        List<InimigoLixo> resultado = new ArrayList<>();
        if (torre == null || inimigosAtivos == null) {
            return resultado;
        }

        for (InimigoLixo inimigo : inimigosAtivos) {
            if (inimigo != null && !inimigo.estaDestruido() && inimigo.isAtiva()) {
                if (estaNoAlcance(torre, inimigo)) {
                    resultado.add(inimigo);
                }
            }
        }
        return resultado;
    }

    /**
     * Aplica dano a outros inimigos que estejam ocupando a mesma célula do alvo principal.
     */
    private void aplicarDanoEmAreaMesmaCelula(TorreTartaruga torre, InimigoLixo alvoPrincipal, List<InimigoLixo> todos) {
        if (torre == null || alvoPrincipal == null || todos == null) {
            return;
        }

        int targetX = alvoPrincipal.getPosX();
        int targetY = alvoPrincipal.getPosY();

        for (InimigoLixo inimigo : todos) {
            if (inimigo != null && inimigo != alvoPrincipal && !inimigo.estaDestruido() && inimigo.isAtiva()) {
                if (inimigo.getPosX() == targetX && inimigo.getPosY() == targetY) {
                    inimigo.receberDano(torre.getDano());
                    registrarAtaque(torre, inimigo, calcularDistancia(torre, inimigo));
                }
            }
        }
    }

    /**
     * Registra o ataque ocorrido no turno para consulta pela interface gráfica.
     */
    private void registrarAtaque(TorreTartaruga torre, InimigoLixo alvo, double distancia) {
        RegistroAtaque registro = new RegistroAtaque(torre, alvo, distancia);
        ataquesDoTurno.add(registro);
        alvosAtingidos.put(torre, alvo);
    }

    /**
     * Retorna a lista de todos os ataques desferidos no turno atual.
     * Utilizada por Marina e Caroline no paintComponent para desenhar g.drawLine().
     */
    public List<RegistroAtaque> getAtaquesDoTurno() {
        return Collections.unmodifiableList(ataquesDoTurno);
    }

    /**
     * Retorna o mapeamento de cada tartaruga para o alvo que ela atingiu no turno atual.
     */
    public Map<TorreTartaruga, InimigoLixo> getAlvosAtingidos() {
        return Collections.unmodifiableMap(alvosAtingidos);
    }

    /**
     * Retorna o alvo atingido por uma tartaruga específica no turno atual, ou null se não atacou.
     */
    public InimigoLixo getAlvoAtingido(TorreTartaruga torre) {
        return alvosAtingidos.get(torre);
    }

    /**
     * Limpa os registros de ataques do turno anterior.
     */
    public void limparAtaquesDoTurno() {
        ataquesDoTurno.clear();
        alvosAtingidos.clear();
    }

    /**
     * Executa a Etapa 4 do Game Loop (Limpeza e Resolução):
     * - Lixos com estaDestruido() concedem conchas/score ao jogador e saem da lista.
     * - Lixos com chegouAoFim() reduzem a vida do jogador e saem da lista.
     * 
     * @param inimigosAtivos Lista de lixos ativos no jogo.
     * @param jogador O jogador da partida.
     */
    public void processarLimpezaEResolucao(List<InimigoLixo> inimigosAtivos, Jogador jogador) {
        if (inimigosAtivos == null || inimigosAtivos.isEmpty()) {
            return;
        }

        for (InimigoLixo inimigo : inimigosAtivos) {
            if (inimigo == null) {
                continue;
            }

            if (inimigo.estaDestruido()) {
                if (jogador != null) {
                    jogador.registrarInimigoDestruido(inimigo.getRecompensa());
                }
            } else if (inimigo.chegouAoFim()) {
                if (jogador != null) {
                    jogador.receberDano(inimigo.getDano());
                }
                inimigo.desativar();
            }
        }

        removerInimigosInativos(inimigosAtivos);
    }

    /**
     * Remove com segurança todos os inimigos inativos (destruídos ou que chegaram ao fim).
     */
    public void removerInimigosInativos(List<InimigoLixo> inimigosAtivos) {
        if (inimigosAtivos == null) {
            return;
        }
        try {
            inimigosAtivos.removeIf(inimigo -> inimigo == null || inimigo.estaDestruido() || inimigo.chegouAoFim() || !inimigo.isAtiva());
        } catch (UnsupportedOperationException e) {
            // Caso receba lista imutável
        }
    }

    /**
     * Extrai a lista de instâncias de TorreTartaruga posicionadas no Mapa.
     */
    public List<TorreTartaruga> extrairTartarugasDoMapa(Mapa mapa) {
        List<TorreTartaruga> resultado = new ArrayList<>();
        if (mapa == null) {
            return resultado;
        }

        for (Celula celula : mapa.getCelulasComTartaruga()) {
            TorreTartaruga torre = celula.getTartaruga();
            if (torre != null) {
                // Garante a sincronia de coordenadas caso não tenham sido setadas
                torre.setPosicao(celula.getColuna(), celula.getLinha());
                resultado.add(torre);
            }
        }
        return resultado;
    }
}
