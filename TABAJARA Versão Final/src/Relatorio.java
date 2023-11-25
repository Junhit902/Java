import java.util.List;
import java.util.*;

import javax.swing.JOptionPane;

public class Relatorio {
    private GerenciarCliente gerenciarCliente;
    private GerenciarProduto gerenciarProduto;
    private GerenciarCompra gerenciarCompra;

    public Relatorio(GerenciarCliente gerenciarCliente, GerenciarProduto gerenciarProduto,
            GerenciarCompra gerenciarCompra) {
        this.gerenciarCliente = gerenciarCliente;
        this.gerenciarProduto = gerenciarProduto;
        this.gerenciarCompra = gerenciarCompra;
    }

    GerenciarCliente cliente = new GerenciarCliente();
    GerenciarProduto produto = new GerenciarProduto();
    GerenciarCompra compra = new GerenciarCompra(cliente, produto);

    public void exibirRelatorio() {

        int opcao;

        do {
            String opcaoStr = JOptionPane.showInputDialog("---- Escolha a opção desejada ----\n"
                    + "===== RELATÓRIO =====\n"
                    + "[1] Relação de todos os clientes que possuem o nome iniciado por uma determinada sequencia de caractere\n"
                    + "[2] Relação de todos os Produtos\n"
                    + "[3] Busca de um produto pelo nome\n"
                    + "[4] Relação de produtos que são perecíveis e que estão com a data de validade vencida\n"
                    + "[5] Relação de todas as compras\n"
                    + "[6] Busca de uma compra pelo número\n"
                    + "[7] Relação de todas as compras que não foram pagas ainda\n"
                    + "[8] Relação das 10 últimas compras pagas\n"
                    + "[9] Apresentação das informações da compra mais cara\n"
                    + "[10] Apresentação das informações da compra mais barata\n"
                    + "[11] Relação do valor total de compras feitas em cada mês nos últimos 12 meses\n"
                    + "Clique em cancelar para voltar ao menu principal.");

            if (opcaoStr == null) {
                opcao = 8;
            } else {

                try {
                    opcao = Integer.parseInt(opcaoStr);
                    switch (opcao) {
                        case 1:
                            String sequencia = JOptionPane.showInputDialog("Digite a sequência de caracteres:");
                            if (sequencia != null && !sequencia.isEmpty()) {
                                List<Cliente> clientesEncontrados = gerenciarCliente.buscarClientesPorNome(sequencia);
                                if (!clientesEncontrados.isEmpty()) {
                                    StringBuilder mensagem = new StringBuilder("Clientes encontrados:\n");
                                    for (Cliente cliente : clientesEncontrados) {
                                        mensagem.append(cliente.getNome()).append("\n");
                                    }
                                    JOptionPane.showMessageDialog(null, mensagem.toString(), "Clientes Encontrados",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "Nenhum cliente encontrado com a sequência fornecida.",
                                            "Clientes Não Encontrados", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, "Relação de todos os Produtos foi selecionada");
                            List<Produto> produtos = gerenciarProduto.getListaProdutos();
                            produto.exibirListaProdutosSimplificada(produtos);
                            break;
                        case 3:
                            JOptionPane.showMessageDialog(null, "Busca de um produto pelo nome foi selecionada");
                            String nomeProduto = JOptionPane.showInputDialog("Digite o nome do produto:");
                            if (nomeProduto != null && !nomeProduto.isEmpty()) {
                                String resultadoBusca = gerenciarProduto.buscarProdutoPorNome(nomeProduto);
                                JOptionPane.showMessageDialog(null, resultadoBusca, "Detalhes do Produto",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Nome do produto inválido.", "Erro",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 4:
                            JOptionPane.showMessageDialog(null,
                                    "Relação de produtos que são perecíveis e que estão com a data de validade vencida foi selecionada");
                            String dataatual = JOptionPane.showInputDialog("Digite a data de hoje: ");
                            List<Produto> produtosVencidos = gerenciarProduto
                                    .verificarProdutosVencidosParaData(dataatual);
                            String mensagem = "Produtos Vencidos:\n";

                            for (Produto produto : produtosVencidos) {
                                mensagem += produto.toString() + "\n";
                            }
                            if (produtosVencidos.isEmpty()) {
                                mensagem = "Não há produtos vencidos.\n";
                            }
                            JOptionPane.showMessageDialog(null, mensagem, "Produtos Vencidos",
                                    JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case 5:
                            JOptionPane.showMessageDialog(null, "Relação de todas as compras foi selecionada");
                            List<Compra> listaCompras = gerenciarCompra.getListaCompras();
                            compra.exibirListaComprasSimplificada(listaCompras);
                            break;
                        case 6:
                            JOptionPane.showMessageDialog(null, "Busca de uma compra pelo número foi selecionada");
                            String compraidStr = JOptionPane.showInputDialog(null,
                                    "Digite o número do identificador da compra:");
                            try {
                                int compraid = Integer.parseInt(compraidStr);
                                Compra compraEncontrada = compra.buscarCompraPeloNumero(compraid,
                                        gerenciarCompra.getListaCompras());
                                if (compraEncontrada != null) {
                                    JOptionPane.showMessageDialog(null, "Compra Encontrada:\n" + compraEncontrada);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Compra não encontrada.");
                                }
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null,
                                        "Número inválido. Por favor, insira um número inteiro.", "Erro",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 7:
                            JOptionPane.showMessageDialog(null,
                                    "Relação de todas as compras que não foram pagas ainda foi selecionada");
                            compra.exibirComprasNaoPagas();
                            break;
                        case 8:
                            JOptionPane.showMessageDialog(null, "Relação das 10 últimas compras pagas foi selecionada");
                            List<Compra> ultimasComprasPagas = gerenciarCompra.obterUltimas10ComprasPagas();
                            if (!ultimasComprasPagas.isEmpty()) {
                                compra.exibirListaComprasSimplificada(ultimasComprasPagas);
                            } else {
                                JOptionPane.showMessageDialog(null, "Não há 10 compras pagas para mostrar.",
                                        "Informação", JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                        case 9:
                            JOptionPane.showMessageDialog(null,
                                    "Apresentação das informações da compra mais cara foi selecionada");
                            Compra compraMaisCara = gerenciarCompra.encontrarCompraMaisCara();
                            if (compraMaisCara != null) {
                                JOptionPane.showMessageDialog(null, "Detalhes da Compra Mais Cara:\n" + compraMaisCara);
                            } else {
                                JOptionPane.showMessageDialog(null, "Não há compras para mostrar.", "Informação",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                        case 10:
                            JOptionPane.showMessageDialog(null,
                                    "Apresentação das informações da compra mais barata foi selecionada");
                            Compra compraMaisBarata = gerenciarCompra
                                    .encontrarCompraMaisBarata(gerenciarCompra.getListaCompras());
                            if (compraMaisBarata != null) {
                                JOptionPane.showMessageDialog(null,
                                        "Detalhes da Compra Mais Barata:\n" + compraMaisBarata);
                            } else {
                                JOptionPane.showMessageDialog(null, "Não há compras para mostrar.", "Informação",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                        case 11:
                            JOptionPane.showMessageDialog(null,
                                    "Relação do valor total de compras feitas em cada mês nos últimos 12 meses foi selecionada");
                            Map<String, Float> valorTotalMesRecente = gerenciarCompra.obterValorTotalMesRecente();
                            Set<String> keys = valorTotalMesRecente.keySet();
                            String totalString = "REGISTRO ULTIMOS MESES:\n";
                            for (String key : keys) {
                                totalString += key + ": " + valorTotalMesRecente.get(key) + "\n";
                            }
                            JOptionPane.showMessageDialog(null, totalString, "Valor Total de Compras por Mês",
                                    JOptionPane.INFORMATION_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Opção inválida!", "Opção inserida: " + opcaoStr,
                                    JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, insira um número inteiro.",
                            "Opção inserida: " + opcaoStr, JOptionPane.ERROR_MESSAGE);
                    opcao = 0;
                }
            }
        } while (opcao != 8);
    }
}
