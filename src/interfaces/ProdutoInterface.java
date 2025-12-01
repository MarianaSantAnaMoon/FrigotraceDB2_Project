package interfaces;

import frigotraceDB2.dao.ProdutoDAO;
import frigotraceDB2.modelo.Produto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProdutoInterface extends JFrame {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Campos (SEM ID produto na tela)
    private JTextField jTextFieldTipoProduto;
    private JTextField jTextFieldLote;
    private JTextField jTextFieldPreco;
    private JTextField jTextFieldPeso;
    private JTextField jTextFieldDataProducao;
    private JTextField jTextFieldDataValidade;

    // Botões
    private JButton jButtonCadastrar;
    private JButton jButtonAtualizar;
    private JButton jButtonExcluir;
    private JButton jButtonLimpar;

    // Tabela
    private JTable jTableProduto;

    public ProdutoInterface() {
        setTitle("Produto");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        sdf.setLenient(false);

        initComponents();
        configurarRestricoes();
        carregarTabelaProdutos();
    }

    private void initComponents() {
        Color fundoLilaz = new Color(240, 240, 255);
        Color botaoLilaz = new Color(204, 204, 255);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(fundoLilaz);

        // Título
        JLabel titulo = new JLabel("PRODUTO", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(36f));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(fundoLilaz);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel jLabelTipoProduto = new JLabel("Tipo de Produto:");
        JLabel jLabelLote = new JLabel("Lote:");
        JLabel jLabelPreco = new JLabel("Preço (R$):");
        JLabel jLabelPeso = new JLabel("Peso (kg):");
        JLabel jLabelDataProducao = new JLabel("Data Produção (yyyy-MM-dd):");
        JLabel jLabelDataValidade = new JLabel("Data Validade (yyyy-MM-dd):");

        jTextFieldTipoProduto = new JTextField(20);
        jTextFieldLote = new JTextField(15);
        jTextFieldPreco = new JTextField(10);
        jTextFieldPeso = new JTextField(10);
        jTextFieldDataProducao = new JTextField(10);
        jTextFieldDataValidade = new JTextField(10);

        // Linha 0 ? Tipo de Produto
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(jLabelTipoProduto, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        painelCampos.add(jTextFieldTipoProduto, gbc);
        gbc.gridwidth = 1;

        // Linha 1 ? Lote
        gbc.gridx = 0; gbc.gridy = 1;
        painelCampos.add(jLabelLote, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldLote, gbc);

        // Linha 2 ? Preço / Peso
        gbc.gridx = 0; gbc.gridy = 2;
        painelCampos.add(jLabelPreco, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldPreco, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelPeso, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldPeso, gbc);

        // Linha 3 ? Data Produção / Data Validade
        gbc.gridx = 0; gbc.gridy = 3;
        painelCampos.add(jLabelDataProducao, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldDataProducao, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelDataValidade, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldDataValidade, gbc);

        // Tabela
        jTableProduto = new JTable();
        JScrollPane scroll = new JScrollPane(jTableProduto);

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

        // Eventos de botões
        jButtonCadastrar.addActionListener(e -> cadastrarProduto());
        jButtonAtualizar.addActionListener(e -> atualizarProduto());
        jButtonExcluir.addActionListener(e -> excluirProduto());
        jButtonLimpar.addActionListener(e -> limparCampos());

        // Seleção na tabela ? preenche campos
        jTableProduto.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    // ==================== RESTRIÇÕES DE CAMPOS ====================
    private void configurarRestricoes() {
        // Tipo de produto: não permite dígitos
        jTextFieldTipoProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (Character.isDigit(c)) {
                    evt.consume();
                }
            }
        });

        // Lote: tudo em maiúsculo, limita tamanho (por exemplo 20 chars)
        jTextFieldLote.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String texto = jTextFieldLote.getText().toUpperCase();
                if (texto.length() > 20) {
                    texto = texto.substring(0, 20);
                }
                jTextFieldLote.setText(texto);
            }
        });

        // Preço: só dígitos, vírgula/ponto, 1 separador decimal
        jTextFieldPreco.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                if (!Character.isDigit(c) && c != ',' && c != '.') {
                    evt.consume();
                    return;
                }

                String texto = jTextFieldPreco.getText();
                if ((c == ',' || c == '.') &&
                        (texto.contains(",") || texto.contains("."))) {
                    evt.consume();
                }
            }
        });

        // Peso: só dígitos e separador decimal, sem sinal negativo
        jTextFieldPeso.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                if (!Character.isDigit(c) && c != ',' && c != '.') {
                    evt.consume();
                    return;
                }

                String texto = jTextFieldPeso.getText();
                if ((c == ',' || c == '.') &&
                        (texto.contains(",") || texto.contains("."))) {
                    evt.consume();
                }
            }
        });
    }

    // ==================== TABELA ====================
    private void carregarTabelaProdutos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
            "ID Produto", "Tipo de Produto", "Lote",
            "Preço (R$)", "Peso (kg)", "Data Produção", "Data Validade"
        });

        try {
            List<Produto> lista = produtoDAO.buscarTodos(); // ajuste se seu DAO usar outro nome

            for (Produto p : lista) {
                String dataProd = "";
                String dataVal = "";
                Date dtProd = p.getDataProducao();
                Date dtVal = p.getDataValidade();
                if (dtProd != null) dataProd = sdf.format(dtProd);
                if (dtVal != null) dataVal = sdf.format(dtVal);

                modelo.addRow(new Object[]{
                    p.getIdProduto(),
                    p.getTipoDeProduto(),
                    p.getLote(),
                    p.getPreco(),
                    p.getPeso(),
                    dataProd,
                    dataVal
                });
            }

            jTableProduto.setModel(modelo);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposDaTabela() {
        int linha = jTableProduto.getSelectedRow();
        if (linha == -1) {
            return;
        }

        Object tipo = jTableProduto.getValueAt(linha, 1);
        Object lote = jTableProduto.getValueAt(linha, 2);
        Object preco = jTableProduto.getValueAt(linha, 3);
        Object peso = jTableProduto.getValueAt(linha, 4);
        Object dataProd = jTableProduto.getValueAt(linha, 5);
        Object dataVal = jTableProduto.getValueAt(linha, 6);

        jTextFieldTipoProduto.setText(tipo != null ? tipo.toString() : "");
        jTextFieldLote.setText(lote != null ? lote.toString() : "");
        jTextFieldPreco.setText(preco != null ? preco.toString() : "");
        jTextFieldPeso.setText(peso != null ? peso.toString() : "");
        jTextFieldDataProducao.setText(dataProd != null ? dataProd.toString() : "");
        jTextFieldDataValidade.setText(dataVal != null ? dataVal.toString() : "");
    }

    private void limparCampos() {
        jTextFieldTipoProduto.setText("");
        jTextFieldLote.setText("");
        jTextFieldPreco.setText("");
        jTextFieldPeso.setText("");
        jTextFieldDataProducao.setText("");
        jTextFieldDataValidade.setText("");
        jTableProduto.clearSelection();
    }

    // ==================== MONTAGEM DO OBJETO ====================
    private Produto montarProdutoDosCampos(Integer id) {
        try {
            String tipo = jTextFieldTipoProduto.getText().trim();
            String lote = jTextFieldLote.getText().trim();
            String precoStr = jTextFieldPreco.getText().trim();
            String pesoStr = jTextFieldPeso.getText().trim();
            String dataProdStr = jTextFieldDataProducao.getText().trim();
            String dataValStr = jTextFieldDataValidade.getText().trim();

            if (tipo.isEmpty() || lote.isEmpty()
                    || precoStr.isEmpty() || pesoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Preencha Tipo, Lote, Preço e Peso.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            // Preço
            BigDecimal preco;
            try {
                String normalizado = precoStr.replace(".", "").replace(",", ".");
                preco = new BigDecimal(normalizado);
                if (preco.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "O preço deve ser maior que zero.",
                            "Validação",
                            JOptionPane.WARNING_MESSAGE);
                    return null;
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this,
                        "Preço inválido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Peso
            Float peso;
            try {
                String normalizado = pesoStr.replace(",", ".");
                peso = Float.parseFloat(normalizado);
                if (peso <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "O peso deve ser maior que zero.",
                            "Validação",
                            JOptionPane.WARNING_MESSAGE);
                    return null;
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this,
                        "Peso inválido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Datas (opcionais, mas se preencher, precisa estar correto)
            Date dataProd = null;
            Date dataVal = null;

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

            // Regra: se as duas datas estiverem preenchidas, validade >= produção
            if (dataProd != null && dataVal != null && dataVal.before(dataProd)) {
                JOptionPane.showMessageDialog(this,
                        "A data de validade não pode ser anterior à data de produção.",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            Produto p = new Produto();
            if (id != null) {
                p.setIdProduto(id);
            }
            p.setTipoDeProduto(tipo);
            p.setLote(lote);
            p.setPreco(preco);
            p.setPeso(peso);
            p.setDataProducao(dataProd);
            p.setDataValidade(dataVal);

            return p;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro nos campos: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ==================== BOTÕES ====================
    private void cadastrarProduto() {
        Produto p = montarProdutoDosCampos(null);
        if (p == null) return;

        try {
            produtoDAO.inserir(p); // se no seu DAO estiver "salvar", troque aqui
            JOptionPane.showMessageDialog(this,
                    "Produto cadastrado com sucesso!");
            carregarTabelaProdutos();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProduto() {
        int linha = jTableProduto.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um produto na tabela!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idObj = jTableProduto.getValueAt(linha, 0);
        Integer id = (idObj instanceof Integer)
                ? (Integer) idObj
                : Integer.parseInt(idObj.toString());

        Produto p = montarProdutoDosCampos(id);
        if (p == null) return;

        try {
            produtoDAO.atualizar(p);
            JOptionPane.showMessageDialog(this,
                    "Produto atualizado com sucesso!");
            carregarTabelaProdutos();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        int linha = jTableProduto.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um produto na tabela!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idObj = jTableProduto.getValueAt(linha, 0);
        Integer id = (idObj instanceof Integer)
                ? (Integer) idObj
                : Integer.parseInt(idObj.toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirma exclusão do produto ID " + id + "?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            produtoDAO.excluir(id);
            JOptionPane.showMessageDialog(this,
                    "Produto excluído com sucesso!");
            carregarTabelaProdutos();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProdutoInterface().setVisible(true);
        });
    }
}
