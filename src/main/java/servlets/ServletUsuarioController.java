package servlets;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOUsuarioRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ModelLogin;

/**
 * Servlet implementation class ServletUsuarioController
 */
@WebServlet("/ServletUsuarioController")
public class ServletUsuarioController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();

	public ServletUsuarioController() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String acao = request.getParameter("acao");

			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
				String idUser = request.getParameter("id");
				
				daoUsuarioRepository.deletarUsuario(idUser);
				
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
				request.setAttribute("modelLogins", modelLogins);
				
				request.setAttribute("msg", "excluído com sucesso!");
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("ajaxDeletar")) {
				String idUser = request.getParameter("id");
				
				daoUsuarioRepository.deletarUsuario(idUser);
				
				response.getWriter().write("excluído com sucesso!");
			}
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUsuarioAjax")) {
                String nomeBusca = request.getParameter("nomeBusca");
                
                List<ModelLogin> dadosJsonUsuario = daoUsuarioRepository.consultaUsuarioList(nomeBusca);
                
                ObjectMapper mapper = new ObjectMapper();
                
                String json = mapper.writeValueAsString(dadosJsonUsuario);
                
                response.getWriter().write(json);
			}
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
				String id = request.getParameter("id");
				
				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioId(id);
				
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
				request.setAttribute("modelLogins", modelLogins);
				
				request.setAttribute("msg", "usuário em edição");
				request.setAttribute("modelLogin", modelLogin);
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUsuarios")) {
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
				
				request.setAttribute("msg", "usuários carregados");
				request.setAttribute("modelLogins", modelLogins);
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}
			else {
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
				request.setAttribute("modelLogins", modelLogins);
				
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String msg = "operação realizada com sucesso!";

			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");

			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			modelLogin.setNome(nome);
			modelLogin.setEmail(email);
			modelLogin.setLogin(login);
			modelLogin.setSenha(senha);

			if (daoUsuarioRepository.validarLogin(modelLogin.getLogin()) && modelLogin.getId() == null) {
				msg = "já exite um usuário com o mesmo login! informe um login diferente";
			} else {
				if (modelLogin.isNew()) {
					msg = "salvo com sucesso!";
				} else {
					msg = "atualizado com sucesso!";
				}

				// gravar usuário no banco de dados
				modelLogin = daoUsuarioRepository.gravarUsuario(modelLogin);
			}

			request.setAttribute("msg", msg);
			request.setAttribute("modelLogin", modelLogin); // manter os dados na tela

			List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList();
			request.setAttribute("modelLogins", modelLogins);
			
			RequestDispatcher redirecionar = request.getRequestDispatcher("principal/usuario.jsp");
			redirecionar.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}

}
