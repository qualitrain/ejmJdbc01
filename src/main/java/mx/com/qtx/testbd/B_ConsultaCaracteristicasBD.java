package mx.com.qtx.testbd;

import java.sql.SQLException;

import mx.com.qtx.persistencia.GestorBD;

public class B_ConsultaCaracteristicasBD {

	public static void main(String[] args) {
		// Ejemplo que muestra las caracteristicas soportadas
		// por el driver JDBC a traves del uso de la clase DatabaseMetaData
		 try {
			 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
			 	gestorBD.mostrarCaracteristicasBD();
		 }
		 catch (SQLException e) {
			 GestorBD.mostrarSQLException(e);
			 System.exit(-1);
		 }
	}

}
