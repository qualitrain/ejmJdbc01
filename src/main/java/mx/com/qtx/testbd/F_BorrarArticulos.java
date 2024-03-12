package mx.com.qtx.testbd;


import java.sql.SQLException;
import java.util.HashMap;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.persistencia.GestorBD;

public class F_BorrarArticulos {

	public static void main(String[] args) {
		// Este ejemplo muestra la eliminacion de un renglo en particular del DBMS.
		// Especial atencion a GestorBD.eliminarArticulo, el cual utiliza el metodo executeUpdate de
		// la clase Statement para llevar a cabo la eliminacion.
		//
		try {
		 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
		 	
			Articulo articulo = gestorBD.recuperarArticuloXid("D-EFR-34X");
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
			GestorBD.mostrarSQLException(ex);
		}

	}

}
