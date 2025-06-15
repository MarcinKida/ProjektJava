package bazowe; // Pakiet bazowy dla okien aplikacji

import javax.swing.*; // Importuje podstawowe komponenty Swing (np. JFrame, JPanel, JLabel)
import javax.swing.plaf.ColorUIResource; // Import do ustawiania globalnego motywu kolorów
import java.awt.*; // Importuje klasy do zarządzania grafiką i układem komponentów

// Klasa bazowa wszystkich okien – ustawia motyw, rozmiar, banner z logo
// Inne klasy okien powinny dziedziczyć po niej
public abstract class OknoBazowe extends JFrame {

    // Kolory dla interfejsu
    protected final Color KOLOR_TLA          = new Color(34, 34, 34); // Ciemne tło aplikacji
    protected final Color KOLOR_PRZYCISKU    = new Color(80, 80, 200); // Kolor przycisków
    protected final Color KOLOR_NAPISU       = Color.WHITE; // Biały tekst

    // Czcionki dla tekstów w oknie
    protected final Font  CZCIONKA_TYTUL     = new Font("Arial", Font.BOLD, 48); // Czcionka tytułu
    protected final Font  CZCIONKA_PODSTAWOWE= new Font("Arial", Font.PLAIN, 24); // Czcionka dla przycisków i etykiet

    // Konstruktor ustawiający wygląd okna
    public OknoBazowe(String tytul) {
        super(tytul); // Ustawia tytuł okna
        ustawMotyw(); // Konfiguracja globalnego motywu aplikacji
        inicjalizujOkno(); // Ustawia właściwości okna
        dodajBanner(); // Dodaje banner z logo i tytułem aplikacji
        initKomponenty(); // Metoda abstrakcyjna – każdy ekran musi mieć własne komponenty
    }

    // Ustawia globalny motyw GUI (kolory, czcionki, przyciski)
    private void ustawMotyw() {
        UIManager.put("Panel.background", new ColorUIResource(KOLOR_TLA));
        UIManager.put("OptionPane.background", new ColorUIResource(KOLOR_TLA));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(KOLOR_NAPISU));
        UIManager.put("Button.font", CZCIONKA_PODSTAWOWE);
        UIManager.put("Button.background", new ColorUIResource(KOLOR_PRZYCISKU));
        UIManager.put("Button.foreground", new ColorUIResource(KOLOR_NAPISU));
        UIManager.put("Label.font", CZCIONKA_PODSTAWOWE);
        UIManager.put("Label.foreground", new ColorUIResource(KOLOR_NAPISU));
        UIManager.put("OptionPane.yesButtonText", "Tak"); // Zmienia domyślne przyciski na język polski
        UIManager.put("OptionPane.noButtonText", "Nie");
        UIManager.put("OptionPane.okButtonText", "OK");
        getContentPane().setBackground(KOLOR_TLA); // Ustawia kolor tła dla całego okna
    }

    // Konfiguruje właściwości okna (rozmiar, zamknięcie, układ)
    private void inicjalizujOkno() {
        setSize(1200, 800); // Ustawia rozmiar okna na 1200x800 pikseli
        setLocationRelativeTo(null); // Wyśrodkowuje okno na ekranie
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Nie pozwala zamknąć okna przez "X"
        getContentPane().setLayout(new BorderLayout(20, 20)); // Ustawia przestrzeń między komponentami
        setResizable(false); // Blokuje możliwość zmiany rozmiaru okna
    }

    // Dodaje u góry banner z logo i tytułem aplikacji
    private void dodajBanner() {
        JPanel banner = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Wyśrodkowanie elementów banneru
        banner.setBackground(KOLOR_TLA); // Nadaje tło bannerowi

        ImageIcon ikona = new ImageIcon(getClass().getResource("/figures/logo.png")); // Wczytuje obraz logo z plików projektu
        JLabel lblIcon = new JLabel(ikona); // Tworzy etykietę z ikoną
        JLabel lblTytul = new JLabel("MagazynMK"); // Tworzy etykietę z tytułem aplikacji
        lblTytul.setFont(CZCIONKA_TYTUL); // Ustawia czcionkę tytułu
        lblTytul.setForeground(KOLOR_NAPISU); // Ustawia kolor tytułu

        banner.add(lblIcon); // Dodaje logo do banneru
        banner.add(lblTytul); // Dodaje tytuł aplikacji do banneru
        add(banner, BorderLayout.NORTH); // Umieszcza banner u góry okna
    }

    // Metoda abstrakcyjna – każda klasa dziedzicząca musi ją zaimplementować
    protected abstract void initKomponenty();
}
