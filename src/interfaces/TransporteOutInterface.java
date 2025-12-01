package interfaces;

import frigotraceDB2.dao.TransporteOutDAO;
import frigotraceDB2.modelo.TransporteOut;

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

public class TransporteOutInterface extends JFrame {

    private final TransporteOutDAO transporteOutDAO = new TransporteOutDAO();
    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;

    private JTextField jTextFieldPlaca;
    private JTextField jTextFieldTemperatura;
    private JTextField jTextFieldDataSaida;
    private JTextField jTextFieldIdVenda;

    private JButton jButtonCadastrar;
    private JButton jButtonAtualizar;
    private JButton jButtonExcluir;
    private JButton jButtonLimpar;

    private JTable jTableTransporteOut;

    public TransporteOutInterface() {
        setTitle("Transporte Out");
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

        JLabel titulo = new JLabel("TRANSPORTE OUT", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(36f));
        painelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBackground(fundoLilaz);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel jLabelPlaca = new JLabel("Veículo (Placa):");
        JLabel jLabelTemperatura = new JLabel("Temperatura (°C):");
        JLabel jLabelDataSaida = new JLabel("Data Saída (yyyy-MM-dd):");
        JLabel jLabelIdVenda = new JLabel("ID Venda:");

        jTextFieldPlaca = new JTextField(10);
        jTextFieldTemperatura = new JTextField(8);
        jTextFieldDataSaida = new JTextField(10);
        jTextFieldIdVenda = new JTextField(6);

        // Linha 0
        gbc.gridx = 0; gbc.gridy = 0;
        painelCampos.add(jLabelPlaca, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldPlaca, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelTemperatura, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldTemperatura, gbc);

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 1;
        painelCampos.add(jLabelDataSaida, gbc);
        gbc.gridx = 1;
        painelCampos.add(jTextFieldDataSaida, gbc);

        gbc.gridx = 2;
        painelCampos.add(jLabelIdVenda, gbc);
        gbc.gridx = 3;
        painelCampos.add(jTextFieldIdVenda, gbc);

        // Tabela
        jTableTransporteOut = new JTable();
        JScrollPane scroll = new JScrollPane(jTableTransporteOut);

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
        jButtonCadastrar.addActionListener(e -> cadastrar());
        jButtonAtualizar.addActionListener(e -> atualizar());
        jButtonExcluir.addActionListener(e -> excluir());
        jButtonLimpar.addActionListener(e -> limparCampos());

        jTableTransporteOut.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCamposDaTabela();
        });
    }

    private void configurarRestricoes() {
        // Placa
        jTextFieldPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String texto = jTextFieldPlaca.getText().toUpperCase();
                if (texto.length() > 8) texto = texto.substring(0, 8);
                texto = texto.replaceAll("[^A-Z0-9-]", "");
                jTextFieldPlaca.setText(texto);
            }
        });

        // Temperatura
        jTextFieldTemperatura.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) return;

                String texto = jTextFieldTemperatura.getText();
                if (c == '-' && texto.isEmpty()) return;

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

        // ID Venda
        jTextFieldIdVenda.addKeyListener(new java.awt.event.KeyAdapter() {
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
            "ID", "Placa", "Temperatura (°C)", "Data Saída", "ID Venda"
        });

        try {
            List<TransporteOut> lista = transporteOutDAO.listar();
            for (TransporteOut t : lista) {
                modelo.addRow(new Object[]{
                    t.getIdTransporteOut(),
                    t.getVeiculoPlaca(),
                    t.getTemperaturaTransporte(),
                    t.getDataSaida(),
                    t.getIdVenda()
                });
            }
            jTableTransporteOut.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar transportes Out: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposDaTabela() {
        int linha = jTableTransporteOut.getSelectedRow();
        if (linha == -1) return;

        jTextFieldPlaca.setText(jTableTransporteOut.getValueAt(linha, 1).toString());
        jTextFieldTemperatura.setText(jTableTransporteOut.getValueAt(linha, 2).toString());
        jTextFieldDataSaida.setText(jTableTransporteOut.getValueAt(linha, 3).toString());
        jTextFieldIdVenda.setText(jTableTransporteOut.getValueAt(linha, 4).toString());
    }

    private void limparCampos() {
        jTextFieldPlaca.setText("");
        jTextFieldTemperatura.setText("");
        jTextFieldDataSaida.setText("");
        jTextFieldIdVenda.setText("");
        jTableTransporteOut.clearSelection();
    }

    // ==================== MONTAGEM DO OBJETO ====================
    private TransporteOut montarDosCampos(Integer id) {
        try {
            String placa = jTextFieldPlaca.getText().trim();
            String tempStr = jTextFieldTemperatura.getText().trim();
            String dataStr = jTextFieldDataSaida.getText().trim();
            String idVendaStr = jTextFieldIdVenda.getText().trim();

            if (placa.isEmpty() || tempStr.isEmpty() ||
                dataStr.isEmpty() || idVendaStr.isEmpty()) {
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
            Date dataSaida;
            try {
                LocalDate ld = LocalDate.parse(dataStr, fmt);
                dataSaida = Date.valueOf(ld);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Data inválida. Use o formato yyyy-MM-dd.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // ID Venda
            int idVenda;
            try {
                idVenda = Integer.parseInt(idVendaStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ID Venda inválido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            TransporteOut t = new TransporteOut();
            if (id != null) t.setIdTransporteOut(id);
            t.setVeiculoPlaca(placa);
            t.setTemperaturaTransporte(temp);
            t.setDataSaida(dataSaida);
            t.setIdVenda(idVenda);

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
        TransporteOut t = montarDosCampos(null);
        if (t == null) return;

        try {
            transporteOutDAO.inserir(t); // se retorno for boolean, pode ignorar
            JOptionPane.showMessageDialog(this,
                    "Transporte Out cadastrado com sucesso!");
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
        int linha = jTableTransporteOut.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um registro na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(jTableTransporteOut.getValueAt(linha, 0).toString());
        TransporteOut t = montarDosCampos(id);
        if (t == null) return;

        try {
            transporteOutDAO.atualizar(t);
            JOptionPane.showMessageDialog(this,
                    "Transporte Out atualizado com sucesso!");
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
        int linha = jTableTransporteOut.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um registro na tabela.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(jTableTransporteOut.getValueAt(linha, 0).toString());

        int resp = JOptionPane.showConfirmDialog(this,
                "Confirma exclusão do transporte Out ID " + id + "?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (resp != JOptionPane.YES_OPTION) return;

        try {
            transporteOutDAO.excluir(id);
            JOptionPane.showMessageDialog(this,
                    "Transporte Out excluído com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransporteOutInterface().setVisible(true));
    }
}
