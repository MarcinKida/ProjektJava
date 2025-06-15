package BazaDanych; // Pakiet odpowiedzialny za połączenie z bazą danych

import java.sql.Connection; // Import klasy Connection do obsługi połączeń SQL
import java.sql.DriverManager; // Import DriverManager do zarządzania sterownikiem bazy
import java.sql.SQLException; // Obsługa wyjątków SQL

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/java"; // Adres bazy danych
    private static final String USER = "root"; // Nazwa użytkownika bazy danych
    private static final String PASSWORD = ""; // Hasło do bazy danych (puste)

    //  Metoda zwracająca połączenie do bazy danych
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD); // Tworzy i zwraca połączenie
    }
}
