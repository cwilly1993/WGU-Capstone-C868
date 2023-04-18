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
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int(11) NOT NULL,
  `username` varchar(500) NOT NULL,
  `password` text NOT NULL,
  `email` varchar(200) NOT NULL,
  `name` varchar(500) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `address` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `username`, `password`, `email`, `name`, `phone`, `address`) VALUES
(1, 'admin1', '$2y$10$vbOZR.IQhRdlNxdbd9hm3eqz0ZXn9LlEknOYYeu6ESV6JVtfLk.x.', 'admin1@calhope.com', 'Admin one', '555-555-2001', '101 Admin Street, Sacramento, CA'),
(2, 'admin2', '$2y$10$qIkmNkypdcDMPH390Kgt6OdRqcHxRp0z1RmEoJio3WOXn/9ZuHhHC', 'admin2@calhope.com', 'Admin Two', '555-555-2002', '102 Admin Street, Sacramento, CA'),
(3, 'admin3', '$2y$10$eI1d8APcSqseipBPBTYMv.74.po0Zji9LlFX2BBOMy77BeQGLqCwW', 'admin3@calhope.com', 'Admin Three', '555-555-2003', '103 Admin Street, Sacramento, CA');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
