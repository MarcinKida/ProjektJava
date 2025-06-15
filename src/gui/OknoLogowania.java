package gui;

import BazaDanych.DatabaseConnector;
import styl.WygladPrzyciskow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// Okno logowania do systemu magazynowego
public class OknoLogowania extends JFrame {
    // Pola formularza logowania
    private final JTextField txtLogin = new JTextField(20);
    private final JPasswordField txtHaslo = new JPasswordField(20);
    private final JButton btnZaloguj = new JButton("Zaloguj");
    private final JButton btnRejestracja = new JButton("Rejestruj");

    // Konstruktor okna logowania
    public OknoLogowania() {
        super("Logowanie");
        initComponents(); // Inicjalizacja interfejsu
        setSize(800, 600);
        setLocationRelativeTo(null); // Wyśrodkowanie okna
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    // Inicjalizacja komponentów GUI
    private void initComponents() {
        // Główny panel z marginesami
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(new Color(240, 240, 250));

        // Panel banneru z logo
        JPanel banner = new JPanel(new FlowLayout(FlowLayout.CENTER));
        banner.setBackground(new Color(220, 220, 240));

        // Logo i tytuł aplikacji
        ImageIcon ikona = new ImageIcon(getClass().getResource("/figures/logo.png"));
        JLabel lblIcon = new JLabel(ikona);
        JLabel lblTytul = new JLabel("MagazynMK");
        lblTytul.setFont(new Font("Segoe UI", Font.BOLD, 36));

        panel.add(banner, BorderLayout.NORTH);
        banner.add(lblIcon);
        banner.add(lblTytul);

        // Panel z formularzem logowania
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 240, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Stylizacja przycisków
        WygladPrzyciskow.ustawStyl(btnZaloguj);
        WygladPrzyciskow.ustawStyl(btnRejestracja);

        // Ustawienie czcionek dla pól tekstowych
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtHaslo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        // Obsługa klawisza ENTER w polu loginu
        txtLogin.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtHaslo.requestFocus(); // Przejście do pola hasła
                }
            }
        });

        // Obsługa klawisza ENTER w polu hasła
        txtHaslo.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onZaloguj(null); // Próba logowania
                }
            }
        });

        // Układanie komponentów w siatce
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Hasło:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtHaslo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(btnZaloguj, gbc);
        gbc.gridx = 1;
        inputPanel.add(btnRejestracja, gbc);

        panel.add(inputPanel, BorderLayout.CENTER);
        add(panel);

        // Obsługa zdarzeń przycisków
        btnZaloguj.addActionListener(this::onZaloguj);
        btnRejestracja.addActionListener(e -> {
            dispose(); // Zamknięcie okna logowania
            new OknoRejestracji(); // Otwarcie okna rejestracji
        });
    }

    // Metoda obsługująca logowanie
    private void onZaloguj(ActionEvent e) {
        String login = txtLogin.getText().trim();
        String haslo = new String(txtHaslo.getPassword());

        // Zapytanie SQL sprawdzające dane logowania
        String sql = "SELECT id, czy_admin FROM uzytkownicy WHERE login=? AND haslo=?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, haslo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                boolean admin = rs.getBoolean("czy_admin");
                dispose(); // Zamknięcie okna logowania

                // Otwarcie odpowiedniego panelu w zależności od uprawnień
                if (admin) {
                    new PanelAdministratora(userId, login).setVisible(true);
                } else {
                    new PanelUzytkownika(userId, login).setVisible(true);
                }
            } else {
                // Komunikat o błędnych danych
                JOptionPane.showMessageDialog(
                        this,
                        "Niepoprawny login lub hasło.",
                        "Błąd logowania",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            // Komunikat o błędzie połączenia
            JOptionPane.showMessageDialog(
                    this,
                    "Błąd połączenia z bazą:\n" + ex.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}