# 🛢️ Planejamento Individual: Bruna Lima Ribeiro
## Módulo: Inimigos (Lixo Marinho) & Mecânicas de Movimento e Atributos

---

### 1. Suas Responsabilidades Principais
1. **Espécies de Inimigos (3 Subclasses Concretas):**
   - Herdam da classe abstrata `InimigoLixo` (no pacote `br.ufv.ccf313.projeto.model.inimigo`):
     - `LixoPlastico`: Padrão/Equilibrado (Vida: 50, Vel: 1 célula/rodada, Recompensa: 10, Dano ao Jogador: 1).
     - `LixoGarrafa`: Rápido e Frágil (Vida: 30, Vel: 2 células/rodada, Recompensa: 15, Dano ao Jogador: 1).
     - `LixoRede`: Resistente/Tanque (Vida: 150, Vel: 0 células/rodada [leva 2 rodadas para mover 1 célula], Recompensa: 30, Dano ao Jogador: 2).
2. **Ciclo de Movimentação no Caminho:**
   - Deslocamento sequencial pelas células do rio (`caminho` gerado pelo Otávio).
   - O lixo só anda **depois** que todas as tartarugas no alcance realizarem seus respectivos ataques naquela rodada.
3. **Escalonamento e Buffs por Onda:**
   - Utilizar o método `aplicarBustDeWave(bonusVida, bonusVelocidade)` disparado a cada onda pela Helena para reforçar os inimigos.
4. **Resolução de Eliminação e Dano:**
   - Ao zerar vida (`vidaAtual <= 0`), desativa-se e libera o valor de `recompensa` em conchas e score.
   - Ao atingir o final do percurso, aplica dano na vida do `Jogador` (`jogador.receberDano(dano)`).

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre a Velocidade e Movimentação:
1. **Como implementar o controle da Velocidade 0 (andar a cada 2 rodadas)?**  
   - Sugestão: um contador interno que incrementa a cada turno. Quando for ímpar apenas espera, quando for par avança 1 célula.
2. **Lixo com Velocidade 2:**  
   - Ele pula direto 2 células ou computa passagem rápida célula por célula? (Recomendado: mover 2 células no mesmo ciclo de movimento após a fase de ataque).

#### 🔹 Sobre Sobreposição de Inimigos:
3. **Múltiplos lixos na mesma célula:**  
   - Foi acordado que se dois lixos ocuparem a mesma célula, ambos sofrem o ataque da tartaruga simultaneamente para simplificação do código.
