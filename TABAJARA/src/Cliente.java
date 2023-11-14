import java.util.*;

public class Cliente {
    private String nome;
    private Endereco endereco;
    private Date dataDeCadastro;

    //Construtor Vazio
    public Cliente(){
        this.endereco = new Endereco();
    }

    public Cliente(String nome, Endereco endereco, Date dataDeCadastro){
        this.nome = nome;
        this.endereco = endereco;
        this.dataDeCadastro = dataDeCadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Date getDataDeCadastro() {
        return dataDeCadastro;
    }

    public void setDataDeCadastro(Date dataDeCadastro) {
        this.dataDeCadastro = dataDeCadastro;
    }

    public String getCpf() {
        return null; // Implemente a lógica para retornar o CPF ou null
    }

    public String getCnpj() {
        return null; // Implemente a lógica para retornar o CNPJ ou null
    }

    @Override
    public String toString() {
        return "==== CLIENTE ===="+ "\nNome: " + nome + "\n==== ENDEREÇO ====" + endereco.toString() + "\nData De Cadastro: " + dataDeCadastro;
    }
}
