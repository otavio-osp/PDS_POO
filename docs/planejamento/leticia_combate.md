# ⚔️ Planejamento Individual: Lethícia Gonçalves da Silva
## Módulo: Sistema de Combate, Detecção de Alcance & Resolução de Dano

---

### 1. Suas Responsabilidades Principais
1. **Lógica de Combate (`GerenciadorCombate.java`):**
   - Integrar o **Mapa (Otávio)**, as **Tartarugas (Tomás)** e os **Lixos (Bruna)**.
   - Para cada tartaruga ativa no mapa:
     1. Identificar quais células do rio estão dentro do seu `alcance` (em quantidade de células).
     2. Buscar o lixo mais avançado em direção à praia (maior progresso no caminho).
     3. Desferir o número de ataques correspondente à `velocidade` da tartaruga.
2. **Resolução de Impacto:**
   - Aplicar `torre.atacar(inimigo)`.
   - Se houver mais de um lixo na mesma célula, ambos recebem o ataque juntos.
   - Quando a vida de um lixo zerar (`inimigo.estaDestruido()`), creditar XP e dinheiro via `jogador.registrarInimigoDestruido(recompensa)`.

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre Detecção de Alcance:
1. **Cálculo da Distância da Tartaruga até a Célula:**  
   - O alcance mede quantas células a tartaruga alcança. Na prática, a distância Manhattan $|x_1 - x_2| + |y_1 - y_2| \le \text{alcance}$ cobre as células vizinhas diretamente.  
   - A tartaruga mira frontalmente no segmento do rio ao seu alcance, atacando o lixo com maior progresso na lista de coordenadas do caminho.

#### 🔹 Sobre a Ordem do Turno:
2. **Ordem de Execução da Rodada:**  
   - Ordem acordada:
     1. Tartarugas no alcance atacam o primeiro lixo da fila.
     2. Lixos derrotados são removidos e pontuam no `Jogador`.
     3. Lixos sobreviventes se movimentam conforme suas velocidades.
     4. Lixos que alcançam a praia causam dano à vida do `Jogador` e são removidos.
