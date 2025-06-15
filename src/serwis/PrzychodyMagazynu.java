package serwis;

import BazaDanych.DatabaseConnector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Klasa do zarządzania przychodami magazynu
public class PrzychodyMagazynu {

    // Przechowuje dane o przychodzie w danym miesiącu
    public static class Przychod {
        private final String miesiac; // Format YYYY-MM
        private final double kwota;   // Suma przychodów

        public Przychod(String miesiac, double kwota) {
            this.miesiac = miesiac;
            this.kwota = kwota;
        }

        // Gettery
        public String getMiesiac() { return miesiac; }
        public double getKwota() { return kwota; }
    }

    // Pobiera przychody z ostatnich 12 miesięcy z bazy
    public static List<Przychod> pobierzPrzychody() {
        List<Przychod> przychody = new ArrayList<>();

        // Zapytanie SQL grupujące przychody po miesiącach
        String sql = "SELECT DATE_FORMAT(data,'%Y-%m') AS miesiac, SUM(kwota) AS suma " +
                "FROM transakcje GROUP BY miesiac ORDER BY miesiac DESC LIMIT 12";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Dodaje wyniki do listy
            while (rs.next()) {
                przychody.add(new Przychod(
                        rs.getString("miesiac"),
                        rs.getDouble("suma")
                ));
            }
        } catch (SQLException ex) {
            // Komunikat o błędzie
            JOptionPane.showMessageDialog(null,
                    "Błąd pobierania przychodów:\n" + ex.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return przychody;
    }

    // Wyświetla przychody w oknie dialogowym z tabelą
    public static void pokazPrzychody() {
        List<Przychod> lista = pobierzPrzychody();

        // Sprawdza czy są dane
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Brak danych o przychodach",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Przygotowuje tabelę
        String[] kolumny = {"Miesiąc", "Przychód [PLN]"};
        DefaultTableModel model = new DefaultTableModel(kolumny, 0);

        // Wypełnia tabelę danymi
        for (Przychod p : lista) {
            model.addRow(new Object[]{p.getMiesiac(), p.getKwota()});
        }

        // Wyświetla tabelę
        JTable tabela = new JTable(model);
        JOptionPane.showMessageDialog(null,
                new JScrollPane(tabela),
                "Przychody magazynu (12 miesięcy)",
                JOptionPane.INFORMATION_MESSAGE);
    }
}