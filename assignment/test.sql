-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th12 13, 2018 lúc 12:37 PM
-- Phiên bản máy phục vụ: 10.1.34-MariaDB
-- Phiên bản PHP: 5.6.37

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `test`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `Data`
--

CREATE TABLE `Data` (
  `GatewayID` int(10) NOT NULL,
  `ID` int(10) NOT NULL,
  `Value` float NOT NULL,
  `Time` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `Data`
--

INSERT INTO `Data` (`GatewayID`, `ID`, `Value`, `Time`) VALUES
(1, 1, 20, 1544016712),
(1, 1, 20, 1544017312),
(1, 1, 20, 1544017912),
(1, 1, 20, 1544018512),
(1, 1, 20, 1544019112),
(1, 1, 20, 1544019712),
(1, 1, 20, 1544020312),
(1, 1, 20, 1544020912),
(1, 1, 20, 1544021512),
(1, 1, 20, 1544022112),
(1, 1, 20, 1544022712),
(1, 1, 20, 1544023312),
(1, 1, 20, 1544023912),
(1, 1, 20, 1544024512),
(1, 1, 20, 1544025112),
(1, 1, 20, 1544025712),
(1, 1, 20, 1544026312),
(1, 1, 20, 1544026912),
(1, 1, 20, 1544027512),
(1, 1, 20, 1544028112),
(1, 1, 20, 1544028712),
(0, 1, 20, 1544016712),
(0, 1, 20, 1544017312),
(0, 1, 20, 1544017912),
(0, 1, 20, 1544018512),
(0, 1, 20, 1544019112),
(0, 1, 20, 1544019712),
(0, 1, 20, 1544020312),
(0, 1, 20, 1544020912),
(0, 1, 20, 1544021512),
(0, 1, 20, 1544022112),
(0, 1, 20, 1544022712),
(0, 1, 20, 1544023312),
(0, 1, 20, 1544023912),
(0, 1, 20, 1544024512),
(0, 1, 20, 1544025112),
(0, 1, 20, 1544025712),
(0, 1, 20, 1544026312),
(0, 1, 20, 1544026912),
(0, 1, 20, 1544027512),
(0, 1, 20, 1544028112),
(0, 1, 20, 1544028712),
(0, 1, 20, 1544029312),
(0, 1, 20, 1544029912),
(0, 2, 20, 1544016712),
(0, 2, 20, 1544017312),
(0, 2, 20, 1544017912),
(0, 2, 20, 1544018512),
(0, 2, 20, 1544019112),
(0, 2, 20, 1544019712),
(0, 2, 20, 1544020312),
(0, 2, 20, 1544020912),
(0, 2, 20, 1544021512),
(0, 2, 20, 1544022112),
(0, 2, 20, 1544022712),
(0, 2, 20, 1544023312),
(0, 2, 20, 1544023912),
(0, 2, 20, 1544024512),
(0, 2, 20, 1544025112),
(0, 2, 20, 1544025712),
(0, 2, 20, 1544026312),
(0, 2, 20, 1544026912),
(0, 2, 20, 1544027512),
(0, 2, 20, 1544028112),
(0, 2, 20, 1544028712),
(0, 2, 20, 1544029312),
(0, 2, 20, 1544029912),
(0, 2, 20, 1544030512),
(0, 2, 20, 1544031112),
(0, 2, 20, 1544031712),
(0, 2, 20, 1544032312),
(0, 2, 20, 1544032912),
(0, 2, 20, 1544033512),
(0, 2, 20, 1544034112),
(1, 2, 65, 2147483647);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `User`
--

CREATE TABLE `User` (
  `Username` char(255) NOT NULL,
  `Password` char(255) NOT NULL,
  `Email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `User`
--

INSERT INTO `User` (`Username`, `Password`, `Email`) VALUES
('lochoang', '12345', 'lochoang75@gmail.com');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
