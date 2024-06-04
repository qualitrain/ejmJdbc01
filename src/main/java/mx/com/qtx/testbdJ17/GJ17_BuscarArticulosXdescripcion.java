package mx.com.qtx.testbdJ17;

import java.sql.SQLException;
import java.util.HashMap;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.persistencia.Config;
import mx.com.qtx.persistencia.GestorBD_J17;

public class GJ17_BuscarArticulosXdescripcion {

	public static void main(String[] args) {
		// EJEMPLO de una busqueda parametrizada usando PreparedStatement y querys con parametros
	
		String patronDescripcion = "%ford%";
		try{
		 	GestorBD_J17 gestorBD = new GestorBD_J17(Config.DBMS,"ejemplosjdbc");
		 	
			System.out.println("============================= Valores UNIVERSO de busqueda ================================");
			HashMap <String,Articulo> listaArticulos = gestorBD.recuperarArticulosTodos();
			for(Articulo art:listaArticulos.values())
				System.out.println(art);

			listaArticulos = gestorBD.buscarArticulosXdescripcion(patronDescripcion);
			
			System.out.println("\n============================= Valores ENCONTRADOS ================================");
			for(Articulo art:listaArticulos.values())
				System.out.println(art);
		}
		catch(SQLException ex){
			GestorBD_J17.mostrarSQLException(ex);
		}

	}

}
