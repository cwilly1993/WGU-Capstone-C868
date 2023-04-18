-- phpMyAdmin SQL Dump
-- version 4.9.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Feb 26, 2022 at 03:01 AM
-- Server version: 5.7.23-23
-- PHP Version: 7.3.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wwilliam_cw_c868`
--

-- --------------------------------------------------------

--
-- Table structure for table `shifts`
--

CREATE TABLE `shifts` (
  `id` int(11) NOT NULL,
  `shift_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `unit_id` int(11) NOT NULL,
  `nurse_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `shifts`
--

INSERT INTO `shifts` (`id`, `shift_date`, `start_time`, `end_time`, `unit_id`, `nurse_id`) VALUES
(1, '2022-03-01', '09:00:00', '21:00:00', 1, 1),
(2, '2022-03-01', '07:00:00', '21:00:00', 4, 2),
(3, '2022-03-02', '09:00:00', '21:00:00', 2, 1),
(4, '2022-03-02', '09:00:00', '21:00:00', 4, 2),
(5, '2022-03-02', '11:00:00', '23:00:00', 2, 3),
(6, '2022-03-03', '09:00:00', '21:00:00', 3, 1),
(7, '2022-03-03', '07:00:00', '19:00:00', 4, 2),
(8, '2022-03-03', '11:00:00', '23:00:00', 2, 3),
(9, '2022-03-04', '09:00:00', '21:00:00', 1, 4),
(10, '2022-03-04', '11:00:00', '23:00:00', 2, 3),
(11, '2022-03-04', '07:00:00', '19:00:00', 3, 4),
(12, '2022-03-05', '05:00:00', '17:00:00', 4, 5),
(13, '2022-03-05', '07:00:00', '19:00:00', 3, 4),
(14, '2022-03-05', '11:00:00', '23:00:00', 2, 6),
(15, '2022-03-06', '07:00:00', '19:00:00', 1, 4),
(16, '2022-03-06', '07:00:00', '19:00:00', 2, 5),
(17, '2022-03-06', '05:00:00', '17:00:00', 4, 1),
(18, '2022-03-07', '05:00:00', '17:00:00', 4, 6),
(19, '2022-03-07', '09:00:00', '21:00:00', 1, 2),
(20, '2022-03-07', '11:00:00', '23:00:00', 2, 5),
(21, '2022-03-08', '11:00:00', '23:00:00', 3, 1),
(22, '2022-03-08', '07:00:00', '19:00:00', 4, 2),
(23, '2022-03-08', '11:00:00', '23:00:00', 2, 6),
(24, '2022-03-08', '09:00:00', '18:00:00', 1, 4),
(25, '2022-03-09', '11:00:00', '23:00:00', 1, 5),
(26, '2022-03-09', '07:00:00', '19:00:00', 4, 3),
(27, '2022-03-09', '05:00:00', '17:00:00', 2, 2),
(28, '2022-03-09', '11:00:00', '23:00:00', 3, 1),
(29, '2022-03-10', '09:00:00', '21:00:00', 1, 1),
(30, '2022-03-10', '11:00:00', '23:00:00', 2, 2),
(31, '2022-03-10', '19:00:00', '07:00:00', 3, 3),
(32, '2022-03-10', '05:00:00', '17:00:00', 4, 4);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `shifts`
--
ALTER TABLE `shifts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `unit_fk` (`unit_id`),
  ADD KEY `shift_fk` (`nurse_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `shifts`
--
ALTER TABLE `shifts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `shifts`
--
ALTER TABLE `shifts`
  ADD CONSTRAINT `shift_fk` FOREIGN KEY (`nurse_id`) REFERENCES `nurses` (`id`),
  ADD CONSTRAINT `unit_fk` FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
