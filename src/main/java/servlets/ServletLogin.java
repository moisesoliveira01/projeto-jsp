package servlets;

import java.io.IOException;

import dao.DAOLoginRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.ModelLogin;

@WebServlet(urlPatterns = {"/principal/ServletLogin", "/ServletLogin"})
public class ServletLogin extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private DAOLoginRepository daoLogin = new DAOLoginRepository();

    public ServletLogin() {

    }
    
    //recebe os dados enviados em parâmetros
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   String acao = request.getParameter("acao");
	   
	   if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("logout")) {
	   request.getSession().invalidate(); //invalida a sessão
	   RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
	   redirecionar.forward(request, response);
	   }
		
	   doPost(request, response);
	}
    
	//recebe os dados enviados por um formulário
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   String login = request.getParameter("login");
	   String senha = request.getParameter("senha");
	   String url = request.getParameter("url");
	   
	   
	   try {
		   if (login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {
		   ModelLogin model = new ModelLogin();
		   model.setLogin(login);
		   model.setSenha(senha);
		   
		      if (daoLogin.validarAutenticacao(model)) {
		      request.getSession().setAttribute("usuario", model.getLogin());
		      
		         if (url == null || url.equals("null")) {
		         url = "principal/principal.jsp"; 
		         }
		      
		      RequestDispatcher redirecionar = request.getRequestDispatcher(url);
		      redirecionar.forward(request, response);
		      }
		      else {
		      RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");
		   	  request.setAttribute("msg", "informe login e senha corretamente!");
		   	  redirecionar.forward(request, response); 	  
		      }
		   
		   }
		   else {
		   RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
		   request.setAttribute("msg", "informe login e senha corretamente!");
		   redirecionar.forward(request, response);
		   }
	   }catch (Exception e) {
	       e.printStackTrace();
	       RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
		   request.setAttribute("msg", e.getMessage());
		   redirecionar.forward(request, response);
	   }
	}

}
