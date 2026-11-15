# 🌊 Planejamento Individual: Helena Santos Perez
## Módulo: Gerenciamento de Ondas (`GerenciadorOndas.java`) & Ciclo de Partida

---

### 1. Suas Responsabilidades Principais
1. **Lógica de Ondas (`GerenciadorOndas.java`):**
   - Determinar a composição dos inimigos em cada uma das waves (`gerarInimigos`).
   - Aplicar bônus progressivo de atributos por onda (`aplicarBustDeWave`) em conjunto com a Bruna.
   - Detectar o encerramento da onda atual (`waveCompleta`) e verificar vitória geral do jogo (`jogoCompleto`).
2. **Integração com Jogador e Interface:**
   - Ao avançar a onda, atualizar `jogador.avancarWave()`.
   - Sinalizar à interface (Carol/Marina) para atualizar o HUD e, se vencer a 10ª onda, disparar `TelaJogo.mostrarVitoria()`.

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre a Composição das Ondas:
1. **Quantidade e Tipos por Onda:**  
   - Fórmula básica atual: `quantidade = waveAtual * 3` distribuída ciclicamente entre `LixoPlastico`, `LixoGarrafa` e `LixoRede`.
   - Você pretende deixar essa fórmula ou criar listas fixas por wave?
2. **Buff por Wave:**  
   - A fórmula atual aplica `bonusVida = (waveAtual - 1) * 10` e `bonusVelocidade = (waveAtual - 1) / 3`. Como você prefere ajustar esse escalonamento?
