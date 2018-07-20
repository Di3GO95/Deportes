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
			TextMessage mensajeTexto = (TextMessage) mensaje;
			try {
				beanMensajes.getMensajesRecibidos().add(mensajeTexto.getText());
				beanMensajes.setNuevosMensajes(true);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
