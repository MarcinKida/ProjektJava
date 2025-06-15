package gui;

import BazaDanych.DatabaseConnector;
import serwis.*;
import styl.WygladPrzyciskow;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;

// Panel administratora systemu magazynowego
public class PanelAdministratora extends JFrame {
    // Pola klasy
    private final int userId;
    private final String login;
    private final JLabel lblWitaj = new JLabel();
    private final JButton btnListaUzytkownikow = new JButton("Lista użytkowników");
    private final JButton btnPrzyznajAdmina = new JButton("Nadaj uprawnienia admina");
    private final JButton btnPrzekroczenieCzasu = new JButton("Przekroczenia czasu");
    private final JButton btnPrzychodyMagazynu = new JButton("Przychody magazynu");
    private final JButton btnWyloguj = new JButton("Wyloguj");

    // Konstruktor panelu administratora
    public PanelAdministratora(int userId, String login) {
        super("Panel administratora");
        this.userId = userId;
        this.login = login;
        // Konfiguracja interfejsu
        lblWitaj.setText("Witaj, " + login + " (admin)");
        lblWitaj.setFont(new Font("Arial", Font.BOLD, 24));
        initComponents();
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        // Panel z przyciskami funkcjonalności
        JPanel btnPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        // Stylizacja przycisków
        WygladPrzyciskow.ustawStyl(btnListaUzytkownikow);
        WygladPrzyciskow.ustawStyl(btnPrzyznajAdmina);
        WygladPrzyciskow.ustawStyl(btnPrzekroczenieCzasu);
        WygladPrzyciskow.ustawStyl(btnPrzychodyMagazynu);
        WygladPrzyciskow.ustawStyl(btnWyloguj);

        // Dodanie przycisków do panelu
        btnPanel.add(btnListaUzytkownikow);
        btnPanel.add(btnPrzyznajAdmina);
        btnPanel.add(btnPrzekroczenieCzasu);
        btnPanel.add(btnPrzychodyMagazynu);
        btnPanel.add(btnWyloguj);

        panel.add(lblWitaj, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.CENTER);
        add(panel);

        // Przypisanie akcji do przycisków
        btnListaUzytkownikow.addActionListener(e -> pokazListeUzytkownikow());
        btnPrzyznajAdmina.addActionListener(e -> przyznajAdmina());
        btnPrzekroczenieCzasu.addActionListener(e -> pokazPrzekroczenieCzasu());
        btnPrzychodyMagazynu.addActionListener(e -> pokazPrzychodyMagazynu());
        btnWyloguj.addActionListener(e -> wylogowanie());
    }

    // Wyświetla listę wszystkich użytkowników i ich produktów
    private void pokazListeUzytkownikow() {
        String sql = "SELECT u.login, u.imie, u.nazwisko, u.nr_tel, u.email, p.nazwa AS produkt, p.ilosc " +
                "FROM uzytkownicy u LEFT JOIN produkty p ON u.id = p.uzytkownik_id";

        String[] cols = {"Login", "Imię", "Nazwisko", "Telefon", "E-mail", "Produkt", "Ilość"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        try (Connection conn = DatabaseConnector.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            // Wypełnianie tabeli danymi
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("login"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("nr_tel"),
                        rs.getString("email"),
                        rs.getString("produkt"),
                        rs.getInt("ilosc")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd ładowania listy użytkowników:\n" + ex.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Okno dialogowe z możliwością usunięcia produktu
        int result = JOptionPane.showConfirmDialog(this,
                new JScrollPane(table),
                "Usuń produkt użytkownika",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && table.getSelectedRow() != -1) {
            String produktDoUsuniecia = (String) table.getValueAt(table.getSelectedRow(), 5);
            Produkty.usunProdukt(produktDoUsuniecia);
            model.removeRow(table.getSelectedRow());
            JOptionPane.showMessageDialog(this,
                    "Produkt został usunięty!",
                    "Sukces", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Nadaje uprawnienia administratora wybranemu użytkownikowi
    private void przyznajAdmina() {
        List<String> listaUzytkownikow = pobierzListeUzytkownikow();
        if (listaUzytkownikow.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Brak użytkowników do nadania uprawnień.",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Wybór użytkownika do awansowania
        String wybranyLogin = (String) JOptionPane.showInputDialog(
                this,
                "Wybierz użytkownika do nadania uprawnień:",
                "Przyznaj administratora",
                JOptionPane.QUESTION_MESSAGE,
                null,
                listaUzytkownikow.toArray(),
                listaUzytkownikow.get(0)
        );

        if (wybranyLogin != null) {
            boolean sukces = UzytkownikSerwis.ustawAdministratora(wybranyLogin);
            if (sukces) {
                JOptionPane.showMessageDialog(this,
                        "Użytkownik " + wybranyLogin + " został administratorem!",
                        "Sukces", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // Pobiera listę użytkowników bez uprawnień admina
    private List<String> pobierzListeUzytkownikow() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT login FROM uzytkownicy WHERE czy_admin = 0";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("login"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Błąd pobierania listy użytkowników:\n" + ex.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    // Wyświetla użytkowników z przekroczonym czasem magazynowania
    private void pokazPrzekroczenieCzasu() {
        List<Kary.Kara> kary = Kary.pobierzWszystkieKary();
        String[] cols = {"Użytkownik", "Produkt", "Dni opóźnienia", "Kwota [PLN]"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        // Wypełnianie tabeli danymi
        for (Kary.Kara k : kary) {
            model.addRow(new Object[]{
                    k.getLoginUzytkownika(),
                    k.getNazwaProduktu(),
                    k.getDniOpoznienia(),
                    k.getKwota()
            });
        }

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Okno dialogowe z możliwością usunięcia kary
        int result = JOptionPane.showConfirmDialog(this,
                new JScrollPane(table),
                "Zaległości użytkowników",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && table.getSelectedRow() != -1) {
            String produktDoUsuniecia = (String) table.getValueAt(table.getSelectedRow(), 1);
            Kary.usunKare(produktDoUsuniecia);
            model.removeRow(table.getSelectedRow());
            JOptionPane.showMessageDialog(this,
                    "Zaległość została usunięta!",
                    "Sukces", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Wyświetla przychody magazynu
    private void pokazPrzychodyMagazynu() {
        PrzychodyMagazynu.pokazPrzychody();
    }

    // Obsługa wylogowania
    private void wylogowanie() {
        Object[] opcje = {"Tak", "Nie"};
        int decyzja = JOptionPane.showOptionDialog(
                this,
                "Czy na pewno chcesz się wylogować?",
                "Wylogowanie",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opcje,
                opcje[0]
        );

        if (decyzja == JOptionPane.YES_OPTION) {
            dispose(); // Zamknięcie panelu
            new MenuGlowne(); // Powrót do menu głównego
        }
    }
}