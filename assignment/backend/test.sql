-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 04, 2018 at 04:19 AM
-- Server version: 10.1.34-MariaDB
-- PHP Version: 5.6.37

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `test`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`` PROCEDURE `AddGeometryColumn` (`catalog` VARCHAR(64), `t_schema` VARCHAR(64), `t_name` VARCHAR(64), `geometry_column` VARCHAR(64), `t_srid` INT)  begin
  set @qwe= concat('ALTER TABLE ', t_schema, '.', t_name, ' ADD ', geometry_column,' GEOMETRY REF_SYSTEM_ID=', t_srid); PREPARE ls from @qwe; execute ls; deallocate prepare ls; end$$

CREATE DEFINER=`` PROCEDURE `DropGeometryColumn` (`catalog` VARCHAR(64), `t_schema` VARCHAR(64), `t_name` VARCHAR(64), `geometry_column` VARCHAR(64))  begin
  set @qwe= concat('ALTER TABLE ', t_schema, '.', t_name, ' DROP ', geometry_column); PREPARE ls from @qwe; execute ls; deallocate prepare ls; end$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `Data`
--

CREATE TABLE `Data` (
  `GatewayID` int(10) NOT NULL,
  `ID` int(10) NOT NULL,
  `Value` int(10) NOT NULL,
  `Time` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Data`
--

INSERT INTO `Data` (`GatewayID`, `ID`, `Value`, `Time`) VALUES
(0, 1, 12, '2304'),
(0, 1, 12, '2304'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234'),
(0, 1, 123, '1234');

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `Username` char(255) NOT NULL,
  `Password` char(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
