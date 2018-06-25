package beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jms.JMSException;
import javax.naming.NamingException;

import jms.confirmaciones.ReceptorCola;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanJMSConfirmaciones")
@SessionScoped
public class BeanJMSConfirmaciones implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> mensajesRecibidos = new LinkedList<>();
	
	public void recibirConfirmaciones() {
		try {
			ReceptorCola.recibir();
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	public List<String> getMensajesRecibidos() {
		return mensajesRecibidos;
	}

	public void setMensajesRecibidos(List<String> mensajesRecibidos) {
		this.mensajesRecibidos = mensajesRecibidos;
	}
}
