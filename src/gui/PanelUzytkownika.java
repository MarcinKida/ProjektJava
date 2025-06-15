package gui;

import serwis.Produkty;
import serwis.Kary;
import serwis.UzytkownikSerwis;
import styl.WygladPrzyciskow;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

// Główny panel użytkownika systemu magazynowego
public class PanelUzytkownika extends JFrame {
    // Pola klasy
    private final int userId;
    private final String login;
    private final JLabel lblWitaj = new JLabel();
    private final JButton btnMojeProd = new JButton("Moje produkty");
    private final JButton btnRezerwuj = new JButton("Zarezerwuj miejsce");
    private final JButton btnZalegle = new JButton("Zaległe zapłaty");
    private final JButton btnCennik = new JButton("Cennik");
    private final JButton btnZmienDane = new JButton("Zmień dane");
    private final JButton btnWyloguj = new JButton("Wyloguj");

    // Konstruktor panelu użytkownika
    public PanelUzytkownika(int userId, String login) {
        super("Panel użytkownika");
        this.userId = userId;
        this.login = login;
        // Konfiguracja powitania
        lblWitaj.setText("Witaj, " + login);
        lblWitaj.setFont(new Font("Arial", Font.BOLD, 24));
        initComponents(); // Inicjalizacja interfejsu
        setSize(1000, 800);
        setLocationRelativeTo(null); // Wyśrodkowanie okna
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
        JPanel btnPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        // Stylizacja przycisków
        WygladPrzyciskow.ustawStyl(btnMojeProd);
        WygladPrzyciskow.ustawStyl(btnRezerwuj);
        WygladPrzyciskow.ustawStyl(btnZalegle);
        WygladPrzyciskow.ustawStyl(btnCennik);
        WygladPrzyciskow.ustawStyl(btnZmienDane);
        WygladPrzyciskow.ustawStyl(btnWyloguj);

        // Dodanie przycisków do panelu
        btnPanel.add(btnMojeProd);
        btnPanel.add(btnRezerwuj);
        btnPanel.add(btnZalegle);
        btnPanel.add(btnCennik);
        btnPanel.add(btnZmienDane);
        btnPanel.add(btnWyloguj);

        panel.add(lblWitaj, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.CENTER);
        add(panel);

        // Przypisanie akcji do przycisków
        btnMojeProd.addActionListener(e -> pokazMojeProdukty());
        btnRezerwuj.addActionListener(e -> rezerwujProdukt());
        btnZalegle.addActionListener(e -> pokazZalegle());
        btnCennik.addActionListener(e -> pokazCennik());
        btnZmienDane.addActionListener(e -> zmienDane());
        btnWyloguj.addActionListener(e -> wylogowanie());
    }

