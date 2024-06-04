USE ejemplosjdbc;
GO

IF OBJECT_ID('dbo.CUENTA_ARTICULOS', 'P') IS NOT NULL
    DROP PROCEDURE dbo.CUENTA_ARTICULOS;
GO

CREATE PROCEDURE dbo.CUENTA_ARTICULOS
    @resultado INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT @resultado = COUNT(*)
    FROM articulo;
END;
GO