# 🛢️ Planejamento Individual: Bruna Lima Ribeiro
## Módulo: Inimigos (Lixo Marinho) & Mecânicas de Movimento e Atributos

---

### 1. Suas Responsabilidades Principais
1. **Tipos de Lixo Marinho (3 tipos concretos):**
   - Modelar a hierarquia a partir de `InimigoLixo` (ex: `GarrafaPet`, `RedeFantasma`, `Microplastico`).
   - Atributos de cada tipo de lixo:
     - `vida`: pontos de integridade física (quanto dano suporta antes de ser destruído).
     - `velocidade`: taxa de deslocamento na matriz (ex: 1 célula por rodada, 2 células por rodada, ou 0 = precisa de 2 rodadas para andar 1 célula).
     - `danoNaPraia`: quantos pontos de vida serão subtraídos do Jogador se este lixo chegar ao final da praia.
     - `valorConchas`: recompensa em moedas concedida ao jogador ao ser abatido.
     - `xp`: pontos que serão somados ao score geral da partida ao ser eliminado.
2. **Mecânica de Movimentação por Rodada:**
   - O lixo segue estritamente a sequência de coordenadas do rio pré-definida pelo Otávio.
   - O lixo só anda **depois** que todas as tartarugas no alcance tiverem executado suas ações/ataques naquela rodada.
3. **Escalonamento e Buffs por Wave:**
   - A cada nova wave iniciada pela Helena, os atributos base dos lixos recebem multiplicadores de escalonamento para aumentar a dificuldade progressivamente.

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre os Tipos de Lixo e Perfis:
1. **Quais serão os 3 tipos de lixo e seus papéis no jogo?**  
   - Exemplo de proposta:
     - **Garrafa PET (Equilibrado/Comum):** Vida média (ex: 40 HP), Velocidade 1 (anda 1 célula por turno), Dano na praia 2, Recompensa 10 conchas e 15 XP.
     - **Microplástico (Rápido e Frágil):** Vida baixa (ex: 18 HP), Velocidade 2 (anda 2 células por turno, difícil de pegar), Dano na praia 1, Recompensa 15 conchas e 25 XP.
     - **Rede Fantasma / Pneu (Tanque Resistente):** Vida altíssima (ex: 120 HP), Velocidade 0 (leva 2 rodadas para andar 1 célula), Dano na praia 5, Recompensa 35 conchas e 50 XP.  
     *Como você pretende distribuir esses valores?*

#### 🔹 Sobre a Velocidade e Ciclo de Movimento:
2. **Como implementar a Velocidade 0 (lixo que anda a cada 2 rodadas)?**  
   - Será com um contador interno `contadorTurnosParaAndar`? Quando o contador atingir 2, ele move 1 célula e zera o contador?
3. **Se um lixo com velocidade 2 andar 2 células em um único turno, ele pode "pular" o alcance de uma tartaruga sem levar dano?**  
   - Ou ele deve computar a passagem célula por célula para que as tartarugas tenham a chance de atacá-lo em cada célula do trajeto?

#### 🔹 Sobre Sobreposição e Dano em Área na Célula:
4. **Mecânica de Múltiplos Lixos na Mesma Célula:**  
   - Foi acordado que se houver 2 ou mais lixos na mesma célula, **ambos levam o ataque simultaneamente** para simplificar o código.
   - Quando uma tartaruga atacar aquela célula, a função aplicará `lixo.receberDano(dano)` em toda a lista de lixos presentes ali?
   - Os dois lixos se movimentam juntos ou cada um tem seu próprio relógio de movimento?

#### 🔹 Sobre Buffs por Wave:
5. **Como os lixos ficam mais fortes a cada wave?**  
   - A vida aumenta em porcentagem fixa por wave (ex: $+15\%$ de vida a cada nova wave)?
   - O dano causado à praia também aumenta ou permanece fixo?
   - O valor das conchas e do XP dropados deve aumentar conforme o lixo fica mais forte?
6. **O que acontece no instante em que o lixo chega na última célula do caminho (a praia)?**  
   - Ele desaparece imediatamente do tabuleiro, chama `jogador.receberDano(this.danoNaPraia)` e não dá conchas nem XP?

