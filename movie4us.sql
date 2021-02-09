-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 09 Lut 2021, 22:47
-- Wersja serwera: 10.4.14-MariaDB
-- Wersja PHP: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `movie4us`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `friends`
--

CREATE TABLE `friends` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `friendID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `friends`
--

INSERT INTO `friends` (`id`, `username`, `friendID`) VALUES
(17, 'test1', 2),
(18, 'test', 2),
(19, 'test1', 3),
(20, 'test', 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `userid` int(11) NOT NULL,
  `fullname` text NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` text NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`userid`, `fullname`, `username`, `password`, `email`) VALUES
(2, 'test', 'test', '$2y$10$Pse5GW2smLlSAAMRRyLpzeH2YLEIhNnr.G13BHO0lAN9F59YxI1kq', 'test'),
(3, 'test1', 'test1', '$2y$10$ph/LcE/8MYrUkZAw7Kp/0eym3vohOAC0eej8KM9KZC35.X7gtn8ou', 'test1'),
(4, 'test2', 'test3', '$2y$10$D.a4eDPmnqurcH7ZonNuxumEHiLVAqIEVwHhKEXoqO0jdulMWe/4O', 'test3'),
(14, 'jkowalski', 'jkowalski', '$2y$10$iqbpRAijul9x7af6HJKNWOJLI7pnlOBAnkBOoNGyi1phySTs3vmZ.', 'jkowalski@pl.pl'),
(36, 'jkowalskii', 'jkowalskii', '$2y$10$3Hxj6sgZFeRRi3rZXuBygOpcd74qeDhAfBQFiAII67z0Rkxopi9Pu', 'jkowalskii@pl.pl'),
(48, 'test4', 'test4', '$2y$10$rM/IQcAwpcMrKX8YU0PosOF1zdLkebxUuMl9e3jzKPxbw50/rExvK', 'test4@gmail.com');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userid`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `friends`
--
ALTER TABLE `friends`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `userid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
