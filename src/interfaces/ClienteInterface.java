package interfaces;

import frigotraceDB2.dao.ClienteDAO;
import frigotraceDB2.modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class ClienteInterface extends JFrame {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    // Campos (sem ID Cliente)
    private JTextField jTextFieldNomeRazao;
    private JTextField jTextFieldCnpjCpf;
    private JTextField jTextFieldIdEndereco;
    private JTable jTableCliente;

    public ClienteInterface() {
        setTitle("Cliente");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initComponents();
        configurarRestricoes();
        carregarTabelaClientes();
    }

    // ===================== INTERFACE =====================
    private void initComponents() {

        // Painel principal com BorderLayout
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(235, 235, 255));

        // ---------- TÍTULO ----------
        JLabel titulo = new JLabel("Cliente", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(new Color(60, 60, 120));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // ---------- FORMULÁRIO (NORTH-CENTER) ----------
        JPanel painelFormWrapper = new JPanel(new BorderLayout());
        painelFormWrapper.setBackground(new Color(235, 235, 255));

        JPanel painelForm = new JPanel();
        painelForm.setBackground(new Color(235, 235, 255));
        painelForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 1 ? Nome/Razão Social
        JLabel lblNomeRazao = new JLabel("Nome / Razão Social:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        painelForm.add(lblNomeRazao, gbc);

        jTextFieldNomeRazao = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        painelForm.add(jTextFieldNomeRazao, gbc);

        // Linha 2 ? CPF
        JLabel lblCnpjCpf = new JLabel("CPF:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        painelForm.add(lblCnpjCpf, gbc);

        jTextFieldCnpjCpf = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        painelForm.add(jTextFieldCnpjCpf, gbc);

        // Linha 3 ? ID Endereço
        JLabel lblIdEndereco = new JLabel("ID Endereço:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        painelForm.add(lblIdEndereco, gbc);

        jTextFieldIdEndereco = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        painelForm.add(jTextFieldIdEndereco, gbc);

        painelFormWrapper.add(painelForm, BorderLayout.NORTH);

        painelPrincipal.add(painelFormWrapper, BorderLayout.CENTER);

        // ---------- TABELA (CENTRO) ----------
        jTableCliente = new JTable();
        JScrollPane scrollTabela = new JScrollPane(jTableCliente);
        scrollTabela.setPreferredSize(new Dimension(800, 300));

        painelPrincipal.add(scrollTabela, BorderLayout.SOUTH);

        // ---------- BOTÕES (SOUTH) ----------
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(new Color(235, 235, 255));

        JButton jButtonCadastrar = new JButton("Cadastrar");
        JButton jButtonAtualizar = new JButton("Atualizar");
        JButton jButtonExcluir = new JButton("Excluir");
        JButton jButtonLimpar = new JButton("Limpar");

        painelBotoes.add(jButtonCadastrar);
        painelBotoes.add(jButtonAtualizar);
        painelBotoes.add(jButtonExcluir);
        painelBotoes.add(jButtonLimpar);

        // Para os botões ficarem abaixo da tabela,
        // colocamos tudo (tabela + botões) num painel extra:
        JPanel painelCentroSul = new JPanel(new BorderLayout());
        painelCentroSul.setBackground(new Color(235, 235, 255));
        painelCentroSul.add(scrollTabela, BorderLayout.CENTER);
        painelCentroSul.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(painelFormWrapper, BorderLayout.NORTH);
        painelPrincipal.add(painelCentroSul, BorderLayout.CENTER);

        setContentPane(painelPrincipal);

        // Ações de botões
        jButtonCadastrar.addActionListener(e -> cadastrarCliente());
        jButtonAtualizar.addActionListener(e -> atualizarCliente());
        jButtonExcluir.addActionListener(e -> excluirCliente());
        jButtonLimpar.addActionListener(e -> limparCampos());

        // Clique na tabela ? preenche campos
        jTableCliente.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    // ===================== RESTRIÇÕES =====================
    private void configurarRestricoes() {

        // Apenas números no ID Endereço
        jTextFieldIdEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) &&
                    c != KeyEvent.VK_BACK_SPACE &&
                    c != KeyEvent.VK_DELETE) {
                    evt.consume();
                }
            }
        });

        // CPF só dígitos e máximo 11
        jTextFieldCnpjCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                char c = evt.getKeyChar();

                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                if (!Character.isDigit(c)) {
                    evt.consume();
                    return;
                }

                if (jTextFieldCnpjCpf.getText().length() >= 11) {
                    evt.consume();
                }
            }
        });
    }

    // ===================== TABELA =====================
    private void carregarTabelaClientes() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
                "ID Cliente", "Nome / Razão Social", "CPF", "ID Endereço"
        });

        List<Cliente> lista = clienteDAO.buscarTodos();   // usa seu DAO

        for (Cliente c : lista) {
            modelo.addRow(new Object[]{
                    c.getIdCliente(),
                    c.getNomeRazaoSocial(),
                    c.getCnpjCpf(),
                    c.getIdEndereco()
            });
        }

        jTableCliente.setModel(modelo);
    }

    // ===================== CRUD =====================
    private void cadastrarCliente() {
        Cliente c = montarClienteDosCampos();
        if (c == null) return;

        clienteDAO.inserir(c);
        carregarTabelaClientes();
        limparCampos();
    }

    private void atualizarCliente() {
        int linha = jTableCliente.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela!");
            return;
        }

        int id = (int) jTableCliente.getValueAt(linha, 0);

        Cliente c = montarClienteDosCampos();
        if (c == null) return;

        c.setIdCliente(id);
        clienteDAO.atualizar(c);
        carregarTabelaClientes();
        limparCampos();
    }

    private void excluirCliente() {
        int linha = jTableCliente.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir!");
            return;
        }

        int id = (int) jTableCliente.getValueAt(linha, 0);
        clienteDAO.excluir(id);
        carregarTabelaClientes();
        limparCampos();
    }

    // ===================== AUXILIARES =====================
    private Cliente montarClienteDosCampos() {

        String nome = jTextFieldNomeRazao.getText().trim();
        String cpf = jTextFieldCnpjCpf.getText().trim();
        String idEndStr = jTextFieldIdEndereco.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha Nome/Razão Social e CPF!");
            return null;
        }

        Integer idEndereco = null;
        if (!idEndStr.isEmpty()) {
            try {
                idEndereco = Integer.parseInt(idEndStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID Endereço deve ser numérico!");
                return null;
            }
        }

        // seu modelo permite construtor (nome, cpf, idEndereco)
        return new Cliente(nome, cpf, idEndereco);
    }

    private void preencherCamposDaTabela() {
        int linha = jTableCliente.getSelectedRow();
        if (linha == -1) return;

        jTextFieldNomeRazao.setText(jTableCliente.getValueAt(linha, 1).toString());
        jTextFieldCnpjCpf.setText(jTableCliente.getValueAt(linha, 2).toString());

        Object idEndObj = jTableCliente.getValueAt(linha, 3);
        jTextFieldIdEndereco.setText(idEndObj == null ? "" : idEndObj.toString());
    }

    private void limparCampos() {
        jTextFieldNomeRazao.setText("");
        jTextFieldCnpjCpf.setText("");
        jTextFieldIdEndereco.setText("");
        jTableCliente.clearSelection();
    }
}