    // Wyświetla listę produktów użytkownika
    private void pokazMojeProdukty() {
        List<Produkty.Produkt> lista = Produkty.pobierzProduktyUzytkownika(userId);
        String[] cols = {"Typ", "Nazwa", "Ilość", "Przyjęcie", "Odbiór"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        // Wypełnianie tabeli danymi
        for (Produkty.Produkt p : lista) {
            model.addRow(new Object[]{
                    p.getTyp().getNazwa(),
                    p.getNazwa(),
                    p.getIlosc(),
                    p.getDataPrzyjecia(),
                    p.getDataOdbioru()
            });
        }

        JTable table = new JTable(model);
        JOptionPane.showMessageDialog(this,
                new JScrollPane(table),
                "Moje produkty",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Zmiana danych użytkownika
    private void zmienDane() {
        String[] opcje = {"Login", "Telefon", "Hasło"};
        String wyb = (String) JOptionPane.showInputDialog(
                this, "Co chcesz zmienić?", "Zmień dane",
                JOptionPane.QUESTION_MESSAGE, null,
                opcje, opcje[0]
        );
        if (wyb == null) return;

        switch (wyb) {
            case "Login":
                String nowyLogin = JOptionPane.showInputDialog(this, "Nowy login:");
                if (nowyLogin != null) UzytkownikSerwis.zmienLogin(userId, nowyLogin);
                break;
            case "Telefon":
                String nowyTelefon = JOptionPane.showInputDialog(this, "Nowy numer telefonu:");
                if (nowyTelefon != null) UzytkownikSerwis.zmienTelefon(userId, nowyTelefon);
                break;
            case "Hasło":
                JPasswordField haslo1 = new JPasswordField();
                JPasswordField haslo2 = new JPasswordField();
                Object[] msg = {"Nowe hasło:", haslo1, "Powtórz hasło:", haslo2};
                int res = JOptionPane.showConfirmDialog(this, msg, "Zmień hasło", JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {
                    UzytkownikSerwis.zmienHaslo(userId,
                            new String(haslo1.getPassword()),
                            new String(haslo2.getPassword()));
                }
                break;
        }
    }

    // Wyświetla zaległe płatności
    private void pokazZalegle() {
        List<Kary.Kara> kary = Kary.pobierzKary(userId);
        String[] cols = {"Produkt", "Dni opóźnienia", "Kwota [PLN]"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Kary.Kara k : kary) {
            model.addRow(new Object[]{
                    k.getNazwaProduktu(),
                    k.getDniOpoznienia(),
                    k.getKwota()
            });
        }

        JTable table = new JTable(model);
        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(table),
                "Zaległe zapłaty",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Pobiera datę od użytkownika
    private LocalDate pobierzDate(String message) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));

        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
        panel.add(spinner);

        int result = JOptionPane.showConfirmDialog(this, panel, "Wybór daty", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return LocalDate.ofInstant(model.getDate().toInstant(), java.time.ZoneId.systemDefault());
        }
        return null;
    }

    // Wyświetla cennik usług magazynowych
    private void pokazCennik() {
        String[] cols = {"Typ produktu", "Cena za szt./dzień [PLN]", "Kara za szt./dzień [PLN]"};
        Object[][] dane = {
                {"Elektronika", "0.01", "1.00"},
                {"Spożywcze", "0.01", "1.00"},
                {"Odzieżowe", "0.01", "1.00"},
                {"Chemiczne", "0.01", "1.00"},
                {"Przemysłowe", "0.01", "1.00"}
        };

        DefaultTableModel model = new DefaultTableModel(dane, cols);
        JTable table = new JTable(model);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(table),
                "Cennik magazynu",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Proces rezerwacji miejsca w magazynie
    private void rezerwujProdukt() {
        // Wybór typu produktu
        Produkty.TypProduktu typ = (Produkty.TypProduktu) JOptionPane.showInputDialog(
                this, "Wybierz typ:", "Rezerwacja",
                JOptionPane.QUESTION_MESSAGE, null,
                Produkty.pobierzTypy().toArray(), null
        );
        if (typ == null) return;

        // Wybór konkretnego produktu
        List<String> nazwy = Produkty.pobierzNazwy(typ);
        String nazwa = (String) JOptionPane.showInputDialog(
                this, "Wybierz produkt:", "Rezerwacja",
                JOptionPane.QUESTION_MESSAGE, null,
                nazwy.toArray(), null
        );
        if (nazwa == null) return;

        // Pobranie ilości
        String iloscStr = JOptionPane.showInputDialog(this, "Ilość (1–10000):");
        if (iloscStr == null) return;
        int ilosc;
        try {
            ilosc = Integer.parseInt(iloscStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Niepoprawna ilość.");
            return;
        }

        // Pobranie dat
        LocalDate dataPrzyjecia = pobierzDate("Wybierz datę przyjęcia:");
        if (dataPrzyjecia == null) return;
        LocalDate dataOdbioru = pobierzDate("Wybierz datę odbioru:");
        if (dataOdbioru == null) return;

        // Obliczenie kosztu
        long dni = ChronoUnit.DAYS.between(dataPrzyjecia, dataOdbioru);
        double koszt = ilosc * dni * 0.01; // 1 grosz za sztukę dziennie

        // Zapis rezerwacji
        Produkty.dodajRezerwacje(userId, typ, nazwa, ilosc, dataPrzyjecia, dataOdbioru);

        // Wyświetlenie informacji o płatności
        JOptionPane.showMessageDialog(this,
                String.format("Koszt magazynowania: %.2f PLN\nOpłata tylko stacjonarnie pod adresem **Rzeszów 0**.", koszt),
                "Koszt rezerwacji", JOptionPane.INFORMATION_MESSAGE);
    }

    // Wylogowanie użytkownika
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