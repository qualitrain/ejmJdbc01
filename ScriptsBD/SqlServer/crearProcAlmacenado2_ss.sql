USE ejemplosjdbc;
GO

IF OBJECT_ID('dbo.calcula_vtasXvendedor', 'P') IS NOT NULL
    DROP PROCEDURE dbo.calcula_vtasXvendedor;
GO

CREATE PROCEDURE dbo.calcula_vtasXvendedor
    @id_persona INT,
    @resultado INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT @resultado = SUM(CANTIDAD * PRECIO_UNITARIO)
    FROM DETALLE_VENTA dv
    JOIN VENTA v ON dv.NUM_VENTA = v.NUM_VENTA
    WHERE v.ID_PERSONA_VENDEDOR = @id_persona;
END;
GO