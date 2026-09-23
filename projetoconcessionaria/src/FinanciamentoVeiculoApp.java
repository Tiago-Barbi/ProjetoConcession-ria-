import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Locale;


public class FinanciamentoVeiculoApp extends JFrame {


    private static final double TAXA_JUROS = 0.10;


    private JComboBox<String> cbMarca;
    private JTextField txtModelo;
    private JComboBox<Integer> cbAno;
    private JTextField txtValor;
    private JRadioButton rbNovo;
    private JRadioButton rbUsado;
    private ButtonGroup grupoTipo;


    private JPanel painelUsado;
    private JTextField txtQuilometragem;
    private JTextField txtProprietarios;


    private JCheckBox chkPossuiEntrada;
    private JLabel lblEntrada;
    private JTextField txtEntrada;
    private JComboBox<Integer> cbParcelas;


    private JPanel painelResultado;
    private JLabel lblValorFinanciado;
    private JLabel lblValorParcela;
    private JLabel lblTotalPagar;


    private JPanel painelPrincipal;

    public FinanciamentoVeiculoApp() {
        super("Simulador de Financiamento de Veículos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(criarCabecalho(), BorderLayout.NORTH);

        painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        painelPrincipal.add(criarPainelVeiculo());
        painelUsado = criarPainelVeiculoUsado();
        painelUsado.setVisible(false);
        painelPrincipal.add(painelUsado);
        painelPrincipal.add(criarPainelFinanciamento());
        painelPrincipal.add(criarPainelBotoes());
        painelResultado = criarPainelResultado();
        painelResultado.setVisible(false);
        painelPrincipal.add(painelResultado);

        JScrollPane scrollPane = new JScrollPane(painelPrincipal);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setMinimumSize(new Dimension(480, 600));
        pack();
        setLocationRelativeTo(null);
    }


    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cabecalho.setBackground(new Color(25, 55, 109));
        JLabel titulo = new JLabel("Simulador de Financiamento de Veículos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new EmptyBorder(12, 12, 12, 12));
        cabecalho.add(titulo);
        return cabecalho;
    }

    private JPanel criarPainelVeiculo() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(criarBorda("Dados do Veículo"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Marca (lista de seleção única)
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        painel.add(new JLabel("Marca:"), gbc);
        cbMarca = new JComboBox<>(new String[]{
                "Selecione", "Chevrolet", "Fiat", "Ford", "Honda", "Hyundai",
                "Jeep", "Nissan", "Renault", "Toyota", "Volkswagen",
                "BMW", "Mercedes", "Peugeot", "Citroën"
        });
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(cbMarca, gbc);
        row++;

        // Modelo (texto livre)
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        painel.add(new JLabel("Modelo:"), gbc);
        txtModelo = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtModelo, gbc);
        row++;

        // Ano (lista de seleção única, 2026 -> 2000)
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        painel.add(new JLabel("Ano:"), gbc);
        Integer[] anos = new Integer[27]; // 2026 até 2000 = 27 valores
        int ano = 2026;
        for (int i = 0; i < anos.length; i++) {
            anos[i] = ano--;
        }
        cbAno = new JComboBox<>(anos);
        cbAno.setSelectedIndex(-1);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(cbAno, gbc);
        row++;

        // Valor (texto livre)
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        painel.add(new JLabel("Valor do carro (R$):"), gbc);
        txtValor = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtValor, gbc);
        row++;

        // Tipo: Novo / Usado
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        painel.add(new JLabel("Tipo do veículo:"), gbc);

        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rbNovo = new JRadioButton("Novo", true);
        rbUsado = new JRadioButton("Usado");
        grupoTipo = new ButtonGroup();
        grupoTipo.add(rbNovo);
        grupoTipo.add(rbUsado);
        painelTipo.add(rbNovo);
        painelTipo.add(rbUsado);

        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(painelTipo, gbc);

        rbNovo.addActionListener(e -> alternarPainelUsado());
        rbUsado.addActionListener(e -> alternarPainelUsado());

        return painel;
    }

