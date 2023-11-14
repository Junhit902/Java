import java.util.*;

public class Compra {
    private int identificador;
    private Date dataCompra;
    private float valorTotal;
    private List<ItemCompra> itensCompra;
    private PessoaFisica cpf;
    private PessoaJuridica cnpj;
    private float totalPago;
    private float faltaPagar;

    public Compra(int identificador, Date dataCompra, float valorTotal, List<ItemCompra> itensCompra, PessoaFisica clienteFisico, PessoaJuridica clienteJuridico, float totalPago, float faltaPagar) {
        this.identificador = identificador;
        this.dataCompra = dataCompra;
        this.valorTotal = valorTotal;
        this.itensCompra = (itensCompra != null) ? itensCompra : new ArrayList<>();
        this.cpf = clienteFisico;
        this.cnpj = clienteJuridico;
        this.totalPago = totalPago;
        this.faltaPagar = faltaPagar;
    }

    public Compra() {
        this.itensCompra = new ArrayList<>();
    }

    public Compra(int identificadorCompra, Date dataAtual, int i, ArrayList<ItemCompra> arrayList,
            String cpfOuCnpjCliente, int j, int k) {
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemCompra> getItensCompra() {
        return itensCompra;
    }

    public void setItensCompra(List<ItemCompra> itensCompra) {
        this.itensCompra = itensCompra;
    }

    public PessoaFisica getCpf() {
        return cpf;
    }

    public void setCpf(PessoaFisica clientePF) {
        this.cpf = clientePF;
    }

    public PessoaJuridica getCnpj() {
        return cnpj;
    }

    public void setCnpj(PessoaJuridica cnpj) {
        this.cnpj = cnpj;
    }

    public float getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(float totalPago) {
        this.totalPago = totalPago;
    }

    public float getFaltaPagar() {
        return faltaPagar;
    }

    public void setFaltaPagar(float faltaPagar) {
        this.faltaPagar = faltaPagar;
    }

}
