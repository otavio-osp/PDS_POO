# 🌊 Planejamento Individual: Helena Santos Perez
## Módulo: Gerenciamento de Ondas (WaveService) & Ciclo do Jogo (GameLoop)

---

### 1. Suas Responsabilidades Principais
1. **Lógica de Ondas (WaveService):**
   - Determinar quantos lixos e de quais espécies entrarão em cada uma das 10 ondas do jogo.
   - Gerenciar o estado da onda: `AGUARDANDO_INICIO`, `EM_ANDAMENTO`, `FINALIZADA`.
   - Controlar o intervalo de spawn (os lixos não podem entrar todos na mesma célula no mesmo segundo; devem ter um espaçamento temporal ou por rodadas).
2. **Escalonamento e Recompensas de Wave:**
   - Calcular e aplicar o buff progressivo de dificuldade para a próxima onda (combinado com a Bruna).
   - Conceder bônus de XP/score e conchas ao jogador ao concluir com sucesso uma onda inteira.
3. **Condições de Vitória:**
   - Detectar quando todos os lixos da 10ª onda foram eliminados, declarando a **Vitória do Jogador** e acionando a tela de vitória e o salvamento de recorde com o Pedro.

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre a Composição das Ondas:
1. **Como será a tabela de ondas do jogo (ex: 10 ondas no total)?**  
   - Exemplo de progressão:
     - *Wave 1:* 5 Garrafas PET (fácil, para aprender).
     - *Wave 2:* 8 Garrafas PET + 2 Microplásticos rápidos.
     - *Wave 3:* 6 Garrafas + 5 Microplásticos + 1 Rede Fantasma (primeiro minichefe).
     - *Wave 4 a 9:* Aumento gradual de quantidade, mesclando tipos de lixo com buffs de HP.
     - *Wave 10 (Chefe Final):* Uma grande enxurrada de lixos rápidos liderados por 3 Redes Fantasmas gigantes.  
     *Você prefere definir essa tabela estática em código ou gerar com algum grau de aleatoriedade controlada?*

2. **Como funcionará o Spawn (entrada) dos lixos no tabuleiro?**  
   - Em cada tick do jogo, se a lista de lixos da wave ainda não estiver vazia, entra 1 lixo na coordenada $(0,0)$ a cada $K$ segundos?
   - O que acontece se a célula inicial de spawn já estiver ocupada por um lixo que ainda não andou? O próximo lixo espera a célula vagar ou entra na mesma célula (empilhando)?

#### 🔹 Sobre a Transição entre Waves:
3. **A transição de wave é automática ou manual?**  
   - *Opção A (Manual):* Ao matar o último lixo da onda, o jogo entra em pausa estratégica ("Fase de Preparação"), permitindo ao jogador respirar, comprar tartarugas e clicar no botão "Iniciar Próxima Onda". (Recomendado para Tower Defense!).
   - *Opção B (Automática):* Terminou a wave, há uma contagem regressiva de 5 segundos e a próxima começa sozinha.  
   *Qual das duas opções será adotada?*

4. **Bônus de Fim de Onda:**  
   - O jogador ganha conchas extras ao completar uma wave (ex: $+50$ conchas de bônus da prefeitura/ONG ecológica)?
   - Quanto de XP fixo por onda será somado ao score final?

#### 🔹 Sobre o GameLoop e Pausa:
5. **Como o tempo de jogo será controlado no código?**  
   - Será usado um `javax.swing.Timer` ou `AnimationTimer` do JavaFX rodando a uma taxa fixa (ex: 60 FPS ou ticks lógicos de 500ms)?
   - Terá opção de pausar o jogo ou acelerar a velocidade para $2\times$?

