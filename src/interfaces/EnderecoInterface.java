package interfaces;

import frigotraceDB2.dao.EnderecoDAO;
import frigotraceDB2.modelo.Endereco;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class EnderecoInterface extends JFrame {

    private final EnderecoDAO enderecoDAO = new EnderecoDAO();

    private JTextField jTextFieldRua;
    private JTextField jTextFieldBairro;
    private JTextField jTextFieldCidade;
    private JTextField jTextFieldEstado;
    private JTextField jTextFieldCep;
    private JTable jTableEndereco;

    public EnderecoInterface() {
        setTitle("Endereço");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        initComponents();
        configurarRestricoes();
        carregarTabelaEnderecos();
    }

    // ===================== INTERFACE =====================
    private void initComponents() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(235, 235, 255));

        JLabel titulo = new JLabel("Endereço", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(new Color(60, 60, 120));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(new Color(235, 235, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblRua = new JLabel("Rua:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        painelForm.add(lblRua, gbc);

        jTextFieldRua = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        painelForm.add(jTextFieldRua, gbc);

        JLabel lblBairro = new JLabel("Bairro:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        painelForm.add(lblBairro, gbc);

        jTextFieldBairro = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        painelForm.add(jTextFieldBairro, gbc);

        JLabel lblCidade = new JLabel("Cidade:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        painelForm.add(lblCidade, gbc);

        jTextFieldCidade = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        painelForm.add(jTextFieldCidade, gbc);

        JLabel lblEstado = new JLabel("UF:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        painelForm.add(lblEstado, gbc);

        jTextFieldEstado = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        painelForm.add(jTextFieldEstado, gbc);

        JLabel lblCep = new JLabel("CEP:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        painelForm.add(lblCep, gbc);

        jTextFieldCep = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1;
        painelForm.add(jTextFieldCep, gbc);

        // Tabela
        jTableEndereco = new JTable();
        JScrollPane scrollTabela = new JScrollPane(jTableEndereco);
        scrollTabela.setPreferredSize(new Dimension(750, 250));

        // Botões (apenas cadastrar/limpar por enquanto)
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(235, 235, 255));

        JButton jButtonCadastrar = new JButton("Cadastrar");
        JButton jButtonLimpar    = new JButton("Limpar");

        Color lilas = new Color(204, 204, 255);
        jButtonCadastrar.setBackground(lilas);
        jButtonLimpar.setBackground(lilas);

        painelBotoes.add(jButtonCadastrar);
        painelBotoes.add(jButtonLimpar);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(new Color(235, 235, 255));
        centro.add(painelForm, BorderLayout.NORTH);
        centro.add(scrollTabela, BorderLayout.CENTER);
        centro.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(centro, BorderLayout.CENTER);
        setContentPane(painelPrincipal);

        // Ações
        jButtonCadastrar.addActionListener(e -> cadastrarEndereco());
        jButtonLimpar.addActionListener(e -> limparCampos());

        jTableEndereco.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    // ===================== RESTRIÇÕES =====================
    private void configurarRestricoes() {
        // UF: max 2 letras
        jTextFieldEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();

                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                if (!Character.isLetter(c)) {
                    evt.consume();
                    return;
                }

                if (jTextFieldEstado.getText().length() >= 2) {
                    evt.consume();
                }
            }
        });

        // CEP: só dígitos, até 8
        jTextFieldCep.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();

                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                if (!Character.isDigit(c)) {
                    evt.consume();
                    return;
                }

                if (jTextFieldCep.getText().length() >= 8) {
                    evt.consume();
                }
            }
        });
    }

    // ===================== TABELA =====================
    private void carregarTabelaEnderecos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
                "ID Endereço", "Rua", "Bairro", "Cidade", "UF", "CEP"
        });

        List<Endereco> lista = enderecoDAO.buscarTodos();
        for (Endereco e : lista) {
            modelo.addRow(new Object[]{
                    e.getIdEndereco(),
                    e.getRua(),
                    e.getBairro(),
                    e.getCidade(),
                    e.getEstado(),
                    e.getCep()
            });
        }

        jTableEndereco.setModel(modelo);
    }

    // ===================== Cadastrar =====================
    private Endereco montarEnderecoDosCampos() {
        String rua    = jTextFieldRua.getText().trim();
        String bairro = jTextFieldBairro.getText().trim();
        String cidade = jTextFieldCidade.getText().trim();
        String estado = jTextFieldEstado.getText().trim().toUpperCase();
        String cep    = jTextFieldCep.getText().trim();

        if (rua.isEmpty() || bairro.isEmpty() || cidade.isEmpty()
                || estado.isEmpty() || cep.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos do endereço!");
            return null;
        }

        if (estado.length() != 2) {
            JOptionPane.showMessageDialog(this, "UF deve ter 2 letras (ex: SP, RJ)!");
            return null;
        }

        String cepDigitos = cep.replaceAll("\\D", "");
        if (cepDigitos.length() != 8) {
            JOptionPane.showMessageDialog(this, "CEP deve ter 8 dígitos!");
            return null;
        }

        return new Endereco(rua, bairro, cidade, estado, cepDigitos);
    }

    private void cadastrarEndereco() {
        Endereco e = montarEnderecoDosCampos();
        if (e == null) return;

        enderecoDAO.inserir(e);
        carregarTabelaEnderecos();
        limparCampos();
    }

    // ===================== AUXILIARES =====================
    private void preencherCamposDaTabela() {
        int linha = jTableEndereco.getSelectedRow();
        if (linha == -1) return;

        jTextFieldRua.setText(jTableEndereco.getValueAt(linha, 1).toString());
        jTextFieldBairro.setText(jTableEndereco.getValueAt(linha, 2).toString());
        jTextFieldCidade.setText(jTableEndereco.getValueAt(linha, 3).toString());
        jTextFieldEstado.setText(jTableEndereco.getValueAt(linha, 4).toString());
        jTextFieldCep.setText(jTableEndereco.getValueAt(linha, 5).toString());
    }

    private void limparCampos() {
        jTextFieldRua.setText("");
        jTextFieldBairro.setText("");
        jTextFieldCidade.setText("");
        jTextFieldEstado.setText("");
        jTextFieldCep.setText("");
        jTableEndereco.clearSelection();
    }
}
