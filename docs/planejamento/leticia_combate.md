# ⚔️ Planejamento Individual: Lethícia Gonçalves da Silva
## Módulo: Sistema de Combate, Detecção de Alcance & Resolução de Dano

---

### 1. Suas Responsabilidades Principais
1. **O "Cérebro" do Combate:**
   - Atuar como a ponte lógica entre as **Tartarugas (Tomás)**, os **Lixos (Bruna)** e o **Mapa (Otávio)**.
   - A cada rodada do ciclo do jogo, verificar para cada tartaruga ativa no tabuleiro:
     1. Quais células do rio estão dentro do seu raio de alcance ($Range$).
     2. Dentre os lixos presentes nessas células, qual está **mais avançado** em direção à praia (alvo prioritário).
     3. Executar o número de ataques permitido pela `velocidadeAtaque` daquela tartaruga.
2. **Aplicação de Dano e Limpeza:**
   - Descontar a vida do(s) lixo(s) atingido(s).
   - Se houver mais de um lixo na mesma célula alvo, aplicar o dano a todos eles (regra de dano em área na célula).
   - Quando a vida de um lixo zerar (`vida <= 0`), removê-lo do mapa, creditar as conchas e somar o XP ao `Jogador`.
   - Notificar a Marina para disparar as animações de ataque da tartaruga e impacto no lixo.

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre Detecção de Alcance e Posição:
1. **Como determinar com precisão qual lixo está "mais à frente"?**  
   - Se o caminho do rio for uma `List<Coordenada> caminho`, cada coordenada possui um índice $0, 1, 2, \dots, N$.  
   - O lixo com o **maior índice na lista do caminho** é automaticamente o mais avançado.  
   *Você concorda com essa lógica para encontrar o alvo prioritário?*
2. **E se houver empate (dois lixos na mesma célula mais avançada)?**  
   - Pela regra acordada, a tartaruga ataca a célula e **ambos recebem o dano**. Como essa chamada de método será estruturada?
3. **Cálculo da Distância da Tartaruga até a Célula do Rio:**  
   - **Definição Fechada:** O alcance analisa **o que está exatamente na frente** da tartaruga ao longo da trilha até o limite de $Range$ células.
   - No `CollisionService`, a tartaruga mapeia o segmento frontal do rio à sua frente e seleciona o lixo visível mais próximo/avançado nessa linha de visada frontal.

#### 🔹 Sobre a Ordem de Execução do Turno (Tick):
4. **Qual é a ordem exata das operações dentro de uma rodada de combate?**  
   - Sugestão de fluxo:
     1. *Fase 1 (Combate):* Todas as tartarugas identificam seus alvos e atacam (aplicam dano).
     2. *Fase 2 (Baixas):* Lixos com HP $\le 0$ são removidos, conchas e XP são entregues ao jogador.
     3. *Fase 3 (Movimento):* Os lixos sobreviventes avançam pelo caminho de acordo com suas velocidades.
     4. *Fase 4 (Dano na Praia):* Lixos que alcançaram a praia causam dano ao jogador e são removidos.  
     *Esse fluxo faz sentido para você ou você inverteria o movimento e o ataque?*

#### 🔹 Sobre Velocidade de Ataque das Tartarugas:
5. **Se uma tartaruga tem Velocidade 2 (ataca 2 vezes antes do lixo andar):**  
   - Ela dá os 2 ataques no mesmo turno?
   - Se o primeiro ataque já matar o lixo alvo, o segundo ataque dela pode ser redirecionado para o próximo lixo da fila imediatamente no mesmo turno?
6. **E se uma tartaruga tiver Velocidade 0.5 (ataca a cada 2 turnos)?**  
   - Como será o controle de recarga/cooldown dessa tartaruga?

