-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mar 13 Février 2018 à 09:36
-- Version du serveur: 5.6.12-log
-- Version de PHP: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `shoppyngapp`
--

-- --------------------------------------------------------

--
-- Structure de la table `product`
--

CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `publication_date` date DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Contenu de la table `product`
--

INSERT INTO `product` (`id`, `name`, `description`, `publication_date`, `price`) VALUES
(1, 'produit1', 'Acciverunt recreati quae casu porrecta planitie quiete sunt omne postquam timor concedentes quae adorti equestrium.', '2018-02-12', '100.00'),
(2, 'produit2', 'Aquae locis iuris plurimis formavit in regiones nusquam visitur calentes in in captis pari multiplicium.', '2018-02-13', '200.00'),
(3, 'produit3', 'Ordinis ei sub efferatus ni fixa responderunt inpenderet comes responderunt unum tunc intempestivam ordinis cum elogio gravius gravius celebrari iussit.', '2018-02-05', '500.00'),
(4, 'produit4', 'Ordinis ei sub efferatus ni fixa responderunt inpenderet comes responderunt unum tunc intempestivam ordinis cum elogio gravius gravius celebrari iussit.', '2018-02-10', '10.00'),
(5, 'produit5', 'Ingenii non quam quod responsa esse qualis ea suspicione admonitum te te alterum es etiam te sint admonitum invitum suspicione.', '2018-02-13', '30.00');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
