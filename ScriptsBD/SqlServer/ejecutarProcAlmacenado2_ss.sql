DECLARE @resultado INT;

-- Asignar un valor al par�metro de entrada @id_persona, por ejemplo, 1
EXEC dbo.calcula_vtasXvendedor @id_persona = 1, @resultado = @resultado OUTPUT;

-- Mostrar el resultado
SELECT @resultado AS TotalVentas;