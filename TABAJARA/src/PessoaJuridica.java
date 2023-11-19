import java.text.SimpleDateFormat;
import java.util.Date;

//Pessoa Jurídica herda 'extends' os atributos e metodos da classe Cliente
public class PessoaJuridica extends Cliente {
    private String cnpj;
    private String razaoSocial;
    private int prazoMax;

    public PessoaJuridica() {
    }

    public PessoaJuridica(String nome, Endereco endereco, Date dataDeCadastro, String cnpj, String razaoSocial,
            int prazoMax) {
        super(nome, endereco, dataDeCadastro);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.prazoMax = prazoMax;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public int getPrazoMax() {
        return prazoMax;
    }

    public void setPrazoMax(int prazoMax) {
        this.prazoMax = prazoMax;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataCadastroStr = (super.getDataDeCadastro() != null) ? dateFormat.format(super.getDataDeCadastro())
                : "N/A";

        return "==== PESSOA JURÍDICA ====" + "\nNome fantasia: " + super.getNome() + "\nCNPJ: " + cnpj
                + "\nRazão Social: " + razaoSocial + "\nPrazo Máx. (em dias) para pagamento: " + prazoMax
                + "\nData de cadastro: " + dataCadastroStr + "\n==== ENDEREÇO ====\n" + super.getEndereco().toString();
    }

}
