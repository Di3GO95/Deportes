package servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controlador.Controlador;
import model.User;

@WebServlet(
		urlPatterns = { "/login" }, 
		initParams = { 
				@WebInitParam(name = "autor", value = "Diego Valera Durán")
		})
public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ServletLogin() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User usuarioActual = (User) request.getSession().getAttribute("usuarioActual"); // guardado en la sesión el usuario actual
		if (usuarioActual != null) {
			RequestDispatcher rd = request.getRequestDispatcher("main");
			//forward
			rd.forward(request, response);
		}
		
		
		// Leemos el contenido de la respuesta del fichero "login.html", en lugar de componer la respuesta aqui
		response.setContentType("text/html");
		String pathFichero = getServletConfig().getServletContext().getRealPath("html/login.html");
		
		BufferedReader br = new BufferedReader(new FileReader(pathFichero));
		String linea;
		
		while ((linea = br.readLine()) != null)
			response.getWriter().println(linea);
		
		br.close();
	}
	
	private void errorUsuario(HttpServletResponse response, String mensajeError) throws IOException{
		response.setHeader("refresh", "5;login"); // a los 5 segundos, redirigimos a login
		response.setContentType("text/html");
		String pathFichero = getServletConfig().getServletContext().getRealPath("html/error.html");
		
		BufferedReader br = new BufferedReader(new FileReader(pathFichero));
		String linea;
		
		while ((linea = br.readLine()) != null) {
			// Justo despues de la etiqueta h2, es donde colocamos nuestro mensaje de error
			if (linea.contains("<h1"))
				linea += "¡Ups! El login falló";
			else if (linea.contains("<h3"))
				linea += mensajeError;
			else if (linea.contains("<!-- redireccion -->"))
				linea += "<a href= 'login' style=\"text-decoration: none; color:black\">AQUÍ</a>";
			else if (linea.contains("<!-- cabecera -->")) {
				linea += "<a href=\"login\" style=\"color:#FFFFFF;\">Login</a>";
				linea += " | <a href=\"registro\" style=\"color:#FFFFFF;\">Registro</a>";
			}
			
			response.getWriter().println(linea);
		}
		
		br.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String usuario = request.getParameter("usuario"); // mismo nombre al name del input del formulario
		String clave = request.getParameter("clave");
		
		boolean login = Controlador.getInstancia().login(usuario, clave);
		
		if (!login) {
			errorUsuario(response, "Error al hacer login. Si no ha validado su correo, compruebe su bandeja de entrada.");
		}else {
			request.getSession().setAttribute("usuarioActual", usuario); // guardado en la sesión el usuario actual
			
			boolean isAdmin = Controlador.getInstancia().isAdmin(usuario);
			if (isAdmin) {
				request.getSession().setAttribute("isAdmin", "true");
			}else {
				request.getSession().setAttribute("isAdmin", "false");
			}
			request.getSession().setAttribute("nuevosMensajes", false);
			
			response.sendRedirect(request.getContextPath() + "/xhtml/main.xhtml");
			
		}
	}

}
