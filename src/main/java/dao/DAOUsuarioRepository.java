package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import models.ModelLogin;

public class DAOUsuarioRepository {

	private Connection connection;

	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}

	public ModelLogin gravarUsuario(ModelLogin objeto) throws Exception {
		if (objeto.isNew()) {

			String sql = "INSERT INTO public.model_login(login, senha, nome, email) VALUES(?, ?, ?, ?);";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, objeto.getLogin());
			statement.setString(2, objeto.getSenha());
			statement.setString(3, objeto.getNome());
			statement.setString(4, objeto.getEmail());
			statement.execute();

			connection.commit();
		} else {
			String sql = " update model_login set login =?, senha=?, nome=?, email=? where id = " + objeto.getId()
					+ ";";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, objeto.getLogin());
			statement.setString(2, objeto.getSenha());
			statement.setString(3, objeto.getNome());
			statement.setString(4, objeto.getEmail());

			statement.executeUpdate();

			connection.commit();
		}

		return this.consultaUsuario(objeto.getLogin());
	}

	public ModelLogin consultaUsuario(String login) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = " select * from model_login where upper(login) = upper('" + login + "') ";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		if (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setSenha(resultado.getString("senha"));
		}

		return modelLogin;
	}
	
	public ModelLogin consultaUsuarioId(String id) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = " select * from model_login where id = ? ";
		System.out.println(id);
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));

		ResultSet resultado = statement.executeQuery();

		if (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setSenha(resultado.getString("senha"));
		}

		return modelLogin;
	}
	
	public List<ModelLogin> consultaUsuarioList(String nome) throws SQLException {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = " select * from model_login where upper(nome) like upper(?) ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setLogin(resultado.getString("login"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioList() throws SQLException {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = " select * from model_login ";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setLogin(resultado.getString("login"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}

	public boolean validarLogin(String login) throws Exception {
		String sql = " select count(1) > 0 as existe from model_login where upper(login) = upper('" + login + "'); ";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		resultado.next(); // entra no resultado do sql
		return resultado.getBoolean("existe");
	}

	public void deletarUsuario(String idUser) throws Exception {
		String sql = " delete from model_login where id = ?; ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(idUser));

		statement.executeUpdate();

		connection.commit();
	}
}
