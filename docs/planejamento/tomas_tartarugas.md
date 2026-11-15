# 🐢 Planejamento Individual: Tomás Viana Resende (Tom)
## Módulo: Tartarugas (Torres) & Sistema de Upgrades

---

### 1. Suas Responsabilidades Principais
1. **Espécies de Tartarugas (3 Subclasses Concretas):**
   - Herdam da classe abstrata `TorreTartaruga` (localizada no pacote `br.ufv.ccf313.projeto.model.tartaruga`):
     - `TartarugaVerde`: Econômica e equilibrada (Custo: 50, Dano: 15, Alcance: 2, Velocidade: 2).
     - `TartarugaDePente`: Ágil e alta cadência (Custo: 100, Dano: 20, Alcance: 2, Velocidade: 3).
     - `TartarugaDeCouro`: Tanque de longo alcance e alto impacto (Custo: 200, Dano: 60, Alcance: 4, Velocidade: 1).
   - O ataque é direto no inimigo (`alvo.receberDano(dano)`), sem sistema de projéteis.
2. **Sistema de Upgrades:**
   - Métodos de evolução que alteram os atributos base (`setDano`, `setAlcance`, `setVelocidade`).
   - Disponibilizado via clique do jogador em uma célula com tartaruga (através do modal da Carol).
   - O custo do upgrade deve ser debitado no saldo do `Jogador` (`jogador.gastarDinheiro(custoUpgrade)`).

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre os Upgrades:
1. **Quantos níveis de upgrade cada tartaruga poderá receber?**  
   - Exemplo: Nível 1 (base) $\rightarrow$ Nível 2 $\rightarrow$ Nível 3 (máximo).
2. **Qual a fórmula ou valor de custo de cada upgrade?**  
   - Exemplo: 50% a 70% do custo de compra original.
3. **Qual foco de evolução de cada espécie?**  
   - Tartaruga de Couro: ganha mais dano bruto (+30) e +1 de alcance.
   - Tartaruga de Pente: ganha mais velocidade de ataque (+1 hit por turno).
   - Tartaruga Verde: evolução balanceada de dano e velocidade.

#### 🔹 Sobre o Combate e Foco de Alvo:
4. **Regra de Prioridade do Alvo:**  
   - Acordado: **sempre focar no lixo que está mais à frente na fila do rio**.
   - Se houver dois ou mais lixos na mesma célula prioritária, todos recebem o dano juntos.
