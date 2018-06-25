package jms.mensajes;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import beans.BeanJMSMensajes;

public class OyenteMensajes implements MessageListener {

	private BeanJMSMensajes beanMensajes;
	
	public OyenteMensajes() {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		beanMensajes = (BeanJMSMensajes) session.get("beanMensajes");
	}
	
	@Override
	public void onMessage(Message mensaje) {
		if (mensaje instanceof TextMessage) {
			System.out.println("BeanMensajes: " + beanMensajes);
			
			TextMessage mensajeTexto = (TextMessage) mensaje;
			System.out.println("OyenteSuscripcion.onMessage()");
			try {
				System.out.println("onMessage1");
				beanMensajes.getMensajesRecibidos().add(mensajeTexto.getText());
				System.out.println("onMessage2");
				beanMensajes.setNuevosMensajes(true);
				System.out.println("onMessage3");
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
