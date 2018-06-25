package beans;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import controlador.Controlador;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanValidar")
@ViewScoped
public class BeanValidar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usuario;
	private String mensajeInformacion;
	
	public void setMensajeInformacion(String mensajeInformacion) {
		this.mensajeInformacion = mensajeInformacion;
	}
	
	public String getMensajeInformacion() {
		return mensajeInformacion;
	}

	@PostConstruct
	public void init() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

		usuario = params.get("usuario");
		
		if (usuario == null) {
			mensajeInformacion = "No se ha podido verificar su correo.";
		}else {
			boolean validado = Controlador.getInstancia().validarUsuario(usuario);
			if (!validado) {
				mensajeInformacion = "No se ha podido verificar su correo.";
			}else {
				mensajeInformacion = "Usuario validado correctamente.";
			}
		}
	}
	
	public String goToMain() {
		return "main";	
	}

}
