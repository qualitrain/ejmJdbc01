package mx.com.qtx.testbdJ17;

import java.sql.SQLException;

import mx.com.qtx.persistencia.GestorBD_J17;

public class JJ17_LlamarStoredProcedure {

	public static void main(String[] args) {
		 try {
			 	GestorBD_J17 gestorBD = new GestorBD_J17(Config.DBMS,"ejemplosjdbc");
			 	
			 	int cantArticulos = gestorBD.recuperarCuantosArticulosHayEnBD();
			 	System.out.println("Hay " + cantArticulos + " articulos dados de alta en la base de datos");
		 }
		 catch (SQLException e) {
			 GestorBD_J17.mostrarSQLException(e);
			 System.exit(-1);
		 }

	}

}
