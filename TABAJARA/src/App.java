import java.text.ParseException;
import javax.swing.JOptionPane;

public class App {
    public static void main(String[] args) throws ParseException {
        System.out.println("**********    **      ******        **           **      **      *******        **");
        System.out.println("/////**///   ****    /*////**      ****         /**     ****    /**////**      ****");
        System.out.println("   /**      **//**   /*   /**     **//**        /**    **//**   /**   /**     **//**");
        System.out.println("   /**     **  //**  /******     **  //**       /**   **  //**  /*******     **  //**");
        System.out.println("   /**    ********** /*//// **  **********      /**  ********** /**///**    **********");
        System.out.println("   /**   /**//////** /*    /** /**//////**  **  /** /**//////** /**  //**  /**//////**");
        System.out.println("   /**   /**     /** /*******  /**     /**/ /*****  /**     /** /**   //** /**     /**");
        System.out.println("   //    //      //  ///////   //      //   /////   //      //  //     //  //      //");

        System.out.println("\n\nSEJA BEM VINDO!!!");
        GerenciarCliente cliente = new GerenciarCliente();
        cliente.carregarClientesDoArquivo();
        GerenciarProduto produto = new GerenciarProduto();
        produto.carregarProdutosDoArquivo();
        GerenciarCompra compra = new GerenciarCompra(cliente, produto);
        compra.carregarComprasDoArquivo();
        Relatorio relatorio = new Relatorio(cliente, produto, compra);
        int opcao;

        do {
            String opcaoStr = JOptionPane.showInputDialog("---- Escolha a opção desejada ----\n"
                + "===== MENU =====\n"
                + "[1] Cadastrar Cliente\n"
                + "[2] Deletar cliente pelo CPF ou CNPJ\n"
                + "[3] Deletar cliente pelo nome\n"
                + "[4] Cadastrar produto\n"
                + "[5] Efetuar compra\n"
                + "[6] Atualizar situação de pagamento de uma compra\n"
                + "[7] Relátorios\n"
                + "[8] Sair"
            );

            // Se a pessoa clicar no "cancelar" ou fechar a janela do JOptionPane, sai do sistema
            if (opcaoStr == null) {
                opcao = 8;
            } else {
                // Tratamento de exceções, para que o programa continue a funcionar após o tratamento da exceção
                try {
                    opcao = Integer.parseInt(opcaoStr); // Convertendo uma String para int
                    switch (opcao) {
                        case 1:
                            JOptionPane.showMessageDialog(null, "Cadastrar Cliente foi selecionada");
                            cliente.cadastrarCliente();
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, "Deletar Cliente pelo CPF ou CNPJ foi selecionada");
                            cliente.deletarpeloCPFouCnpj();
                            break;
                        case 3:
                            JOptionPane.showMessageDialog(null, "Deletar Cliente pelo nome foi selecionada");
                            cliente.deletarClientePeloNome();
                            break;
                        case 4:
                            JOptionPane.showMessageDialog(null, "Cadastrar produto foi selecionada");
                            produto.cadastrarProduto();
                            break;
                        case 5:
                            JOptionPane.showMessageDialog(null, "Efetuar compra foi selecionada");
                            compra.exibirListas();
                            compra.realizarCompra();
                            break;
                        case 6:
                            JOptionPane.showMessageDialog(null, "Atualizar situação de pagamento de uma compra foi selecionada");
                            compra.atualizarSituacaoPagamento();
                            break;
                        case 7:
                            JOptionPane.showMessageDialog(null, "Relátorios foi selecionada");
                            relatorio.exibirRelatorio();
                            break;
                        case 8:
                        JOptionPane.showMessageDialog(null, "Sair foi selecionada");
                        JOptionPane.showMessageDialog(null, "Saindo...");
                        cliente.atualizarArquivoClientes();
                        produto.atualizarArquivoProdutos();
                        compra.atualizarArquivoCompras();
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Opção inválida!", "Opção inserida: " + opcaoStr, JOptionPane.ERROR_MESSAGE);
                            break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, insira um número inteiro.", "Opção inserida: " + opcaoStr, JOptionPane.ERROR_MESSAGE);
                    opcao = 0; // Definindo a opcao como 0 em caso de entrada inválida
                }
            }
        } while (opcao != 8);
    }
}
