package mx.com.qtx.testbd;

import java.sql.SQLException;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.persistencia.GestorBD;

public class E_ActualizarArticulos {

	public static void main(String[] args) {
		// Este ejemplo muestra la actualizacion de un renglon especifico del DBMS.
		// Los principales puntos de atencion son la recuperacion por id del renglon
		// a traves de GestorBD.recuperarArticuloXid y GestorBD.actualizarArticulo.
		// Este ultimo usa el metodo executeUpdate de la clase Statement para efectuar
		// la actualizacion.
		//
		try {
		 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
		 	
			Articulo articulo = gestorBD.recuperarArticuloXid("X-1");
			System.out.println("============================= Valores antes de actualizacion ================================");
			System.out.println(articulo);

			String nuevaDescripcion = "*** "+articulo.getDescripcion()+" ***";
			articulo.setDescripcion(nuevaDescripcion);
			gestorBD.actualizarArticulo(articulo);
			
			System.out.println("============================= Valores DESPUES de actualizaci�n ================================");
			articulo = gestorBD.recuperarArticuloXid("X-1");
			System.out.println(articulo);
		}
		catch(SQLException ex){
			GestorBD.mostrarSQLException(ex);
		}

	}

}
