package servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import controlador.Controlador;
import funciones.Utils;
import model.User;

@WebServlet("/editarPerfil")
@MultipartConfig
public class ServletEditPerfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private User userActual;
	  
    public ServletEditPerfil() {
        super();
        userActual = null; 
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String usuarioActual = (String) request.getSession().getAttribute("usuarioActual"); 
		userActual = Controlador.getInstancia().getUserByUsuario(usuarioActual);
		
		if (userActual == null) {
			RequestDispatcher rd = request.getRequestDispatcher("login");
			//forward
			rd.forward(request, response);
			return;
		}
		
		
		String pathFichero = getServletConfig().getServletContext().getRealPath("html/editarPerfil.html");
		BufferedReader br = new BufferedReader(new FileReader(pathFichero));
		String linea;
		
		while ((linea = br.readLine()) != null) {
			// Nombre de usuario en la cabecera
			if (linea.contains("Hola"))
				linea += userActual.getUsuario();
			
			// Datos del usuario (nombre de usuario, mail, tlf)
			else if (linea.contains("input")) {
				if (linea.contains("usuario"))
					linea += " value='" + userActual.getUsuario() + "'";
				else if (linea.contains("mail"))
					linea += " value='" + userActual.getMail() + "'";
				else if (linea.contains("telefono"))
					linea += " value='" + userActual.getTelefono() + "'";
			}
			
			// Foto de perfil
			else if (linea.contains("foto")) {
				byte[] fotoEncoded = Base64.getEncoder().encode(userActual.getFotoPerfil());
				String fotoSrc = "data:image/png;base64, " + new String(fotoEncoded);
				String fotoHtml = "<br><br><img src=\"" + fotoSrc + "\" width=100px height=100px alt=\"fotoPerfil\" />";
				
				linea += fotoHtml;
			}
			
			response.getWriter().println(linea);
		}
		
		br.close();
	}
	
	private void errorUsuario(HttpServletResponse response, String mensajeError) throws IOException{
		response.setHeader("refresh", "5;editarPerfil"); // a los 5 segundos, redirigimos
		response.setContentType("text/html");
		String pathFichero = getServletConfig().getServletContext().getRealPath("html/error.html");
		
		System.out.println("Pathfichero: " + pathFichero);
		BufferedReader br = new BufferedReader(new FileReader(pathFichero));
		String linea;
		
		while ((linea = br.readLine()) != null) {
			// Justo despues de la etiqueta h2, es donde colocamos nuestro mensaje de error
			if (linea.contains("<h1>"))
				linea += "¡Ups! La modificación falló";
			else if (linea.contains("<h3>"))
				linea += mensajeError;
			
			else if (linea.contains("<!-- redireccion -->"))
				linea += "<a href= 'editarPerfil' style=\"text-decoration: none; color:black\">AQUÍ</a>";
			
			else if (linea.contains("<!-- cabecera -->")) {
				linea += "Hola, " + userActual.getUsuario();
				linea += " | <a href='editarPerfil' style=\"color:white\">Editar perfil</a>";
				linea += " | <a href='logout' style=\"color:white\">Cerrar sesión</a>";
			}
			response.getWriter().println(linea);
		}
		
		br.close();
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String usuario = request.getParameter("usuario");
		String claveActual = request.getParameter("claveActual");
		String claveNueva1 = request.getParameter("claveNueva1");
		String claveNueva2 = request.getParameter("claveNueva2");
		String mail = request.getParameter("mail");
		String telefono = request.getParameter("telefono");
		
		if (!claveNueva1.equals(claveNueva2)){ // Clave incorrecta
			errorUsuario(response, "Las claves no coinciden.");
			return;
		}
		
		if (!userActual.getClave().equals(claveActual)) {
			errorUsuario(response, "La clave actual no correcta.");
			return;
		}
		
		Part fotoPart = request.getPart("foto");
		boolean existeFoto = fotoPart.getSize() > 0;
		
		if (existeFoto) {
			InputStream fotoIS = fotoPart.getInputStream();
			
			byte[] fotoPerfilArrayBytes = Utils.inputStreamToByteArray(fotoIS);
			
			fotoIS.close();
			
			Controlador.getInstancia().actualizarUsuario(usuario, claveNueva1, mail, telefono, fotoPerfilArrayBytes);
			
		}else {
			Controlador.getInstancia().actualizarUsuario(usuario, claveNueva1, mail, telefono);
		}
		request.getSession().setAttribute("usuarioActual", usuario);
		
		
		
		response.setHeader("refresh", "5;xhtml/main.xhtml"); // a los 5 segundos, redirigimos a login
		response.setContentType("text/html");
		String pathFichero = getServletConfig().getServletContext().getRealPath("html/usuarioActualizado.html");
		
		BufferedReader br = new BufferedReader(new FileReader(pathFichero));
		String linea;
		
		while ((linea = br.readLine()) != null) {
			if (linea.contains("Hola"))
				linea += userActual.getUsuario();
			response.getWriter().println(linea);
		}
		
		br.close();
	}

}
