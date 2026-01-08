
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `techgear_db` DEFAULT CHARACTER SET utf8mb4 ;
USE `techgear_db` ;
CREATE TABLE IF NOT EXISTS `techgear_db`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `password` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(255) NULL DEFAULT NULL,
  `last_name` VARCHAR(255) NULL DEFAULT NULL,
  `enabled` TINYINT(1) NULL DEFAULT 1,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username` (`username` ASC) ,
  UNIQUE INDEX `email` (`email` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `techgear_db`.`carrito` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `estado` VARCHAR(255) NULL DEFAULT NULL,
  `idusuario` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK16gdpwsq75906240tn9chp0g0` (`idusuario` ASC) ,
  CONSTRAINT `FK16gdpwsq75906240tn9chp0g0`
    FOREIGN KEY (`idusuario`)
    REFERENCES `techgear_db`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `techgear_db`.`products` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `sku` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `price` DOUBLE NOT NULL,
  `stock` INT(11) NULL DEFAULT NULL,
  `category` VARCHAR(255) NULL DEFAULT NULL,
  `active` TINYINT(1) NULL DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `sku` (`sku` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `techgear_db`.`carrito_producto` (
  `id_carrito_producto` INT(11) NOT NULL AUTO_INCREMENT,
  `cantidad` DOUBLE NULL DEFAULT NULL,
  `carrito_id_carrito` INT(11) NULL DEFAULT NULL,
  `products_id` INT(11) NULL DEFAULT NULL,
  `carrito_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id_carrito_producto`),
  INDEX `FKsjbni15ikefjpxbm7rxdb2vkm` (`products_id` ASC) ,
  INDEX `FKbu1h63xmtw8657wtm43d8tvuh` (`carrito_id_carrito` ASC) ,
  INDEX `FKnrmojvi6x2js6rf5ut25o1thq` (`carrito_id` ASC) ,
  CONSTRAINT `FKbu1h63xmtw8657wtm43d8tvuh`
    FOREIGN KEY (`carrito_id_carrito`)
    REFERENCES `techgear_db`.`carrito` (`id`),
  CONSTRAINT `FKnrmojvi6x2js6rf5ut25o1thq`
    FOREIGN KEY (`carrito_id`)
    REFERENCES `techgear_db`.`carrito` (`id`),
  CONSTRAINT `FKsjbni15ikefjpxbm7rxdb2vkm`
    FOREIGN KEY (`products_id`)
    REFERENCES `techgear_db`.`products` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `techgear_db`.`consultas` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `asunto` VARCHAR(255) NOT NULL,
  `mensaje` VARCHAR(255) NOT NULL,
  `fecha` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `techgear_db`.`empleado` (
  `idEmpleado` INT(11) NOT NULL AUTO_INCREMENT,
  `nombres` VARCHAR(255) NOT NULL,
  `usuario` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `rol` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`idEmpleado`),
  UNIQUE INDEX `usuario_UNIQUE` (`usuario` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `techgear_db`.`orders` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NULL DEFAULT NULL,
  `status` VARCHAR(255) NULL DEFAULT NULL,
  `total` DOUBLE NULL DEFAULT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  INDEX `user_id` (`user_id` ASC) ,
  CONSTRAINT `orders_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `techgear_db`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `techgear_db`.`order_items` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_id` INT(11) NOT NULL,
  `product_id` INT(11) NOT NULL,
  `quantity` INT(11) NOT NULL,
  `unit_price` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `order_id` (`order_id` ASC) ,
  INDEX `product_id` (`product_id` ASC) ,
  CONSTRAINT `order_items_ibfk_1`
    FOREIGN KEY (`order_id`)
    REFERENCES `techgear_db`.`orders` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2`
    FOREIGN KEY (`product_id`)
    REFERENCES `techgear_db`.`products` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `techgear_db`.`product_images` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `product_id` INT(11) NOT NULL,
  `url` VARCHAR(255) NULL DEFAULT NULL,
  `display_order` INT(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `product_id` (`product_id` ASC) ,
  CONSTRAINT `product_images_ibfk_1`
    FOREIGN KEY (`product_id`)
    REFERENCES `techgear_db`.`products` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO techgear_db.empleado (nombres, usuario, password) VALUES ('asd', 'asd', '$10$Fj3qmxvlRCDtdFPv3MOMx.SLKi/ARrT0BUyV/CShiKvHrM5/hhLo.');