package serwis;

import javax.swing.JOptionPane;
import BazaDanych.DatabaseConnector;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

// Zarządza produktami: typy, nazwy, rezerwacje
public class Produkty {

    // Dostępne typy produktów
    public enum TypProduktu {
        ELEKTRONICZNE("Elektroniczne"),
        SPOŻYWCZE("Spożywcze"),
        ODZIEŻOWE("Odzieżowe"),
        CHEMICZNE("Chemiczne"),
        PRZEMYSŁOWE("Przemysłowe");

        private final String nazwa;

        TypProduktu(String nazwa) {
            this.nazwa = nazwa;
        }

        public String getNazwa() {
            return nazwa;
        }
    }

    // Przechowuje dane produktu
    public static class Produkt {
        private final int id;
        private final TypProduktu typ;
        private final String nazwa;
        private final int ilosc;
        private final LocalDate dataPrzyjecia;
        private final LocalDate dataOdbioru;

        public Produkt(int id, TypProduktu typ, String nazwa,
                       int ilosc, LocalDate dataPrzyj, LocalDate dataOdbior) {
            this.id = id;
            this.typ = typ;
            this.nazwa = nazwa;
            this.ilosc = ilosc;
            this.dataPrzyjecia = dataPrzyj;
            this.dataOdbioru = dataOdbior;
        }

        // Gettery
        public int getId() { return id; }
        public TypProduktu getTyp() { return typ; }
        public String getNazwa() { return nazwa; }
        public int getIlosc() { return ilosc; }
        public LocalDate getDataPrzyjecia() { return dataPrzyjecia; }
        public LocalDate getDataOdbioru() { return dataOdbioru; }
    }

    // Przykładowe nazwy produktów dla typów
    private static final Map<TypProduktu, List<String>> DOSTEPNE = new EnumMap<>(TypProduktu.class);
    static {
        DOSTEPNE.put(TypProduktu.ELEKTRONICZNE, Arrays.asList("Laptop", "Smartfon", "Telewizor"));
        DOSTEPNE.put(TypProduktu.SPOŻYWCZE, Arrays.asList("Jajka", "Mąka", "Cukier"));
        DOSTEPNE.put(TypProduktu.ODZIEŻOWE, Arrays.asList("Koszulka", "Spodnie", "Kurtka"));
        DOSTEPNE.put(TypProduktu.CHEMICZNE, Arrays.asList("Farba", "Rozpuszczalnik", "Płyn czyszczący"));
        DOSTEPNE.put(TypProduktu.PRZEMYSŁOWE, Arrays.asList("Rura", "Śruba", "Łożysko"));
    }

    // Zwraca listę typów produktów
    public static List<TypProduktu> pobierzTypy() {
        return new ArrayList<>(DOSTEPNE.keySet());
    }

    // Zwraca nazwy produktów dla typu
    public static List<String> pobierzNazwy(TypProduktu typ) {
        return DOSTEPNE.getOrDefault(typ, Collections.emptyList());
    }

    // Pobiera produkty użytkownika z bazy
    public static List<Produkt> pobierzProduktyUzytkownika(int userId) {
        List<Produkt> lista = new ArrayList<>();
        String sql = "SELECT id, typ, nazwa, ilosc, data_przyjecia, data_odbioru "
                + "FROM produkty WHERE uzytkownik_id=?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                try {
                    TypProduktu t = TypProduktu.valueOf(rs.getString("typ").toUpperCase());
                    lista.add(new Produkt(
                            rs.getInt("id"),
                            t,
                            rs.getString("nazwa"),
                            rs.getInt("ilosc"),
                            rs.getDate("data_przyjecia").toLocalDate(),
                            rs.getDate("data_odbioru").toLocalDate()
                    ));
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Nieznany typ produktu: " + rs.getString("typ"),
                            "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Błąd ładowania produktów:\n" + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    // Pobiera wszystkie produkty
    public static List<Produkt> pobierzWszystkieProdukty() {
        List<Produkt> lista = new ArrayList<>();
        String sql = "SELECT id, typ, nazwa, ilosc, data_przyjecia, data_odbioru FROM produkty";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Produkt(
                        rs.getInt("id"),
                        TypProduktu.valueOf(rs.getString("typ").toUpperCase()),
                        rs.getString("nazwa"),
                        rs.getInt("ilosc"),
                        rs.getDate("data_przyjecia").toLocalDate(),
                        rs.getDate("data_odbioru").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Błąd pobierania produktów:\n" + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    // Usuwa produkt po nazwie
    public static void usunProdukt(String nazwa) {
        String sql = "DELETE FROM produkty WHERE nazwa = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nazwa);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null,
                        "Produkt usunięty",
                        "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Nie znaleziono produktu",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Błąd usuwania:\n" + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Dodaje nową rezerwację
    public static boolean dodajRezerwacje(int userId, TypProduktu typ,
                                          String nazwa, int ilosc,
                                          LocalDate dataPrzyj, LocalDate dataOdb) {
        if (ilosc < 1 || ilosc > 10000) {
            JOptionPane.showMessageDialog(null,
                    "Ilość musi być 1-10000",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO produkty(typ,nazwa,ilosc,uzytkownik_id,data_przyjecia,data_odbioru) "
                + "VALUES(?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typ.name());
            ps.setString(2, nazwa);
            ps.setInt(3, ilosc);
            ps.setInt(4, userId);
            ps.setDate(5, java.sql.Date.valueOf(dataPrzyj));
            ps.setDate(6, java.sql.Date.valueOf(dataOdb));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Produkt zarezerwowany",
                    "Sukces", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Błąd rezerwacji:\n" + e.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}