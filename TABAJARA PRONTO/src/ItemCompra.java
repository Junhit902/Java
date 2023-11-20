public class ItemCompra {
    private int quantidade;
    private Produto produto;

    public ItemCompra(int quantidade, Produto produto) {
        this.quantidade = quantidade;
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade > 0) {
            this.quantidade = quantidade;
        }
    }

    public String paraString() {
        return "==== ItemCompra ====" + "\nQuantidade: " + quantidade + "\nNome do produto: " + produto.getNomeProduto()
                +
                "Valor unitário: " + produto.getValorUnitario();
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto2) {
    }

}
