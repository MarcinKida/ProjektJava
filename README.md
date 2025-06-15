-- Tabela użytkownicy
CREATE TABLE `uzytkownicy` (
  `id`           INT AUTO_INCREMENT PRIMARY KEY,
  `login`        VARCHAR(50)  NOT NULL UNIQUE,
  `imie`         VARCHAR(50)  NOT NULL,
  `nazwisko`     VARCHAR(50)  NOT NULL,
  `nr_tel`       VARCHAR(15)  NOT NULL,
  `email`        VARCHAR(255) NOT NULL UNIQUE,
  `haslo`        VARCHAR(255) NOT NULL,
  `czy_admin`    BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- Wstawienie użytkownika admin
INSERT INTO `uzytkownicy` (`login`, `imie`, `nazwisko`, `nr_tel`, `email`, `haslo`, `czy_admin`)
VALUES (
  'admin',
  'admin',
  'admin',
  '123456789',
  'admin@pl',
  'admin1',
  TRUE
);

-- Tabela produkty
CREATE TABLE `produkty` (
  `id`              INT AUTO_INCREMENT PRIMARY KEY,
  `typ`             ENUM('ELEKTRONICZNE', 'SPOŻYWCZE', 'ODZIEŻOWE', 'CHEMICZNE', 'PRZEMYSŁOWE') NOT NULL,
  `nazwa`           VARCHAR(150) NOT NULL,
  `ilosc`           INT NOT NULL CHECK (`ilosc` BETWEEN 1 AND 10000),
  `uzytkownik_id`   INT NOT NULL,
  `data_przyjecia`  DATE NOT NULL,
  `data_odbioru`    DATE NOT NULL,
  FOREIGN KEY (`uzytkownik_id`)
    REFERENCES `uzytkownicy`(`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- Tabela kary
CREATE TABLE `kary` (
  `id`              INT AUTO_INCREMENT PRIMARY KEY,
  `uzytkownik_id`   INT NOT NULL,
  `produkt_id`      INT NOT NULL,
  `dni_opoznienia`  INT NOT NULL,
  `kara_kwota`      DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (`uzytkownik_id`)
    REFERENCES `uzytkownicy`(`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY (`produkt_id`)
    REFERENCES `produkty`(`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- Tabela transakcje
CREATE TABLE `transakcje` (
  `id`    INT AUTO_INCREMENT PRIMARY KEY,
  `data`  DATE NOT NULL,
  `kwota` DECIMAL(10,2) NOT NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- Wstawienie przykładowych transakcji z losowymi kwotami
INSERT INTO transakcje (data, kwota) VALUES
('2024-06-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2024-07-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2024-08-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2024-09-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2024-10-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2024-11-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2024-12-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2025-01-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2025-02-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2025-03-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2025-04-01', ROUND(RAND() * 15000 + 5000, 2)), 
('2025-05-01', ROUND(RAND() * 15000 + 5000, 2));
