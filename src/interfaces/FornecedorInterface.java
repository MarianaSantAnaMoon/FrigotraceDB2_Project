package interfaces;

import frigotraceDB2.dao.FornecedorDAO;
import frigotraceDB2.modelo.Fornecedor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FornecedorInterface extends JFrame {

    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();

    // Campos de entrada (sem ID)
    private JTextField jTextFieldNomeFornecedor;
    private JTextField jTextFieldCNPJCPF;

    // Botões
    private JButton jButtonCadastrar;
    private JButton jButtonAtualizar;
    private JButton jButtonExcluir;
    private JButton jButtonLimpar;

    // Tabela
    private JTable jTableFornecedor;

    public FornecedorInterface() {
        setTitle("Fornecedor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        initComponents();
        configurarRestricoes();
        carregarTabelaFornecedores();
    }

    private void initComponents() {
        Color fundoLilaz = new Color(240, 240, 255);
        Color botaoLilaz = new Color(204, 204, 255);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(fundoLilaz);

        JLabel titulo = new JLabel("Fornecedor", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(36f));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(fundoLilaz);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel jLabelNome = new JLabel("Nome / Razão Social:");
        JLabel jLabelCnpjCpf = new JLabel("CNPJ/CPF:");

        jTextFieldNomeFornecedor = new JTextField(30);
        jTextFieldCNPJCPF = new JTextField(14);

        // Linha 0 ? Nome
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(jLabelNome, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        painelCampos.add(jTextFieldNomeFornecedor, gbc);
        gbc.gridwidth = 1;

        // Linha 1 ? CNPJ/CPF
        gbc.gridx = 0; gbc.gridy = 1;
        painelCampos.add(jLabelCnpjCpf, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldCNPJCPF, gbc);

        // Tabela
        jTableFornecedor = new JTable();
        JScrollPane scroll = new JScrollPane(jTableFornecedor);

        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));
        painelCentro.setBackground(fundoLilaz);
        painelCentro.add(painelCampos, BorderLayout.NORTH);
        painelCentro.add(scroll, BorderLayout.CENTER);

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

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);

        // Eventos
        jButtonCadastrar.addActionListener(e -> cadastrarFornecedor());
        jButtonAtualizar.addActionListener(e -> atualizarFornecedor());
        jButtonExcluir.addActionListener(e -> excluirFornecedor());
        jButtonLimpar.addActionListener(e -> limparCampos());

        jTableFornecedor.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    // ===== RESTRIÇÕES =====
    private void configurarRestricoes() {
        // Nome: não permite números
        jTextFieldNomeFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (Character.isDigit(c)) {
                    evt.consume();
                }
            }
        });

        // CNPJ/CPF: só dígitos, no máximo 14 (CNPJ) ? aceitando 11 (CPF) ou 14
        jTextFieldCNPJCPF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                if (!Character.isDigit(c)) {
                    evt.consume();
                    return;
                }

                if (jTextFieldCNPJCPF.getText().length() >= 14) {
                    evt.consume();
                }
            }
        });
    }

    // ===== TABELA =====
    private void carregarTabelaFornecedores() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
            "ID Fornecedor", "Nome / Razão Social", "CNPJ/CPF", "ID Endereço"
        });

        try {
            List<Fornecedor> lista = fornecedorDAO.buscarTodos();

            for (Fornecedor f : lista) {
                modelo.addRow(new Object[]{
                    f.getIdFornecedor(),
                    f.getNomeRazaoSocial(),
                    f.getCnpjCpf(),
                    f.getIdEndereco()
                });
            }

            jTableFornecedor.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar fornecedores: " + e.getMessage());
        }
    }

    private void limparCampos() {
        jTextFieldNomeFornecedor.setText("");
        jTextFieldCNPJCPF.setText("");
        jTableFornecedor.clearSelection();
    }

    // Monta objeto a partir dos campos da tela
    private Fornecedor montarFornecedorDosCampos() {
        try {
            String nome = jTextFieldNomeFornecedor.getText().trim();
            String cnpjCpf = jTextFieldCNPJCPF.getText().trim();

            if (nome.isEmpty() || cnpjCpf.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos!");
                return null;
            }

            // Aceita CPF (11) ou CNPJ (14)
            if (!(cnpjCpf.length() == 11 || cnpjCpf.length() == 14)) {
                JOptionPane.showMessageDialog(this,
                        "CNPJ/CPF deve ter 11 (CPF) ou 14 (CNPJ) dígitos.");
                return null;
            }

            Fornecedor f = new Fornecedor();
            f.setNomeRazaoSocial(nome);
            f.setCnpjCpf(cnpjCpf);
            // idEndereco pode ser setado depois, se você tiver essa tela integrada
            f.setIdEndereco(null);

            return f;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro nos campos: " + e.getMessage());
            return null;
        }
    }

    private void preencherCamposDaTabela() {
        int linha = jTableFornecedor.getSelectedRow();
        if (linha == -1) return;

        jTextFieldNomeFornecedor.setText(
                jTableFornecedor.getValueAt(linha, 1).toString());
        jTextFieldCNPJCPF.setText(
                jTableFornecedor.getValueAt(linha, 2).toString());
    }

    // ===== BOTÕES =====
    private void cadastrarFornecedor() {
        Fornecedor f = montarFornecedorDosCampos();
        if (f == null) return;

        try {
            fornecedorDAO.inserir(f);
            carregarTabelaFornecedores();
            limparCampos();
            JOptionPane.showMessageDialog(this,
                    "Fornecedor cadastrado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void atualizarFornecedor() {
        int linha = jTableFornecedor.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um fornecedor na tabela!");
            return;
        }

        Fornecedor f = montarFornecedorDosCampos();
        if (f == null) return;

        int id = (int) jTableFornecedor.getValueAt(linha, 0);
        f.setIdFornecedor(id);

        try {
            fornecedorDAO.atualizar(f);
            carregarTabelaFornecedores();
            limparCampos();
            JOptionPane.showMessageDialog(this,
                    "Fornecedor atualizado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar: " + e.getMessage());
        }
    }

    private void excluirFornecedor() {
        int linha = jTableFornecedor.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um fornecedor para excluir!");
            return;
        }

        int id = (int) jTableFornecedor.getValueAt(linha, 0);

        try {
            fornecedorDAO.excluir(id);
            carregarTabelaFornecedores();
            limparCampos();
            JOptionPane.showMessageDialog(this,
                    "Fornecedor excluído com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FornecedorInterface().setVisible(true);
        });
    }
}
