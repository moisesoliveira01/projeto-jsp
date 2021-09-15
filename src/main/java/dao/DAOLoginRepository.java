package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import models.ModelLogin;

public class DAOLoginRepository {
    
	private Connection connection;
	
	public DAOLoginRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public boolean validarAutenticacao(ModelLogin modelLogin) throws SQLException {
		String sql = " select * from model_login where upper(login) = upper(?) and upper(senha) = upper(?) ";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		//setar atributos para as "?"
		statement.setString(1, modelLogin.getLogin()); 
		statement.setString(2, modelLogin.getSenha());
		
		ResultSet resultset = statement.executeQuery();
		
		if (resultset.next()) {
	    return true; //autenticado
		}
		
		return false; //não autenticado
	}
}
