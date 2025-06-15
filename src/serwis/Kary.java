package serwis;

import BazaDanych.DatabaseConnector;

import javax.swing.JOptionPane;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class Kary {

    // Model przechowujący dane o karze
    public static class Kara {
        private final String loginUzytkownika; // login użytkownika
        private final String nazwaProduktu;    // nazwa spóźnionego produktu
        private final long dniOpoznienia;      // liczba dni opóźnienia
        private final double kwota;            // naliczona kwota kary

        // Konstruktor inicjalizujący
        public Kara(String loginUzytkownika, String nazwaProduktu, long dniOpoznienia, double kwota) {
            this.loginUzytkownika = loginUzytkownika;
            this.nazwaProduktu = nazwaProduktu;
            this.dniOpoznienia = dniOpoznienia;
            this.kwota = kwota;
        }

        // Gettery
        public String getLoginUzytkownika() {
            return loginUzytkownika;
        }

        public String getNazwaProduktu() {
            return nazwaProduktu;
        }

        public long getDniOpoznienia() {
            return dniOpoznienia;
        }

        public double getKwota() {
            return kwota;
        }
    }

    // Pobiera kary dla konkretnego użytkownika
    public static List<Kara> pobierzKary(int userId) {
        List<Kara> kary = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Zapytanie SQL
        String sql = "SELECT p.nazwa, p.data_odbioru, p.ilosc, u.login " +
                "FROM produkty p " +
                "JOIN uzytkownicy u ON p.uzytkownik_id = u.id " +
                "WHERE p.uzytkownik_id = ? AND p.data_odbioru < ?";

        // Połączenie z bazą
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(today));
            ResultSet rs = ps.executeQuery();

            // Przetwarzanie wyników
            while (rs.next()) {
                String login = rs.getString("login");
                String nazwa = rs.getString("nazwa");
                LocalDate odb = rs.getDate("data_odbioru").toLocalDate();
                int ilosc = rs.getInt("ilosc");
                long dni = ChronoUnit.DAYS.between(odb, today);
                double kwota = dni * ilosc * 1.0; // 1 zł/dzień/szt
                kary.add(new Kara(login, nazwa, dni, kwota));
            }
        } catch (SQLException e) {
            // Obsługa błędu
            JOptionPane.showMessageDialog(null,
                    "Błąd pobierania kar:\n" + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return kary;
    }

    // Pobiera wszystkie kary
    public static List<Kara> pobierzWszystkieKary() {
        List<Kara> lista = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Zapytanie SQL
        String sql = "SELECT u.login, p.nazwa, p.data_odbioru, p.ilosc " +
                "FROM produkty p " +
                "JOIN uzytkownicy u ON p.uzytkownik_id = u.id " +
                "WHERE p.data_odbioru < ?";

        // Połączenie z bazą
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(today));
            ResultSet rs = ps.executeQuery();

            // Przetwarzanie wyników
            while (rs.next()) {
                String login = rs.getString("login");
                String nazwa = rs.getString("nazwa");
                LocalDate odb = rs.getDate("data_odbioru").toLocalDate();
                int ilosc = rs.getInt("ilosc");
                long dni = ChronoUnit.DAYS.between(odb, today);
                double kwota = dni * ilosc * 1.0; // 1 zł/dzień/szt
                lista.add(new Kara(login, nazwa, dni, kwota));
            }
        } catch (SQLException e) {
            // Obsługa błędu
            JOptionPane.showMessageDialog(null,
                    "Błąd pobierania kar:\n" + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    // Usuwa karę dla produktu
    public static void usunKare(String nazwaProduktu) {
        // Zapytanie SQL
        String sql = "DELETE FROM kary WHERE nazwa_produktu = ?";

        // Połączenie z bazą
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nazwaProduktu);
            int usuniete = ps.executeUpdate();

            // Komunikat o wyniku
            if (usuniete > 0) {
                JOptionPane.showMessageDialog(null,
                        "Kara usunięta",
                        "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Nie znaleziono kary",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            // Obsługa błędu
            JOptionPane.showMessageDialog(null,
                    "Błąd usuwania:\n" + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
