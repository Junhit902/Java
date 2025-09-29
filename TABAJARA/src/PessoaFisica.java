import java.sql.Date;
import java.text.SimpleDateFormat;

public class PessoaFisica extends Cliente {
    private String cpf;
    private int maxParcelas;

    public PessoaFisica() {
    }

    public PessoaFisica(String nome, Endereco endereco, Date dataDeCadastro, String cpf, int maxParcelas) {
        super(nome, endereco, dataDeCadastro);
        this.cpf = cpf;
        this.maxParcelas = maxParcelas;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getMaxParcelas() {
        return maxParcelas;
    }

    public void setMaxParcelas(int maxParcelas) {
        this.maxParcelas = maxParcelas;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataCadastroStr = (super.getDataDeCadastro() != null) ? dateFormat.format(super.getDataDeCadastro())
                : "N/A";

        return "\n==== PESSOA FÍSICA ====" + "\nNome completo: " + super.getNome() + "\nCPF: " + cpf
                + "\nMáx. de parcelas: " + maxParcelas + "\nData de cadastro: " + dataCadastroStr +
                "\n==== ENDEREÇO ====\n" + super.getEndereco().toString();
    }
}
