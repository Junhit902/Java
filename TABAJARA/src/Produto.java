import java.text.SimpleDateFormat;
import java.util.Date;

public class Produto{
    private int codigo;
    private String nomeProduto;
    private String descricao;
    private double valorUnitario;
    private Date dataValidade;

    public Produto(int codigoProduto, String nomeProduto, String descricao, double valorUnitario, Date dataValidade2){
        this.codigo = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.dataValidade = dataValidade2;
    }

    public Produto(String nomeProduto, double valorUnitario) {
        this.nomeProduto = nomeProduto;
        this.valorUnitario = valorUnitario;
    }
    
    //Construtor vazio
    public Produto() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public boolean estaVencido() {
        if (dataValidade == null) {
            return false; // Se a data de validade não estiver definida, o produto não está vencido.
        }
        Date dataAtual = new Date(); // Obtém a data atual
        return dataAtual.after(dataValidade); // Verifica se a data atual é posterior à data de validade.
    }

    public String getPreco() {
        return null;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataValidadeStr = (getDataValidade() != null) ? dateFormat.format(getDataValidade()) : "N/A";

        return "\n==== Produto ====" + "\nCódigo: " + codigo + "\nNome do produto: " + nomeProduto + "\nDescrição: " + descricao
                + "\nValor unitário: R$" + valorUnitario + "\nData de validade: " + dataValidadeStr;
    }
}
