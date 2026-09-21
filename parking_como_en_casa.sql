-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema parking_como_en_casa
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `parking_como_en_casa` ;

-- -----------------------------------------------------
-- Schema parking_como_en_casa
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `parking_como_en_casa` DEFAULT CHARACTER SET utf8 ;
USE `parking_como_en_casa` ;

-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`rol`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`rol` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`rol` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `nombre_UNIQUE` ON `parking_como_en_casa`.`rol` (`nombre` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`usuario` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_rol` INT NOT NULL,
  `nombre_completo` VARCHAR(150) NOT NULL,
  `documento` VARCHAR(20) NOT NULL,
  `fecha_registro` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_usuario_rol`
    FOREIGN KEY (`id_rol`)
    REFERENCES `parking_como_en_casa`.`rol` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `documento_UNIQUE` ON `parking_como_en_casa`.`usuario` (`documento` ASC);

CREATE INDEX `fk_usuario_rol_idx` ON `parking_como_en_casa`.`usuario` (`id_rol` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`credenciales_acceso`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`credenciales_acceso` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`credenciales_acceso` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `correo` VARCHAR(100) NOT NULL,
  `clave` VARCHAR(225) NOT NULL,
  `ultimo_acceso` DATETIME NULL,
  `cuenta_activa` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_credenciales_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `parking_como_en_casa`.`usuario` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `id_usuario_UNIQUE` ON `parking_como_en_casa`.`credenciales_acceso` (`id_usuario` ASC);

CREATE UNIQUE INDEX `correo_UNIQUE` ON `parking_como_en_casa`.`credenciales_acceso` (`correo` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`tipo_vehiculo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`tipo_vehiculo` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`tipo_vehiculo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre_tipo` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `nombre_tipo_UNIQUE` ON `parking_como_en_casa`.`tipo_vehiculo` (`nombre_tipo` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`tarifas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`tarifas` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`tarifas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_tipo_vehiculo` INT NOT NULL,
  `modalidad` VARCHAR(50) NOT NULL,
  `valor_unidad` DECIMAL(10,2) NOT NULL,
  `tarifa_vigente` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_tarifas_tipo_vehiculo`
    FOREIGN KEY (`id_tipo_vehiculo`)
    REFERENCES `parking_como_en_casa`.`tipo_vehiculo` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX `fk_tarifas_tipo_vehiculo_idx` ON `parking_como_en_casa`.`tarifas` (`id_tipo_vehiculo` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`tarifas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`tarifas` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`tarifas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_tipo_vehiculo` INT NOT NULL,
  `modalidad` VARCHAR(50) NOT NULL,
  `valor_unidad` DECIMAL(10,2) NOT NULL,
  `tarifa_vigente` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_tarifas_tipo_vehiculo`
    FOREIGN KEY (`id_tipo_vehiculo`)
    REFERENCES `parking_como_en_casa`.`tipo_vehiculo` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX `fk_tarifas_tipo_vehiculo_idx` ON `parking_como_en_casa`.`tarifas` (`id_tipo_vehiculo` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`vehiculos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`vehiculos` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`vehiculos` (
  `placa` VARCHAR(10) NOT NULL,
  `id_tipo_vehiculo` INT NOT NULL,
  `marca` VARCHAR(50) NULL,
  `modelo` VARCHAR(50) NULL,
  `color` VARCHAR(50) NULL,
  PRIMARY KEY (`placa`),
  CONSTRAINT `fk_vehiculos_tipo_vehiculo`
    FOREIGN KEY (`id_tipo_vehiculo`)
    REFERENCES `parking_como_en_casa`.`tipo_vehiculo` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `placa_UNIQUE` ON `parking_como_en_casa`.`vehiculos` (`placa` ASC);

CREATE INDEX `fk_vehiculos_tipo_vehiculo_idx` ON `parking_como_en_casa`.`vehiculos` (`id_tipo_vehiculo` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`cliente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`cliente` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`cliente` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre_completo` VARCHAR(150) NOT NULL,
  `documento` VARCHAR(20) NOT NULL,
  `telefono` VARCHAR(20) NULL,
  `correo` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `documento_UNIQUE` ON `parking_como_en_casa`.`cliente` (`documento` ASC);

CREATE UNIQUE INDEX `correo_UNIQUE` ON `parking_como_en_casa`.`cliente` (`correo` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`suscripciones_mensuales`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`suscripciones_mensuales` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`suscripciones_mensuales` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_cliente` INT NOT NULL,
  `id_placa` VARCHAR(10) NOT NULL,
  `fecha_inicio` DATE NOT NULL,
  `fecha_fin` DATE NOT NULL,
  `valor_mensualidad` DECIMAL(10,2) NOT NULL,
  `estado_suscripcion` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_suscripciones_cliente`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `parking_como_en_casa`.`cliente` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_suscripciones_vehiculos`
    FOREIGN KEY (`id_placa`)
    REFERENCES `parking_como_en_casa`.`vehiculos` (`placa`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX `fk_suscripciones_cliente_idx` ON `parking_como_en_casa`.`suscripciones_mensuales` (`id_cliente` ASC);

CREATE INDEX `fk_suscripciones_vehiculos_idx` ON `parking_como_en_casa`.`suscripciones_mensuales` (`id_placa` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`espacio_parqueo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`espacio_parqueo` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`espacio_parqueo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `codigo_espacio` VARCHAR(10) NOT NULL,
  `estado_actual` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `codigo_espacio_UNIQUE` ON `parking_como_en_casa`.`espacio_parqueo` (`codigo_espacio` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`metodo_pago`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`metodo_pago` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`metodo_pago` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre_metodo` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `nombre_metodo_UNIQUE` ON `parking_como_en_casa`.`metodo_pago` (`nombre_metodo` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`cajas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`cajas` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`cajas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `fecha_hora_apertura` DATETIME NOT NULL,
  `fecha_hora_cierre` DATETIME NULL,
  `monto_inicial` DECIMAL(10,2) NOT NULL,
  `monto_final_calculado` DECIMAL(10,2) NULL,
  `monto_final_real` DECIMAL(10,2) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_cajas_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `parking_como_en_casa`.`usuario` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX `fk_cajas_usuario_idx` ON `parking_como_en_casa`.`cajas` (`id_usuario` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`registros_parqueo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`registros_parqueo` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`registros_parqueo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_placa` VARCHAR(10) NOT NULL,
  `id_usuario` INT NOT NULL,
  `id_suscripcion` INT NULL,
  `id_espacio_parqueo` INT NULL,
  `fecha_hora_ingreso` DATETIME NOT NULL,
  `fecha_hora_salida` DATETIME NULL,
  `tiempo_permanencia` INT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_registros_vehiculos`
    FOREIGN KEY (`id_placa`)
    REFERENCES `parking_como_en_casa`.`vehiculos` (`placa`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_registros_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `parking_como_en_casa`.`usuario` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_registros_suscripcion`
    FOREIGN KEY (`id_suscripcion`)
    REFERENCES `parking_como_en_casa`.`suscripciones_mensuales` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_registros_espacio`
    FOREIGN KEY (`id_espacio_parqueo`)
    REFERENCES `parking_como_en_casa`.`espacio_parqueo` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE INDEX `fk_registros_vehiculos_idx` ON `parking_como_en_casa`.`registros_parqueo` (`id_placa` ASC);

CREATE INDEX `fk_registros_usuario_idx` ON `parking_como_en_casa`.`registros_parqueo` (`id_usuario` ASC);

CREATE INDEX `fk_registros_suscripcion_idx` ON `parking_como_en_casa`.`registros_parqueo` (`id_suscripcion` ASC);

CREATE INDEX `fk_registros_espacio_idx` ON `parking_como_en_casa`.`registros_parqueo` (`id_espacio_parqueo` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`comprobantes_pago`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`comprobantes_pago` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`comprobantes_pago` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_registro_parqueo` INT NULL,
  `id_suscripcion` INT NULL,
  `id_metodo_pago` INT NOT NULL,
  `numero_ticket` VARCHAR(20) NOT NULL,
  `subtotal` DECIMAL(10,2) NOT NULL,
  `impuestos` DECIMAL(10,2) NULL,
  `total_pagado` DECIMAL(10,2) NOT NULL,
  `fecha_hora_pago` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_comprobantes_registro`
    FOREIGN KEY (`id_registro_parqueo`)
    REFERENCES `parking_como_en_casa`.`registros_parqueo` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_comprobantes_suscripcion`
    FOREIGN KEY (`id_suscripcion`)
    REFERENCES `parking_como_en_casa`.`suscripciones_mensuales` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_comprobantes_metodo_pago`
    FOREIGN KEY (`id_metodo_pago`)
    REFERENCES `parking_como_en_casa`.`metodo_pago` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `numero_ticket_UNIQUE` ON `parking_como_en_casa`.`comprobantes_pago` (`numero_ticket` ASC);

CREATE INDEX `fk_comprobantes_registro_idx` ON `parking_como_en_casa`.`comprobantes_pago` (`id_registro_parqueo` ASC);

CREATE INDEX `fk_comprobantes_suscripcion_idx` ON `parking_como_en_casa`.`comprobantes_pago` (`id_suscripcion` ASC);

CREATE INDEX `fk_comprobantes_metodo_pago_idx` ON `parking_como_en_casa`.`comprobantes_pago` (`id_metodo_pago` ASC);


-- -----------------------------------------------------
-- Table `parking_como_en_casa`.`auditoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `parking_como_en_casa`.`auditoria` ;

CREATE TABLE IF NOT EXISTS `parking_como_en_casa`.`auditoria` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `accion_realizada` VARCHAR(225) NOT NULL,
  `tabla_afectada` VARCHAR(50) NULL,
  `descripcion_detalle` TEXT NULL,
  `fecha_hora_accion` DATETIME NOT NULL,
  `direccion_ip` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_auditoria_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `parking_como_en_casa`.`usuario` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `fk_auditoria_usuario_idx` ON `parking_como_en_casa`.`auditoria` (`id_usuario` ASC);

-- =====================================================
-- Restricciones adicionales de validación
-- =====================================================

ALTER TABLE `parking_como_en_casa`.`tarifas`
ADD CONSTRAINT `chk_tarifas_valor`
CHECK (`valor_unidad` >= 0);

ALTER TABLE `parking_como_en_casa`.`suscripciones_mensuales`
ADD CONSTRAINT `chk_suscripciones_valor`
CHECK (`valor_mensualidad` >= 0);

ALTER TABLE `parking_como_en_casa`.`suscripciones_mensuales`
ADD CONSTRAINT `chk_suscripciones_fechas`
CHECK (`fecha_fin` >= `fecha_inicio`);

ALTER TABLE `parking_como_en_casa`.`espacio_parqueo`
ADD CONSTRAINT `chk_espacio_estado`
CHECK (`estado_actual` IN ('DISPONIBLE', 'OCUPADO', 'FUERA_DE_SERVICIO'));

ALTER TABLE `parking_como_en_casa`.`registros_parqueo`
ADD CONSTRAINT `chk_registros_fecha_salida`
CHECK (`fecha_hora_salida` IS NULL OR `fecha_hora_salida` >= `fecha_hora_ingreso`);

ALTER TABLE `parking_como_en_casa`.`registros_parqueo`
ADD CONSTRAINT `chk_registros_permanencia`
CHECK (`tiempo_permanencia` IS NULL OR `tiempo_permanencia` >= 0);

ALTER TABLE `parking_como_en_casa`.`comprobantes_pago`
ADD CONSTRAINT `chk_comprobantes_subtotal`
CHECK (`subtotal` >= 0);

ALTER TABLE `parking_como_en_casa`.`comprobantes_pago`
ADD CONSTRAINT `chk_comprobantes_impuestos`
CHECK (`impuestos` IS NULL OR `impuestos` >= 0);

ALTER TABLE `parking_como_en_casa`.`comprobantes_pago`
ADD CONSTRAINT `chk_comprobantes_total`
CHECK (`total_pagado` >= 0);

ALTER TABLE `parking_como_en_casa`.`comprobantes_pago`
ADD CONSTRAINT `chk_comprobantes_origen`
CHECK (`id_registro_parqueo` IS NOT NULL OR `id_suscripcion` IS NOT NULL);

ALTER TABLE `parking_como_en_casa`.`cajas`
ADD CONSTRAINT `chk_cajas_monto_inicial`
CHECK (`monto_inicial` >= 0);

ALTER TABLE `parking_como_en_casa`.`cajas`
ADD CONSTRAINT `chk_cajas_monto_calculado`
CHECK (`monto_final_calculado` IS NULL OR `monto_final_calculado` >= 0);

ALTER TABLE `parking_como_en_casa`.`cajas`
ADD CONSTRAINT `chk_cajas_monto_real`
CHECK (`monto_final_real` IS NULL OR `monto_final_real` >= 0);

ALTER TABLE `parking_como_en_casa`.`cajas`
ADD CONSTRAINT `chk_cajas_fecha_cierre`
CHECK (`fecha_hora_cierre` IS NULL OR `fecha_hora_cierre` >= `fecha_hora_apertura`);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
