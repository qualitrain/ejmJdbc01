USE `ejemplosjdbc`;
DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `calcula_vtasXvendedor`(IN id_persona INT, OUT resultado INT)
BEGIN
    SELECT SUM(DET.CANTIDAD * DET.PRECIO_UNITARIO) INTO resultado
    FROM DETALLE_VENTA AS DET, VENTA AS VTA
    WHERE 
        DET.NUM_VENTA = VTA.NUM_VENTA AND
        VTA.ID_PERSONA_VENDEDOR = id_persona;
END$$

DELIMITER ;