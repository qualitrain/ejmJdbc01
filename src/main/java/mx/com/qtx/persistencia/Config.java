package mx.com.qtx.persistencia;

public class Config {
//	public static final String DBMS = "mysql";
	public static final String DBMS = "sqlserver";

	public static String getNombreStoredProcedure(String nombreBD, String nombreSP) {
		switch (Config.DBMS) {
		case "mysql": return nombreBD + "." + nombreSP;
		case "sqlserver":return nombreBD + ".dbo." + nombreSP;
		default:
			return nombreBD + "." + nombreSP;
		}
	}
}
