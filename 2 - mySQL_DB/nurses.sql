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
-- Table structure for table `nurses`
--

CREATE TABLE `nurses` (
  `id` int(11) NOT NULL,
  `username` varchar(200) NOT NULL,
  `password` text NOT NULL,
  `email` varchar(200) NOT NULL,
  `name` varchar(500) NOT NULL,
  `phone` varchar(25) NOT NULL,
  `address` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `nurses`
--

INSERT INTO `nurses` (`id`, `username`, `password`, `email`, `name`, `phone`, `address`) VALUES
(1, 'testNurse1', '$2y$10$QqgOcbB9U6OyawPtVC99OOk2fBuxcFCbFaDoE7cW7IFe3iFQ8fk.e', 'nurse1@calhope.com', 'Nurse One', '555-555-1001', '101 Main Street, Sacramento, CA'),
(2, 'testNurse2', '$2y$10$7oohH5RPr9/y4K/BtXXxsea9powd7ho6ltCsLyZX61s19WXbY8nFO', 'nurse2@calhope.com', 'Nurse Two', '555-555-1002', '102 Main Street, Sacramento, CA'),
(3, 'testNurse3', '$2y$10$I27yppQFc9x6Y/5G56GnLe6/qha/DXFRYBeb0hq.64lZXUUFUonBm', 'nurse3@calhope.com', 'Nurse Three', '555-555-1003', '103 Main Street, Sacramento, CA'),
(4, 'testNurse4', '$2y$10$sCXBeHryK9GPp7Amw/a9EegL/TG73int.bbSypSJ8KsXScQled8Wu', 'nurse4@calhope.com', 'Nurse Four', '555-555-1004', '104 Main Street, Sacramento, CA'),
(5, 'testNurse5', '$2y$10$.pxZjDLQAaioikDEBNcZM.nvmfr4qCQNF1AHkb49/1.fnSzcXFvE2', 'nurse5@calhope.com', 'Nurse Five', '555-555-1005', '105 Main Street, Sacramento, CA'),
(6, 'testNurse6', '$2y$10$ZPHakrv0kMhVu.uA25u00uv8plkJJmtIeQ8Oe4UANcEJGI.JREiDW', 'nurse6@calhope.com', 'Nurse 6', '555-555-1006', '106 Main Street, Sacramento, CA');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `nurses`
--
ALTER TABLE `nurses`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `nurses`
--
ALTER TABLE `nurses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
