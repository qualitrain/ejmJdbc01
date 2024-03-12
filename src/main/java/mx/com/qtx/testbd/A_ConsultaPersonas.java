package mx.com.qtx.testbd;

import java.sql.SQLException;

import mx.com.qtx.entidades.GrupoPersonas;
import mx.com.qtx.entidades.Persona;
import mx.com.qtx.persistencia.GestorBD;

public class A_ConsultaPersonas {

	public static void main(String[] args) {
		// Ejemplo de adquisicion de la conexion a la base de datos,
		// de la instanciacion del objeto de transporte Statement
		// Y de la lectura del resultado a partir del objeto ResultSet
		 
		 try {
		 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
		    GrupoPersonas unGrupoPersonas = gestorBD.recuperarPersonasTodas();
		    unGrupoPersonas.mostrar();
		    
		    Persona unaPersona = unGrupoPersonas.getPersonaPorID(2);
		    System.out.println("La persona con id 2 se llama "+ unaPersona.getNombre());
		 }
		 catch (SQLException e) {
			 GestorBD.mostrarSQLException(e);
			 System.exit(-1);
		 }
	}
}

