package mx.com.qtx.testbd;

import java.sql.SQLException;

import mx.com.qtx.persistencia.GestorBD;

public class J_LlamarStoredProcedure {

	public static void main(String[] args) {
		 try {
			 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
			 	
			 	int cantArticulos = gestorBD.recuperarCuantosArticulosHayEnBD();
			 	System.out.println("Hay "+cantArticulos+" articulos dados de alta en la base de datos");
		 }
		 catch (SQLException e) {
			 GestorBD.mostrarSQLException(e);
			 System.exit(-1);
		 }

	}

}
