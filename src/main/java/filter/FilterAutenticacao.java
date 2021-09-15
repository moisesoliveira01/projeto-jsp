package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/principal/*"}) //intercepta as requisições que vierem do projeto ou mapeamento
public class FilterAutenticacao implements Filter {
    
	//criar conexão com o banco
	private static Connection connection;
	
    public FilterAutenticacao() {
    }

    //encerra os processos quando o servidor é parado
	public void destroy() {
		try {
			connection.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	//intercepta as requisições e as respostas no sistema
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
       
	   try {
		
		   HttpServletRequest req = (HttpServletRequest) request;
	       HttpSession session = req.getSession();
	       
	       String usuarioLogado = (String) session.getAttribute("usuario");
	       
	       String urlParaAutenticar = req.getServletPath(); //url que está sendo acessada
	       
	       //verificar se o usuário está logado
	          if (usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {
	        	
	          //redireciona para a página de login antes de acessar a página que o usuário tentou
	    	  RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar); 
	          request.setAttribute("msg", "você precisa realizar o login!");
	          redirecionar.forward(request, response);
	          return; //para a execução e redireciona para o login
	          }
	          else {
	          chain.doFilter(request, response); //redireciona
	          }
	          
	          connection.commit(); //salva as alterações no banco de dados
          
	   }catch (Exception e) {
		    e.printStackTrace();
		    
		    e.printStackTrace();
		    RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		    
		    try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	    }
	}
	
    //inicia os processos e recursos quando o servidor sobe o projeto
	public void init(FilterConfig fConfig) throws ServletException {
	    connection = SingleConnectionBanco.getConnection();
	}

}
