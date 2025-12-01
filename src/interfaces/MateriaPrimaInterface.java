package interfaces;

import frigotraceDB2.dao.MateriaPrimaDAO;
import frigotraceDB2.modelo.MateriaPrima;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MateriaPrimaInterface extends JFrame {

    private final MateriaPrimaDAO mpDAO = new MateriaPrimaDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Campos (SEM campo de ID de matéria-prima)
    private JTextField jTextFieldSISB_SIF;
    private JTextField jTextFieldDataProducao;
    private JTextField jTextFieldDataValidade;
    private JTextField jTextFieldTemperatura;
    private JTextField jTextFieldIdFornecedor;

    // Botões
    private JButton jButtonCadastrar;
    private JButton jButtonAtualizar;
    private JButton jButtonExcluir;
    private JButton jButtonLimpar;

    // Tabela
    private JTable jTableMateriaPrima;

    public MateriaPrimaInterface() {
        setTitle("Matéria-Prima");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        sdf.setLenient(false);

        initComponents();
        configurarRestricoes();
        carregarTabela();
    }

    private void initComponents() {
        Color fundoLilaz = new Color(240, 240, 255);
        Color botaoLilaz = new Color(204, 204, 255);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(fundoLilaz);

        // Título
        JLabel titulo = new JLabel("Matéria-Prima", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(36f));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(fundoLilaz);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel jLabelSISB_SIF = new JLabel("SISB/SIF:");
        JLabel jLabelDataProducao = new JLabel("Data Produção (yyyy-MM-dd):");
        JLabel jLabelDataValidade = new JLabel("Data Validade (yyyy-MM-dd):");
        JLabel jLabelTemperatura = new JLabel("Temperatura (°C):");
        JLabel jLabelIdFornecedor = new JLabel("ID Fornecedor:");

        jTextFieldSISB_SIF = new JTextField(15);
        jTextFieldDataProducao = new JTextField(10);
        jTextFieldDataValidade = new JTextField(10);
        jTextFieldTemperatura = new JTextField(8);
        jTextFieldIdFornecedor = new JTextField(8);

        // Linha 0 ? SISB/SIF
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(jLabelSISB_SIF, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelCampos.add(jTextFieldSISB_SIF, gbc);
        gbc.gridwidth = 1;

        // Linha 1 ? Data Produção / Data Validade
        gbc.gridx = 0; gbc.gridy = 1;
        painelCampos.add(jLabelDataProducao, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldDataProducao, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelDataValidade, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldDataValidade, gbc);

        // Linha 2 ? Temperatura / ID Fornecedor
        gbc.gridx = 0; gbc.gridy = 2;
        painelCampos.add(jLabelTemperatura, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldTemperatura, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelIdFornecedor, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldIdFornecedor, gbc);

        // Tabela
        jTableMateriaPrima = new JTable();
        JScrollPane scroll = new JScrollPane(jTableMateriaPrima);

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

        // Eventos de botão
        jButtonCadastrar.addActionListener(e -> cadastrarMateriaPrima());
        jButtonAtualizar.addActionListener(e -> atualizarMateriaPrima());
        jButtonExcluir.addActionListener(e -> excluirMateriaPrima());
        jButtonLimpar.addActionListener(e -> limparCampos());

        // Clique na tabela preenche os campos
        jTableMateriaPrima.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    // ==================== RESTRIÇÕES DE CAMPOS ====================
    private void configurarRestricoes() {
        // SISB/SIF tudo em maiúsculo
        jTextFieldSISB_SIF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSISB_SIF.setText(jTextFieldSISB_SIF.getText().toUpperCase());
            }
        });

        // Temperatura: só número, -, vírgula ou ponto, 1 separador decimal, sinal só no início
        jTextFieldTemperatura.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                if (!Character.isDigit(c) && c != '-' && c != ',' && c != '.') {
                    evt.consume();
                    return;
                }

                String texto = jTextFieldTemperatura.getText();

                // Apenas um separador decimal
                if ((c == ',' || c == '.') &&
                    (texto.contains(",") || texto.contains("."))) {
                    evt.consume();
                }

                // Sinal '-' apenas na primeira posição
                if (c == '-' && texto.length() > 0) {
                    evt.consume();
                }
            }
        });

        // ID Fornecedor: só dígitos
        jTextFieldIdFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;
                if (!Character.isDigit(c)) {
                    evt.consume();
                }
            }
        });
    }

    // ==================== TABELA ====================
    private void carregarTabela() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("SISB/SIF");
        model.addColumn("Data Produção");
        model.addColumn("Data Validade");
        model.addColumn("Temperatura");
        model.addColumn("ID Fornecedor");

        try {
            List<MateriaPrima> lista = mpDAO.buscarTodos();
            for (MateriaPrima mp : lista) {
                model.addRow(new Object[]{
                    mp.getIdMateriaPrima(),
                    mp.getSisbSif(),
                    mp.getDataProducao() != null ? sdf.format(mp.getDataProducao()) : "",
                    mp.getDataValidade() != null ? sdf.format(mp.getDataValidade()) : "",
                    mp.getTemperatura() != null ? mp.getTemperatura() : "",
                    mp.getIdFornecedor() != null ? mp.getIdFornecedor() : ""
                });
            }
            jTableMateriaPrima.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar tabela: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        jTextFieldSISB_SIF.setText("");
        jTextFieldDataProducao.setText("");
        jTextFieldDataValidade.setText("");
        jTextFieldTemperatura.setText("");
        jTextFieldIdFornecedor.setText("");
        jTableMateriaPrima.clearSelection();
    }

    private void preencherCamposDaTabela() {
        int linha = jTableMateriaPrima.getSelectedRow();
        if (linha == -1) return;

        jTextFieldSISB_SIF.setText(valorTabela(linha, 1));
        jTextFieldDataProducao.setText(valorTabela(linha, 2));
        jTextFieldDataValidade.setText(valorTabela(linha, 3));
        jTextFieldTemperatura.setText(valorTabela(linha, 4));
        jTextFieldIdFornecedor.setText(valorTabela(linha, 5));
    }

    private String valorTabela(int linha, int coluna) {
        Object obj = jTableMateriaPrima.getValueAt(linha, coluna);
        return obj != null ? obj.toString() : "";
    }

    // ==================== MONTAGEM DO OBJETO ====================
    private MateriaPrima montarMateriaPrimaDosCampos(Integer id) {
        try {
            String sisb = jTextFieldSISB_SIF.getText().trim();
            String dataProdStr = jTextFieldDataProducao.getText().trim();
            String dataValStr = jTextFieldDataValidade.getText().trim();
            String tempStr = jTextFieldTemperatura.getText().trim();
            String idFornStr = jTextFieldIdFornecedor.getText().trim();

            if (sisb.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "SISB/SIF é obrigatório.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            Date dataProd = null;
            Date dataVal = null;
            Float temperatura = null;
            Integer idFornecedor = null;

            // Datas (opcionais, mas se preencher precisa estar corretas)
            if (!dataProdStr.isEmpty()) {
                try {
                    dataProd = sdf.parse(dataProdStr);
                } catch (ParseException pe) {
                    JOptionPane.showMessageDialog(this,
                            "Data de produção inválida. Use o formato yyyy-MM-dd.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }

            if (!dataValStr.isEmpty()) {
                try {
                    dataVal = sdf.parse(dataValStr);
                } catch (ParseException pe) {
                    JOptionPane.showMessageDialog(this,
                            "Data de validade inválida. Use o formato yyyy-MM-dd.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }

            // Regra: se as duas datas estiverem preenchidas, validade não pode ser antes da produção
            if (dataProd != null && dataVal != null && dataVal.before(dataProd)) {
                JOptionPane.showMessageDialog(this,
                        "A data de validade não pode ser anterior à data de produção.",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            // Temperatura (opcional)
            if (!tempStr.isEmpty()) {
                try {
                    temperatura = Float.parseFloat(tempStr.replace(",", "."));
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this,
                            "Temperatura inválida.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }

            // ID Fornecedor (opcional)
            if (!idFornStr.isEmpty()) {
                try {
                    idFornecedor = Integer.parseInt(idFornStr);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this,
                            "ID Fornecedor inválido.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }

            MateriaPrima mp;
            if (id == null) {
                // construtor sem ID
                mp = new MateriaPrima(sisb, dataProd, dataVal, temperatura, idFornecedor);
            } else {
                // construtor com ID
                mp = new MateriaPrima(id, sisb, dataProd, dataVal, temperatura, idFornecedor);
            }

            return mp;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro nos campos: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ==================== BOTÕES ====================
    private void cadastrarMateriaPrima() {
        MateriaPrima mp = montarMateriaPrimaDosCampos(null);
        if (mp == null) return;

        try {
            mpDAO.inserir(mp);
            JOptionPane.showMessageDialog(this,
                    "Matéria-prima cadastrada com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarMateriaPrima() {
        int linha = jTableMateriaPrima.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma matéria-prima na tabela!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) jTableMateriaPrima.getValueAt(linha, 0);
        MateriaPrima mp = montarMateriaPrimaDosCampos(id);
        if (mp == null) return;

        try {
            mpDAO.atualizar(mp);
            JOptionPane.showMessageDialog(this,
                    "Matéria-prima atualizada com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirMateriaPrima() {
        int linha = jTableMateriaPrima.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma matéria-prima na tabela para excluir.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) jTableMateriaPrima.getValueAt(linha, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirma exclusão do ID " + id + "?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            mpDAO.excluir(id);
            JOptionPane.showMessageDialog(this,
                    "Matéria-prima excluída com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // main só para testes isolados (o Principal já abre essa tela também)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MateriaPrimaInterface().setVisible(true);
        });
    }
}
