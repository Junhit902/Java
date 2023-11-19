import java.util.*;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;

public class GerenciarCliente {
    boolean Datavalida;
    ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();

    public void cadastrarCliente() {
        Object[] tiposClientes = { "Pessoa Física", "Pessoa Jurídica" };
        Object clienteSelecionado = JOptionPane.showInputDialog(null, "Escolha um tipo de cliente", "Cliente",
                JOptionPane.INFORMATION_MESSAGE, null, tiposClientes, tiposClientes[0]);

        if (clienteSelecionado != null) {
            if (clienteSelecionado.equals("Pessoa Física")) {
                cadastrarPessoaFisica();
            } else if (clienteSelecionado.equals("Pessoa Jurídica")) {
                cadastrarPessoaJuridica();
            } else {
                JOptionPane.showMessageDialog(null, "Opção inválida", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleção cancelada");
        }
    }

    public void cadastrarPessoaFisica() {
        PessoaFisica cf = new PessoaFisica();
        Endereco enderecof = new Endereco();

        String nome = JOptionPane.showInputDialog("Digite o seu nome completo:").toUpperCase();
        String cpf = JOptionPane.showInputDialog("Digite o seu CPF (123.456.789-01):");
        // Verifica se o nome ou CPF já existem na lista de clientes
        if (clienteFisicaJaCadastrado(nome, cpf)) {
            JOptionPane.showMessageDialog(null, "Um cliente com o mesmo nome ou CPF já está cadastrado.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        cf.setNome(nome);
        cf.setCpf(cpf);
        do {
            String dataInput = JOptionPane.showInputDialog("Digite a data de cadastro (Dia/Mês/Ano):");
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dataDeCadastro = null;
                dataDeCadastro = dateFormat.parse(dataInput);
                cf.setDataDeCadastro(dataDeCadastro);
                Datavalida = true;
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de data inválido. Use o formato (Dia/Mês/Ano).", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                Datavalida = false;
            }
        } while (!Datavalida);

        enderecof.setRua(JOptionPane.showInputDialog("Digite a Rua:"));
        enderecof.setNumero(Integer.parseInt(JOptionPane.showInputDialog("Digite o Número:")));
        enderecof.setBairro(JOptionPane.showInputDialog("Digite o Bairro:"));
        enderecof.setCep(JOptionPane.showInputDialog("Digite o CEP:"));
        enderecof.setCidade(JOptionPane.showInputDialog("Digite a Cidade:"));
        enderecof.setEstado(JOptionPane.showInputDialog("Digite o Estado:"));
        cf.setMaxParcelas(Integer.parseInt(JOptionPane.showInputDialog("Digite o máximo de parcelas (qtd. máx):")));

        cf.setEndereco(enderecof);
        listaClientes.add(cf);

        JOptionPane.showMessageDialog(null, cf.toString() + "\n\nCliente Pessoa Física cadastrado com sucesso.",
                "Cadastro Concluído", JOptionPane.INFORMATION_MESSAGE);

        salvarClienteEmArquivo(cf, "Pessoa Física");
    }

    public void cadastrarPessoaJuridica() {
        PessoaJuridica cj = new PessoaJuridica();
        Endereco enderecoj = new Endereco();

        String nomeFantasia = JOptionPane.showInputDialog("Digite o seu nome fantasia:").toUpperCase();
        String cnpj = JOptionPane.showInputDialog("Digite o seu CNPJ:").replaceAll("[^0-9]", "");
        // Verifique se o nome fantasia ou CNPJ já existem na lista de clientes
        if (clienteJuridicoJaCadastrado(nomeFantasia, cnpj)) {
            JOptionPane.showMessageDialog(null, "Uma empresa com o mesmo nome fantasia ou CNPJ já está cadastrada.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cj.setNome(nomeFantasia);
        cj.setCnpj(cnpj);
        cj.setRazaoSocial(JOptionPane.showInputDialog("Digite a razão social:"));
        cj.setPrazoMax(Integer.parseInt(JOptionPane.showInputDialog("Digite o prazo máximo em dias:")));

        do {
            String dataInput = JOptionPane.showInputDialog("Digite a data de cadastro (Dia/Mês/Ano):");
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date dataDeCadastro = null;
                dataDeCadastro = dateFormat.parse(dataInput);
                cj.setDataDeCadastro(dataDeCadastro);
                Datavalida = true;
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de data inválido. Use o formato (Dia/Mês/Ano).", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                Datavalida = false;
            }
        } while (!Datavalida);

        enderecoj.setRua(JOptionPane.showInputDialog("Digite a Rua:"));
        enderecoj.setNumero(Integer.parseInt(JOptionPane.showInputDialog("Digite o Número:")));
        enderecoj.setBairro(JOptionPane.showInputDialog("Digite o Bairro:"));
        enderecoj.setCep(JOptionPane.showInputDialog("Digite o CEP:"));
        enderecoj.setCidade(JOptionPane.showInputDialog("Digite a Cidade:"));
        enderecoj.setEstado(JOptionPane.showInputDialog("Digite o Estado:"));

        cj.setEndereco(enderecoj);
        listaClientes.add(cj);

        JOptionPane.showMessageDialog(null, cj.toString() + "\n\nCliente Pessoa Jurídica cadastrado com sucesso.",
                "Cadastro Concluído", JOptionPane.INFORMATION_MESSAGE);

        salvarClienteEmArquivo(cj, "Pessoa Jurídica");
    }

    // Método para verificar se um cliente já está cadastrado por nome ou CPF
    private boolean clienteFisicaJaCadastrado(String nome, String cpf) {
        for (Cliente cliente : listaClientes) {
            if (cliente instanceof PessoaFisica) {
                PessoaFisica pessoaFisica = (PessoaFisica) cliente;
                if (cliente.getNome().toUpperCase().equals(nome) || pessoaFisica.getCpf().equals(cpf)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Método para verificar se uma empresa já está cadastrada por nome fantasia ou
    // CNPJ
    private boolean clienteJuridicoJaCadastrado(String nomeFantasia, String cnpj) {
        for (Cliente cliente : listaClientes) {
            if (cliente instanceof PessoaJuridica) {
                PessoaJuridica pessoaJuridica = (PessoaJuridica) cliente;
                if (pessoaJuridica.getNome().toUpperCase().equals(nomeFantasia)
                        || pessoaJuridica.getCnpj().equals(cnpj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void salvarClienteEmArquivo(Cliente cliente, Object clienteSelecionado) {
        String pastaBaseDados = "baseDados";
        File arquivo = new File(pastaBaseDados, "clientes.txt");

        try {
            if (!arquivo.getParentFile().exists()) {
                arquivo.getParentFile().mkdirs();
            }

            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
                if (clienteSelecionado.equals("Pessoa Física") && cliente instanceof PessoaFisica) {
                    String dadosCliente = ((PessoaFisica) cliente).toString();
                    writer.write(dadosCliente);
                    writer.write("\n");
                } else if (clienteSelecionado.equals("Pessoa Jurídica") && cliente instanceof PessoaJuridica) {
                    String dadosCliente = ((PessoaJuridica) cliente).toString();
                    writer.write(dadosCliente);
                    writer.write("\n");
                }

                // Adiciona uma linha em branco para separar os dados dos clientes
                writer.write("\n");

                JOptionPane.showMessageDialog(null, "Cliente salvo no arquivo 'clientes.txt' com sucesso.", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar o cliente no arquivo: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarArquivoClientes() {
        String pastaBaseDados = "baseDados";
        File arquivoOriginal = new File(pastaBaseDados, "clientes.txt");

        try {
            if (!arquivoOriginal.getParentFile().exists()) {
                arquivoOriginal.getParentFile().mkdirs();
            }

            if (!arquivoOriginal.exists()) {
                arquivoOriginal.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoOriginal, false))) {
                for (Cliente cliente : listaClientes) {
                    // Escreva as informações do cliente no arquivo, da mesma maneira que no método
                    // salvarClienteEmArquivo
                    if (cliente instanceof PessoaFisica) {
                        String dadosCliente = ((PessoaFisica) cliente).toString();
                        writer.write(dadosCliente);
                        writer.write("\n");
                    } else if (cliente instanceof PessoaJuridica) {
                        String dadosCliente = ((PessoaJuridica) cliente).toString();
                        writer.write(dadosCliente);
                        writer.write("\n");
                    }
                    writer.write("\n");
                }
                JOptionPane.showMessageDialog(null, "Arquivo 'clientes.txt' atualizado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar o arquivo 'clientes.txt': " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar o arquivo 'clientes.txt': " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deletarpeloCPFouCnpj() {
        ImageIcon icon = new ImageIcon("icons\\warning.png");
        if (listaClientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum cliente cadastrado para deletar.", "Aviso",
                    JOptionPane.INFORMATION_MESSAGE, icon);
            return;
        }

        Object[] tiposClientes = { "Pessoa Física", "Pessoa Jurídica" };
        Object clienteSelecionado = JOptionPane.showInputDialog(null, "Escolha um tipo de cliente que deseja deletar:",
                "Cliente", JOptionPane.INFORMATION_MESSAGE, null, tiposClientes, tiposClientes[0]);

        if (clienteSelecionado != null) {
            if (clienteSelecionado.equals("Pessoa Física")) {
                deletarClientePeloCPF();
            } else if (clienteSelecionado.equals("Pessoa Jurídica")) {
                deletarClientePeloCNPJ();
            } else {
                JOptionPane.showMessageDialog(null, "Opção inválida", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleção cancelada");
        }
    }

    public void deletarClientePeloCPF() {
        // Carregando os CPFs dos clientes do arquivo
        List<String> cpfsClientes = new ArrayList<>();

        for (Cliente cliente : listaClientes) {
            if (cliente instanceof PessoaFisica) {
                cpfsClientes.add(((PessoaFisica) cliente).getCpf());
            }
        }

        // Exibindo os CPFs dos clientes em um JOptionPane
        Object cpfSelecionado = JOptionPane.showInputDialog(null, "Escolha o CPF para deletar (Pessoa Física)",
                "Deletar Cliente", JOptionPane.QUESTION_MESSAGE, null, cpfsClientes.toArray(), cpfsClientes.get(0));

        if (cpfSelecionado != null) {
            // O usuário selecionou um CPF, agora podemos prosseguir com a exclusão
            String cpfClienteSelecionado = cpfSelecionado.toString();
            List<Cliente> clientesParaRemover = new ArrayList<>();

            for (Cliente cliente : listaClientes) {
                if (cliente instanceof PessoaFisica) {
                    PessoaFisica pessoaFisica = (PessoaFisica) cliente;
                    if (pessoaFisica.getCpf().equals(cpfClienteSelecionado)) {
                        clientesParaRemover.add(cliente);
                    }
                }
            }
            listaClientes.removeAll(clientesParaRemover);
            atualizarArquivoClientes(); // Atualizar o arquivo após a remoção dos clientes
            if (clientesParaRemover.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Nenhum cliente com o CPF '" + cpfClienteSelecionado + "' foi encontrado (Pessoa Física).",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cliente(s) com o CPF '" + cpfClienteSelecionado + "' removido(s) com sucesso (Pessoa Física).",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleção cancelada");
        }
    }

    public void deletarClientePeloCNPJ() {
        // Carregando os CNPJs dos clientes do arquivo
        List<String> cnpjsClientes = new ArrayList<>();

        for (Cliente cliente : listaClientes) {
            if (cliente instanceof PessoaJuridica) {
                cnpjsClientes.add(((PessoaJuridica) cliente).getCnpj());
            }
        }

        // Exibindo os CNPJs dos clientes em um JOptionPane
        Object cnpjSelecionado = JOptionPane.showInputDialog(null, "Escolha o CNPJ para deletar (Pessoa Jurídica)",
                "Deletar Cliente", JOptionPane.QUESTION_MESSAGE, null, cnpjsClientes.toArray(), cnpjsClientes.get(0));

        if (cnpjSelecionado != null) {
            // O usuário selecionou um CNPJ, agora podemos prosseguir com a exclusão
            String cnpjClienteSelecionado = cnpjSelecionado.toString();
            List<Cliente> clientesParaRemover = new ArrayList<>();

            for (Cliente cliente : listaClientes) {
                if (cliente instanceof PessoaJuridica) {
                    PessoaJuridica pessoaJuridica = (PessoaJuridica) cliente;
                    if (pessoaJuridica.getCnpj().equals(cnpjClienteSelecionado)) {
                        clientesParaRemover.add(cliente);
                    }
                }
            }
            listaClientes.removeAll(clientesParaRemover);
            atualizarArquivoClientes(); // Atualiza o arquivo após a remoção dos clientes
            if (clientesParaRemover.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Nenhum cliente com o CNPJ '" + cnpjClienteSelecionado + "' foi encontrado (Pessoa Jurídica).",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cliente(s) com o CNPJ '" + cnpjClienteSelecionado
                                + "' removido(s) com sucesso (Pessoa Jurídica).",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleção cancelada");
        }
    }

    public void deletarClientePeloNome() {
        ImageIcon icon = new ImageIcon("icons\\warning.png");
        if (listaClientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum cliente cadastrado para deletar.", "Aviso",
                    JOptionPane.INFORMATION_MESSAGE, icon);
            return;
        }
        // Carregando os nomes dos clientes do arquivo
        List<String> nomesClientes = new ArrayList<>();

        for (Cliente cliente : listaClientes) {
            nomesClientes.add(cliente.getNome());
        }

        // Exibindo os nomes dos clientes em um JOptionPane
        Object nomeSelecionado = JOptionPane.showInputDialog(null, "Escolha o cliente para deletar", "Deletar Cliente",
                JOptionPane.QUESTION_MESSAGE, null, nomesClientes.toArray(), nomesClientes.get(0));

        if (nomeSelecionado != null) {
            // O usuário selecionou um nome, agora podemos prosseguir com a exclusão
            String nomeClienteSelecionado = nomeSelecionado.toString();
            List<Cliente> clientesParaRemover = new ArrayList<>();

            for (Cliente cliente : listaClientes) {
                if (cliente.getNome().equalsIgnoreCase(nomeClienteSelecionado)) {
                    clientesParaRemover.add(cliente);
                }
            }
            listaClientes.removeAll(clientesParaRemover);
            atualizarArquivoClientes(); // Mova a chamada para atualizar o arquivo após a remoção dos clientes
            if (clientesParaRemover.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Nenhum cliente com o nome '" + nomeClienteSelecionado + "' foi encontrado.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cliente(s) com o nome '" + nomeClienteSelecionado + "' removido(s) com sucesso.", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleção cancelada");
        }
    }

    // Construtor vazio
    public GerenciarCliente() {

    }

    public void carregarClientesDoArquivo() {
        String pastaBaseDados = "baseDados";
        File arquivo = new File(pastaBaseDados, "clientes.txt");
        System.out.println(arquivo.getAbsolutePath());

        if (arquivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String linha;

                while ((linha = reader.readLine()) != null) {
                    if (linha.startsWith("==== PESSOA FÍSICA ====")) {
                        PessoaFisica pessoaFisica = new PessoaFisica();
                        Endereco endereco = new Endereco(); // Criando um objeto Endereco para cada pessoa física
                        while ((linha = reader.readLine()) != null && !linha.isEmpty()) {
                            if (linha.startsWith("Nome completo: ")) {
                                pessoaFisica.setNome(linha.replace("Nome completo: ", ""));
                            } else if (linha.startsWith("CPF: ")) {
                                pessoaFisica.setCpf(linha.replace("CPF: ", ""));
                            } else if (linha.startsWith("Máx. de parcelas: ")) {
                                pessoaFisica.setMaxParcelas(Integer.parseInt(linha.replace("Máx. de parcelas: ", "")));
                            } else if (linha.startsWith("Data de cadastro: ")) {
                                String dataDeCadastroStr = linha.replace("Data de cadastro: ", "");
                                Date dataDeCadastro = null;
                                if (!dataDeCadastroStr.equals("N/A")) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    dataDeCadastro = dateFormat.parse(dataDeCadastroStr);
                                }
                                pessoaFisica.setDataDeCadastro(dataDeCadastro);
                            } else if (linha.startsWith("Rua: ")) {
                                endereco.setRua(linha.replace("Rua: ", ""));
                            } else if (linha.startsWith("Número: ")) {
                                endereco.setNumero(Integer.parseInt(linha.replace("Número: ", "")));
                            } else if (linha.startsWith("Bairro: ")) {
                                endereco.setBairro(linha.replace("Bairro: ", ""));
                            } else if (linha.startsWith("CEP: ")) {
                                endereco.setCep(linha.replace("CEP: ", ""));
                            } else if (linha.startsWith("Cidade: ")) {
                                endereco.setCidade(linha.replace("Cidade: ", ""));
                            } else if (linha.startsWith("Estado: ")) {
                                endereco.setEstado(linha.replace("Estado: ", ""));
                            }
                        }
                        pessoaFisica.setEndereco(endereco); // Associando o objeto de endereço à pessoa física
                        listaClientes.add(pessoaFisica);
                    } else if (linha.startsWith("==== PESSOA JURÍDICA ====")) {
                        PessoaJuridica pessoaJuridica = new PessoaJuridica();
                        Endereco endereco = new Endereco(); // Criando um objeto Endereco para cada pessoa jurídica
                        while ((linha = reader.readLine()) != null && !linha.isEmpty()) {
                            if (linha.startsWith("Nome fantasia: ")) {
                                pessoaJuridica.setNome(linha.replace("Nome fantasia: ", ""));
                            } else if (linha.startsWith("CNPJ: ")) {
                                pessoaJuridica.setCnpj(linha.replace("CNPJ: ", ""));
                            } else if (linha.startsWith("Razão Social: ")) {
                                pessoaJuridica.setRazaoSocial(linha.replace("Razão Social: ", ""));
                            } else if (linha.startsWith("Prazo Máx. (em dias) para pagamento: ")) {
                                pessoaJuridica.setPrazoMax(
                                        Integer.parseInt(linha.replace("Prazo Máx. (em dias) para pagamento: ", "")));
                            } else if (linha.startsWith("Data de cadastro: ")) {
                                String dataDeCadastroStr = linha.replace("Data de cadastro: ", "");
                                Date dataDeCadastro = null;
                                if (!dataDeCadastroStr.equals("N/A")) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    dataDeCadastro = dateFormat.parse(dataDeCadastroStr);
                                }
                                pessoaJuridica.setDataDeCadastro(dataDeCadastro);
                            } else if (linha.startsWith("==== ENDEREÇO ====")) {
                                endereco.setRua(linha.replace("Rua: ", ""));
                            } else if (linha.startsWith("Número: ")) {
                                endereco.setNumero(Integer.parseInt(linha.replace("Número: ", "")));
                            } else if (linha.startsWith("Bairro: ")) {
                                endereco.setBairro(linha.replace("Bairro: ", ""));
                            } else if (linha.startsWith("CEP: ")) {
                                endereco.setCep(linha.replace("CEP: ", ""));
                            } else if (linha.startsWith("Cidade: ")) {
                                endereco.setCidade(linha.replace("Cidade: ", ""));
                            } else if (linha.startsWith("Estado: ")) {
                                endereco.setEstado(linha.replace("Estado: ", ""));
                            }
                        }
                        pessoaJuridica.setEndereco(endereco); // Associando o objeto de endereço à pessoa jurídica
                        listaClientes.add(pessoaJuridica);
                    }
                }
            } catch (IOException | ParseException e) {
                JOptionPane.showMessageDialog(null, "Erro ao carregar clientes do arquivo: " + e.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Cliente encontrarClientePorCPF(String cpf) {
        for (Cliente cliente : listaClientes) {
            if (cliente instanceof PessoaFisica && ((PessoaFisica) cliente).getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null; // Retorna null se não encontrar nenhum cliente com o CPF fornecido
    }

    public Cliente encontrarClientePessoaJuridicaPorCNPJ(String cnpj) {
        for (Cliente cliente : listaClientes) {
            if (cliente instanceof PessoaJuridica && ((PessoaJuridica) cliente).getCnpj().equals(cnpj)) {
                return cliente;
            }
        }
        return null; // Retorna null se não encontrar nenhum cliente com o CNPJ fornecido
    }

    public ArrayList<Cliente> getListaClientes() {
        return listaClientes;
    }

    // Método para obter uma representação em string da lista de clientes
    public String obterListaClientesString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de Clientes:\n");

        for (Cliente cliente : listaClientes) {
            sb.append(cliente.toString()).append("\n\n");
        }

        return sb.toString();
    }

    // Método para exibir a lista de clientes por meio do JOptionPane
    public void exibirListaClientes() {
        String listaClientesString = obterListaClientesString();
        JOptionPane.showMessageDialog(null, listaClientesString, "Lista de Clientes", JOptionPane.INFORMATION_MESSAGE);
    }

    // (a) Relação de todos os Clientes que possuem o nome iniciado por uma
    // determinada sequência de caracteres;
    public List<Cliente> buscarClientesPorNome(String sequencia) {
        List<Cliente> clientesEncontrados = new ArrayList<>();
        // Certifique-se de que a lista de clientes não está vazia
        if (listaClientes != null && !listaClientes.isEmpty()) {
            for (Cliente cliente : listaClientes) {
                // Verifique se o nome do cliente inicia com a sequência fornecida (ignorando
                // maiúsculas/minúsculas)
                if (cliente.getNome().toLowerCase().startsWith(sequencia.toLowerCase())) {
                    clientesEncontrados.add(cliente);
                }
            }
        }
        return clientesEncontrados;
    }
}
