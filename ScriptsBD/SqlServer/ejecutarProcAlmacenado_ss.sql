-- Declarar una variable para el parámetro de salida
DECLARE @resultado INT;

-- Ejecutar el procedimiento almacenado
EXEC dbo.CUENTA_ARTICULOS @resultado = @resultado OUTPUT;

-- Mostrar el resultado
SELECT @resultado AS TotalArticulos;