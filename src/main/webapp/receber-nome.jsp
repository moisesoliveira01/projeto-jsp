<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <% String nome = request.getParameter("nome"); 
    String idade = request.getParameter("idade");
    
    out.print("Nome: " + nome + "; Idade: " + idade);
  %>
</body>
</html>