package mx.com.qtx.testbd;

import java.sql.SQLException;
import java.text.NumberFormat;

import mx.com.qtx.persistencia.GestorBD;

public class K_LlamarStoredProcedure2 {
	public static void main(String[] args) {
		 try {
			 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
			 	
			 	int numVendedor=1;
			 	long vtasAcumuladas = gestorBD.recuperarImporteAcumuladoVtasxVendedor(numVendedor);
			 	String vtasFormateadas = NumberFormat.getCurrencyInstance().format(vtasAcumuladas);
			 	System.out.println("El vendedor numero " + numVendedor + " tiene ventas por un total de"
			 			+ " " + vtasFormateadas);
			 }
			 catch (SQLException e) {
				 GestorBD.mostrarSQLException(e);
				 System.exit(-1);
			 }

	}

}
