package funciones;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;


public class Utils {

	/**
	 * Resetea los campos de una vista JSF, haciendo una redirecci�n hacia la propia
	 * p�gina.
	 * <p>
	 * S�lo funciona con "ViewScoped" y "Request" beans.
	 * 
	 * @return String con la URL resultante
	 */
	public static String resetView() {
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot view = context.getViewRoot();
		return view.getViewId() + "?faces-redirect=true";
	}

	public static String concatenar(String c1, String c2) {
		return c1 + c2;
	}

	/**
	 * Convierte un string en un objeto Date. El string debe seguir el siguiente
	 * formato:
	 * <p>
	 * EEE MMM dd HH:mm:ss zzzz yyyy
	 * <p>
	 * Por ejemplo:
	 * <p>
	 * Sun May 27 07:31:00 CEST 2018
	 * 
	 * @param fechaString
	 * @return objeto Date o null si no puede hacer la conversi�n
	 */
	public static Date fromStringToDate(String fechaString) {
		DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.US);
		Date fecha = null;
		try {
			fecha = format.parse(fechaString);
		} catch (ParseException e) {
		}
		return fecha;
	}

	/**
	 * Comprueba si se ha hecho un nuevo registro (de temporada, por ejemplo) y
	 * muestra un mensaje en caso afirmativo.
	 * <p>
	 * Para la comprobaci�n, se mira si la URL tiene el par�metro "registrado".
	 * <p>
	 * 
	 * Ejemplo de url donde se muestra un mensaje:
	 * <p>
	 * http://localhost:8080/practica/xhtml/registrarTemporada.xhtml?registrado=true
	 * 
	 * @param mensaje
	 *            Mensaje a mostrar
	 */
	public static void comprobarRegistroYMostrarMensaje(String mensaje) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

		String registrado = params.get("registrado");

		if (registrado != null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public static void redirigirALogin() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		try {
			response.sendRedirect(request.getContextPath() + "/login");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getUsuarioActual() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		return (String) request.getSession().getAttribute("usuarioActual");
	}
	
	
	/**
	 * M�todo encargado de enviar un correo desde la direcci�n
	 * aadd.diegovalera@gmail.com hasta un destinatario.
	 * <p>
	 * En el correo, se enviar� un enlace que permitir� al
	 * usuario receptor validar su correo.
	 * @param to Email al que enviar el correo.
	 * @param usuario Nombre de usuario al que se le env�a el email
	 */
	public static void enviarCorreo(String to, String usuario) {
		final String from = "aadd.diegovalera@gmail.com";
		final String pswd = "123456789_aadd";
		final String titulo = "Registro en AADD - Diego Valera Duran";
		String cuerpo = "Se ha registrado en AADD - Diego Valera Dur�n, pero necesita verificar su correo, pinche en el siguiente enlace: \n\n";
		cuerpo += "http://localhost:8080/practica/xhtml/validar.xhtml?usuario=" + usuario;
		
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		//props.put("mail.debug", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
		final String username = from;//
		final String password = pswd;
		try {
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// Creando un nuevo mensaje
			Message msg = new MimeMessage(session);

			// Estableciendo los campos DESDE y PARA
			msg.setFrom(new InternetAddress(username));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			msg.setSubject(titulo);
			msg.setText(cuerpo);
			msg.setSentDate(new Date());
			Transport.send(msg);
		} catch (MessagingException e) {
			System.err.println("Error al enviar el mensaje: " + e);
		}
	}
	
	
	public static byte[] inputStreamToByteArray(InputStream is) {
		byte[] res = null;
		try {
			res = IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	/**
	 * Comprueba si se encuentra en la sesi�n actual el atributo "mensajes_temporadas". Este atributo contiene
	 * los ids de las temporadas en las que est� registrado el usuario. 
	 * </br>
	 * Por ejemplo, si el usuario est� inscrito en 3 temporadas con IDs 100, 200 y 300, respectivamente, este
	 * atributo contendr�:
	 * </br></br>
	 * (temporada = 100) OR (temporada = 200) OR (temporada = 300)
	 * 
	 * </br></br>
	 * Si no est� el atributo, se a�ade.</br>
	 * Si est� el atributo, y es igual, no se hace nada.</br>
	 * Si est� el atributo pero es distinto, se sustituye.</br>
	 * 
	 * @param selector String con el que comparar el atributo de sesi�n
	 * @return Un objeto del tipo Mensajes_Estados para identificar en qu� estado se encuentra. </br></br>
	 * Si no est� el atributo, se devuelve NO_EXISTE</br>
	 * Si est� el atributo, y es igual, se devuelve EXISTE_IGUAL.</br>
	 * Si est� el atributo pero es distinto, se devuelve EXISTE_DISTINTO.</br>
	 */
	public static Mensajes_Estados inicializarMensajes(String selector) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		String mensajes_temporadas = (String) request.getSession().getAttribute("mensajes_temporadas");
		
		if (mensajes_temporadas == null) {
			System.out.println("nulo");
			request.getSession().setAttribute("mensajes_temporadas", selector);
			return Mensajes_Estados.NO_EXISTE;
		}else {
			System.out.println("no nulo");
			if (mensajes_temporadas.equals(selector)) {
				System.out.println("Es igual");
				return Mensajes_Estados.EXISTE_IGUAL;
			}else {
				System.out.println("no es igual");
				request.getSession().setAttribute("mensajes_temporadas", selector);
				return Mensajes_Estados.EXISTE_DISTINTO;
			}
		}
	}
	
	public static void mostrarNotificacionNuevosMensajes() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		
		request.getSession().setAttribute("nuevosMensajes", true);
	}
	
	public static void ocultarNotificacionNuevosMensajes() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		
		request.getSession().setAttribute("nuevosMensajes", false);
	}
	
	

}
