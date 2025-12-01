package interfaces;

import frigotraceDB2.dao.EmailDAO;
import frigotraceDB2.modelo.Email;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class EmailInterface extends JFrame {

    private final EmailDAO emailDAO = new EmailDAO();

    private JTextField jTextFieldEmail;
    private JTable jTableEmail;

    public EmailInterface() {
        setTitle("Email");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        initComponents();
        configurarRestricoes();
        carregarTabelaEmails();
    }

    // ===================== INTERFACE =====================
    private void initComponents() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(235, 235, 255));

        // Título
        JLabel titulo = new JLabel("Email", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(new Color(60, 60, 120));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // ---------- Formulário ----------
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(new Color(235, 235, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblEmail = new JLabel("Endereço de Email:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        painelForm.add(lblEmail, gbc);

        jTextFieldEmail = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        painelForm.add(jTextFieldEmail, gbc);

        // ---------- Tabela ----------
        jTableEmail = new JTable();
        JScrollPane scrollTabela = new JScrollPane(jTableEmail);
        scrollTabela.setPreferredSize(new Dimension(550, 200));

        // ---------- Botões ----------
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(235, 235, 255));

        JButton jButtonCadastrar = new JButton("Cadastrar");
        JButton jButtonAtualizar = new JButton("Atualizar");
        JButton jButtonExcluir   = new JButton("Excluir");
        JButton jButtonLimpar    = new JButton("Limpar");

        Color lilas = new Color(204, 204, 255);
        for (JButton b : new JButton[]{jButtonCadastrar, jButtonAtualizar, jButtonExcluir, jButtonLimpar}) {
            b.setBackground(lilas);
        }

        painelBotoes.add(jButtonCadastrar);
        painelBotoes.add(jButtonAtualizar);
        painelBotoes.add(jButtonExcluir);
        painelBotoes.add(jButtonLimpar);

        // Centro + Sul
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(new Color(235, 235, 255));
        centro.add(painelForm, BorderLayout.NORTH);
        centro.add(scrollTabela, BorderLayout.CENTER);
        centro.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(centro, BorderLayout.CENTER);
        setContentPane(painelPrincipal);

        // Ações
        jButtonCadastrar.addActionListener(e -> cadastrarEmail());
        jButtonAtualizar.addActionListener(e -> atualizarEmail());
        jButtonExcluir.addActionListener(e -> excluirEmail());
        jButtonLimpar.addActionListener(e -> limparCampos());

        jTableEmail.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    // ===================== RESTRIÇÕES =====================
    private void configurarRestricoes() {
        jTextFieldEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                // Limite de tamanho
                if (jTextFieldEmail.getText().length() >= 80) {
                    evt.consume();
                }
            }
        });
    }

    // ===================== TABELA =====================
    private void carregarTabelaEmails() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID Email", "Endereço de Email"});

        List<Email> lista = emailDAO.listar();
        for (Email e : lista) {
            modelo.addRow(new Object[]{
                    e.getIdEmail(),
                    e.getEnderecoEmail()
            });
        }

        jTableEmail.setModel(modelo);
    }

    // ===================== CRUD =====================
    private Email montarEmailDosCampos() {
        String emailStr = jTextFieldEmail.getText().trim();

        if (emailStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o email!");
            return null;
        }

        // Validação simples de email
        if (!emailStr.contains("@") || !emailStr.contains(".")) {
            JOptionPane.showMessageDialog(this, "Email inválido!");
            return null;
        }

        Email e = new Email();
        e.setEnderecoEmail(emailStr);
        return e;
    }

    private void cadastrarEmail() {
        Email e = montarEmailDosCampos();
        if (e == null) return;

        emailDAO.inserir(e);
        carregarTabelaEmails();
        limparCampos();
    }

    private void atualizarEmail() {
        int linha = jTableEmail.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um email na tabela!");
            return;
        }

        int id = (int) jTableEmail.getValueAt(linha, 0);
        Email e = montarEmailDosCampos();
        if (e == null) return;

        e.setIdEmail(id);
        emailDAO.atualizar(e);
        carregarTabelaEmails();
        limparCampos();
    }

    private void excluirEmail() {
        int linha = jTableEmail.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um email para excluir!");
            return;
        }

        int id = (int) jTableEmail.getValueAt(linha, 0);
        emailDAO.excluir(id);
        carregarTabelaEmails();
        limparCampos();
    }

    // ===================== AUXILIARES =====================
    private void preencherCamposDaTabela() {
        int linha = jTableEmail.getSelectedRow();
        if (linha == -1) return;

        jTextFieldEmail.setText(jTableEmail.getValueAt(linha, 1).toString());
    }

    private void limparCampos() {
        jTextFieldEmail.setText("");
        jTableEmail.clearSelection();
    }
}
