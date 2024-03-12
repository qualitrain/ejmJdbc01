package mx.com.qtx.testbdJ17;

import java.sql.SQLException;

import mx.com.qtx.persistencia.GestorBD_J17;

public class BJ17_ConsultaCaracteristicasBD {

	public static void main(String[] args) {
		// Ejemplo que muestra las caracteristicas soportadas
		// por el driver JDBC a traves del uso de la clase DatabaseMetaData
		 try {
			 	GestorBD_J17 gestorBD = new GestorBD_J17("mysql","ejemplosJDBC");
			 	gestorBD.mostrarCaracteristicasBD();
		 }
		 catch (SQLException e) {
			 GestorBD_J17.mostrarSQLException(e);
			 System.exit(-1);
		 }
	}

}
