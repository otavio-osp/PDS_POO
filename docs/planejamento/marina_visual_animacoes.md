# 🎨 Planejamento Individual: Marina Larissa Xavier Soares
## Módulo: Renderização Gráfica, Sprites & Efeitos Visuais de Animação

---

### 1. Suas Responsabilidades Principais
1. **Renderização do Cenário e Entidades:**
   - Desenhar visualmente a grade do mapa (`grade[linhas][colunas]`) em `TelaJogo.java`.
   - Renderizar os sprites das tartarugas nas células ocupadas e dos dejetos em trânsito no caminho do rio.
2. **Efeitos Visuais e Animações Solicitadas:**
   - **Ataque da Tartaruga:** Aumenta brevemente de escala no momento do ataque e retorna ao tamanho original.
   - **Feedback de Dano no Lixo:** O lixo encolhe e fica vermelho temporariamente ao levar dano, retornando ao padrão em seguida.
   - **Lixo Boiando:** Efeito ondulatório suave (oscilação nas posições X e Y com sutil variação senoidal de tamanho) para simular que está flutuando no rio.
3. **HUD (Painel Informativo):**
   - Atualizar dinamicamente os rótulos de Vida, Dinheiro e Onda Atual (`atualizarHUD`).

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre Dimensões e Animações:
1. **Qual o tamanho em pixels de cada célula da grade?**  
   - Exemplo sugerido: $64 \times 64$ pixels.
2. **Implementação da flutuação (lixo boiando):**  
   - Variação suave de coordenadas $Y$ via função matemática senoidal $\sin(\text{tempo})$ ao desenhar o sprite do inimigo.
