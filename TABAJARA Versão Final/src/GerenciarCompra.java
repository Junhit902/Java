import javax.swing.JOptionPane;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GerenciarCompra {
    private GerenciarCliente gerenciarCliente;
    private GerenciarProduto gerenciarProduto;
    private List<Compra> listaCompras;

    public GerenciarCompra(GerenciarCliente gerenciarCliente, GerenciarProduto gerenciarProduto) {
        this.gerenciarCliente = gerenciarCliente;
        this.gerenciarProduto = gerenciarProduto;
        this.listaCompras = new ArrayList<>();
    }

    public void exibirListas() {
        gerenciarCliente.exibirListaClientes();
        gerenciarProduto.exibirListaProdutos();
    }

    public void realizarCompra() {
        List<Produto> listaProdutos = gerenciarProduto.getListaProdutos();
        List<Cliente> listaClientes = gerenciarCliente.getListaClientes();

        if (listaProdutos.isEmpty() || listaClientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há produtos ou clientes cadastrados para realizar a compra.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Cliente cliente = obterClienteParaCompra(listaClientes);
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<ItemCompra> itensCompra = obterItensParaCompra(listaProdutos);

        if (itensCompra.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operação cancelada. Nenhum item selecionado.", "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Date dataCompra = obterDataCompra();

        float valorTotal = calcularValorTotalCompra(itensCompra);

        Compra compra = new Compra();
        compra.setIdentificador(gerarIdentificadorCompra());
        compra.setDataCompra(dataCompra);
        compra.setItensCompra(itensCompra);
        compra.setCpf(cliente instanceof PessoaFisica ? (PessoaFisica) cliente : null);
        compra.setCnpj(cliente instanceof PessoaJuridica ? (PessoaJuridica) cliente : null);
        compra.setValorTotal(valorTotal);

        float valorPago = exibirDetalhesCompraEObterValorPago(compra);

        compra.setTotalPago(valorPago);
        float faltaPagar = compra.getValorTotal() - valorPago;
        compra.setFaltaPagar(faltaPagar);

        listaCompras.add(compra);

        salvarCompraEmArquivo(compra);

        exibirRecibo(compra, valorPago, faltaPagar);

        JOptionPane.showMessageDialog(null, "Compra realizada com sucesso!", "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private float exibirDetalhesCompraEObterValorPago(Compra compra) {
        float valorTotal = compra.getValorTotal();

        StringBuilder detalhesCompra = new StringBuilder("Detalhes da Compra:\n");
        detalhesCompra.append("Cliente: ")
                .append(compra.getCpf() != null ? compra.getCpf().getNome() : compra.getCnpj().getNome()).append("\n");
        detalhesCompra.append("Valor Total da Compra: R$ ").append(String.format("%.2f", valorTotal)).append("\n");

        detalhesCompra.append("Itens Comprados:\n");
        for (ItemCompra item : compra.getItensCompra()) {
            detalhesCompra.append("  - ").append(item.getQuantidade()).append("x ")
                    .append(item.getProduto().getNomeProduto())
                    .append(" - R$ ").append(String.format("%.2f", item.getProduto().getValorUnitario())).append("\n");
        }

        try {
            String valorPagoStr = JOptionPane.showInputDialog(
                    detalhesCompra + "\nDigite o valor que deseja pagar (obs: insira com ponto ao invés da vírgula):");

            if (valorPagoStr == null) {
                return 0;
            }

            float valorPago = Float.parseFloat(valorPagoStr);

            while (valorPago < 0 || valorPago > valorTotal) {
                valorPagoStr = JOptionPane.showInputDialog(
                        "Valor excedeu o valor total da compra. Digite novamente o valor que deseja pagar:");

                if (valorPagoStr == null) {
                    return 0;
                }

                valorPago = Float.parseFloat(valorPagoStr);
            }

            return valorPago;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Operação cancelada.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    private void exibirRecibo(Compra compra, float valorPago, float faltaPagar) {
        StringBuilder recibo = new StringBuilder("==== RECIBO ====\n");
        recibo.append("Identificador: ").append(compra.getIdentificador()).append("\n");
        recibo.append("Data da Compra: ").append(new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataCompra()))
                .append("\n");

        if (compra.getCpf() != null) {
            recibo.append("Cliente (CPF): ").append(compra.getCpf().getNome()).append(" - ")
                    .append(((PessoaFisica) compra.getCpf()).getCpf()).append("\n");
        } else if (compra.getCnpj() != null) {
            recibo.append("Cliente (CNPJ): ").append(compra.getCnpj().getNome()).append(" - ")
                    .append(((PessoaJuridica) compra.getCnpj()).getCnpj()).append("\n");
        }

        recibo.append("Valor Total: R$ ").append(String.format("%.2f", compra.getValorTotal())).append("\n");
        recibo.append("Total Pago: R$ ").append(String.format("%.2f", valorPago)).append("\n");
        recibo.append("Falta Pagar: R$ ").append(String.format("%.2f", faltaPagar)).append("\n");

        JOptionPane.showMessageDialog(null, recibo.toString(), "Recibo", JOptionPane.INFORMATION_MESSAGE);
    }

    private Cliente obterClienteParaCompra(List<Cliente> listaClientes) {

        StringBuilder clientesString = new StringBuilder(
                "Escolha um cliente, se identifique inserindo um número inteiro que se encontra ao lado do nome\n");
        for (int i = 0; i < listaClientes.size(); i++) {
            Cliente cliente = listaClientes.get(i);
            clientesString.append("[").append(i).append("] ").append(cliente.getNome());
            if (cliente instanceof PessoaFisica) {
                clientesString.append(" (CPF: ").append(((PessoaFisica) cliente).getCpf()).append(")");
            } else if (cliente instanceof PessoaJuridica) {
                clientesString.append(" (CNPJ: ").append(((PessoaJuridica) cliente).getCnpj()).append(")");
            }
            clientesString.append("\n");
        }

        try {
            int escolhaCliente = Integer.parseInt(JOptionPane.showInputDialog(clientesString.toString()));
            if (escolhaCliente >= 0 && escolhaCliente < listaClientes.size()) {
                return listaClientes.get(escolhaCliente);
            } else {
                JOptionPane.showMessageDialog(null, "Índice inválido. Operação cancelada.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Operação cancelada.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private List<ItemCompra> obterItensParaCompra(List<Produto> listaProdutos) {
        List<ItemCompra> itensCompra = new ArrayList<>();

        StringBuilder produtosString = new StringBuilder(
                "Escolha os produtos (insira o número inteiro do produto que queira comprar):\n");
        for (int i = 0; i < listaProdutos.size(); i++) {
            Produto produto = listaProdutos.get(i);
            produtosString.append("[").append(i).append("] ").append(produto.getNomeProduto()).append(" - R$ ")
                    .append(produto.getValorUnitario()).append("\n");
        }

        try {
            String escolhaProdutos = JOptionPane.showInputDialog(produtosString.toString());

            if (escolhaProdutos == null) {
                return Collections.emptyList();
            }

            String[] indicesEscolhidos = escolhaProdutos.split(",");

            for (String indice : indicesEscolhidos) {
                int indiceProduto = Integer.parseInt(indice.trim());
                if (indiceProduto >= 0 && indiceProduto < listaProdutos.size()) {
                    Produto produtoEscolhido = listaProdutos.get(indiceProduto);
                    int quantidade = Integer.parseInt(JOptionPane
                            .showInputDialog("Digite a quantidade para " + produtoEscolhido.getNomeProduto()));
                    itensCompra.add(new ItemCompra(quantidade, produtoEscolhido));
                } else {
                    JOptionPane.showMessageDialog(null, "Índice inválido. Operação cancelada.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return Collections.emptyList();
                }
            }

            return itensCompra;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Operação cancelada.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return Collections.emptyList();
        }
    }

    private Date obterDataCompra() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String dataCompraStr = JOptionPane.showInputDialog("Digite a data da compra (Dia/Mês/Ano):");

            if (dataCompraStr == null) {
                return null;
            }

            return dateFormat.parse(dataCompraStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de data inválido. Operação cancelada.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private float calcularValorTotalCompra(List<ItemCompra> itensCompra) {
        float valorTotal = 0;
        for (ItemCompra item : itensCompra) {

            if (item.getProduto() != null) {
                valorTotal += item.getProduto().getValorUnitario() * item.getQuantidade();
            }
        }
        return valorTotal;
    }

    private int gerarIdentificadorCompra() {

        return listaCompras.size() + 1;
    }

    private void salvarCompraEmArquivo(Compra compra) {
        String pastaBaseDados = "baseDados";
        File arquivo = new File(pastaBaseDados, "compras.txt");

        try {
            if (!arquivo.getParentFile().exists()) {
                arquivo.getParentFile().mkdirs();
            }

            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, false))) {

                writer.write("\n\n==== COMPRA ====\n");
                writer.write("Identificador: " + compra.getIdentificador() + "\n");
                writer.write(
                        "Data da Compra: " + new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataCompra()) + "\n");

                if (compra.getCpf() != null) {
                    writer.write("Cliente (CPF): " + ((PessoaFisica) compra.getCpf()).getCpf() + "\n");
                } else if (compra.getCnpj() != null) {
                    writer.write("Cliente (CNPJ): " + ((PessoaJuridica) compra.getCnpj()).getCnpj() + "\n");
                }

                writer.write("Valor Total: R$" + compra.getValorTotal() + "\n");
                writer.write("---- Itens Comprados ----\n");

                for (ItemCompra item : compra.getItensCompra()) {
                    writer.write("Nome do produto: " + item.getProduto().getNomeProduto() + "\n");
                    writer.write("Preço unitário: R$" + item.getProduto().getValorUnitario() + "\n");
                    writer.write("Quantidade: " + item.getQuantidade() + "\n");
                }

                writer.write("Total Pago: R$" + compra.getTotalPago() + "\n");
                writer.write("Falta Pagar: R$" + compra.getFaltaPagar() + "\n\n");

                JOptionPane.showMessageDialog(null, "Compra salva no arquivo 'compras.txt' com sucesso.", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar a compra no arquivo: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarArquivoCompras() {
        String pastaBaseDados = "baseDados";
        File arquivoOriginal = new File(pastaBaseDados, "compras.txt");

        try {
            if (!arquivoOriginal.getParentFile().exists()) {
                arquivoOriginal.getParentFile().mkdirs();
            }

            if (!arquivoOriginal.exists()) {
                arquivoOriginal.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoOriginal, false))) {
                for (Compra compra : listaCompras) {

                    writer.write("\n\n==== COMPRA ====\n");
                    writer.write("Identificador: " + compra.getIdentificador() + "\n");
                    writer.write("Data da Compra: " + new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataCompra())
                            + "\n");

                    if (compra.getCpf() != null) {
                        writer.write("Cliente (CPF): " + ((PessoaFisica) compra.getCpf()).getCpf() + "\n");
                    } else if (compra.getCnpj() != null) {
                        writer.write("Cliente (CNPJ): " + ((PessoaJuridica) compra.getCnpj()).getCnpj() + "\n");
                    }

                    writer.write("Valor Total: R$" + compra.getValorTotal() + "\n");
                    writer.write("---- Itens Comprados ----\n");

                    for (ItemCompra item : compra.getItensCompra()) {
                        writer.write("Nome do produto: " + item.getProduto().getNomeProduto() + "\n");
                        writer.write("Preço unitário: R$" + item.getProduto().getValorUnitario() + "\n");
                        writer.write("Quantidade: " + item.getQuantidade() + "\n");
                    }

                    writer.write("Total Pago: R$" + compra.getTotalPago() + "\n");
                    writer.write("Falta Pagar: R$" + compra.getFaltaPagar() + "\n\n");
                }

                JOptionPane.showMessageDialog(null, "Arquivo 'compras.txt' atualizado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao atualizar o arquivo 'compras.txt': " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar o arquivo 'compras.txt': " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarComprasDoArquivo() {
        String pastaBaseDados = "baseDados";
        File arquivo = new File(pastaBaseDados, "compras.txt");

        if (arquivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String linha;

                while ((linha = reader.readLine()) != null) {
                    if (linha.startsWith("==== COMPRA ====")) {
                        Compra compra = new Compra();
                        List<ItemCompra> itensCompra = new ArrayList<>();
                        while ((linha = reader.readLine()) != null && !linha.isEmpty()) {
                            if (linha.startsWith("Identificador: ")) {
                                compra.setIdentificador(Integer.parseInt(linha.replace("Identificador: ", "")));
                            } else if (linha.startsWith("Data da Compra: ")) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                compra.setDataCompra(dateFormat.parse(linha.replace("Data da Compra: ", "").trim()));
                            } else if (linha.startsWith("Cliente (CPF): ")) {

                                String cpf = linha.replace("Cliente (CPF): ", "").trim();

                                Cliente clientePF = gerenciarCliente.encontrarClientePorCPF(cpf);
                                PessoaFisica pf = (PessoaFisica) clientePF;
                                if (clientePF != null) {
                                    compra.setCpf(pf);
                                } else {

                                    JOptionPane.showMessageDialog(null,
                                            "Cliente com CPF '" + cpf + "' não encontrado. Operação cancelada.", "Erro",
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            } else if (linha.startsWith("Cliente (CNPJ): ")) {

                                String cnpj = linha.replace("Cliente (CNPJ): ", "").trim();

                                Cliente clientePJ = gerenciarCliente.encontrarClientePessoaJuridicaPorCNPJ(cnpj);
                                PessoaJuridica pj = (PessoaJuridica) clientePJ;

                                if (clientePJ != null) {
                                    compra.setCnpj(pj);
                                } else {

                                    JOptionPane.showMessageDialog(null,
                                            "Cliente com CNPJ '" + cnpj + "' não encontrado. Operação cancelada.",
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            } else if (linha.startsWith("Valor Total: ")) {
                                compra.setValorTotal(Float.parseFloat(linha.replace("Valor Total: R$", "").trim()));
                            } else if (linha.startsWith("Nome do produto: ")) {
                                String nomeProduto = linha.replace("Nome do produto: ", "").trim();
                                float valorUnitario = Float
                                        .parseFloat(reader.readLine().replace("Preço unitário: R$", "").trim());

                                linha = reader.readLine();
                                int quantidade = Integer.parseInt(linha.replace("Quantidade: ", "").trim());

                                Produto produto = new Produto(nomeProduto, valorUnitario);
                                itensCompra.add(new ItemCompra(quantidade, produto));
                            } else if (linha.startsWith("Total Pago: ")) {
                                compra.setTotalPago(Float.parseFloat(linha.replace("Total Pago: R$", "").trim()));
                            } else if (linha.startsWith("Falta Pagar: ")) {
                                compra.setFaltaPagar(Float.parseFloat(linha.replace("Falta Pagar: R$", "").trim()));
                            }
                        }

                        compra.setItensCompra(itensCompra);
                        listaCompras.add(compra);
                    }
                }
            } catch (IOException | ParseException e) {
                JOptionPane.showMessageDialog(null, "Erro ao carregar compras do arquivo: " + e.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void atualizarSituacaoPagamento() {

        String identificadorStr = JOptionPane.showInputDialog("Digite o Identificador da compra:");

        if (identificadorStr == null) {
            return;
        }

        try {
            int identificador = Integer.parseInt(identificadorStr);

            Compra compra = encontrarCompraPorIdentificador(identificador);

            if (compra == null) {
                JOptionPane.showMessageDialog(null,
                        "Compra não encontrada. Verifique o Identificador e tente novamente.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            exibirDetalhesCompra(compra);

            float faltaPagar = compra.getFaltaPagar();
            JOptionPane.showMessageDialog(null, "Falta Pagar: R$ " + String.format("%.2f", faltaPagar),
                    "Situação de Pagamento", JOptionPane.INFORMATION_MESSAGE);

            float valorPago = obterValorPago(faltaPagar);
            if (valorPago < 0) {
                return;
            }

            float novoTotalPago = compra.getTotalPago() + valorPago;
            float novoFaltaPagar = faltaPagar - valorPago;

            compra.setTotalPago(novoTotalPago);
            compra.setFaltaPagar(novoFaltaPagar);

            atualizarArquivoCompras();

            JOptionPane.showMessageDialog(null, "Pagamento registrado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Identificador inválido. Operação cancelada.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirDetalhesCompra(Compra compra) {
        StringBuilder detalhesCompra = new StringBuilder("Detalhes da Compra:\n");

        detalhesCompra.append("Identificador: ").append(compra.getIdentificador()).append("\n");
        detalhesCompra.append("Data da Compra: ")
                .append(new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataCompra())).append("\n");

        if (compra.getCpf() != null) {
            detalhesCompra.append("Cliente (CPF): ").append(compra.getCpf().getNome()).append(" - ")
                    .append(((PessoaFisica) compra.getCpf()).getCpf()).append("\n");
        } else if (compra.getCnpj() != null) {
            detalhesCompra.append("Cliente (CNPJ): ").append(compra.getCnpj().getNome()).append(" - ")
                    .append(((PessoaJuridica) compra.getCnpj()).getCnpj()).append("\n");
        }

        detalhesCompra.append("Valor Total da Compra: R$ ").append(String.format("%.2f", compra.getValorTotal()))
                .append("\n");

        detalhesCompra.append("Itens Comprados:\n");
        for (ItemCompra item : compra.getItensCompra()) {
            detalhesCompra.append("  - ").append(item.getQuantidade()).append("x ")
                    .append(item.getProduto().getNomeProduto())
                    .append(" - R$ ").append(String.format("%.2f", item.getProduto().getValorUnitario())).append("\n");
        }

        detalhesCompra.append("Total Pago: R$ ").append(String.format("%.2f", compra.getTotalPago())).append("\n");
        detalhesCompra.append("Falta Pagar: R$ ").append(String.format("%.2f", compra.getFaltaPagar())).append("\n");

        JOptionPane.showMessageDialog(null, detalhesCompra.toString(), "Detalhes da Compra",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private Compra encontrarCompraPorIdentificador(int identificador) {
        for (Compra compra : listaCompras) {
            if (compra.getIdentificador() == identificador) {
                return compra;
            }
        }
        return null;
    }

    private float obterValorPago(float faltaPagar) {
        try {
            String valorPagoStr = JOptionPane
                    .showInputDialog("Digite o valor que deseja pagar (obs: insira com ponto ao invés da vírgula):");

            if (valorPagoStr == null) {
                return -1;
            }

            float valorPago = Float.parseFloat(valorPagoStr);

            while (valorPago < 0 || valorPago > faltaPagar) {
                valorPagoStr = JOptionPane.showInputDialog(
                        "Valor excedeu o valor total a pagar. Digite novamente o valor que deseja pagar:");

                if (valorPagoStr == null) {
                    return -1;
                }

                valorPago = Float.parseFloat(valorPagoStr);
            }

            return valorPago;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Operação cancelada.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    public List<Compra> getListaCompras() {
        return this.listaCompras;
    }

    public void exibirListaComprasSimplificada(List<Compra> compras) {
        if (compras.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há compras registradas.", "Lista de Compras",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder mensagem = new StringBuilder("Lista de Compras:\n");

            for (Compra compra : compras) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dataCompraStr = (compra.getDataCompra() != null) ? dateFormat.format(compra.getDataCompra())
                        : "N/A";

                mensagem.append("Compra ").append(compra.getIdentificador()).append(" - ").append(dataCompraStr)
                        .append(" - R$").append(compra.getValorTotal()).append("\n");
            }

            JOptionPane.showMessageDialog(null, mensagem.toString(), "Lista de Compras",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Compra buscarCompraPeloNumero(int numeroCompra, List<Compra> listaCompras) {
        for (Compra compra : listaCompras) {
            if (compra.getIdentificador() == numeroCompra) {
                return compra;
            }
        }
        return null;
    }

    public List<Compra> listarComprasNaoPagas() {
        List<Compra> comprasNaoPagas = new ArrayList<>();
        for (Compra compra : getListaCompras()) {
            if (compra.getFaltaPagar() > 0) {
                comprasNaoPagas.add(compra);
            }
        }
        return comprasNaoPagas;
    }

    public void exibirComprasNaoPagas() {
        List<Compra> comprasNaoPagas = listarComprasNaoPagas();

        if (comprasNaoPagas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todas as compras estão pagas.", "Compras Pagas",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder mensagem = new StringBuilder("Compras não pagas:\n");

            for (Compra compra : comprasNaoPagas) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dataCompraStr = (compra.getDataCompra() != null) ? dateFormat.format(compra.getDataCompra())
                        : "N/A";

                mensagem.append("Compra ").append(compra.getIdentificador()).append(" - ").append(dataCompraStr)
                        .append(" - R$").append(compra.getValorTotal()).append("\n");
            }

            JOptionPane.showMessageDialog(null, mensagem.toString(), "Compras Não Pagas",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public List<Compra> listarUltimas10ComprasPagas() {
        List<Compra> comprasPagas = new ArrayList<>();
        List<Compra> todasCompras = getListaCompras();

        todasCompras.stream()
                .filter(compra -> compra.getTotalPago() == compra.getValorTotal())
                .sorted(Comparator.comparing(Compra::getDataCompra).reversed())
                .limit(10)
                .forEach(comprasPagas::add);
        return comprasPagas;
    }

    public List<Compra> obterUltimas10ComprasPagas() {
        List<Compra> todasCompras = getListaCompras();
        List<Compra> ultimas10ComprasPagas = new ArrayList<>();

        for (int i = todasCompras.size() - 1; i >= 0; i--) {
            Compra compra = todasCompras.get(i);

            if (compra.getFaltaPagar() == 0) {
                ultimas10ComprasPagas.add(compra);
            }

            if (ultimas10ComprasPagas.size() == 10) {
                break;
            }
        }

        return ultimas10ComprasPagas;
    }

    public Compra encontrarCompraMaisCara() {
        List<Compra> todasCompras = getListaCompras();

        if (todasCompras.isEmpty()) {
            return null;
        }

        Compra compraMaisCara = todasCompras.get(0);

        for (Compra compra : todasCompras) {
            if (compra.getValorTotal() > compraMaisCara.getValorTotal()) {
                compraMaisCara = compra;
            }
        }

        return compraMaisCara;
    }

    public Compra encontrarCompraMaisBarata(List<Compra> compras) {
        if (compras.isEmpty()) {
            return null;
        }

        Compra compraMaisBarata = compras.get(0);

        for (Compra compra : compras) {
            if (compra.getValorTotal() < compraMaisBarata.getValorTotal()) {
                compraMaisBarata = compra;
            }
        }

        return compraMaisBarata;
    }

    public Map<String, Float> obterValorTotalComprasPorMes() {
        Map<String, Float> valorTotalPorMes = new HashMap<>();

        List<Compra> listaCompras = getListaCompras();
        for (Compra compra : listaCompras) {
            Date dataCompra = compra.getDataCompra();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataCompra);

            int mes = calendar.get(Calendar.MONTH) + 1;

            int ano = calendar.get(Calendar.YEAR);

            float valorTotal = compra.getValorTotal();

            String chave = mes + "/" + ano;

            if (valorTotalPorMes.containsKey(chave)) {
                valorTotalPorMes.put(chave, valorTotalPorMes.get(chave) + valorTotal);
            } else {
                valorTotalPorMes.put(chave, valorTotal);
            }
        }
        return valorTotalPorMes;
    }

    public Map<String, Float> obterValorTotalMesRecente() {
        Map<String, Float> valorTotalMesRecente = new LinkedHashMap<>();
        Map<String, Float> valorTotalPorMesExistente = obterValorTotalComprasPorMes();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            int mes = calendar.get(Calendar.MONTH) + 1;
            int ano = calendar.get(Calendar.YEAR);
            if (mes == 0) {
                mes = 12;
                ano -= 1;
            }
            String chave = mes + "/" + ano;
            if (valorTotalPorMesExistente.containsKey(chave)) {
                valorTotalMesRecente.put(chave, valorTotalPorMesExistente.get(chave));
            } else
                valorTotalMesRecente.put(chave, 0f);
        }
        return valorTotalMesRecente;
    }
}
