SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- --------------------------------------------------------

--
-- Table structure for table `requests`
--

CREATE TABLE `requests` (
  `id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` varchar(500) NOT NULL,
  `type` varchar(500) NOT NULL,
  `notes` varchar(2000) DEFAULT NULL,
  `nurse_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `requests`
--

INSERT INTO `requests` (`id`, `start_date`, `end_date`, `status`, `type`, `notes`, `nurse_id`) VALUES
(10001, '2022-04-04', '2022-04-08', 'Pending', 'Vacation', 'Planning for a familiy trip!', 1),
(10002, '2022-03-13', '2022-03-13', 'Pending', 'Shift Change', 'I would prefer not work this day if possible.', 2),
(10003, '2022-03-28', '2022-04-01', 'Pending', 'Sick Leave', 'Having a scheduled surgery on March 28th.', 2),
(10004, '2022-05-23', '2022-05-27', 'Pending', 'Vacation', 'Vacation time.', 3),
(10005, '2022-07-23', '2022-10-29', 'Approved', 'Parental Leave', 'My baby is due on August 1st.', 3),
(10006, '2022-08-23', '2022-08-27', 'Pending', 'Vacation', 'Trip for my honeymoon.', 4),
(10007, '2022-03-11', '2022-03-11', 'Pending', 'Shift Change', 'Request for shift change.', 5),
(10008, '2022-03-12', '2022-03-12', 'Pending', 'Shift Change', 'Request for shift change.', 5),
(10009, '2022-05-16', '2022-05-20', 'Pending', 'Personal Time', 'It\'s personal.', 5),
(10010, '2022-07-04', '2022-07-15', 'Pending', 'Vacation', 'Family trip for the summer.', 6),
(10011, '2022-02-26', '2022-02-27', 'Pending', 'Personal Time', 'Personal time', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `requests`
--
ALTER TABLE `requests`
  ADD PRIMARY KEY (`id`),
  ADD KEY `nurse_fk` (`nurse_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `requests`
--
ALTER TABLE `requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10012;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `requests`
--
ALTER TABLE `requests`
  ADD CONSTRAINT `nurse_fk` FOREIGN KEY (`nurse_id`) REFERENCES `nurses` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
