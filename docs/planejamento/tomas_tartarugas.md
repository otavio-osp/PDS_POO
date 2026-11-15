# 🐢 Planejamento Individual: Tomás Viana Resende (Tom)
## Módulo: Tartarugas (Torres) & Sistema de Upgrades

---

### 1. Suas Responsabilidades Principais
1. **Espécies de Tartarugas (3 tipos concretos):**
   - Modelar a hierarquia herdando da classe abstrata `TorreTartaruga` (ex: `TartarugaVerde`, `TartarugaDePente`, `TartarugaDeCouro`).
   - Atributos centrais de cada tartaruga:
     - `custoCompra`: quantidade de conchas necessárias para adquiri-la.
     - `range`: raio de alcance medido em número de células na matriz (ex: 2 células, 3 células).
     - `dano`: quantidade de vida que subtrai do(s) lixo(s) atingido(s).
     - `velocidadeAtaque`: quantos disparos/golpes ela desfere antes que o lixo dê um passo para a próxima célula.
     - `nivel`: nível atual da tartaruga (ex: Nível 1 a 3).
2. **Sistema de Upgrades:**
   - Métodos para aplicar upgrades quando o jogador clicar na tartaruga existente e tiver conchas suficientes.
   - Cada espécie de tartaruga deve ter um perfil de evolução diferente (ex: uma ganha mais alcance, outra mais cadência/velocidade, outra mais dano bruto).

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre os Atributos e Balanceamento:
1. **Quais serão os valores base das 3 espécies no Nível 1?**  
   - Exemplo de proposta para balanceamento:
     - **Tartaruga Verde (Econômica/Básica):** Custo baixo (ex: 40 conchas), Dano moderado (ex: 15), Range curto (2 células), Velocidade padrão (1 hit por turno).
     - **Tartaruga de Pente (Atiradora Rápida):** Custo médio (ex: 80 conchas), Dano baixo (ex: 8), Range longo (3 células), Velocidade alta (2 hits por turno).
     - **Tartaruga de Couro (Tanque/Dano Pesado):** Custo alto (ex: 120 conchas), Dano massivo (ex: 35), Range curto (1 ou 2 células), Velocidade lenta (1 hit a cada 2 turnos).  
     *Você concorda com esses perfis ou prefere outros valores?*

2. **Como o Range (alcance) é medido geometricamente na matriz?**  
   - **Definição Fechada:** O alcance é **direcional e frontal**, focado em analisar **o que está exatamente na frente** da tartaruga ao longo do fluxo do rio até o limite de $Range$ células.
   - A tartaruga posicionada mira diretamente para a seção do caminho que passa à sua frente. Se $Range = 2$, ela enxerga até 2 células à sua frente na trilha; se $Range = 3$, até 3 células à frente.
   - Isso garante que a tartaruga concentre seus disparos em linha reta/frontal contra o avanço dos dejetos.

#### 🔹 Sobre o Sistema de Upgrades:
3. **Quantos níveis de upgrade cada tartaruga pode receber?**  
   - Até o Nível 3 (Base $\rightarrow$ Nível 2 $\rightarrow$ Nível 3)? Ou upgrades ilimitados com custo exponencial?
4. **Qual é o custo do upgrade?**  
   - É uma porcentagem do custo de compra (ex: 70% do valor da torre) ou um valor fixo por nível?
5. **O que muda exatamente em cada upgrade?**  
   - Exemplo: Ao upar a Tartaruga de Pente, aumenta o Range de 3 para 4 e a Velocidade de 2 para 3, mas o dano quase não sobe. Ao upar a de Couro, o dano dobra mas o range continua 1. Como será a tabela de upgrades de cada espécie?
6. **Existirá opção de "Vender Torre"?**  
   - Se o jogador quiser remover uma tartaruga da célula para colocar outra, ele recebe algum reembolso de conchas (ex: 50% do valor investido)?

#### 🔹 Sobre a Seleção de Alvo:
7. **Regra de Prioridade do Alvo:**  
   - A regra padrão acordada é: **sempre focar no lixo que está mais à frente no caminho**.  
   - Se houver dois tipos de lixos na mesma célula mais avançada, o dano atinge os dois juntos conforme a regra de simplificação acordada?

