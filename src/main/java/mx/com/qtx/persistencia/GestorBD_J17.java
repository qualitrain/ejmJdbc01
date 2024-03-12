package mx.com.qtx.persistencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import mx.com.qtx.entidades.Articulo;
import mx.com.qtx.entidades.DetalleVenta;
import mx.com.qtx.entidades.GrupoPersonas;
import mx.com.qtx.entidades.Persona;
import mx.com.qtx.entidades.Venta;
/**
 * 
 * Esta versión es para Java 11+ 
 * Usa el try-catch with resources, lo que simplifica mucho la programación de servicios de datos
 * 
 * @author Alejandro Cruz Rojas
 *
 */
public class GestorBD_J17 {
		private String dbms = "mysql";
		private String usuarioBD = "root";
		private String passwordBD = "root";
		private String nombreServidorBD = "localhost";
		private String puertoBD = "";
		private String nombreBD = "ejemplosJDBC";
		private boolean crearBdSiNoExiste=false;

		public GestorBD_J17(String dbms, String nombreBD) throws SQLException {
			super();
			this.dbms = dbms;
			this.nombreBD = nombreBD;
			
			switch(dbms) {
				case "mysql":
					this.puertoBD = "3306";
					break;
				case "sqlserver":
					this.puertoBD = "1433";
					break;
			}
		}

		public String getDbms() {
			return dbms;
		}

		public void setDbms(String dbms) {
			this.dbms = dbms;
		}

		public String getUsuarioBD() {
			return usuarioBD;
		}

		public void setUsuarioBD(String usuarioBD) {
			this.usuarioBD = usuarioBD;
		}

		public String getPasswordBD() {
			return passwordBD;
		}

		public void setPasswordBD(String passwordBD) {
			this.passwordBD = passwordBD;
		}

		public String getNombreServidorBD() {
			return nombreServidorBD;
		}

		public void setNombreServidorBD(String nombreServidorBD) {
			this.nombreServidorBD = nombreServidorBD;
		}

		public String getPuertoBD() {
			return puertoBD;
		}

		public void setPuertoBD(String puertoBD) {
			this.puertoBD = puertoBD;
		}

		public String getNombreBD() {
			return nombreBD;
		}

		public void setNombreBD(String nombreBD) {
			this.nombreBD = nombreBD;
		}

		public boolean isCrearBdSiNoExiste() {
			return crearBdSiNoExiste;
		}

		public void setCrearBdSiNoExiste(boolean crearBdSiNoExiste) {
			this.crearBdSiNoExiste = crearBdSiNoExiste;
		}

		public Connection getConexionBD() throws SQLException {

		    Properties propiedadesConexion = new Properties();
		    propiedadesConexion.put("user", usuarioBD);
		    propiedadesConexion.put("password", passwordBD);

		    String url;
		    switch(this.dbms) {
			    case "mysql":
			    	url = "jdbc:" + dbms + "://" 
			              + nombreServidorBD 
			              + ":" + puertoBD 
			              + "/" + nombreBD
			              + "?"
			              + "serverTimezone=UTC"
			              + "&createDatabaseIfNotExist="
			              + (this.crearBdSiNoExiste ? "true": "false");
			    	
			    	return DriverManager.getConnection(url, propiedadesConexion);
			    	
			    case "derby":
			    	url = "jdbc:" 
			              + dbms 
			              + ":" 
			              + nombreServidorBD 
			              + ";create=true";
			    	
			    	return DriverManager.getConnection(url, propiedadesConexion);
			    	
			    case "sqlserver":
			    	url = "jdbc:"
			    			+ this.dbms
			    			+ "://"
			    			+ this.nombreServidorBD
			    			+ ":"
			    			+ this.puertoBD
			    			+ ";"
			    			+ "databaseName=" + this.nombreBD
			    			+ ";"
			    			+ "encrypt=true"
			    			+ ";"
			    			+ "trustServerCertificate=true" 
			    			+ ";";

			    	System.out.println(url);
			    	return DriverManager.getConnection(url,"sa","el3f4ant3R*sa");
			    	
			    default:
			    	return null;
		    }
		}

