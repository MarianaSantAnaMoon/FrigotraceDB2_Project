package frigotraceDB2;

import interfaces.ClienteInterface;
import interfaces.FornecedorInterface;
import interfaces.MateriaPrimaInterface;
import interfaces.ProdutoInterface;
import interfaces.VendaInterface;
import interfaces.TransporteInInterface;
import interfaces.TransporteOutInterface;
import interfaces.EmailInterface;
import interfaces.TelefoneInterface;
import interfaces.EnderecoInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("FrigoTraceDB - Menu Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 460);
        setLocationRelativeTo(null); // centraliza

        // Tons de azul
        Color azulFundo = new Color(220, 235, 255);    // fundo clarinho
        Color azulHeader = new Color(0, 80, 170);      // azul mais forte
        Color azulBotao = new Color(0, 120, 215);      // azul dos botões
        Color azulBotaoTexto = Color.WHITE;

        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(azulFundo);

        
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setBackground(azulHeader);
        painelHeader.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel lblTitulo = new JLabel("FrigoTraceDB", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));

        JLabel lblSubtitulo = new JLabel("Equipe A  Sistema de Rastreamento", SwingConstants.CENTER);
        lblSubtitulo.setForeground(new Color(220, 235, 255));
        lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 18));

        painelHeader.add(lblTitulo, BorderLayout.CENTER);
        painelHeader.add(lblSubtitulo, BorderLayout.SOUTH);

        painelHeader.setBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0, 50, 120))
        );

        // Painel de botões (grade)
        JPanel painelBotoes = new JPanel(new GridLayout(4, 3, 10, 10));
        painelBotoes.setBackground(azulFundo);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Criar botões
        JButton btnCliente        = new JButton("Cliente");
        JButton btnFornecedor     = new JButton("Fornecedor");
        JButton btnMateriaPrima   = new JButton("Matéria-prima");
        JButton btnProduto        = new JButton("Produto");
        JButton btnVenda          = new JButton("Venda");
        JButton btnTransporteIn   = new JButton("Transporte In");
        JButton btnTransporteOut  = new JButton("Transporte Out");
        JButton btnEmail          = new JButton("Email");
        JButton btnTelefone       = new JButton("Telefone");
        JButton btnEndereco       = new JButton("Endereço");
        JButton btnSair           = new JButton("Sair");

        JButton[] botoes = {
            btnCliente, btnFornecedor, btnMateriaPrima,
            btnProduto, btnVenda, btnTransporteIn,
            btnTransporteOut, btnEmail, btnTelefone,
            btnEndereco, btnSair
        };

        for (JButton b : botoes) {
            b.setBackground(azulBotao);
            b.setForeground(azulBotaoTexto);
            b.setFocusPainted(false);
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        // Adiciona os botões na grade (ordem)
        painelBotoes.add(btnCliente);
        painelBotoes.add(btnFornecedor);
        painelBotoes.add(btnMateriaPrima);

        painelBotoes.add(btnProduto);
        painelBotoes.add(btnVenda);
        painelBotoes.add(btnTransporteIn);

        painelBotoes.add(btnTransporteOut);
        painelBotoes.add(btnEmail);
        painelBotoes.add(btnTelefone);

        painelBotoes.add(btnEndereco);
        painelBotoes.add(btnSair);
        // um espaço da grade fica vazio visualmente, o GridLayout encaixa

        // Monta a tela
        painelPrincipal.add(painelHeader, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);

        setContentPane(painelPrincipal);

        // ======= Ações dos botões =======
        btnCliente.addActionListener(e -> new ClienteInterface().setVisible(true));
        btnFornecedor.addActionListener(e -> new FornecedorInterface().setVisible(true));
        btnMateriaPrima.addActionListener(e -> new MateriaPrimaInterface().setVisible(true));
        btnProduto.addActionListener(e -> new ProdutoInterface().setVisible(true));
        btnVenda.addActionListener(e -> new VendaInterface().setVisible(true));
        btnTransporteIn.addActionListener(e -> new TransporteInInterface().setVisible(true));
        btnTransporteOut.addActionListener(e -> new TransporteOutInterface().setVisible(true));
        btnEmail.addActionListener(e -> new EmailInterface().setVisible(true));
        btnTelefone.addActionListener(e -> new TelefoneInterface().setVisible(true));
        btnEndereco.addActionListener(e -> new EnderecoInterface().setVisible(true));

        btnSair.addActionListener(e -> {
            dispose();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
