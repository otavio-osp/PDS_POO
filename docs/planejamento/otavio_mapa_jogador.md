# 🗺️ Planejamento Individual: Otávio Soares Pedroso
## Módulos: Mapa (Malha / Grid) & Entidade Jogador

---

### 1. Suas Responsabilidades Principais
1. **Estrutura do Mapa (Malha):**
   - Criar a matriz bidimensional (`CelulaGrid[][]`) que representará o tabuleiro do jogo.
   - Definir os tipos de células: 
     - Trilha do Rio/Mar (onde os lixos se movimentam da nascente até a praia).
     - Células de Terra Válidas (onde o jogador pode clicar para colocar ou upar tartarugas).
     - Células Bloqueadas (cenário inativo onde não se coloca nada e lixo não passa).
     - Ponto Inicial de Spawn (onde os dejetos surgem).
     - Ponto Final (a Praia, onde o lixo que passar causa dano ao jogador).
2. **Entidade Jogador (Estado da Partida):**
   - Atributos principais: `vidaPraia` (quantidade de dano que pode receber antes do Game Over), `conchas` (moeda para comprar/upar tartarugas), `score` (soma acumulada dos pontos/XPs de lixos eliminados) e `waveAtual`.
   - Métodos para debitar dano na vida, adicionar conchas ganhas e verificar condição de derrota (`vida <= 0`).

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre a Matriz e o Caminho do Rio:
1. **Qual será o tamanho da matriz do tabuleiro?**  
   - Exemplo: $8 \times 10$, $10 \times 12$ ou $12 \times 16$? Uma matriz menor deixa as tartarugas mais próximas do caminho, enquanto uma maior exige mais estratégia de alcance.
2. **Como o trajeto do lixo será representado no código?**  
   - O caminho será uma lista ordenada de coordenadas (ex: `List<Coordenada> caminho = [(0,2), (1,2), (2,2), (2,3)...]`) ou cada célula de água terá um ponteiro para a `proximaCelula`?  
   - *Dica:* Uma lista de coordenadas fixa facilita muito o trabalho da Bruna (movimentação) e da Letícia (saber qual lixo está mais à frente).
3. **O caminho terá bifurcações (mais de um rio) ou será um caminho único linear?**
4. **Quantas células de terra ao redor do rio estarão liberadas para colocar tartarugas?** Todas as de terra ou haverá terrenos especiais com bônus de visão/alcance?

#### 🔹 Sobre a Célula e Interação:
5. **O que acontece se o jogador clicar em uma célula do rio?** O clique deve ser ignorado ou emitir algum feedback sonoro/visual de "posicionamento inválido"?
6. **Uma célula de terra pode ter mais de uma tartaruga ao mesmo tempo?** Como o método `isOcupada()` deve se comportar?

#### 🔹 Sobre a Entidade Jogador:
7. **Quais são os valores iniciais do Jogador ao iniciar uma nova partida?**  
   - Vida inicial da praia: 20 pontos? 100 pontos?
   - Conchas iniciais: 100 conchas? Dá para comprar quantas tartarugas logo no round 1?
8. **Como o Score será calculado?**  
   - Será estritamente a soma de XP dos lixos abatidos, ou haverá bônus de conchas sobrando e bônus ao zerar cada wave?
9. **O que acontece imediatamente quando a `vidaPraia` chega a 0?**  
   - O jogo pausa na hora? Dispara um evento/notificação de Game Over para o controlador do Pedro salvar o ranking?