		public GrupoPersonas recuperarPersonasTodas()throws SQLException {

			final String SQL = "Select * from persona";
			
			GrupoPersonas grupoPersonas = new GrupoPersonas();
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement()){
					try (ResultSet resultSet = stmt.executeQuery(SQL)) {
			        	int idPersona;
			        	String nombre;
			        	String direccion;
			        	Date fechaNacimiento;
	
				        while (resultSet.next()) {
				        	idPersona = resultSet.getInt("id_persona");
				        	nombre = resultSet.getString("nombre");
				        	direccion = resultSet.getString("direccion");
				        	fechaNacimiento = resultSet.getDate("fecha_nacimiento");
				        	
				        	grupoPersonas.agregarPersona(new Persona(idPersona, nombre, direccion,fechaNacimiento));
				        }
					}
				}
			}
			return grupoPersonas;
		}

		public void mostrarCaracteristicasBD() throws SQLException{
			try (Connection conexionBD = this.getConexionBD()){
				DatabaseMetaData dbMetaData = conexionBD.getMetaData();
				// Se checa el tipo de ResultSet que soporta la base de datos
				System.out.println("========== Sobre ResultSet =============");
				if (dbMetaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY)) {
		              System.out.println("Tipo de ResultSet Soportado: TYPE_FORWARD_ONLY");
				}
				if (dbMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)) {
		             System.out.println("Tipo de ResultSet Soportado: TYPE_SCROLL_INSENSITIVE");
				}
				if (dbMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE)) {
		             System.out.println("Tipo de ResultSet Soportado: TYPE_SCROLL_SENSITIVE");
				} 

				if(dbMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY) ){
		             System.out.println("Concurrencia soportada: TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY");
				}
				if(dbMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) ){
		             System.out.println("Concurrencia soportada: TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE");
				}
				if(dbMetaData.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT)){
		             System.out.println("Apertura/cierre de cursores soportada: HOLD_CURSORS_OVER_COMMIT");
				}
				if(dbMetaData.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT)){
		             System.out.println("Apertura/cierre de cursores soportada: CLOSE_CURSORS_AT_COMMIT");
				}
				
				System.out.println("========== Sobre Transaccionalidad =============");
				
				if(dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_NONE)){
					System.out.println("Se soporta en nivel de aislamiento trasaccional TRANSACTION_NONE");
				}
				if(dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_COMMITTED)){
					System.out.println("Se soporta en nivel de aislamiento trasaccional TRANSACTION_READ_COMMITTED");
				}
				if(dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED)){
					System.out.println("Se soporta en nivel de aislamiento trasaccional TRANSACTION_READ_UNCOMMITTED");
				}
				if(dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ)){
					System.out.println("Se soporta en nivel de aislamiento trasaccional TRANSACTION_REPEATABLE_READ");
				}
				if(dbMetaData.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE)){
					System.out.println("Se soporta en nivel de aislamiento trasaccional TRANSACTION_SERIALIZABLE");
				}
			}
		}

		public HashMap<String,Articulo> recuperarArticulosTodos()throws SQLException {

			final String SQL = "Select * from articulo";

			HashMap<String,Articulo> grupoArticulos = new HashMap<String,Articulo>();
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement()){
					try (ResultSet resultSet = stmt.executeQuery(SQL)) {
			        	String cveArticulo;
			        	String descripcion;
			        	float costoProv1;
			        	float precioLista;
		
				        while (resultSet.next()) {
				        	cveArticulo = resultSet.getString(1);
				        	descripcion = resultSet.getString(2);
				        	costoProv1 = resultSet.getFloat(3);
				        	precioLista = resultSet.getFloat(4);
				        	
				        	grupoArticulos.put(cveArticulo, new Articulo(cveArticulo,descripcion,costoProv1,precioLista));
				        }
					}
				}
		    }
			
			return grupoArticulos;
		}

		public void factorizarPrecioArticulos(float factor) throws SQLException {

			final String SQL = "Select * from articulo";
			
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
		                   ResultSet.CONCUR_UPDATABLE)){
			        try(ResultSet resultSet = stmt.executeQuery(SQL)){		
			        	float precioLista;
		
				        while (resultSet.next()) {
				        	precioLista = resultSet.getFloat("precio_lista");
				        	resultSet.updateFloat("precio_lista", precioLista * factor);
				        	resultSet.updateRow();
				        }
			        }
				}
		    }
			
		}
		
		public int insertarArticulo(Articulo articulo) throws SQLException {
			int numAfectacionesBD = 0;
			
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement()){
					numAfectacionesBD=stmt.executeUpdate("Insert into articulo values ('"
															+ articulo.getCveArticulo()+"','"
															+ articulo.getDescripcion()+"',"
															+Float.toString(articulo.getCostoProv1())+","
															+Float.toString(articulo.getPrecioLista())+")");
				}
			}
			return numAfectacionesBD;
		}

		public Articulo recuperarArticuloXid(String cveArticulo) throws SQLException {
			
			Articulo articulo=null;
			
			try (Connection conexionBD = this.getConexionBD()){			
				try (Statement stmt = conexionBD.createStatement()){
					try (ResultSet resultSet = stmt.executeQuery("Select * from articulo where cve_Articulo ='"+
															cveArticulo+"'")){
							if(resultSet.next()){ // El cursor se avanza para posicionarlo en el renglon leido
								String descripcion = resultSet.getString("descripcion");
								float costoProv1 = resultSet.getFloat("costo_prov_1");
								float precioLista = resultSet.getFloat("precio_lista");
								articulo=new Articulo(cveArticulo,descripcion,costoProv1,precioLista);
							}
					}
				}
			}
			return articulo;
		}

		public int actualizarArticulo(Articulo articulo) throws SQLException {
			int numAfectacionesBD = 0;
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement()){
					numAfectacionesBD = 
							stmt.executeUpdate("Update articulo set "
											+ "descripcion ='" + articulo.getDescripcion() + "',"
											+ "costo_prov_1=" + Float.toString(articulo.getCostoProv1()) + ","
											+ "precio_lista=" + Float.toString(articulo.getPrecioLista()) 
											+ " where cve_articulo ='"+articulo.getCveArticulo() + "'");
				}
			}
			return numAfectacionesBD;
		}
		
		public int eliminarArticulo(Articulo articulo) throws SQLException {
			
			int numAfectacionesBD = 0;
			
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement()) {
					numAfectacionesBD = 
							stmt.executeUpdate("Delete from articulo where cve_articulo ='"
				                               + articulo.getCveArticulo() + "'");
				}
			}
			return numAfectacionesBD;
		}
		
		public HashMap<String,Articulo> buscarArticulosXdescripcion(String patronDescripcion) throws SQLException {
			HashMap<String,Articulo> mapArticulos = new HashMap<String,Articulo>();
			String SQL = "Select * from Articulo "
					    + "where descripcion like ?";
			//          + "and precio_lista > ? and precio_lista < ?");
			
			try (Connection conexionBD = this.getConexionBD()){
				try (PreparedStatement pStmt = conexionBD.prepareStatement(SQL)){
					pStmt.setString(1, patronDescripcion);
//					pStmt.setFloat(2, 1.0f);
//					pStmt.setFloat(3, 36.0f);
					try (ResultSet rsArticulos = pStmt.executeQuery()) {
						while(rsArticulos.next()){
							String cveArticulo = rsArticulos.getString("cve_articulo");
							String descripcion = rsArticulos.getString("descripcion");
							float costoProv1 = rsArticulos.getFloat("costo_prov_1");
							float precioLista = rsArticulos.getFloat("precio_lista");
							mapArticulos.put(cveArticulo, new Articulo(cveArticulo,descripcion,costoProv1,precioLista));
						}
					}
				}
			}
			return mapArticulos;
		}
		
		public Persona recuperarPersonaXid(int idPersona) throws SQLException {
			
			Persona persona=null; //Devuelve null si no encuentra el objeto
			String SQL = "Select * from persona "
					   + "where id_persona = ";
			
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement()) {
					try (ResultSet resultSet=stmt.executeQuery(SQL + idPersona)){
						if(resultSet.next()){ // El cursor se avanza para posicionarlo en el renglon le�do
							String nombre = resultSet.getString("nombre");			
							String direccion = resultSet.getString("direccion");
							Date fechaNacimiento = resultSet.getDate("fecha_nacimiento");

							persona=new Persona(idPersona,nombre,direccion,fechaNacimiento);
						}
					}
				}
			}
			return persona;			
		}
		
		public int insertarVentaNoTransaccional (Venta nuevaVenta)throws SQLException{
			// Inserta una relacion maestro-detalle con relaciones hacia otros objetos en la base de datos.
			// No observa comportamiento transaccional: Permite actualizar la mitad o parte de una transaccion
			// llevando a problemas de integridad en caso de error.
			// Tambien utiliza el concepto de llave auto-generada
			
			int numVenta = 0;
			
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmtInsercionVenta = conexionBD.createStatement()){
					String strInsertVenta="Insert into venta set " +
										"fecha_venta =  curdate()," + // La fecha la obtiene del servidor de base de datos usando una funcion SQL
										"id_persona_cte = " + nuevaVenta.getCliente().getIdPersona() + "," +
										"id_persona_vendedor = " + nuevaVenta.getVendedor().getIdPersona();
					
					stmtInsercionVenta.executeUpdate(strInsertVenta,Statement.RETURN_GENERATED_KEYS ); // Se requiere para pode recuperar los valores de las llaves auto-generadas
					try (ResultSet rsLlavesAutoGeneradas = stmtInsercionVenta.getGeneratedKeys()){
						this.mostrarLlavesAutoGeneradas(rsLlavesAutoGeneradas);
						numVenta = this.getValorLlaveAutoGenerada(rsLlavesAutoGeneradas, 1);
					}
					
					for(DetalleVenta detVta: nuevaVenta.getDetallesVta().values()){
						String strInsertDetalleVenta="Insert into detalle_venta set"+
													" num_venta ="+ numVenta +"," +
													"num_detalle =" + detVta.getNumDetalle() + "," +
													"cantidad =" + detVta.getCantidad() + "," +
													"cve_articulo ='" + detVta.getArticuloVendido().getCveArticulo() + "'," +
													"precio_unitario ="+ detVta.getArticuloVendido().getPrecioLista();
						stmtInsercionVenta.executeUpdate(strInsertDetalleVenta);			
					}
				}
			}
			return numVenta;
		}

		private void mostrarLlavesAutoGeneradas(ResultSet rsLlavesAutoGeneradas) 
				throws SQLException{ //El ResultSet debe haber sido obtenido llamando a Statement.getGeneratedKeys()
			if (rsLlavesAutoGeneradas.next()) {
		         ResultSetMetaData rsmd = rsLlavesAutoGeneradas.getMetaData();
		         int colCount = rsmd.getColumnCount();
		         do {
		             for (int i = 1; i <= colCount; i++) {
		                 String llave = rsLlavesAutoGeneradas.getString(i);
		                 System.out.println("la llave autogenerada en la columna " + i + " es " + llave);
		             }
		         }
		         while (rsLlavesAutoGeneradas.next());
			} 
			else {
		         System.out.println("No hay llaves auto-generadas");
			}
		}

		private int getValorLlaveAutoGenerada(ResultSet rsLlavesAutoGeneradas, int posicion) throws SQLException{ //El ResultSet debe haber sido obtenido llamando a Statement.getGeneratedKeys()
			rsLlavesAutoGeneradas.beforeFirst();
			if (rsLlavesAutoGeneradas.next()) {
				return rsLlavesAutoGeneradas.getInt(posicion);
			} 
			else {
		         return -1;
			}
		}
		
		public Venta recuperarVentaXid(int numVenta) throws SQLException {
			Venta venta=null;
			String SQL = "Select * from venta where num_venta =";
			
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement()){
					try (ResultSet resultSet=stmt.executeQuery(SQL + numVenta)) {
						if(resultSet.next()){ // El cursor se avanza para posicionarlo en el renglon leido
							Date fechaVenta = resultSet.getDate("fecha_venta");
							int idPersonaCte = resultSet.getInt("id_persona_cte");
							int idPersonaVendedor = resultSet.getInt("id_persona_vendedor");
							Persona personaCte = this.recuperarPersonaXid(idPersonaCte);
							Persona personaVendedor = this.recuperarPersonaXid(idPersonaVendedor);
							venta = new Venta(numVenta,fechaVenta,personaCte,personaVendedor);
						
							List<DetalleVenta> listaDetallesVenta = this.recuperarDetallesDeUnaVenta(numVenta);
							for(DetalleVenta det:listaDetallesVenta){
								int numDetalle = det.getNumDetalle();
								int cantidad = det.getCantidad();
								float precioUnitario = det.getPrecioUnitario();
								Articulo articulo = det.getArticuloVendido();
								venta.agregarDetalle(numDetalle, cantidad, articulo, precioUnitario);
							}
						}
					}
				}
			}
			return venta;
		}

		public List<DetalleVenta> recuperarDetallesDeUnaVenta(int numVenta) throws SQLException{
			ArrayList<DetalleVenta> listaDetallesVenta = new ArrayList<DetalleVenta>();
			String SQL = "Select * from detalle_venta " +
						 "where num_venta = ";
			
			try (Connection conexionBD = this.getConexionBD()){
				try (Statement stmt = conexionBD.createStatement()){
					try (ResultSet resultSet = stmt.executeQuery(SQL + numVenta))
					  {
						 while(resultSet.next()){
							int numDetalle = resultSet.getInt("num_detalle");
							int cantidad = resultSet.getInt("cantidad");
							String cveArticulo = resultSet.getString("cve_articulo");
							float precioUnitario = resultSet.getFloat("precio_unitario");
							
							listaDetallesVenta.add(new DetalleVenta(numDetalle, cantidad, new Articulo(cveArticulo), precioUnitario));
						}
					 }
				 }
			}
			
			for(DetalleVenta det:listaDetallesVenta){
				String cveArticulo = det.getArticuloVendido()
						                .getCveArticulo();
				det.setArticuloVendido(this.recuperarArticuloXid(cveArticulo));
			}
			
			return listaDetallesVenta;
		}
		
		public int insertarVentaTransaccional (Venta nuevaVenta) throws SQLException{
			// Inserta una relacion maestro-detalle con relaciones hacia otros objetos en la base de datos.
			// Tambien utiliza el concepto de llave auto-generada
			int numVenta = 0;
			
			try (Connection conexionBD = this.getConexionBD()){
				conexionBD.setAutoCommit(false); //********* SE ELIMINA COMPORTAMIENTO POR DEFECTO
				conexionBD.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED); // Nivel de aislamiento
				
				try (Statement stmtInsercionVenta = conexionBD.createStatement()){
					String strInsertVenta="Insert into venta set " +
										"fecha_venta =  curdate()," + 
										"id_persona_cte = " + nuevaVenta.getCliente().getIdPersona() + "," +
										"id_persona_vendedor = " + nuevaVenta.getVendedor().getIdPersona();
					
					stmtInsercionVenta.executeUpdate(strInsertVenta,Statement.RETURN_GENERATED_KEYS ); 
					try (ResultSet rsLlavesAutoGeneradas = stmtInsercionVenta.getGeneratedKeys()){
						numVenta = this.getValorLlaveAutoGenerada(rsLlavesAutoGeneradas,1);
					}
					
					for(DetalleVenta detVta: nuevaVenta.getDetallesVta().values()){
						String strInsertDetalleVenta="Insert into detalle_venta set"+
													" num_venta ="+ numVenta +"," +
													"num_detalle =" + detVta.getNumDetalle() + "," +
													"cantidad =" + detVta.getCantidad() + "," +
													"cve_articulo ='" + detVta.getArticuloVendido().getCveArticulo() + "'," +
													"precio_unitario ="+ detVta.getArticuloVendido().getPrecioLista();
						stmtInsercionVenta.executeUpdate(strInsertDetalleVenta);			
					}
					conexionBD.commit(); // *** EN ESTE MOMENTO SE ACTUALIZA TODA LA TRANSACCION
					conexionBD.setAutoCommit(true);
				}
				catch(SQLException ex){
					conexionBD.rollback(); // EN CASO DE EXCEPCION SE DEJA TODO COMO ESTABA
					conexionBD.setAutoCommit(true);
					throw ex;
				}
			}
			return numVenta;
		}

		public int recuperarCuantosArticulosHayEnBD() throws SQLException{ //Llama a un stored procedure!!
			int numArticulos = 0;
			
			String SQL = "{call "
					+ this.nombreBD
					+ ".CUENTA_ARTICULOS(?)}"; // Modifique para probar que sucede 
			                                                        // cuando se invoca un procedimiento inexistente
           // El nombre del procedimiento debe incluir el nombre de la base de datos -en algunas versiones
           // de Mysql (5.0)-
			
			try (Connection conexionBD = this.getConexionBD()){
				try (CallableStatement callStmt = conexionBD.prepareCall(SQL)){ 

		        /*
		         * El metodo GestorBD.mostrarMetaDatosStoreProcedure(callStmt) explora 
		         * -> los parametros recibidos de la base de datos
		         * -> la metainformacion de la base de datos.
		         * 
		         * Es util, porque cuando no se encuentra el procedimiento almacenado
		         * se lanza una excepcion (en Mysql) con un error con un mensaje ambiguo, que dice
		         * "No output parameters returned by procedure" 
		         * o alguno que indica que el parametro no es de tipo OUT:
		         * Parameter number 1 is not an OUT parameter, que NO indica que
		         * en realidad NO EXISTE el procedimiento almacenado que se busca -revise nombre de
		         * esquema y nombre de stored procedure CUIDADOSAMENTE
		         */
		        
	//		        callStmt.registerOutParameter(1, Types.INTEGER); //Registra el tipo del parametro OUT 1 por posicion
			        callStmt.registerOutParameter("resultado", Types.INTEGER); //Registra el tipo del par�metro OUT 1 por nombre
			        
			        callStmt.executeQuery(); //Devuelve un ResultSet, que en este caso particular no se usa
			        numArticulos = callStmt.getInt(1); // recupera el valor devuelto por el stored procedure
				}
		    }
			
			return numArticulos;
		}

		public long recuperarImporteAcumuladoVtasxVendedor(int numVendedor) 
				throws SQLException{ //Llama a un stored procedure!!
			
			long importeDeVtas = 0;
			String SQL = "{call "
					+ this.nombreBD
					+ ".calcula_vtasXvendedor(?,?)}";
			
			try (Connection conexionBD = this.getConexionBD()){
				try( CallableStatement callStmt = conexionBD.prepareCall(SQL)){
			        callStmt.setInt("id_persona",numVendedor);
			        callStmt.registerOutParameter("resultado", Types.INTEGER); //Registra el tipo del parametro OUT 1 por nombre
			        
			        callStmt.executeQuery(); //Devuelve un ResultSet, que en este caso particular no se usa
			        importeDeVtas = callStmt.getInt(2); // recupera el valor devuelto por el stored procedure
				}
		    }
			
			return importeDeVtas;
		}

		public void mostrarListaDeStoredProcedures() 
				throws SQLException {
			try (Connection conexionBD = this.getConexionBD()){
				DatabaseMetaData datosBD = conexionBD.getMetaData();
				int nRenglones=0;
				
				try (ResultSet rsStoredProcedures = datosBD.getProcedures(this.nombreBD, null, "%")){
				// el primer parametro puede ser null, cuando hay un solo esquema dado de alta
				// en la base de datos. En ese caso se usa ese por defecto. Si hay mas de uno
				// es obligatorio especificar cual de los esquemas (CATALOG) de quiere consultar
				
					ResultSetMetaData rsMetaDatosProcedures =  rsStoredProcedures.getMetaData();
					System.out.println("Columnas de la tabla de Procedures de la base de datos");
					for(int i=1;i<=rsMetaDatosProcedures.getColumnCount();i++){
						System.out.println(rsMetaDatosProcedures.getColumnName(i));
					}
					System.out.println("-Procedures en la base de datos -");
					nRenglones=0;
				      while (rsStoredProcedures.next()) {
				          System.out.println(
				            "PROCEDURE_CAT:"+rsStoredProcedures.getString("PROCEDURE_CAT")
				            + ", PROCEDURE_SCHEM:"+rsStoredProcedures.getString("PROCEDURE_SCHEM")
				            + ", PROCEDURE_NAME:"+rsStoredProcedures.getString("PROCEDURE_NAME"));
				          nRenglones++;
				      }
					  System.out.println("Num. de renglones recuperados:"+nRenglones);
				}
		    }
		}

		public void mostrarListaDeCatalogsBD() 
				throws SQLException {
			try (Connection conexionBD = this.getConexionBD()){
				DatabaseMetaData datosBD = conexionBD.getMetaData();
				int nRenglones=0;
				try (ResultSet rsCatalogos = datosBD.getCatalogs()){
					ResultSetMetaData rsMetaDatosCatalogos =  rsCatalogos.getMetaData();
					System.out.println("Columnas de la tabla de los catalogos de la base de datos");
					for(int i=1;i<=rsMetaDatosCatalogos.getColumnCount();i++){
						System.out.println("    " + rsMetaDatosCatalogos.getColumnName(i));
					}
					
					System.out.println("\n-Catalogos en la base de datos -");
					rsCatalogos.beforeFirst();
					while(rsCatalogos.next()){
						System.out.println("    " + rsCatalogos.getString("TABLE_CAT"));
						nRenglones++;
					}
					System.out.println("Num. de renglones recuperados:"+nRenglones);
				}				
		    }
		}
	
		public static void mostrarSQLException(SQLException ex) {

		    for (Throwable e : ex) {
		        if (e instanceof SQLException) {
		        		SQLException sqlEx = (SQLException) e;
		                System.err.println("SQLState: [" + sqlEx.getSQLState() + "]");
		                System.err.println("Codigo de error: [" + sqlEx.getErrorCode() +"]");
		                System.err.println("Mensaje: [" + sqlEx.getMessage() +"]");

		                Throwable causaEx = ex.getCause();
		                while(causaEx != null) {
		                    System.err.println("Causa-->" + causaEx + "<--");
		                    causaEx = causaEx.getCause();
		                }
		                System.err.println("-------------------------------- Arbol de errores: ---------------------------------");
		                e.printStackTrace(System.err);
		        }
		    }
		}

		public HashMap<Persona,List<Venta>> obtenerVentasDeVendedores(GrupoPersonas listaVendedores)throws SQLException{
			// No probado 
			String strQuery = "Select * from Venta where id_persona_vendedor = ?";
			
			HashMap<Persona,List<Venta>> ventasVendedor = new HashMap<Persona,List<Venta>>();		

			try (Connection conexionBD = this.getConexionBD()){
				try (PreparedStatement pStmt = conexionBD.prepareStatement(strQuery)){
					while(listaVendedores.getLLaves().hasNext()){
						ArrayList<Venta> ventasDeLaPersona = new ArrayList<Venta>();
						int idPersonaVendedor = listaVendedores.getLLaves().next();
						pStmt.setInt(1,idPersonaVendedor);
						try (ResultSet rsVentasDelaPersona = pStmt.executeQuery()){
							while(rsVentasDelaPersona.next()){
								int numVenta = rsVentasDelaPersona.getInt("num_venta");
								Date fechaVenta = rsVentasDelaPersona.getDate("fecha_venta");
								int idPersonaCte = rsVentasDelaPersona.getInt("id_persona_cte");
								Persona personaCte = new Persona(idPersonaCte,null,null,null);
								Persona personaVendedor = listaVendedores.getPersonaPorID(idPersonaVendedor);
								Venta venta = new Venta(numVenta,fechaVenta,personaCte,personaVendedor);
								ventasDeLaPersona.add(venta);
							}//fin while
						}
						ventasVendedor.put(listaVendedores.getPersonaPorID(idPersonaVendedor), ventasDeLaPersona);
					} //fin while
				} 
			}
			
			return ventasVendedor;
		}
}
