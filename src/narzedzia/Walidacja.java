package narzedzia; // Pakiet z narzędziami pomocniczymi

import javax.swing.JOptionPane; // Importuje okno dialogowe do wyświetlania komunikatów

// Klasa do walidacji danych użytkownika i pól tekstowych
public class Walidacja {

    // Sprawdza poprawność loginu
    public static boolean sprawdzLogin(String login) {
        if (login.matches("^[a-zA-Z0-9]{4,}$")) { // Sprawdza, czy login zawiera tylko litery/cyfry i ma min. 4 znaki
            return true;
        }
        JOptionPane.showMessageDialog(null,
                "Login musi mieć co najmniej 4 znaki i zawierać tylko litery oraz cyfry.",
                "Błąd walidacji",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // Sprawdza poprawność adresu e-mail
    public static boolean sprawdzEmail(String email) {
        if (email == null || !email.contains("@")) { // Sprawdza, czy e-mail zawiera znak '@'
            JOptionPane.showMessageDialog(null,
                    "Adres e-mail musi zawierać znak '@'.",
                    "Błąd walidacji",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Sprawdza poprawność numeru telefonu
    public static boolean sprawdzNrTel(String nrTel) {
        if (nrTel == null || !nrTel.matches("^\\d{9,11}$")) { // Sprawdza, czy numer składa się tylko z cyfr i ma 9-11 znaków
            JOptionPane.showMessageDialog(null,
                    "Numer telefonu musi zawierać od 9 do 11 cyfr.",
                    "Błąd walidacji",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Sprawdza poprawność hasła
    public static boolean sprawdzHaslo(String haslo) {
        if (haslo.length() >= 6) { // Sprawdza, czy hasło ma co najmniej 6 znaków
            return true;
        }
        JOptionPane.showMessageDialog(null,
                "Hasło musi mieć co najmniej 6 znaków.",
                "Błąd walidacji",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // Sprawdza, czy dwa hasła są identyczne
    public static boolean sprawdzHasla(String haslo1, String haslo2) {
        if (sprawdzHaslo(haslo1) && haslo1.equals(haslo2)) { // Porównuje dwa hasła
            return true;
        }
        JOptionPane.showMessageDialog(null,
                "Hasła nie są identyczne.",
                "Błąd walidacji",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

}
