package mx.com.qtx.testbdJ17;

import java.sql.SQLException;
import java.util.List;

import mx.com.qtx.entidades.GrupoPersonas;
import mx.com.qtx.entidades.Persona;
import mx.com.qtx.persistencia.Config;
import mx.com.qtx.persistencia.GestorBD_J17;

public class A2J17_ConsultaPersonasPrepStmt {

	public static void main(String[] args) {
		// Ejemplo de adquisicion de la conexion a la base de datos,
		// de la instanciacion del objeto de transporte PreparedStatement
		// Y de la lectura del resultado a partir del objeto ResultSet
		 
		 try {
		 	GestorBD_J17 gestorBD = new GestorBD_J17(Config.DBMS,"ejemplosjdbc");
		    List<Persona> lstPersonas = gestorBD.recuperarPersonasConNombreQueContiene("Ana");
		    lstPersonas.forEach(perI -> System.out.println(perI.getNombre()));
		 }
		 catch (SQLException e) {
			 GestorBD_J17.mostrarSQLException(e);
			 System.exit(-1);
		 }
	}
}

