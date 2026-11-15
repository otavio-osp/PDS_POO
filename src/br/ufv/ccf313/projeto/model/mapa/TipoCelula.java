package br.ufv.ccf313.projeto.model.mapa;

// Define o tipo de cada celula da grade do mapa
public enum TipoCelula {
    LIVRE,    // celula onde o jogador pode posicionar uma tartaruga
    CAMINHO,  // celula por onde os inimigos se deslocam (rio/mar)
    OCUPADA   // celula que ja possui uma tartaruga posicionada
}
