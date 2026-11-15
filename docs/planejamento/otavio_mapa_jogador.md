# 🗺️ Planejamento Individual: Otávio Soares Pedroso
## Módulos: Mapa (Malha / Grid) & Entidade Jogador

---

### 1. Suas Responsabilidades Principais
1. **Estrutura do Mapa (Malha de Células):**
   - Implementar a matriz bidimensional (`grade[linhas][colunas]`) na classe `Mapa`.
   - Utilizar o enum `TipoCelula`:
     - `CAMINHO`: Rio/Mar onde os dejetos se movimentam em direção à praia.
     - `LIVRE`: Célula de terra onde o jogador pode clicar para comprar ou colocar uma tartaruga.
     - `OCUPADA`: Célula que já possui uma `TorreTartaruga` posicionada (permite clicar para upgrade).
   - Manter a sequência ordenada de coordenadas do trajeto (`List<int[]> caminho`) da nascente até a praia.
2. **Entidade Jogador (Estado da Partida):**
   - Atributos centrais já estruturados em `Jogador.java`:
     - `vida`: Inimigos que podem passar antes da derrota (Game Over).
     - `dinheiro`: Moeda para comprar tartarugas ou upgrades.
     - `score`: Pontuação total obtida pela soma do XP dos lixos abatidos.
     - `waveAtual`: Onda em que o jogador se encontra.
   - Fornecer métodos para debitar dano na vida (`receberDano`), adicionar recompensas (`registrarInimigoDestruido`), validar compras (`gastarDinheiro`) e checar vitalidade (`estaVivo`).

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre a Matriz e o Caminho do Rio:
1. **Qual será o tamanho da matriz do tabuleiro?**  
   - Exemplo sugerido: $8 \times 12$ ou $10 \times 14$ células.
2. **O caminho terá formato linear simples ou em zigue-zague (curvas)?**  
   - *Dica:* Um caminho em zigue-zague (curvas) permite que uma mesma tartaruga tenha o rio no seu alcance por mais rodadas.
3. **O clique em célula de caminho (rio/mar) terá algum feedback?**  
   - Ignorar o clique ou acionar mensagem discreta ("Posição inválida: fluxo do rio")?

#### 🔹 Sobre o Jogador e Balanceamento Inicial:
4. **Quais são os valores iniciais da partida?**  
   - `vidaInicial`: ex: 10 a 20 vidas (cada lixo comum que passa tira 1).
   - `dinheiroInicial`: ex: 150 a 200 conchas (permite colocar 1 tartaruga média ou 2 básicas no início).
5. **Comportamento imediato ao zerar a vida (`vida <= 0`):**  
   - Disparar o Game Over chamando `TelaJogo.mostrarGameOver()` para registrar a pontuação e wave com o Pedro.
