package mx.com.qtx.testbdJ17;

import java.sql.SQLException;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.persistencia.Config;
import mx.com.qtx.persistencia.GestorBD_J17;

public class E2J17_ActualizarArticulos {

	public static void main(String[] args) {
		// Este ejemplo muestra la actualizacion de un renglon especifico del DBMS.
		// Los principales puntos de atencion son la recuperacion por id del renglon
		// a traves de GestorBD.recuperarArticuloXidPstmt y GestorBD.actualizarArticuloPstmt.
		// Este ultimo usa el metodo executeUpdate de la clase PreparedStatement para efectuar
		// la actualizacion.
		//
		try {
		 	GestorBD_J17 gestorBD = new GestorBD_J17(Config.DBMS,"ejemplosjdbc");
		 	
			Articulo articulo = gestorBD.recuperarArticuloXidPstmt("X-1");
			System.out.println("============================= Valores antes de actualizacion ================================");
			System.out.println(articulo);

			String nuevaDescripcion = "+ " + articulo.getDescripcion() + " +";
			articulo.setDescripcion(nuevaDescripcion);
			gestorBD.actualizarArticuloPstmt(articulo);
			
			System.out.println("\n============================= Valores DESPUES de actualizacion ================================");
			articulo = gestorBD.recuperarArticuloXidPstmt("X-1");
			System.out.println(articulo);
		}
		catch(SQLException ex){
			GestorBD_J17.mostrarSQLException(ex);
		}
	}

}
