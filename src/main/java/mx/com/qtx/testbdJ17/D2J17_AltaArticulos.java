package mx.com.qtx.testbdJ17;

import java.sql.SQLException;
import java.util.HashMap;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.persistencia.Config;
import mx.com.qtx.persistencia.GestorBD_J17;

public class D2J17_AltaArticulos {

	public static void main(String[] args) {
		// Ejemplo que muestra la INSERCION de un renglon en el DBMS.
		// Los metodos de atencion para el estudiante son: 
		// GestorBD.insertarArticuloPstmt() que usa el metodo executeUpdate de la clase PreparedStatement
		
		Articulo nuevoArticulo = new Articulo("D-EFR-PSTMT","Filtro Aire Ford Bronco 2024",562.5f,845.34f);
		try {
		 	GestorBD_J17 gestorBD = new GestorBD_J17(Config.DBMS,"ejemplosjdbc");
		 	
			HashMap <String,Articulo> listaArticulos = gestorBD.recuperarArticulosTodos();
			System.out.println("============================= Valores antes de insercion ================================");
			for(Articulo art:listaArticulos.values())
				System.out.println(art);

			gestorBD.insertarArticuloPstmt(nuevoArticulo);
			
			listaArticulos = gestorBD.recuperarArticulosTodos();
			System.out.println("============================= Valores DESPUES de insercion ================================");
			for(Articulo art:listaArticulos.values())
				System.out.println(art);
		}
		catch(SQLException ex){
			GestorBD_J17.mostrarSQLException(ex);
		}

	}

}
