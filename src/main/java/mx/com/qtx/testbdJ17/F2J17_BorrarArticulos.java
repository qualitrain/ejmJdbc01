package mx.com.qtx.testbdJ17;


import java.sql.SQLException;
import java.util.HashMap;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.persistencia.Config;
import mx.com.qtx.persistencia.GestorBD_J17;

public class F2J17_BorrarArticulos {

	public static void main(String[] args) {
		// Este ejemplo muestra la eliminacion de un renglon en particular del DBMS.
		// Especial atencion a GestorBD.eliminarArticulo, el cual utiliza el metodo executeUpdate de
		// la clase PreparedStatement para llevar a cabo la eliminacion.
		//
		try {
		 	GestorBD_J17 gestorBD = new GestorBD_J17(Config.DBMS,"ejemplosjdbc");
		 	
			Articulo articulo = gestorBD.recuperarArticuloXidPstmt("D-EFR-PSTMT");
			System.out.println("============================= Valores antes de borrar ================================");
			HashMap <String,Articulo> listaArticulos = gestorBD.recuperarArticulosTodos();
			for(Articulo art:listaArticulos.values())
				System.out.println(art);
			
			gestorBD.eliminarArticulo(articulo);
			
			System.out.println("============================= Valores DESPUES de borrar ================================");
			listaArticulos = gestorBD.recuperarArticulosTodos();
			for(Articulo art:listaArticulos.values())
				System.out.println(art);
		}
		catch(SQLException ex){
			GestorBD_J17.mostrarSQLException(ex);
		}

	}

}
