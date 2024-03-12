package mx.com.qtx.testbd;

import java.sql.SQLException;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.entidades.Persona;
import mx.com.qtx.entidades.Venta;
import mx.com.qtx.persistencia.GestorBD;

public class H_RegistrarVenta_NOTRANSACC {
	public static void main(String[] args) {
		// EJEMPLO de la insercion de una transaccion de varias tablas, que no se comporta transaccionalmente
		// Y que ademas hace uso de indices auto-generados por la base de datos
		
		try{
		 	GestorBD gestorBD = new GestorBD("mysql","ejemplosJDBC");
		 	
			Persona unVendedor = gestorBD.recuperarPersonaXid(1);
			Persona unCliente = gestorBD.recuperarPersonaXid(2);
			
			Venta nuevaVenta = new Venta(0,null,unCliente,unVendedor);
			Articulo articulo1 = gestorBD.recuperarArticuloXid("A-23");
			nuevaVenta.agregarDetalle(1, 5, articulo1,articulo1.getPrecioLista());
			
			Articulo articulo2 = gestorBD.recuperarArticuloXid("A-24");
			nuevaVenta.agregarDetalle(2, 5, articulo2,articulo2.getPrecioLista());
			
			Articulo articulo3 = gestorBD.recuperarArticuloXid("X-1");
			nuevaVenta.agregarDetalle(3, 8, articulo3,articulo3.getPrecioLista());
			
			Articulo articulo4 = gestorBD.recuperarArticuloXid("X-2");
			nuevaVenta.agregarDetalle(4, 6, articulo4,articulo4.getPrecioLista());

			Articulo articulo5 = gestorBD.recuperarArticuloXid("D-EFR-34X");
			nuevaVenta.agregarDetalle(5, 4, articulo5,articulo5.getPrecioLista());

			int numVenta = gestorBD.insertarVentaNoTransaccional(nuevaVenta);
			
			// Probando con la recuperacion de la venta x id ...
			Venta ventaInsertada = gestorBD.recuperarVentaXid(numVenta);
			ventaInsertada.mostrar();
			
			}
		catch(SQLException ex){
			GestorBD.mostrarSQLException(ex);
		}

	} 

}
