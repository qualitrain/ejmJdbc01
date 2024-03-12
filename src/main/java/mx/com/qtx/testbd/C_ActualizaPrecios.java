package mx.com.qtx.testbd;

import java.sql.SQLException;
import java.util.HashMap;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.persistencia.GestorBD;

public class C_ActualizaPrecios {

	public static void main(String[] args) {
		// Ejemplo sobre manejo de ResultSet para ACTUALIZAR
		// una columna especifica en el DBMS en un grupo de renglones. El metodo principal de
		// atencion para el estudiante es el de factorizarPrecioArticulos,
		// el cual utiliza los metodos updateFloat y updateRow de ResultSet,
		// asi como variaciones en la creacion del Statement para el manejo de concurrencia.
		//
		// Este ejemplo combina la lectura de un grupo de renglones con su actualizacion

		try {
		 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
		 	
			HashMap <String,Articulo> listaArticulos = gestorBD.recuperarArticulosTodos();
			
			System.out.println("==== Valores antes de factorizar ====");
			for(Articulo art:listaArticulos.values())
				System.out.println(art);
			
			float FACTOR_INCREMENTO = 1.5f;
			
			gestorBD.factorizarPrecioArticulos(FACTOR_INCREMENTO);
			listaArticulos = gestorBD.recuperarArticulosTodos();
			System.out.println("==== Valores DESPUES de factorizar por " + FACTOR_INCREMENTO
					+"====");
			for(Articulo art:listaArticulos.values())
				System.out.println(art);
		}
		catch(SQLException ex){
			GestorBD.mostrarSQLException(ex);
		}
	}

}
