package mx.com.qtx.persistencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
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

public class GestorBD {
	private String dbms = "mysql";
	static final private String usuarioBD = "root";
	static final private String passwordBD = "root";
	static final private String nombreServidorBD = "localhost";
	static final private String puertoBD = "3306";
	private String nombreBD = "ejemplosJDBC";
	private Connection conexionBD=null;
		

	public GestorBD(String dbms, String nombreBD) throws SQLException {
		super();
		this.dbms = dbms;
		this.nombreBD = nombreBD;
		
		this.setConexionBD();
	}

	private void setConexionBD() throws SQLException {

	    if (this.conexionBD != null) //Ya hay conexion
	    	return;
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
		              + "?serverTimezone=UTC&createDatabaseIfNotExist=false";
		    	this.conexionBD = DriverManager.getConnection(url, propiedadesConexion);
		    	break;
		    	
		    case "derby":
		    	url = "jdbc:" + dbms + ":" +
			            nombreServidorBD +
			            ";create=true";
		    	this.conexionBD = DriverManager.getConnection(url, propiedadesConexion);
		    	break;
	    }
	}
	
	public static void mostrarSQLException(SQLException ex) {

	    for (Throwable e : ex) {
	        if (e instanceof SQLException) {
	        	
	            if (ignorarSQLException(((SQLException)e).getSQLState()) == false) {

	                System.err.println("SQLState: " + ((SQLException)e).getSQLState()+"<--");

	                System.err.println("Codigo de error-->" + ((SQLException)e).getErrorCode()+"<--");

	                System.err.println("Mensaje de error-->" + e.getMessage()+"<--");

	                Throwable t = ex.getCause();
	                while(t != null) {
	                    System.err.println("Causa-->" + t + "<--");
	                    t = t.getCause();
	                }
	                System.err.println("-------------------------------- Arbol de errores: ---------------------------------");
	                e.printStackTrace(System.err);

	            }
	        }
	    }
	}
	
	public void mostrarCaracteristicasBD()throws SQLException{
		if (this.conexionBD==null){
			System.err.println("No hay conexion abierta");
			return;
		}
		try {
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
		catch (SQLException ex){
			throw ex;
		}
		finally {
			if (this.conexionBD!=null){
				this.conexionBD.close();
				this.conexionBD = null;
			}
		}
	}
	
	public static boolean ignorarSQLException(String sqlState) {
		    if (sqlState == null) {
		      System.out.println("El estado SQL no esta definido!");
		      return false;
		    }
		    // X0Y32: El archivo Jar ya existe en el schema
		    if (sqlState.equalsIgnoreCase("X0Y32"))
		      return true;
		    // 42Y55: La Tabla ya existe en el esquema
		    if (sqlState.equalsIgnoreCase("42Y55"))
		      return true;
		    return false;
	  }
	
	public GrupoPersonas recuperarPersonasTodas()throws SQLException {

		GrupoPersonas grupoPersonas = new GrupoPersonas();
		Statement stmt = null;
		try {
			if (this.conexionBD == null){ //No hay conexion de BD a�n
				this.setConexionBD();
			}
	        stmt = this.conexionBD.createStatement();
	        ResultSet resultSet = stmt.executeQuery("Select * from persona");

        	int idPersona;
        	String nombre;
        	String direccion;
        	Date fechaNacimiento;

	        while (resultSet.next()) {
	        	idPersona = resultSet.getInt(1);
	        	nombre = resultSet.getString(2);
	        	direccion = resultSet.getString(3);
	        	fechaNacimiento = resultSet.getDate(4);
	        	
	        	grupoPersonas.agregarPersona(new Persona(idPersona, nombre, direccion,fechaNacimiento));
	        }
	    }
		catch (SQLException e ) {
	        throw e;
	    } 
		finally {
	        if (stmt != null) { 
	        	stmt.close();
	        }
	        if (this.conexionBD != null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
	    }
		
		return grupoPersonas;
	}
	
	
	public HashMap<String,Articulo> recuperarArticulosTodos()throws SQLException {

		HashMap<String,Articulo> grupoArticulos = new HashMap<String,Articulo>();
		Statement stmt = null;
		try {
			if (this.conexionBD == null){ //No hay conexion de BD aun
				this.setConexionBD();
			}
	        stmt = conexionBD.createStatement();
	        ResultSet resultSet = stmt.executeQuery("Select * from articulo");

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
		catch (SQLException e ) {
	        throw e;
	    } 
		finally {
	        if (stmt != null) { 
	        	stmt.close(); 
	        }
	        if (this.conexionBD!=null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
	    }
		
		return grupoArticulos;
	}
	
	public int recuperarCuantosArticulosHayEnBD() throws SQLException{ //Llama a un stored procedure!!
		int numArticulos = 0;
		CallableStatement callStmt = null;
		try {
			if (this.conexionBD == null){ //No hay conexion de BD aun
				this.setConexionBD();
			}
	        callStmt = conexionBD.prepareCall("{call ejemplosjdbc.CUENTA_ARTICULOS(?)}"); // Modifique para probar que sucede 
																					 // cuando se invoca un procedimiento inexistente
	        // El nombre del procedimiento debe incluir el nombre de la base de datos -en algunas versiones
	        // de Mysql (5.0)-

	        /*
	         * El metodo GestorBD.mostrarMetaDatosStoreProcedure(callStmt) 
	         * explora los parametros recibidos de la base de datos y metainformacion de
	         * la base de datos.
	         * Es util, porque cuando no se encuentra el procedimiento almacenado
	         * se lanza una excepcion (en Mysql) con un error que dice
	         * "No output parameters returned by procedure" 
	         * o alguno que indica que el parametro no es de tipo OUT:
	         * Parameter number 1 is not an OUT parameter, lo cual es muy criptico pq no indica que
	         * en realidad NO EXISTE el procedimiento almacenado que se busca -revise nombre de
	         * esquema y nombre de stored procedure CUIDADOSAMENTE
	         */
	        
//	        callStmt.registerOutParameter(1, Types.INTEGER); //Registra el tipo del parametro OUT 1 por posicion
	        callStmt.registerOutParameter("resultado", Types.INTEGER); //Registra el tipo del par�metro OUT 1 por nombre
	        
	        callStmt.executeQuery(); //Devuelve un ResultSet, que en este caso particular no se usa
	        numArticulos = callStmt.getInt(1); // recupera el valor devuelto por el stored procedure
	    }
		catch (SQLException e ) {
	        throw e;
	    } 
		finally {
	        if (callStmt != null) { 
	        	callStmt.close(); 
	        }
	        if (this.conexionBD != null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
	    }
		
		return numArticulos;
	}
	
	public void mostrarListaDeStoredProcedures() 
			throws SQLException {
		try {
			if (this.conexionBD == null){ //No hay conexion de BD a�n
				this.setConexionBD();
			}
			DatabaseMetaData datosBD = this.conexionBD.getMetaData();
			int nRenglones=0;
			
			ResultSet rsStoredProcedures = datosBD.getProcedures("ejemplosjdbc", null, "%");
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
			rsStoredProcedures.close();

	    }
		catch (SQLException e ) {
	        throw e;
	    } 
		finally {
	        if (this.conexionBD != null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
	    }
	}
	
	public void mostrarListaDeCatalogsBD() 
			throws SQLException {
		try {
			if (this.conexionBD == null){ //No hay conexion de BD a�n
				this.setConexionBD();
			}
			DatabaseMetaData datosBD = this.conexionBD.getMetaData();
			int nRenglones=0;
			ResultSet rsCatalogos = datosBD.getCatalogs();
			ResultSetMetaData rsMetaDatosCatalogos =  rsCatalogos.getMetaData();
			System.out.println("Columnas de la tabla de los catalogos de la base de datos");
			for(int i=1;i<=rsMetaDatosCatalogos.getColumnCount();i++){
				System.out.println(rsMetaDatosCatalogos.getColumnName(i));
			}
			
			System.out.println("-Catalogos en la base de datos -");
			rsCatalogos.beforeFirst();
			while(rsCatalogos.next()){
				System.out.println(rsCatalogos.getString("TABLE_CAT"));
				nRenglones++;
			}
			System.out.println("Num. de renglones recuperados:"+nRenglones);

			rsCatalogos.close();
			
	    }
		catch (SQLException e ) {
	        throw e;
	    } 
		finally {
	        if (this.conexionBD != null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
	    }
	}
	
	public void mostrarListaDeSchemasBD() 
			throws SQLException {
		try {
			if (this.conexionBD == null){ //No hay conexion de BD a�n
				this.setConexionBD();
			}
			DatabaseMetaData datosBD = this.conexionBD.getMetaData();
			int nRenglones=0;
			ResultSet rsEsquemas = datosBD.getSchemas();
			ResultSetMetaData rsMetaDatosSchemas =  rsEsquemas.getMetaData();
			System.out.println("Columnas de la tabla de esquemas de la base de datos");
			for(int i=1;i<=rsMetaDatosSchemas.getColumnCount();i++){
				System.out.println(rsMetaDatosSchemas.getColumnName(i));
			}
			System.out.println("-Esquemas en la base de datos -");
			rsEsquemas.beforeFirst();
			while(rsEsquemas.next()){
				System.out.println("TABLE_SCHEM:"+rsEsquemas.getString("TABLE_SCHEM")
						+",TABLE_CATALOG:"+rsEsquemas.getString("TABLE_CATALOG"));
				nRenglones++;
			}
			System.out.println("Num. de renglones recuperados:"+nRenglones);

			rsEsquemas.close();
			
	    }
		catch (SQLException e ) {
	        throw e;
	    } 
		finally {
	        if (this.conexionBD != null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
	    }
	}

	public void mostrarMetaDatosStoredProcedure(CallableStatement callStmt) 
			throws SQLException {

        ParameterMetaData datosSP = callStmt.getParameterMetaData();
        
        System.out.println("========= Datos del Stored Procedure =========");
        
        int numParametros = datosSP.getParameterCount();
        System.out.println("Numero de Parametros:"+numParametros);
        
        for(int i=1;i<=numParametros;i++){
        	System.out.println("------------------------------------");
        	System.out.println("Parametro "+i);
        	
            int modoParametro = datosSP.getParameterMode(1);
            String cadModoParametro = this.getParameterMetaDataMode(modoParametro);
            System.out.println("Modo Parametro 1 :"+cadModoParametro);
            System.out.println("Clase Java:"+datosSP.getParameterClassName(i));
            System.out.println("Tipo en BD:"+datosSP.getParameterTypeName(i));
        }
	}

	private String getParameterMetaDataMode(int modo){
		switch(modo){
		case ParameterMetaData.parameterModeIn:
			return "Parametro en modo In activo";
		case ParameterMetaData.parameterModeInOut:
			return "Parametro en modo In-Out activo";
		case ParameterMetaData.parameterModeOut:
			return "Parametro en modo Out activo";
		case ParameterMetaData.parameterModeUnknown:
			return "Parametro en modo Desconocido!";
		default:
			return null;	
		}
	}
	
	public long recuperarImporteAcumuladoVtasxVendedor(int numVendedor) 
			throws SQLException{ //Llama a un stored procedure!!
		long importeDeVtas = 0;
		CallableStatement callStmt = null;
		try {
			if (this.conexionBD == null){ //No hay conexion de BD a�n
				this.setConexionBD();
			}
	        callStmt = conexionBD.prepareCall("{call ejemplosjdbc.calcula_vtasXvendedor(?,?)}");
	        callStmt.setInt("id_persona",numVendedor);
	        callStmt.registerOutParameter("resultado", Types.INTEGER); //Registra el tipo del parametro OUT 1 por nombre
	        
	        callStmt.executeQuery(); //Devuelve un ResultSet, que en este caso particular no se usa
	        importeDeVtas = callStmt.getInt(2); // recupera el valor devuelto por el stored procedure
	    }
		catch (SQLException e ) {
	        throw e;
	    } 
		finally {
	        if (callStmt != null) { 
	        	callStmt.close(); 
	        }
	        if (this.conexionBD != null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
	    }
		
		return importeDeVtas;
	}

	public void factorizarPrecioArticulos(float factor)throws SQLException {

		Statement stmt = null;
		try {
			if (this.conexionBD == null){ //No hay conexion de BD a�n
				this.setConexionBD();
			}
	        stmt = conexionBD.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
	                   ResultSet.CONCUR_UPDATABLE);
	        ResultSet resultSet = stmt.executeQuery("Select * from articulo");

        	float precioLista;

	        while (resultSet.next()) {
	        	precioLista = resultSet.getFloat("precio_lista");
	        	resultSet.updateFloat("precio_lista", precioLista*factor);
	        	resultSet.updateRow();
	        }
	    }
		catch (SQLException e ) {
	        throw e;
	    } 
		finally {
	        if (stmt != null) { 
	        	stmt.close(); 
	        }
	        if (this.conexionBD != null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
	    }
		
	}
	
	public int insertarArticulo(Articulo articulo) throws SQLException {
		int numAfectacionesBD = 0;
		Statement stmt = null;
		try {
			if (this.conexionBD == null)
				this.setConexionBD();
			stmt = this.conexionBD.createStatement();
			numAfectacionesBD=stmt.executeUpdate("Insert into articulo values ('"
													+ articulo.getCveArticulo()+"','"
													+ articulo.getDescripcion()+"',"
													+Float.toString(articulo.getCostoProv1())+","
													+Float.toString(articulo.getPrecioLista())+")");
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
	        if (stmt != null) { 
	        	stmt.close(); 
	        }
	        if (this.conexionBD != null){
	        	this.conexionBD.close();
	        	this.conexionBD = null;
	        }
		}
		return numAfectacionesBD;
	}
	
	public int actualizarArticulo(Articulo articulo) throws SQLException {
// Este ejemplo maneja CORRECTAMENTE los bloques de try-catch para asegurar un cierre exitoso de recursos
		if (this.conexionBD == null)
			this.setConexionBD();
		
		int numAfectacionesBD = 0;
		Statement stmt = null;
		try {
			stmt = this.conexionBD.createStatement();
			try {
				numAfectacionesBD=stmt.executeUpdate("Update articulo set descripcion ='"+articulo.getDescripcion()+"',"
													+"costo_prov_1="+Float.toString(articulo.getCostoProv1())+","
													+"precio_lista="+Float.toString(articulo.getPrecioLista())+
													" where cve_articulo ='"+articulo.getCveArticulo()+"'");
			}
			catch (SQLException ex){
				throw ex;
			}
			finally{
				if (stmt != null)
					stmt.close();
			}
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
	        if (this.conexionBD != null) { 
	        	this.conexionBD.close(); 
	        	this.conexionBD=null;
	        }
		}
		return numAfectacionesBD;
	}
	
	public Articulo recuperarArticuloXid(String cveArticulo) throws SQLException {
		if (this.conexionBD == null)
			this.setConexionBD();
		
		Articulo articulo=null;
		Statement stmt = null;
		ResultSet resultSet = null;
		
		try {
		
			stmt = this.conexionBD.createStatement();
			try {
				resultSet=stmt.executeQuery("Select * from articulo where cve_Articulo ='"+
													cveArticulo+"'");
				try {
					if(resultSet.next()){ // El cursor se avanza para posicionarlo en el renglon leido
						String descripcion = resultSet.getString("descripcion");
						float costoProv1 = resultSet.getFloat("costo_prov_1");
						float precioLista = resultSet.getFloat("precio_lista");
						articulo=new Articulo(cveArticulo,descripcion,costoProv1,precioLista);
					}
				}
				catch (SQLException ex){
					throw (ex);
				}
				finally{
					resultSet.close();
				}
			}
			catch (SQLException ex){
				throw ex;
			}
			finally
			{
				if (stmt != null)
					stmt.close();
				if (this.conexionBD != null){
					this.conexionBD.close();
					this.conexionBD = null;
				}
			}
		
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
	        if (this.conexionBD != null) { 
	        	this.conexionBD.close(); 
	        	this.conexionBD = null;
	        }
			
		}
		return articulo;
		
	}

	public Venta recuperarVentaXid(int numVenta) throws SQLException {
		if (this.conexionBD == null)
			this.setConexionBD();
		
		Venta venta=null;
		Statement stmt = null;
		ResultSet resultSet = null;
		
		try {
			stmt = this.conexionBD.createStatement();
			try {
				resultSet=stmt.executeQuery("Select * from venta where num_venta ="+
													numVenta);
				try {
					if(resultSet.next()){ // El cursor se avanza para posicionarlo en el renglon le�do
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
				catch (SQLException ex){
					throw (ex);
				}
				finally{
					resultSet.close();
				}
			}
			catch (SQLException ex){
				throw ex;
			}
			finally
			{
				if (stmt != null)
					stmt.close();
			}
		
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
	        if (this.conexionBD != null) { 
	        	this.conexionBD.close(); 
	        	this.conexionBD = null;
	        }
		}
		return venta;
	}

	public List<DetalleVenta> recuperarDetallesDeUnaVenta(int numVenta) throws SQLException{
		if (this.conexionBD == null){
			this.setConexionBD();
		}
		
		ArrayList<DetalleVenta> listaDetallesVenta = new ArrayList<DetalleVenta>();
		
		try {
			 Statement stmt = this.conexionBD.createStatement();
			 try{
				 ResultSet resultSet = stmt.executeQuery("Select * from detalle_venta " +
				 													"where num_venta ="+ numVenta);
				 try {
					 while(resultSet.next()){
						int numDetalle = resultSet.getInt("num_detalle");
						int cantidad = resultSet.getInt("cantidad");
						String cveArticulo = resultSet.getString("cve_articulo");
						float precioUnitario = resultSet.getFloat("precio_unitario");
						
						listaDetallesVenta.add(new DetalleVenta(numDetalle, cantidad, new Articulo(cveArticulo), precioUnitario));
					}
				 }
				 catch (SQLException ex){
						throw ex;
					}
				 finally {
					 resultSet.close();
				 }
			 }
			 catch (SQLException ex){
					throw ex;
				}
			 finally {
				 stmt.close();
			 }
		}
		catch (SQLException ex){
			throw ex;
		}
		finally{
			if (this.conexionBD != null)
			{
				this.conexionBD.close();
				this.conexionBD = null;
			}
		}
		for(DetalleVenta det:listaDetallesVenta){
			String cveArticulo = det.getArticuloVendido().getCveArticulo();
			det.setArticuloVendido(this.recuperarArticuloXid(cveArticulo));
		}
		return listaDetallesVenta;
	}
	
	public int eliminarArticulo(Articulo articulo) throws SQLException {
		if (this.conexionBD == null)
			this.setConexionBD();
		
		int numAfectacionesBD = 0;
		Statement stmt = null;
		try {
			stmt = this.conexionBD.createStatement();
			try {
				numAfectacionesBD=stmt.executeUpdate("Delete from articulo where cve_articulo ='"+articulo.getCveArticulo()+"'");
			}
			catch (SQLException ex){
				throw ex;
			}
			finally
			{
				if (stmt != null)
					stmt.close();
			}
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
			if (this.conexionBD != null) { 
				this.conexionBD.close(); 
				this.conexionBD=null;
			}
		}
		return numAfectacionesBD;
	}
	
	public HashMap<String,Articulo> buscarArticulosXdescripcion(String patronDescripcion) throws SQLException {
		if (this.conexionBD == null)
			this.setConexionBD();
		
		PreparedStatement pStmt = null;
		HashMap<String,Articulo> mapArticulos = new HashMap<String,Articulo>();
		
		try {
			pStmt = this.conexionBD.prepareStatement("Select * from Articulo where descripcion like ?");
					  							//+"and precio_lista > ? and precio_lista < ?");
				try {
					pStmt.setString(1, patronDescripcion);
//					pStmt.setFloat(2, 1.0f);
//					pStmt.setFloat(3, 36.0f);
					ResultSet rsArticulos = pStmt.executeQuery();
					try{
						
						while(rsArticulos.next()){
							String cveArticulo = rsArticulos.getString("cve_articulo");
							String descripcion = rsArticulos.getString("descripcion");
							float costoProv1 = rsArticulos.getFloat("costo_prov_1");
							float precioLista = rsArticulos.getFloat("precio_lista");
							mapArticulos.put(cveArticulo, new Articulo(cveArticulo,descripcion,costoProv1,precioLista));
						}
					}
					catch (SQLException ex){
						throw ex;
					}
					finally {
						if (rsArticulos != null)
							rsArticulos.close();
					}
					
				}
				catch (SQLException ex){
					throw ex;
				}
				finally {
					if (pStmt != null)
						pStmt.close();
				}
			}
		catch (SQLException ex){
			throw ex;
			}
		finally {
			if (this.conexionBD != null){
				this.conexionBD.close();
				this.conexionBD = null;
			}
		}
		return mapArticulos;
	}
	
	public HashMap<Persona,List<Venta>> obtenerVentasDeVendedores(GrupoPersonas listaVendedores)throws SQLException{
		if (this.conexionBD == null)
			this.setConexionBD();
		
		String strQuery = "Select * from Venta where id_persona_vendedor = ?";
		
		HashMap<Persona,List<Venta>> ventasVendedor = new HashMap<Persona,List<Venta>>();		
		PreparedStatement pStmt = null;
		ResultSet rsVentasDelaPersona=null;

		try {
			pStmt = this.conexionBD.prepareStatement(strQuery);
			try {
				while(listaVendedores.getLLaves().hasNext()){
					ArrayList<Venta> ventasDeLaPersona = new ArrayList<Venta>();
					int idPersonaVendedor = listaVendedores.getLLaves().next();
					pStmt.setInt(1,idPersonaVendedor);
					try{ // Recuperar las ventas del vendedor de la iteraci�n
						rsVentasDelaPersona = pStmt.executeQuery();
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
					catch (SQLException ex){
						throw ex;
					}
					finally {
						if (rsVentasDelaPersona != null){
							rsVentasDelaPersona.close();
							rsVentasDelaPersona = null;
						}
					}
					ventasVendedor.put(listaVendedores.getPersonaPorID(idPersonaVendedor), ventasDeLaPersona);
				} //fin while
			}
			catch (SQLException ex){
				throw ex;
			}
			finally {
				if (pStmt != null)
					pStmt.close();
				}
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
			if (this.conexionBD != null){
				this.conexionBD.close();
				this.conexionBD = null;
			}
		}
		
		return ventasVendedor;
	}

	public int insertarVentaNoTransaccional (Venta nuevaVenta)throws SQLException{
		// Inserta una relacion maestro-detalle con relaciones hacia otros objetos en la base de datos.
		// No observa comportamiento transaccional: Permite actualizar la mitad o parte de una transaccion
		// llevando a problemas de integridad en caso de error.
		// Tambien utiliza el concepto de llave auto-generada
		if (this.conexionBD ==null){
			this.setConexionBD();
		}
		
		int numVenta = 0;
		
		try {
			Statement stmtInsercionVenta = this.conexionBD.createStatement();
			try {
				String strInsertVenta="Insert into venta set " +
									"fecha_venta =  curdate()," + // La fecha la obtiene del servidor de base de datos usando una funcion SQL
									"id_persona_cte = " + nuevaVenta.getCliente().getIdPersona() + "," +
									"id_persona_vendedor = " + nuevaVenta.getVendedor().getIdPersona();
				
				stmtInsercionVenta.executeUpdate(strInsertVenta,Statement.RETURN_GENERATED_KEYS ); // Se requiere para pode recuperar los valores de las llaves auto-generadas
				ResultSet rsLlavesAutoGeneradas = stmtInsercionVenta.getGeneratedKeys();
				try {
					this.mostrarLlavesAutoGeneradas(rsLlavesAutoGeneradas);
					numVenta = this.getValorLlaveAutoGenerada(rsLlavesAutoGeneradas,1);
				}
				catch(SQLException ex){
					throw ex;
				}
				finally {
					rsLlavesAutoGeneradas.close();
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
			catch(SQLException ex){
				throw ex;
			}
			finally {
				stmtInsercionVenta.close();
			}
			
		}
		catch(SQLException ex){
			throw ex;
		}
		finally{
			if(this.conexionBD !=null){
				this.conexionBD.close();
				this.conexionBD = null;
			}
		}
		return numVenta;
	}
	
	public void mostrarLlavesAutoGeneradas(ResultSet rsLlavesAutoGeneradas) 
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
	
	public int getValorLlaveAutoGenerada(ResultSet rsLlavesAutoGeneradas,int posicion) throws SQLException{ //El ResultSet debe haber sido obtenido llamando a Statement.getGeneratedKeys()
		rsLlavesAutoGeneradas.beforeFirst();
		if (rsLlavesAutoGeneradas.next()) {
			return rsLlavesAutoGeneradas.getInt(posicion);
		} 
		else {
	         return -1;
		}
	}
	
	public Persona recuperarPersonaXid(int idPersona) throws SQLException {
		if (this.conexionBD == null)
			this.setConexionBD();
		
		Persona persona=null; //Devuelve null si no encuentra el objeto
		Statement stmt = null;
		ResultSet resultSet = null;
		
		try {		
			stmt = this.conexionBD.createStatement();
			try {
				resultSet=stmt.executeQuery("Select * from persona where id_persona ="+
						idPersona);
				try {
					if(resultSet.next()){ // El cursor se avanza para posicionarlo en el renglon le�do
						String nombre = resultSet.getString("nombre");			
						String direccion = resultSet.getString("direccion");
						Date fechaNacimiento = resultSet.getDate("fecha_nacimiento");

						persona=new Persona(idPersona,nombre,direccion,fechaNacimiento);
					}
				}
				catch (SQLException ex){
					throw (ex);
				}
				finally{
					resultSet.close();
				}
			}
			catch (SQLException ex){
				throw ex;
			}
			finally
			{
				if (stmt != null)
					stmt.close();
			}
		
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
	        if (this.conexionBD != null) { 
	        	this.conexionBD.close(); 
	        	this.conexionBD = null;
	        }
			
		}
		return persona;
		
	}
	
	public int insertarVentaTransaccional (Venta nuevaVenta)throws SQLException{
		// Inserta una relacion maestro-detalle con relaciones hacia otros objetos en la base de datos.
		// Tambien utiliza el concepto de llave auto-generada
		if (this.conexionBD ==null){
			this.setConexionBD();
		}
		
		int numVenta = 0;
		
		try {
			this.conexionBD.setAutoCommit(false); //********* SE ELIMINA COMPORTAMIENTO POR DEFECTO
			this.conexionBD.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED); // Nivel de aislamiento
			
			Statement stmtInsercionVenta = this.conexionBD.createStatement();
			try {
				String strInsertVenta="Insert into venta set " +
									"fecha_venta =  curdate()," + 
									"id_persona_cte = " + nuevaVenta.getCliente().getIdPersona() + "," +
									"id_persona_vendedor = " + nuevaVenta.getVendedor().getIdPersona();
				
				stmtInsercionVenta.executeUpdate(strInsertVenta,Statement.RETURN_GENERATED_KEYS ); 
				ResultSet rsLlavesAutoGeneradas = stmtInsercionVenta.getGeneratedKeys();
				try {
					numVenta = this.getValorLlaveAutoGenerada(rsLlavesAutoGeneradas,1);
				}
				catch(SQLException ex){
					this.conexionBD.rollback(); // EN CASO DE EXCEPCION SE DEJA TODO COMO ESTABA
					throw ex;
				}
				finally {
					rsLlavesAutoGeneradas.close(); 
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
				this.conexionBD.commit(); // *** EN ESTE MOMENTO SE ACTUALIZA TODA LA TRANSACCION
				this.conexionBD.setAutoCommit(true);
			}
			catch(SQLException ex){
				this.conexionBD.rollback(); // EN CASO DE EXCEPCION SE DEJA TODO COMO ESTABA
				throw ex;
			}
			finally {
				stmtInsercionVenta.close();
			}
			
		}
		catch(SQLException ex){
			throw ex;
		}
		finally{
			if(this.conexionBD !=null){
				this.conexionBD.close();
				this.conexionBD = null;
			}
		}
		return numVenta;
	}

}
