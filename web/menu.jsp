<%-- 
    Document   : menu
    Created on : 22-abr-2021, 20:06:11
    Author     : Ivan
--%>

<%@page import="tawevents.dto.UsuarioDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Menu</title>
        <link rel="stylesheet" href="menuCSS.css">
    </head>
    <%
        UsuarioDTO usuario = (UsuarioDTO)session.getAttribute("usuario");
        if (usuario == null) {
    %>        
    <jsp:forward page="inicioSesion.jsp" />
        
    <%
        }
    %>
    <body>

    <ul class="topnav">
        <li> <button type="submit" class="btn btn-success btn-lg btn-block" disabled>Has iniciado sesión como <%= usuario.getNickname() %></button>
        <li><form method="POST" action="homeAdmin.jsp">
            <button type="submit" class="btn btn-success btn-lg btn-block">Home</button>
            </form> </li>
        <li><form method="POST" action="ServletUsuarioListar">
            <button type="submit" class="btn btn-success btn-lg btn-block">Usuarios</button>
            </form> </li>
        <li><form method="POST" action="ServletEventoListar">
            <button type="submit" class="btn btn-success btn-lg btn-block">Eventos</button>
            </form> </li>
        <li class="right"><form method="POST" action="ServletCerrarSesion">
            <button type="submit" onclick="return confirm('¿Estás seguro de que quieres cerrar sesión?')" class="btn btn-success btn-lg btn-block">Cerrar sesión</button>
            </form> </li>        
    </ul>

    </body>
</html>