    private JPanel criarPainelVeiculoUsado() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(criarBorda("Dados do Veículo Usado"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painel.add(new JLabel("Quilometragem (km):"), gbc);
        txtQuilometragem = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtQuilometragem, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Nº de proprietários:"), gbc);
        txtProprietarios = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtProprietarios, gbc);

        return painel;
    }

    private JPanel criarPainelFinanciamento() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(criarBorda("Condições de Financiamento"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        painel.add(new JLabel("Possui entrada?"), gbc);
        chkPossuiEntrada = new JCheckBox("Sim");
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(chkPossuiEntrada, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        lblEntrada = new JLabel("Valor de entrada (R$):");
        painel.add(lblEntrada, gbc);
        txtEntrada = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(txtEntrada, gbc);
        lblEntrada.setVisible(false);
        txtEntrada.setVisible(false);
        row++;

        chkPossuiEntrada.addActionListener(e -> alternarCampoEntrada());

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        painel.add(new JLabel("Quantidade de parcelas:"), gbc);
        cbParcelas = new JComboBox<>(new Integer[]{12, 24, 36, 48, 60});
        cbParcelas.setSelectedIndex(-1);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(cbParcelas, gbc);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnLimpar = new JButton("Limpar");
        JButton btnCalcular = new JButton("Calcular");
        btnCalcular.setFont(btnCalcular.getFont().deriveFont(Font.BOLD));

        btnLimpar.addActionListener(this::onLimpar);
        btnCalcular.addActionListener(this::onCalcular);

        painel.add(btnLimpar);
        painel.add(btnCalcular);
        return painel;
    }

    private JPanel criarPainelResultado() {
        JPanel painel = new JPanel(new GridLayout(3, 1, 5, 5));
        painel.setBorder(criarBorda("Resultado da Simulação"));

        Font fonteResultado = new Font("SansSerif", Font.PLAIN, 14);

        lblValorFinanciado = new JLabel("Valor financiado: -");
        lblValorParcela = new JLabel("Valor da parcela: -");
        lblTotalPagar = new JLabel("Total a pagar: -");

        lblValorFinanciado.setFont(fonteResultado);
        lblValorParcela.setFont(fonteResultado);
        lblTotalPagar.setFont(fonteResultado.deriveFont(Font.BOLD));

        painel.add(lblValorFinanciado);
        painel.add(lblValorParcela);
        painel.add(lblTotalPagar);

        return painel;
    }

    private TitledBorder criarBorda(String titulo) {
        TitledBorder borda = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), titulo);
        borda.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        return borda;
    }

    private void alternarPainelUsado() {
        painelUsado.setVisible(rbUsado.isSelected());
        revalidarJanela();
    }

    private void alternarCampoEntrada() {
        boolean possuiEntrada = chkPossuiEntrada.isSelected();
        lblEntrada.setVisible(possuiEntrada);
        txtEntrada.setVisible(possuiEntrada);
        if (!possuiEntrada) {
            txtEntrada.setText("");
        }
        revalidarJanela();
    }

    private void revalidarJanela() {
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }


    private void onLimpar(ActionEvent e) {
        cbMarca.setSelectedIndex(0);
        txtModelo.setText("");
        cbAno.setSelectedIndex(-1);
        txtValor.setText("");

        rbNovo.setSelected(true);
        painelUsado.setVisible(false);
        txtQuilometragem.setText("");
        txtProprietarios.setText("");

        chkPossuiEntrada.setSelected(false);
        lblEntrada.setVisible(false);
        txtEntrada.setVisible(false);
        txtEntrada.setText("");
        cbParcelas.setSelectedIndex(-1);

        painelResultado.setVisible(false);
        lblValorFinanciado.setText("Valor financiado: -");
        lblValorParcela.setText("Valor da parcela: -");
        lblTotalPagar.setText("Total a pagar: -");

        revalidarJanela();
    }

