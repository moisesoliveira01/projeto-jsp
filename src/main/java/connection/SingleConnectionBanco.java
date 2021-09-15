package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {
   
	private static String banco = "jdbc:postgresql://localhost:5432/postgres?autoReconnect=true";
    private static String user = "postgres";
    private static String senha = "31560466";
    private static Connection connection = null;
    
    public static Connection getConnection() {
    	return connection;
    }
    
    //para chamar a classe diretamente, sem criar uma instância
    static {
    	conectar();
    }
    
    public SingleConnectionBanco() {
    	conectar();
    }
    
    public static void conectar() {
        try {
        	if (connection == null) {
        	Class.forName("org.postgresql.Driver"); //carrega o driver de conexão do banco
        	connection = DriverManager.getConnection(banco, user, senha);
        	connection.setAutoCommit(false); //não realizar alterações sem um comando
        	}
        }catch (Exception e) {
		    e.printStackTrace();
		}
    }
}
