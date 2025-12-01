package interfaces;

import frigotraceDB2.dao.VendaDAO;
import frigotraceDB2.modelo.Venda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VendaInterface extends JFrame {

    private final VendaDAO vendaDAO = new VendaDAO();

    // Componentes
    private JTextField jTextFieldDataEntrega;
    private JTextField jTextFieldDataVenda;
    private JTextField jTextFieldValorTotal;
    private JTextField jTextFieldIdProduto;
    private JTextField jTextFieldCliente;

    private JButton jButtonCadastrar;
    private JButton jButtonAtualizar;
    private JButton jButtonExcluir;
    private JButton jButtonLimpar;

    private JTable jTableVenda;

    public VendaInterface() {
        setTitle("Venda");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        initComponents();
        configurarRestricoes();
        carregarTabelaVendas();
    }

    private void initComponents() {
        Color fundoLilaz = new Color(240, 240, 255);
        Color botaoLilaz = new Color(204, 204, 255);

        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(fundoLilaz);

        // Título
        JLabel jLabelTitulo = new JLabel("Venda", SwingConstants.CENTER);
        jLabelTitulo.setFont(jLabelTitulo.getFont().deriveFont(36f));
        painelPrincipal.add(jLabelTitulo, BorderLayout.NORTH);

        // Painel de campos (GridBagLayout)
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(fundoLilaz);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel jLabelDataEntrega = new JLabel("Data da Entrega:");
        JLabel jLabelIdProduto = new JLabel("ID Produto:");
        JLabel jLabelDataVenda = new JLabel("Data da Venda:");
        JLabel jLabelValorTotal = new JLabel("Valor total:");
        JLabel jLabelIdCliente = new JLabel("ID Cliente:");

        jTextFieldDataEntrega = new JTextField(10);
        jTextFieldIdProduto = new JTextField(5);
        jTextFieldDataVenda = new JTextField(10);
        jTextFieldValorTotal = new JTextField(10);
        jTextFieldCliente = new JTextField(5);

        // Linha 1 ? Data Entrega / ID Produto
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(jLabelDataEntrega, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldDataEntrega, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelIdProduto, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldIdProduto, gbc);

        // Linha 2 ? Data Venda / ID Cliente
        gbc.gridx = 0; gbc.gridy = 1;
        painelCampos.add(jLabelDataVenda, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldDataVenda, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelIdCliente, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldCliente, gbc);

        // Linha 3 ? Valor Total
        gbc.gridx = 0; gbc.gridy = 2;
        painelCampos.add(jLabelValorTotal, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldValorTotal, gbc);

        // Painel da tabela
        jTableVenda = new JTable();
        JScrollPane scrollTabela = new JScrollPane(jTableVenda);

        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));
        painelCentro.setBackground(fundoLilaz);
        painelCentro.add(painelCampos, BorderLayout.NORTH);
        painelCentro.add(scrollTabela, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(fundoLilaz);

        jButtonCadastrar = new JButton("Cadastrar");
        jButtonAtualizar = new JButton("Atualizar");
        jButtonExcluir = new JButton("Excluir");
        jButtonLimpar = new JButton("Limpar");

        JButton[] botoes = { jButtonCadastrar, jButtonAtualizar, jButtonExcluir, jButtonLimpar };
        for (JButton b : botoes) {
            b.setBackground(botaoLilaz);
            b.setFocusPainted(false);
            b.setFont(b.getFont().deriveFont(14f));
            b.setPreferredSize(new java.awt.Dimension(110, 35));
            painelBotoes.add(b);
        }

        // Monta tudo no principal
        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);

        // Eventos dos botões
        jButtonCadastrar.addActionListener(e -> cadastrarVenda());
        jButtonAtualizar.addActionListener(e -> atualizarVenda());
        jButtonExcluir.addActionListener(e -> excluirVenda());
        jButtonLimpar.addActionListener(e -> limparCampos());

        // Clique na tabela
        jTableVenda.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCamposDaTabela();
            }
        });
    }

    private void configurarRestricoes() {
        // IDs numéricos
        jTextFieldIdProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;
                if (!Character.isDigit(c)) evt.consume();
            }
        });

        jTextFieldCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;
                if (!Character.isDigit(c)) evt.consume();
            }
        });

        // Valor Total: numérico com vírgula/ponto
        jTextFieldValorTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;
                if (!Character.isDigit(c) && c != ',' && c != '.') {
                    evt.consume();
                    return;
                }
                if ((c == ',' || c == '.') &&
                        (jTextFieldValorTotal.getText().contains(",")
                                || jTextFieldValorTotal.getText().contains("."))) {
                    evt.consume();
                }
            }
        });
    }

    private void carregarTabelaVendas() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
            "ID Venda", "ID Produto", "Data Venda",
            "Valor Total", "Data Entrega", "ID Cliente"
        });

        try {
            List<Venda> lista = vendaDAO.listarTodos();

            for (Venda v : lista) {
                modelo.addRow(new Object[]{
                    v.getIdVenda(),
                    v.getIdProduto(),
                    v.getDataVenda(),
                    v.getValorTotal(),
                    v.getDataEntrega(),
                    v.getIdCliente()
                });
            }

            jTableVenda.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar tabela: " + e.getMessage());
        }
    }

    private void limparCampos() {
        jTextFieldIdProduto.setText("");
        jTextFieldDataVenda.setText("");
        jTextFieldValorTotal.setText("");
        jTextFieldDataEntrega.setText("");
        jTextFieldCliente.setText("");
        jTableVenda.clearSelection();
    }

    private Venda montarVendaDosCampos() {
        try {
            String dataVendaStr = jTextFieldDataVenda.getText().trim();
            String valorTotalStr = jTextFieldValorTotal.getText().trim();
            String dataEntregaStr = jTextFieldDataEntrega.getText().trim();
            String idProdutoStr = jTextFieldIdProduto.getText().trim();
            String idClienteStr = jTextFieldCliente.getText().trim();

            // Campos obrigatórios
            if (dataVendaStr.isEmpty() || valorTotalStr.isEmpty()
                    || dataEntregaStr.isEmpty()
                    || idProdutoStr.isEmpty() || idClienteStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return null;
            }

            int idProduto;
            int idCliente;
            try {
                idProduto = Integer.parseInt(idProdutoStr);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID Produto inválido!");
                return null;
            }
            try {
                idCliente = Integer.parseInt(idClienteStr);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "ID Cliente inválido!");
                return null;
            }

            // Valor total
            BigDecimal valorTotal;
            try {
                String valorNormalized = valorTotalStr
                        .replace(".", "")
                        .replace(",", ".");
                valorTotal = new BigDecimal(valorNormalized);
                if (valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Valor total deve ser maior que zero!");
                    return null;
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Valor total inválido!");
                return null;
            }

            // Datas
            Date sqlDataVenda = parseToSqlDate(dataVendaStr);
            if (sqlDataVenda == null) {
                JOptionPane.showMessageDialog(this,
                        "Data da venda inválida! Use yyyy-MM-dd ou dd/MM/yyyy");
                return null;
            }

            Date sqlDataEntrega = parseToSqlDate(dataEntregaStr);
            if (sqlDataEntrega == null) {
                JOptionPane.showMessageDialog(this,
                        "Data de entrega inválida! Use yyyy-MM-dd ou dd/MM/yyyy");
                return null;
            }

            // Regra: entrega não pode ser antes da venda
            if (sqlDataEntrega.before(sqlDataVenda)) {
                JOptionPane.showMessageDialog(this,
                        "A data de entrega não pode ser anterior à data da venda.",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            Venda v = new Venda();
            v.setIdProduto(idProduto);
            v.setDataVenda(sqlDataVenda);
            v.setValorTotal(valorTotal);
            v.setDataEntrega(sqlDataEntrega);
            v.setIdCliente(idCliente);

            return v;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro nos campos: " + e.getMessage());
            return null;
        }
    }

    // Converte String em java.sql.Date aceitando "yyyy-MM-dd" ou "dd/MM/yyyy"
    private static Date parseToSqlDate(String input) {
        if (input == null) return null;

        input = input.trim();
        if (input.isEmpty()) return null;

        // 1) Tenta formato ISO: yyyy-MM-dd
        try {
            LocalDate ld = LocalDate.parse(input,
                    DateTimeFormatter.ISO_LOCAL_DATE);
            return Date.valueOf(ld);
        } catch (DateTimeParseException ex) {
            // ignora e tenta o próximo formato
        }

        // 2) Tenta formato brasileiro: dd/MM/yyyy
        try {
            DateTimeFormatter fmt =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate ld = LocalDate.parse(input, fmt);
            return Date.valueOf(ld);
        } catch (DateTimeParseException e) {
            return null; // nenhum formato funcionou
        }
    }

    private void preencherCamposDaTabela() {
        int linha = jTableVenda.getSelectedRow();
        if (linha == -1) return;

        jTextFieldIdProduto.setText(
                jTableVenda.getValueAt(linha, 1).toString());
        jTextFieldDataVenda.setText(
                jTableVenda.getValueAt(linha, 2).toString());
        jTextFieldValorTotal.setText(
                jTableVenda.getValueAt(linha, 3).toString());
        jTextFieldDataEntrega.setText(
                jTableVenda.getValueAt(linha, 4).toString());
        jTextFieldCliente.setText(
                jTableVenda.getValueAt(linha, 5).toString());
    }

    private void cadastrarVenda() {
        Venda novaVenda = montarVendaDosCampos();
        if (novaVenda == null) return;

        try {
            vendaDAO.salvar(novaVenda);
            carregarTabelaVendas();
            limparCampos();
            JOptionPane.showMessageDialog(this,
                    "Venda cadastrada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void atualizarVenda() {
        int linha = jTableVenda.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma venda na tabela!");
            return;
        }

        int idVenda = (int) jTableVenda.getValueAt(linha, 0);

        Venda v = montarVendaDosCampos();
        if (v == null) return;
        v.setIdVenda(idVenda);

        try {
            vendaDAO.atualizar(v);
            carregarTabelaVendas();
            limparCampos();
            JOptionPane.showMessageDialog(this, "Venda atualizada!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar: " + e.getMessage());
        }
    }

    private void excluirVenda() {
        int linha = jTableVenda.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma venda para excluir!");
            return;
        }

        int idVenda = (int) jTableVenda.getValueAt(linha, 0);

        try {
            vendaDAO.deletar(idVenda);
            carregarTabelaVendas();
            limparCampos();
            JOptionPane.showMessageDialog(this, "Venda excluída!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VendaInterface().setVisible(true);
        });
    }
}
