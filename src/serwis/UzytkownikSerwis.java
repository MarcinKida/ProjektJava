package serwis;

import BazaDanych.DatabaseConnector;
import narzedzia.Walidacja;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UzytkownikSerwis {

    // Metoda zmieniająca login użytkownika
    public static boolean zmienLogin(int userId, String nowyLogin) {
        // Sprawdzenie poprawności nowego loginu
        if (!Walidacja.sprawdzLogin(nowyLogin)) return false;

        // Zapytanie SQL do aktualizacji loginu
        String sql = "UPDATE uzytkownicy SET login = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Ustawienie parametrów zapytania
            ps.setString(1, nowyLogin);
            ps.setInt(2, userId);
            // Wykonanie aktualizacji
            ps.executeUpdate();

            // Wyświetlenie komunikatu o sukcesie
            JOptionPane.showMessageDialog(
                    null,
                    "Login został zmieniony.",
                    "Sukces",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        } catch (SQLException e) {
            // Obsługa błędu i wyświetlenie komunikatu
            JOptionPane.showMessageDialog(
                    null,
                    "Błąd zmiany loginu:\n" + e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    // Metoda zmieniająca numer telefonu użytkownika
    public static boolean zmienTelefon(int userId, String nowyTel) {
        // Walidacja numeru telefonu
        if (!Walidacja.sprawdzNrTel(nowyTel)) return false;

        // Zapytanie SQL do aktualizacji numeru telefonu
        String sql = "UPDATE uzytkownicy SET nr_tel = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Ustawienie parametrów zapytania
            ps.setString(1, nowyTel);
            ps.setInt(2, userId);
            // Wykonanie aktualizacji
            ps.executeUpdate();

            // Komunikat o sukcesie
            JOptionPane.showMessageDialog(
                    null,
                    "Numer telefonu został zmieniony.",
                    "Sukces",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        } catch (SQLException e) {
            // Obsługa błędu
            JOptionPane.showMessageDialog(
                    null,
                    "Błąd zmiany numeru telefonu:\n" + e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    // Metoda zmieniająca hasło użytkownika
    public static boolean zmienHaslo(int userId, String noweHaslo, String potwierdzenie) {
        // Sprawdzenie czy hasła są poprawne i zgodne
        if (!Walidacja.sprawdzHasla(noweHaslo, potwierdzenie)) return false;

        // Zapytanie SQL do aktualizacji hasła
        String sql = "UPDATE uzytkownicy SET haslo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Ustawienie parametrów zapytania
            ps.setString(1, noweHaslo);
            ps.setInt(2, userId);
            // Wykonanie aktualizacji
            ps.executeUpdate();

            // Komunikat o sukcesie
            JOptionPane.showMessageDialog(
                    null,
                    "Hasło zostało zmienione.",
                    "Sukces",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        } catch (SQLException e) {
            // Obsługa błędu
            JOptionPane.showMessageDialog(
                    null,
                    "Błąd zmiany hasła:\n" + e.getMessage(),
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    // Metoda ustawiająca użytkownikowi uprawnienia administratora
    public static boolean ustawAdministratora(String login) {
        // Zapytanie sprawdzające istnienie użytkownika
        String sqlSprawdz = "SELECT id FROM uzytkownicy WHERE login = ?";
        // Zapytanie aktualizujące uprawnienia
        String sqlAktualizuj = "UPDATE uzytkownicy SET czy_admin = 1 WHERE login = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement psSprawdz = conn.prepareStatement(sqlSprawdz)) {

            // Sprawdzenie czy użytkownik istnieje
            psSprawdz.setString(1, login);
            ResultSet rs = psSprawdz.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "Użytkownik o podanym loginie nie istnieje.",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Nadanie uprawnień administratora
            try (PreparedStatement psAktualizuj = conn.prepareStatement(sqlAktualizuj)) {
                psAktualizuj.setString(1, login);
                psAktualizuj.executeUpdate();
                JOptionPane.showMessageDialog(null,
                        "Użytkownik " + login + " został administratorem.",
                        "Sukces", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (SQLException ex) {
            // Obsługa błędów
            JOptionPane.showMessageDialog(null,
                    "Błąd przyznawania administratora:\n" + ex.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}