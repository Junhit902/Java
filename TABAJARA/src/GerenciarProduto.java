import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GerenciarProduto {
    private List<Produto> listaProdutos = new ArrayList<>();

    public void cadastrarProduto() {
        Produto produto = new Produto();

        produto.setNomeProduto(JOptionPane.showInputDialog("Digite o nome do produto:"));

        if (produtoJaCadastrado(produto.getNomeProduto())) {
            JOptionPane.showMessageDialog(null,
                    "Um produto com o mesmo nome já está cadastrado. Por favor, escolha um nome diferente.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        produto.setValorUnitario(Double
                .parseDouble(JOptionPane.showInputDialog("Digite o preço do produto (obs: usar . ao invés de ,):")));
        produto.setDescricao(JOptionPane.showInputDialog("Digite a descrição do produto:"));
        produto.setCodigo(
                Integer.parseInt(JOptionPane.showInputDialog("Digite o código de identificação do produto: ")));

        Object[] tiposProduto = { "Perecível", "Não Perecível" };
        Object tipoProdutoSelecionado = JOptionPane.showInputDialog(null, "Escolha o tipo do produto",
                "Tipo do Produto", JOptionPane.INFORMATION_MESSAGE, null, tiposProduto, tiposProduto[0]);

        if (tipoProdutoSelecionado != null) {
            if (tipoProdutoSelecionado.equals("Perecível")) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataValidade = dateFormat
                            .parse(JOptionPane.showInputDialog("Digite a data de validade (Dia/Mês/Ano):"));
                    produto.setDataValidade(dataValidade);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Formato de data inválido. Use o formato (Dia/Mês/Ano).",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }

            listaProdutos.add(produto);

            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso.", "Cadastro Concluído",
                    JOptionPane.INFORMATION_MESSAGE);

            salvarProdutosEmArquivo();
        }
    }

    private boolean produtoJaCadastrado(String nomeProduto) {
        for (Produto produto : listaProdutos) {
            if (produto.getNomeProduto().equalsIgnoreCase(nomeProduto)) {
                return true;
            }
        }
        return false;
    }

    public void salvarProdutosEmArquivo() {
        String pastaBaseDados = "baseDados";
        File arquivo = new File(pastaBaseDados, "produtos.txt");

        try {
            if (!arquivo.getParentFile().exists()) {
                arquivo.getParentFile().mkdirs();
            }

            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, false))) {
                for (Produto produto : listaProdutos) {

                    writer.write("\n\nCódigo do produto: " + produto.getCodigo());
                    writer.write("\nNOME DO PRODUTO: " + produto.getNomeProduto());
                    writer.write("\nDescrição: " + produto.getDescricao());
                    writer.write("\nValor unitário: R$" + produto.getValorUnitario());

                    Date dataValidade = produto.getDataValidade();
                    if (dataValidade != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        writer.write("\nData de validade: " + dateFormat.format(dataValidade));
                    } else {
                        writer.write("\nData de validade: N/A");
                    }

                    writer.write("\n");
                }

                JOptionPane.showMessageDialog(null, "Produtos salvos no arquivo 'produtos.txt' com sucesso.", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar os produtos no arquivo: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public GerenciarProduto() {

    }

    public void carregarProdutosDoArquivo() {
        String pastaBaseDados = "baseDados";
        File arquivo = new File(pastaBaseDados, "produtos.txt");

        if (arquivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = reader.readLine()) != null) {

                    if (linha.startsWith("Código do produto: ")) {
                        int codigo = Integer.parseInt(linha.replace("Código do produto: ", ""));
                        String nomeProduto = reader.readLine().replace("NOME DO PRODUTO: ", "");
                        String descricao = reader.readLine().replace("Descrição: ", "");
                        double valorUnitario = Double.parseDouble(reader.readLine().replace("Valor unitário: R$", ""));
                        String dataValidadeStr = reader.readLine().replace("Data de validade: ", "");
                        Date dataValidade = null;
                        if (!dataValidadeStr.equals("N/A")) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            dataValidade = dateFormat.parse(dataValidadeStr);
                        }

                        Produto produto = new Produto(codigo, nomeProduto, descricao, valorUnitario, dataValidade);
                        listaProdutos.add(produto);
                    }
                }
            } catch (IOException | ParseException e) {
                JOptionPane.showMessageDialog(null, "Erro ao carregar produtos do arquivo: " + e.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void atualizarArquivoProdutos() {
        String pastaBaseDados = "baseDados";
        File arquivo = new File(pastaBaseDados, "produtos.txt");

        try {
            if (!arquivo.getParentFile().exists()) {
                arquivo.getParentFile().mkdirs();
            }

            if (!arquivo.exists()) {
                arquivo.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, false))) {
                for (Produto produto : listaProdutos) {

                    writer.write("\n\nCódigo do produto: " + produto.getCodigo());
                    writer.write("\nNOME DO PRODUTO: " + produto.getNomeProduto());
                    writer.write("\nDescrição: " + produto.getDescricao());
                    writer.write("\nValor unitário: R$" + produto.getValorUnitario());

                    Date dataValidade = produto.getDataValidade();
                    if (dataValidade != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        writer.write("\nData de validade: " + dateFormat.format(dataValidade));
                    } else {
                        writer.write("\nData de validade: N/A");
                    }

                    writer.write("\n");
                }

                JOptionPane.showMessageDialog(null, "Arquivo 'produtos.txt' atualizado com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o arquivo 'produtos.txt': " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public Produto encontrarProdutoPorNome(String nomeProduto) {
        for (Produto produto : listaProdutos) {
            if (produto.getNomeProduto().equalsIgnoreCase(nomeProduto)) {
                return produto;
            }
        }
        return null;
    }

    public List<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public String obterListaProdutosString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de Produtos:\n");

        for (Produto produto : listaProdutos) {
            sb.append(produto.toString()).append("\n\n");
        }

        return sb.toString();
    }

    public void exibirListaProdutos() {
        String listaProdutosString = obterListaProdutosString();
        JOptionPane.showMessageDialog(null, listaProdutosString, "Lista de Produtos", JOptionPane.INFORMATION_MESSAGE);
    }

    public void exibirListaProdutosSimplificada(List<Produto> produtos) {
        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há produtos cadastrados.", "Lista de Produtos",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder mensagem = new StringBuilder("Lista de Produtos:\n");

            for (Produto produto : produtos) {
                mensagem.append(produto.getNomeProduto()).append(" - R$").append(produto.getValorUnitario())
                        .append("\n");
            }

            JOptionPane.showMessageDialog(null, mensagem.toString(), "Lista de Produtos",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public String buscarProdutoPorNome(String nome) {
        for (Produto produto : getListaProdutos()) {
            if (produto.getNomeProduto().equalsIgnoreCase(nome)) {

                return produto.toString();
            }
        }

        return "Produto não encontrado com o nome: " + nome;
    }

    public List<Produto> verificarProdutosVencidosParaData(String dataStr) {
        List<Produto> produtosVencidos = new ArrayList<>();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dataVerificacao = dateFormat.parse(dataStr);

            for (Produto produto : listaProdutos) {
                if (produto.getDataValidade() != null && produto.getDataValidade().before(dataVerificacao)) {
                    produtosVencidos.add(produto);
                }
            }

        } catch (ParseException e) {
            System.out.println("Erro ao converter a data: " + e.getMessage());
        }

        return produtosVencidos;
    }

    public void exibirProdutosVencidos(List<Produto> produtosVencidos) {
        String mensagem = "Produtos Vencidos:\n";

        for (Produto produto : produtosVencidos) {
            mensagem += produto.toString() + "\n";
        }

        if (produtosVencidos.isEmpty()) {
            mensagem = "Não há produtos vencidos.\n";
        }

        JOptionPane.showMessageDialog(null, mensagem, "Produtos Vencidos", JOptionPane.INFORMATION_MESSAGE);
    }
}
