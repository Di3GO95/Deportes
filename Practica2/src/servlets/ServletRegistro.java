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
		urlPatterns = { "/registro" }, 
		initParams = { 
				@WebInitParam(name = "autor", value = "Diego Valera Durán")
		})
public class ServletRegistro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public ServletRegistro() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User usuarioActual = (User) request.getSession().getAttribute("usuarioActual"); // guardado en la sesión el usuario actual
		if (usuarioActual != null) {
			RequestDispatcher rd = request.getRequestDispatcher("main");
			//forward
			rd.forward(request, response);
			return;
		}
		
		// Leemos el contenido de la respuesta del fichero "login.html", en lugar de componer la respuesta aqui
		response.setContentType("text/html");
		String pathFichero = getServletConfig().getServletContext().getRealPath("html/registro.html");
				
		BufferedReader br = new BufferedReader(new FileReader(pathFichero));
		String linea;
				
		while ((linea = br.readLine()) != null)
			response.getWriter().println(linea);
				
		br.close();
	}
	
	private void errorUsuario(HttpServletResponse response, String mensajeError) throws IOException{
		response.setHeader("refresh", "5;registro"); // a los 5 segundos, redirigimos a registro
		response.setContentType("text/html");
		String pathFichero = getServletConfig().getServletContext().getRealPath("html/error.html");
		
		BufferedReader br = new BufferedReader(new FileReader(pathFichero));
		String linea;
		
		while ((linea = br.readLine()) != null) {
			// Justo despues de la etiqueta h2, es donde colocamos nuestro mensaje de error
			if (linea.contains("<h1"))
				linea += "¡Ups! El registro falló";
			else if (linea.contains("<h3"))
				linea += mensajeError;
			else if (linea.contains("<!-- redireccion -->"))
				linea += "<a href= 'registro' style=\"text-decoration: none; color:black\">AQUÍ</a>";
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
		String clave2 = request.getParameter("clave2");
		String mail = request.getParameter("mail");
		String telefono = request.getParameter("telefono");
		
		if (!clave.equals(clave2)){ // Clave incorrecta
			//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Las claves no coinciden");
			errorUsuario(response, "Las claves no coinciden.");
			return;
		}else{ // Registrar usuario
			
			boolean usuarioRegistrado;
			User usuarioEnbbdd = Controlador.getInstancia().getUserByUsuario(usuario);
			if (usuarioEnbbdd == null)
				usuarioRegistrado = false;
			else
				usuarioRegistrado = true;
			
			//boolean usuarioRegistrado = Controlador.getInstancia().registrarUsuario(usuario, clave2, mail, telefono, fotoPerfilIS);
			if (usuarioRegistrado) {
				errorUsuario(response, "El usuario ya existe.");
				return;
			}
			
			Controlador.getInstancia().registrarUsuario(usuario, clave2, mail, telefono);
			
			response.setHeader("refresh", "5;login"); // a los 5 segundos, redirigimos a login
			response.setContentType("text/html");
			String pathFichero = getServletConfig().getServletContext().getRealPath("html/usuarioRegistrado.html");
			
			BufferedReader br = new BufferedReader(new FileReader(pathFichero));
			String linea;
			
			while ((linea = br.readLine()) != null)
				response.getWriter().println(linea);
			
			br.close();
			
		}
	}

}
