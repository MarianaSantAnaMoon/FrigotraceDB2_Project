package interfaces;

import frigotraceDB2.dao.TelefoneDAO;
import frigotraceDB2.modelo.Telefone;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class TelefoneInterface extends JFrame {

    private final TelefoneDAO telefoneDAO = new TelefoneDAO();

    private JTextField jTextFieldNumero;
    private JTable jTableTelefone;

    public TelefoneInterface() {
        setTitle("Telefone");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // centraliza

        initComponents();
        configurarMascaraTelefone();
        carregarTabelaTelefones();
    }

    // ===================== INTERFACE =====================
    private void initComponents() {
        // Cores
        Color fundo = new Color(235, 235, 255);
        Color lilas = new Color(204, 204, 255);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(fundo);

        // Título
        JLabel titulo = new JLabel("Telefone", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(new Color(60, 60, 120));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(fundo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNumero = new JLabel("Número:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        painelForm.add(lblNumero, gbc);

        jTextFieldNumero = new JTextField();
        jTextFieldNumero.setColumns(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        painelForm.add(jTextFieldNumero, gbc);

        // Tabela
        jTableTelefone = new JTable();
        JScrollPane scrollTabela = new JScrollPane(jTableTelefone);
        scrollTabela.setPreferredSize(new Dimension(550, 200));

        // Botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(fundo);

        JButton jButtonCadastrar = new JButton("Cadastrar");
        JButton jButtonAtualizar = new JButton("Atualizar");
        JButton jButtonExcluir   = new JButton("Excluir");
        JButton jButtonLimpar    = new JButton("Limpar");
        JButton jButtonVoltar    = new JButton("Voltar");

        JButton[] botoes = {
            jButtonCadastrar, jButtonAtualizar,
            jButtonExcluir, jButtonLimpar, jButtonVoltar
        };

        for (JButton b : botoes) {
            b.setBackground(lilas);
        }

        painelBotoes.add(jButtonCadastrar);
        painelBotoes.add(jButtonAtualizar);
        painelBotoes.add(jButtonExcluir);
        painelBotoes.add(jButtonLimpar);
        painelBotoes.add(jButtonVoltar);

        // Centro com formulário + tabela + botões
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(fundo);
        centro.add(painelForm, BorderLayout.NORTH);
        centro.add(scrollTabela, BorderLayout.CENTER);
        centro.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(centro, BorderLayout.CENTER);
        setContentPane(painelPrincipal);

        // Ações
        jButtonCadastrar.addActionListener(e -> cadastrarTelefone());
        jButtonAtualizar.addActionListener(e -> atualizarTelefone());
        jButtonExcluir.addActionListener(e -> excluirTelefone());
        jButtonLimpar.addActionListener(e -> limparCampos());
        jButtonVoltar.addActionListener(e -> dispose());

        jTableTelefone.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    // ===================== MÁSCARA DO TELEFONE =====================
    private void configurarMascaraTelefone() {
        jTextFieldNumero.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();

                // Permite backspace/delete
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    return;
                }

                // Bloqueia letras e outros símbolos
                if (!Character.isDigit(c)) {
                    evt.consume();
                    return;
                }

                // Limita quantidade de dígitos totais (11 = DDD + 9 dígitos)
                String apenasDigitos = jTextFieldNumero.getText().replaceAll("\\D", "");
                if (apenasDigitos.length() >= 11) {
                    evt.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent evt) {
                String digits = jTextFieldNumero.getText().replaceAll("\\D", ""); // só números

                StringBuilder formatado = new StringBuilder();

                if (digits.length() > 0) {
                    formatado.append("(");
                    // DDD
                    if (digits.length() >= 2) {
                        formatado.append(digits.substring(0, 2));
                        formatado.append(") ");
                        // parte inicial do número
                        if (digits.length() >= 7) {
                            formatado.append(digits.substring(2, 7));
                            formatado.append("-");
                            // final do número
                            if (digits.length() > 7) {
                                formatado.append(digits.substring(7));
                            }
                        } else if (digits.length() > 2) {
                            formatado.append(digits.substring(2));
                        }
                    } else {
                        // só 1 dígito do DDD
                        formatado.append(digits);
                    }
                }

                jTextFieldNumero.setText(formatado.toString());
            }
        });
    }

    // ===================== TABELA =====================
    private void carregarTabelaTelefones() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID Telefone", "Número"});

        List<Telefone> lista = telefoneDAO.listar(); // ajusta se o método tiver outro nome
        for (Telefone t : lista) {
            modelo.addRow(new Object[]{
                    t.getIdTelefone(),
                    t.getNumero()
            });
        }

        jTableTelefone.setModel(modelo);
    }

    // ===================== CRUD =====================
    private Telefone montarTelefoneDosCampos() {
        String numeroFormatado = jTextFieldNumero.getText().trim();
        String apenasDigitos = numeroFormatado.replaceAll("\\D", "");

        if (apenasDigitos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o número de telefone!");
            return null;
        }

        // 10 ou 11 dígitos (fixo ou celular)
        if (apenasDigitos.length() < 10 || apenasDigitos.length() > 11) {
            JOptionPane.showMessageDialog(this,
                    "Telefone inválido! Use DDD + número (10 ou 11 dígitos).");
            return null;
        }

        Telefone t = new Telefone();
        // Você pode salvar só os dígitos, ou o formatado.
        // Aqui vou salvar COM máscara para ficar igual ao que aparece.
        t.setNumero(numeroFormatado);
        return t;
    }

    private void cadastrarTelefone() {
        Telefone t = montarTelefoneDosCampos();
        if (t == null) return;

        telefoneDAO.inserir(t);  // ajusta se seu DAO tiver outro nome
        carregarTabelaTelefones();
        limparCampos();
    }

    private void atualizarTelefone() {
        int linha = jTableTelefone.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um telefone na tabela!");
            return;
        }

        int id = (int) jTableTelefone.getValueAt(linha, 0);
        Telefone t = montarTelefoneDosCampos();
        if (t == null) return;

        t.setIdTelefone(id);
        telefoneDAO.atualizar(t);
        carregarTabelaTelefones();
        limparCampos();
    }

    private void excluirTelefone() {
        int linha = jTableTelefone.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um telefone para excluir!");
            return;
        }

        int id = (int) jTableTelefone.getValueAt(linha, 0);
        telefoneDAO.excluir(id);
        carregarTabelaTelefones();
        limparCampos();
    }

    // ===================== AUXILIARES =====================
    private void preencherCamposDaTabela() {
        int linha = jTableTelefone.getSelectedRow();
        if (linha == -1) return;

        Object numero = jTableTelefone.getValueAt(linha, 1);
        jTextFieldNumero.setText(numero == null ? "" : numero.toString());
    }

    private void limparCampos() {
        jTextFieldNumero.setText("");
        jTableTelefone.clearSelection();
    }
}