    private void onCalcular(ActionEvent e) {
        StringBuilder erros = new StringBuilder();


        if (cbMarca.getSelectedIndex() <= 0) {
            erros.append("- Selecione a marca do veículo.\n");
        }


        if (txtModelo.getText().trim().isEmpty()) {
            erros.append("- Informe o modelo do veículo.\n");
        }


        if (cbAno.getSelectedIndex() == -1) {
            erros.append("- Selecione o ano do veículo.\n");
        }


        double valorVeiculo = 0;
        String txtValorStr = txtValor.getText().trim();
        if (txtValorStr.isEmpty()) {
            erros.append("- Informe o valor do carro.\n");
        } else {
            try {
                valorVeiculo = parseValor(txtValorStr);
                if (valorVeiculo <= 0) {
                    erros.append("- O valor do carro deve ser maior que zero.\n");
                }
            } catch (NumberFormatException ex) {
                erros.append("- O valor do carro deve ser numérico (ex: 55000.00).\n");
            }
        }


        if (rbUsado.isSelected()) {
            String km = txtQuilometragem.getText().trim();
            if (km.isEmpty()) {
                erros.append("- Informe a quilometragem do veículo usado.\n");
            } else {
                try {
                    double kmValor = Double.parseDouble(km.replace(",", "."));
                    if (kmValor < 0) {
                        erros.append("- A quilometragem não pode ser negativa.\n");
                    }
                } catch (NumberFormatException ex) {
                    erros.append("- A quilometragem deve ser numérica.\n");
                }
            }

            String prop = txtProprietarios.getText().trim();
            if (prop.isEmpty()) {
                erros.append("- Informe a quantidade de proprietários.\n");
            } else {
                try {
                    int propValor = Integer.parseInt(prop);
                    if (propValor < 1) {
                        erros.append("- A quantidade de proprietários deve ser no mínimo 1.\n");
                    }
                } catch (NumberFormatException ex) {
                    erros.append("- A quantidade de proprietários deve ser um número inteiro.\n");
                }
            }
        }


        double valorEntrada = 0;
        if (chkPossuiEntrada.isSelected()) {
            String txtEntradaStr = txtEntrada.getText().trim();
            if (txtEntradaStr.isEmpty()) {
                erros.append("- Informe o valor de entrada.\n");
            } else {
                try {
                    valorEntrada = parseValor(txtEntradaStr);
                    if (valorEntrada < 0) {
                        erros.append("- O valor de entrada não pode ser negativo.\n");
                    } else if (valorEntrada >= valorVeiculo && valorVeiculo > 0) {
                        erros.append("- O valor de entrada deve ser menor que o valor do carro.\n");
                    }
                } catch (NumberFormatException ex) {
                    erros.append("- O valor de entrada deve ser numérico (ex: 10000.00).\n");
                }
            }
        }


        if (cbParcelas.getSelectedIndex() == -1) {
            erros.append("- Selecione a quantidade de parcelas.\n");
        }

        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Corrija os seguintes campos:\n\n" + erros,
                    "Dados inválidos", JOptionPane.WARNING_MESSAGE);
            painelResultado.setVisible(false);
            revalidarJanela();
            return;
        }


        int numeroParcelas = (Integer) cbParcelas.getSelectedItem();

        double valorFinanciado = valorVeiculo - valorEntrada;
        double valorTotal = valorFinanciado * (1 + TAXA_JUROS);
        double valorParcela = valorTotal / numeroParcelas;
        double totalAPagar = valorParcela * numeroParcelas;

        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        lblValorFinanciado.setText("Valor financiado: " + moeda.format(valorFinanciado));
        lblValorParcela.setText("Valor da parcela (" + numeroParcelas + "x): " + moeda.format(valorParcela));
        lblTotalPagar.setText("Total a pagar: " + moeda.format(totalAPagar));

        painelResultado.setVisible(true);
        revalidarJanela();
    }


    private double parseValor(String texto) throws NumberFormatException {
        String limpo = texto.replace("R$", "").trim();
        if (limpo.contains(",")) {
            limpo = limpo.replace(".", "").replace(",", ".");
        }
        return Double.parseDouble(limpo);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> {
            FinanciamentoVeiculoApp app = new FinanciamentoVeiculoApp();
            app.setVisible(true);
        });
    }
}