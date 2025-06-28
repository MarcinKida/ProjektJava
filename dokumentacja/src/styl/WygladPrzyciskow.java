package styl; // Pakiet odpowiedzialny za wygląd komponentów GUI

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter; // Obsługa zdarzeń myszy
import java.awt.event.MouseEvent; // Konkretny typ zdarzenia myszy

// Klasa ustawia jednolity wygląd wszystkich przycisków w aplikacji
public class WygladPrzyciskow {

    // Kolory dla przycisków
    private static final Color KOLOR_TLA        = new Color(55, 120, 250); // Podstawowy kolor tła
    private static final Color KOLOR_TLA_HOVER  = new Color(30, 90, 220);  // Kolor po najechaniu myszką
    private static final Color KOLOR_NAPISU     = Color.WHITE;             // Kolor tekstu
    // Czcionka przycisków
    private static final Font CZCIONKA = new Font("Segoe UI", Font.BOLD, 18);
    // Nadaje styl przyciskowi (kolor, czcionka, animacja)
    public static void ustawStyl(JButton przycisk) {
        przycisk.setBackground(KOLOR_TLA); // Ustawia kolor tła
        przycisk.setForeground(KOLOR_NAPISU); // Ustawia kolor tekstu
        przycisk.setFont(CZCIONKA); // Ustawia czcionkę
        przycisk.setFocusPainted(false); // Usuwa obramowanie fokusu
        przycisk.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Zaokrąglone rogi
        // Animacja zmiany koloru po najechaniu myszką
        przycisk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                przycisk.setBackground(KOLOR_TLA_HOVER); // Zmienia kolor tła
            }

            @Override
            public void mouseExited(MouseEvent e) {
                przycisk.setBackground(KOLOR_TLA); // Przywraca oryginalny kolor tła
            }
        });
    }
}
