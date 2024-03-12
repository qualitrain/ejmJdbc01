/*
SQLyog - Free MySQL GUI v5.0
Host - 5.0.22-community-nt : Database - ejemplosjdbc
*********************************************************************
Server version : 5.0.22-community-nt
*/


create database if not exists `ejemplosjdbc`;

USE `ejemplosjdbc`;

/*Table structure for table `persona` */

DROP TABLE IF EXISTS `persona`;

CREATE TABLE `persona` (
  `id_persona` int(5) NOT NULL,
  `nombre` varchar(40) NOT NULL,
  `direccion` varchar(45) default NULL,
  `fecha_nacimiento` date NOT NULL,
  PRIMARY KEY  (`id_persona`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `articulo` */
DROP TABLE IF EXISTS `articulo`;

CREATE TABLE `articulo` (
  `cve_articulo` varchar(15) NOT NULL,
  `descripcion` varchar(40) NOT NULL,
  `costo_prov_1` float default '0',
  `precio_lista` float default '0',
  PRIMARY KEY  (`cve_articulo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `detalle_venta` */

DROP TABLE IF EXISTS `detalle_venta`;

CREATE TABLE `detalle_venta` (
  `num_venta` int(6) NOT NULL,
  `num_detalle` int(3) NOT NULL,
  `cantidad` int(4) NOT NULL,
  `cve_articulo` varchar(15) NOT NULL,
  `precio_unitario` float default NULL,
  PRIMARY KEY  (`num_venta`,`num_detalle`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `venta` */

DROP TABLE IF EXISTS `venta`;

CREATE TABLE `venta` (
  `num_venta` int(6) NOT NULL auto_increment,
  `fecha_venta` date NOT NULL,
  `id_persona_cte` int(5) NOT NULL,
  `id_persona_vendedor` int(5) default NULL,
  PRIMARY KEY  (`num_venta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

