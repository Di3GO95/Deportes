package jms.mensajes;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class PublicadorMensajes {
	public static void enviar(String texto, int temporada) throws NamingException, JMSException {
		InitialContext iniCtx = new InitialContext();
		Object tmp = iniCtx.lookup("ConnectionFactory");
		TopicConnectionFactory qcf = (TopicConnectionFactory) tmp;
		TopicConnection conn = qcf.createTopicConnection();
		
		Topic topic = (Topic) iniCtx.lookup("topic/adApartado");
		TopicSession session = conn.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		
		TextMessage message = session.createTextMessage();
		message.setText(texto);
		message.setIntProperty("temporada", temporada);
		
		TopicPublisher topicPublisher = session.createPublisher(topic);
		topicPublisher.publish(message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, 7 * 24 * 3600 * 1000L);
		
		conn.close();
	}
}
