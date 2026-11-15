# 🎨 Planejamento Individual: Marina Larissa Xavier Soares
## Módulo: Renderização Gráfica, Sprites & Efeitos Visuais de Animação

---

### 1. Suas Responsabilidades Principais
1. **Renderização do Cenário e Entidades:**
   - Desenhar a malha do mapa (células de terra, praia e o fluxo do rio/mar).
   - Renderizar os sprites visuais de cada tartaruga posicionada e de cada lixo em trânsito.
2. **Efeitos Visuais e Animações Específicas Solicitadas:**
   - **Animação de Ataque da Tartaruga:** Quando uma tartaruga desferir um ataque, ela deve sofrer um breve pulso de escala (aumentar ligeiramente de tamanho por alguns milissegundos e retornar ao tamanho original), dando a impressão visual do disparo/mordida.
   - **Feedback de Dano no Lixo:** Quando um lixo for atingido, ele deve momentaneamente encolher e piscar em tonalidade avermelhada (flash de dano), retornando à sua cor e escala padrão em seguida.
   - **Efeito de Flutuação do Lixo:** Enquanto navega pelo rio, o lixo deve parecer que está boiando (uma leve oscilação suave nas posições $X$ e $Y$ combinada com uma sutil variação senoidal de escala).

---

### 2. Perguntas Técnicas e Decisões de Projeto (Responda antes de codar)

#### 🔹 Sobre a Tecnologia e Resolução Gráfica:
1. **Qual tecnologia de renderização gráfica será utilizada?**  
   - Java Swing (`JPanel` com sobrescrita de `paintComponent(Graphics2D g)`) ou JavaFX (`Canvas` com `GraphicsContext`)?  
   - *(Dica: Swing com `Graphics2D` costuma ser o mais direto e compatível com as máquinas dos laboratórios da UFV).*
2. **Qual será o tamanho em pixels de cada célula da matriz?**  
   - Exemplo: Cada célula terá $64 \times 64$ pixels? $48 \times 48$? $80 \times 80$?
   - Qual será a resolução total da janela do jogo (ex: $1024 \times 768$ ou $1280 \times 720$)?

#### 🔹 Sobre a Implementação das Animações:
3. **Como implementar a animação de ataque da tartaruga sem travar o jogo?**  
   - Usar uma variável `escalaAtaque` que varia de $1.0 \rightarrow 1.25 \rightarrow 1.0$ controlada por um temporizador ou contagem de frames dentro do GameLoop?
4. **Como fazer o lixo piscar em vermelho e encolher?**  
   - Quando `lixo.receberDano()` for chamado, ativa-se uma flag `frameHit = 5`. Enquanto `frameHit > 0`, o desenho do sprite aplica um filtro de cor vermelha (`AlphaComposite` ou tintura) e multiplica o tamanho por $0.85$?
5. **Efeito do Lixo Boiando:**  
   - Como implementar a oscilação de flutuação? Sugestão matemática:
     $$\text{offsetY} = \sin(\text{tempo} \times 4) \times 3 \text{ pixels}$$
     Isso gera um efeito ondulatório suave e natural sem custo de processamento. Você aprova essa fórmula?

#### 🔹 Sobre Sprites e Recursos Visuais:
6. **De onde virão as imagens dos sprites?**  
   - Serão arquivos PNG com fundo transparente colocados na pasta `assets/images/`?
   - Caso uma imagem não carregue, haverá um desenho geométrico reserva (fallback de cor sólida) para o jogo não quebrar?
7. **Barra de Vida dos Lixos:**  
   - Cada lixo terá uma pequena barra de vida verde/vermelha desenhada sobre a cabeça dele para o jogador saber quanto HP ainda resta?

