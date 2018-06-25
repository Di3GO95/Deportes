package jms.confirmaciones;

import java.util.Map;
import javax.faces.context.FacesContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import beans.BeanJMSConfirmaciones;

public class OyenteCola implements MessageListener {

	private BeanJMSConfirmaciones beanMensajes;

	public OyenteCola() {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		beanMensajes = (BeanJMSConfirmaciones) session.get("beanJMSConfirmaciones");
	}

	@Override
	public void onMessage(Message mensaje) {
		if (mensaje instanceof TextMessage) {
			TextMessage mensajeTexto = (TextMessage) mensaje;
			System.out.println("OyenteCola.onMessage()");
			try {
				beanMensajes.getMensajesRecibidos().add(mensajeTexto.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
