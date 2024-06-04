package mx.com.qtx.testbdJ17;

import java.sql.SQLException;
import java.text.NumberFormat;

import mx.com.qtx.persistencia.Config;
import mx.com.qtx.persistencia.GestorBD_J17;

public class KJ17_LlamarStoredProcedure2 {
	public static void main(String[] args) {
		 try {
			 	GestorBD_J17 gestorBD = new GestorBD_J17(Config.DBMS,"ejemplosjdbc");
			 	
			 	int numVendedor=1;
			 	long vtasAcumuladas = gestorBD.recuperarImporteAcumuladoVtasxVendedor(numVendedor);
			 	String vtasFormateadas = NumberFormat.getCurrencyInstance()
			 			                             .format(vtasAcumuladas);
			 	System.out.println("El vendedor numero " + numVendedor 
			 			          + " tiene ventas por un total de" + " " + vtasFormateadas);
			 }
			 catch (SQLException e) {
				 GestorBD_J17.mostrarSQLException(e);
				 System.exit(-1);
			 }
	}

}
