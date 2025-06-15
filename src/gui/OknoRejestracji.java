package gui;

import BazaDanych.DatabaseConnector;
import narzedzia.Walidacja;
import styl.WygladPrzyciskow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// Okno rejestracji nowego użytkownika
public class OknoRejestracji extends JFrame {
    // Pola formularza rejestracyjnego
    private final JTextField txtLogin = new JTextField(20);
    private final JTextField txtImie = new JTextField(20);
    private final JTextField txtNazwisko = new JTextField(20);
    private final JTextField txtNrTel = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JPasswordField txtHaslo = new JPasswordField(20);
    private final JPasswordField txtHaslo2 = new JPasswordField(20);
    private final JButton btnRejestruj = new JButton("Zarejestruj");
    private final JButton btnPowrot = new JButton("Powrót");

    public OknoRejestracji() {
        super("Rejestracja");
        initComponents(); // Inicjalizacja interfejsu
        setSize(800, 600);
        setLocationRelativeTo(null); // Wyśrodkowanie okna
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Zamknięcie tylko tego okna
        setResizable(false);
        setVisible(true);
    }

    // Inicjalizacja komponentów GUI
    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel nagłówka z logo
        JPanel banner = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon ikona = new ImageIcon(getClass().getResource("/figures/logo.png"));
        JLabel lblIcon = new JLabel(ikona);
        JLabel lblTytul = new JLabel("MagazynMK");
        lblTytul.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(banner, BorderLayout.NORTH);
        banner.add(lblIcon);
        banner.add(lblTytul);

        // Panel z formularzem rejestracji
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Stylizacja przycisków
        WygladPrzyciskow.ustawStyl(btnRejestruj);
        WygladPrzyciskow.ustawStyl(btnPowrot);

        // Obsługa klawisza ENTER do nawigacji między polami
        JTextField[] pola = {txtLogin, txtImie, txtNazwisko, txtNrTel, txtEmail, txtHaslo, txtHaslo2};
        for (int i = 0; i < pola.length - 1; i++) {
            int finalI = i;
            pola[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        pola[finalI + 1].requestFocus(); // Przejście do następnego pola
                    }
                }
            });
        }

        // ENTER w ostatnim polu potwierdza rejestrację
        txtHaslo2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onRejestruj(null); // Wywołanie rejestracji
                }
            }
        });

        // Etykiety i pola formularza
        String[] labels = {"Login:", "Imię:", "Nazwisko:", "Nr telefonu:", "E-mail:", "Hasło:", "Powtórz hasło:"};
        JComponent[] fields = {txtLogin, txtImie, txtNazwisko, txtNrTel, txtEmail, txtHaslo, txtHaslo2};

        // Dodanie pól do formularza
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            inputPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            inputPanel.add(fields[i], gbc);
        }

        // Przyciski na dole formularza
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        inputPanel.add(btnRejestruj, gbc);
        gbc.gridx = 1;
        inputPanel.add(btnPowrot, gbc);

        panel.add(inputPanel, BorderLayout.CENTER);
        add(panel);

        // Obsługa zdarzeń przycisków
        btnRejestruj.addActionListener(this::onRejestruj);
        btnPowrot.addActionListener(e -> {
            dispose(); // Zamknięcie okna rejestracji
            new OknoLogowania(); // Otwarcie okna logowania
        });
    }

    // Metoda obsługująca rejestrację
    private void onRejestruj(ActionEvent e) {
        // Pobranie danych z formularza
        String login = txtLogin.getText().trim();
        String imie = txtImie.getText().trim();
        String nazwisko = txtNazwisko.getText().trim();
        String nrTel = txtNrTel.getText().trim();
        String email = txtEmail.getText().trim();
        String haslo = new String(txtHaslo.getPassword());
        String haslo2 = new String(txtHaslo2.getPassword());

        // Walidacja danych
        if (!Walidacja.sprawdzLogin(login) ||
                !Walidacja.sprawdzNrTel(nrTel) ||
                !Walidacja.sprawdzEmail(email)) {
            return; // Przerwanie jeśli walidacja nie powiedzie się
        }

        // Sprawdzenie zgodności haseł
        if (!haslo.equals(haslo2)) {
            JOptionPane.showMessageDialog(this,
                    "Hasła nie są identyczne!",
                    "Błąd rejestracji", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Zapis do bazy danych
        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "INSERT INTO uzytkownicy(login, imie, nazwisko, nr_tel, email, haslo) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, login);
                ps.setString(2, imie);
                ps.setString(3, nazwisko);
                ps.setString(4, nrTel);
                ps.setString(5, email);
                ps.setString(6, haslo);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Rejestracja zakończona pomyślnie!",
                            "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Zamknięcie okna rejestracji
                    new OknoLogowania(); // Powrót do logowania
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd podczas rejestracji: " + ex.getMessage(),
                    "Błąd bazy danych", JOptionPane.ERROR_MESSAGE);
        }
    }
}