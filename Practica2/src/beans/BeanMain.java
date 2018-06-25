package beans;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.ServletException;

import funciones.Utils;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanMain")
@ViewScoped
public class BeanMain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException, IOException {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
	}
	
}
