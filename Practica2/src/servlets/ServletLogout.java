package servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/logout")
public class ServletLogout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ServletLogout() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String usuarioActual = (String) request.getSession().getAttribute("usuarioActual"); 
		
		if (usuarioActual == null) { // Nada que desconectar, no hay usuario conectado
			RequestDispatcher rd = request.getRequestDispatcher("login");
			//forward
			rd.forward(request, response);
			return;
		}else {
			request.getSession().removeAttribute("usuarioActual");
			request.getSession().removeAttribute("isAdmin");
		}
		
		response.setHeader("refresh", "5;login"); // a los 5 segundos, redirigimos a login
		response.setContentType("text/html");
		String pathFichero = getServletConfig().getServletContext().getRealPath("html/logout.html");
		
		BufferedReader br = new BufferedReader(new FileReader(pathFichero));
		String linea;
		
		while ((linea = br.readLine()) != null)
			response.getWriter().println(linea);
		
		br.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
