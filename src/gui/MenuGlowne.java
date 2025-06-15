package gui;

import styl.WygladPrzyciskow;
import javax.swing.*;
import java.awt.*;

// Główne menu aplikacji z przyciskami akcji
public class MenuGlowne extends JFrame {
    // Przyciski menu
    private final JButton btnLogowanie = new JButton("Logowanie");
    private final JButton btnRejestracja = new JButton("Rejestracja");
    private final JButton btnWyjscie = new JButton("Wyjście");

    // Konstruktor głównego menu
    public MenuGlowne() {
        super("Menu Główne");
        initComponents(); // Inicjalizacja komponentów
        setSize(800, 600); // Rozmiar okna
        setLocationRelativeTo(null); // Wyśrodkowanie
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Zamknięcie aplikacji przy wyjściu
        setResizable(false); // Blokada zmiany rozmiaru
        setVisible(true); // Widoczność okna
    }

    // Inicjalizacja komponentów GUI
    private void initComponents() {
        // Główny panel z obramowaniem
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(new Color(240, 240, 250)); // Kolor tła

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

        // Panel z przyciskami menu
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        btnPanel.setBackground(new Color(240, 240, 250));

        // Stylizacja przycisków
        WygladPrzyciskow.ustawStyl(btnLogowanie);
        WygladPrzyciskow.ustawStyl(btnRejestracja);
        WygladPrzyciskow.ustawStyl(btnWyjscie);

        // Dodanie przycisków do panelu
        btnPanel.add(btnLogowanie);
        btnPanel.add(btnRejestracja);
        btnPanel.add(btnWyjscie);
        panel.add(btnPanel, BorderLayout.CENTER);

        add(panel);

        // Obsługa zdarzeń przycisków
        btnLogowanie.addActionListener(e -> {
            dispose(); // Zamknięcie bieżącego okna
            new OknoLogowania(); // Otwarcie okna logowania
        });

        btnRejestracja.addActionListener(e -> {
            dispose();
            new OknoRejestracji(); // Otwarcie okna rejestracji
        });

        btnWyjscie.addActionListener(e -> System.exit(0)); // Wyjście z aplikacji
    }
}