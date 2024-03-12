package mx.com.qtx.testbd;

import java.sql.SQLException;
import java.util.HashMap;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.persistencia.GestorBD;

public class G_BuscarArticulosXdescripcion {

	public static void main(String[] args) {
		// EJEMPLO de una busqueda parametrizada usando PreparedStatement y querys con parametros
	
		String patronDescripcion = "%ford%";
		try{
		 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
		 	
			System.out.println("============================= Valores UNIVERSO de busqueda ================================");
			HashMap <String,Articulo> listaArticulos = gestorBD.recuperarArticulosTodos();
			for(Articulo art:listaArticulos.values())
				System.out.println(art);

			listaArticulos = gestorBD.buscarArticulosXdescripcion(patronDescripcion);
			
			System.out.println("============================= Valores ENCONTRADOS ================================");
			for(Articulo art:listaArticulos.values())
				System.out.println(art);
		}
		catch(SQLException ex){
			GestorBD.mostrarSQLException(ex);
		}

	}

}
