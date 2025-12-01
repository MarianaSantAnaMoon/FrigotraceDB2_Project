package interfaces;

import frigotraceDB2.dao.TransporteInDAO;
import frigotraceDB2.modelo.TransporteIn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TransporteInInterface extends JFrame {

    private final TransporteInDAO transporteInDAO = new TransporteInDAO();
    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

    // Campos (sem ID na tela)
    private JTextField jTextFieldPlaca;
    private JTextField jTextFieldTemperatura;
    private JTextField jTextFieldDataRecebida;
    private JTextField jTextFieldIdMateriaPrima;

    // Botões
    private JButton jButtonCadastrar;
    private JButton jButtonAtualizar;
    private JButton jButtonExcluir;
    private JButton jButtonLimpar;

    // Tabela
    private JTable jTableTransporteIn;

    public TransporteInInterface() {
        setTitle("Transporte In");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        initComponents();
        configurarRestricoes();
        carregarTabela();
    }

    private void initComponents() {
        Color fundoLilaz = new Color(240, 240, 255);
        Color botaoLilaz = new Color(204, 204, 255);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(fundoLilaz);

        JLabel titulo = new JLabel("TRANSPORTE IN", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(36f));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(fundoLilaz);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel jLabelPlaca = new JLabel("Veículo (Placa):");
        JLabel jLabelTemperatura = new JLabel("Temperatura (°C):");
        JLabel jLabelDataRecebida = new JLabel("Data Recebida (yyyy-MM-dd):");
        JLabel jLabelIdMateriaPrima = new JLabel("ID Matéria-Prima:");

        jTextFieldPlaca = new JTextField(10);
        jTextFieldTemperatura = new JTextField(8);
        jTextFieldDataRecebida = new JTextField(10);
        jTextFieldIdMateriaPrima = new JTextField(6);

        // Linha 0 ? Placa / Temperatura
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(jLabelPlaca, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldPlaca, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelTemperatura, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldTemperatura, gbc);

        // Linha 1 ? Data / ID Matéria-Prima
        gbc.gridx = 0; gbc.gridy = 1;
        painelCampos.add(jLabelDataRecebida, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldDataRecebida, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelIdMateriaPrima, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldIdMateriaPrima, gbc);

        // Tabela
        jTableTransporteIn = new JTable();
        JScrollPane scroll = new JScrollPane(jTableTransporteIn);

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
        jButtonCadastrar.addActionListener(e -> cadastrar());
        jButtonAtualizar.addActionListener(e -> atualizar());
        jButtonExcluir.addActionListener(e -> excluir());
        jButtonLimpar.addActionListener(e -> limparCampos());

        // Seleção da tabela ? preenche campos
        jTableTransporteIn.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    // ==================== RESTRIÇÕES ====================
    private void configurarRestricoes() {
        // Placa ? maiúscula, máx 8, letras/números/hífen
        jTextFieldPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String texto = jTextFieldPlaca.getText().toUpperCase();
                if (texto.length() > 8) texto = texto.substring(0, 8);
                // remove caracteres inválidos
                texto = texto.replaceAll("[^A-Z0-9-]", "");
                jTextFieldPlaca.setText(texto);
            }
        });

        // Temperatura ? dígitos, vírgula/ponto, 1 sinal negativo
        jTextFieldTemperatura.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                String texto = jTextFieldTemperatura.getText();

                if (c == '-' && texto.isEmpty()) return; // permite sinal no começo

                if (!Character.isDigit(c) && c != ',' && c != '.') {
                    evt.consume();
                    return;
                }

                if ((c == ',' || c == '.') &&
                    (texto.contains(",") || texto.contains("."))) {
                    evt.consume();
                }
            }
        });

        // ID Matéria-Prima ? só números
        jTextFieldIdMateriaPrima.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;
                if (!Character.isDigit(c)) evt.consume();
            }
        });
    }

    // ==================== TABELA ====================
    private void carregarTabela() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
            "ID", "Placa", "Temperatura (°C)", "Data Recebida", "ID Matéria-Prima"
        });

        try {
            List<TransporteIn> lista = transporteInDAO.listar(); // seu DAO tem public List<TransporteIn> listar()
            for (TransporteIn t : lista) {
                modelo.addRow(new Object[]{
                    t.getIdTransporteIn(),
                    t.getVeiculoPlaca(),
                    t.getTemperaturaTransporte(),
                    t.getDataRecebida(),
                    t.getIdMateriaPrima()
                });
            }
            jTableTransporteIn.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar transportes In: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposDaTabela() {
        int linha = jTableTransporteIn.getSelectedRow();
        if (linha == -1) return;

        jTextFieldPlaca.setText(jTableTransporteIn.getValueAt(linha, 1).toString());
        jTextFieldTemperatura.setText(jTableTransporteIn.getValueAt(linha, 2).toString());
        jTextFieldDataRecebida.setText(jTableTransporteIn.getValueAt(linha, 3).toString());
        jTextFieldIdMateriaPrima.setText(jTableTransporteIn.getValueAt(linha, 4).toString());
    }

    private void limparCampos() {
        jTextFieldPlaca.setText("");
        jTextFieldTemperatura.setText("");
        jTextFieldDataRecebida.setText("");
        jTextFieldIdMateriaPrima.setText("");
        jTableTransporteIn.clearSelection();
    }

    // ==================== MONTAGEM DO OBJETO ====================
    private TransporteIn montarDosCampos(Integer id) {
        try {
            String placa = jTextFieldPlaca.getText().trim();
            String tempStr = jTextFieldTemperatura.getText().trim();
            String dataStr = jTextFieldDataRecebida.getText().trim();
            String idMateriaStr = jTextFieldIdMateriaPrima.getText().trim();

            if (placa.isEmpty() || tempStr.isEmpty() ||
                dataStr.isEmpty() || idMateriaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos.",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            // Temperatura
            float temp;
            try {
                String norm = tempStr.replace(",", ".");
                temp = Float.parseFloat(norm);
                if (temp < -60 || temp > 60) {
                    JOptionPane.showMessageDialog(this,
                            "Temperatura deve estar entre -60 e 60 °C.",
                            "Validação",
                            JOptionPane.WARNING_MESSAGE);
                    return null;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Temperatura inválida.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Data
            Date data;
            try {
                LocalDate ld = LocalDate.parse(dataStr, fmt);
                data = Date.valueOf(ld);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Data inválida. Use o formato yyyy-MM-dd.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // ID Matéria-Prima
            int idMateria;
            try {
                idMateria = Integer.parseInt(idMateriaStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID Matéria-Prima inválido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            TransporteIn t = new TransporteIn();
            if (id != null) t.setIdTransporteIn(id);
            t.setVeiculoPlaca(placa);
            t.setTemperaturaTransporte(temp);
            t.setDataRecebida(data);
            t.setIdMateriaPrima(idMateria);

            return t;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro nos campos: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ==================== BOTÕES ====================
    private void cadastrar() {
        TransporteIn t = montarDosCampos(null);
        if (t == null) return;

        try {
            transporteInDAO.inserir(t);
            JOptionPane.showMessageDialog(this,
                    "Transporte In cadastrado com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizar() {
        int linha = jTableTransporteIn.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um registro na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(jTableTransporteIn.getValueAt(linha, 0).toString());
        TransporteIn t = montarDosCampos(id);
        if (t == null) return;

        try {
            transporteInDAO.atualizar(t);
            JOptionPane.showMessageDialog(this,
                    "Transporte In atualizado com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int linha = jTableTransporteIn.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um registro na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(jTableTransporteIn.getValueAt(linha, 0).toString());

        int resp = JOptionPane.showConfirmDialog(this,
                "Confirma exclusão do transporte In ID " + id + "?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (resp != JOptionPane.YES_OPTION) return;

        try {
            transporteInDAO.excluir(id);
            JOptionPane.showMessageDialog(this,
                    "Transporte In excluído com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // main opcional para testar isolado
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransporteInInterface().setVisible(true));
    }
}
