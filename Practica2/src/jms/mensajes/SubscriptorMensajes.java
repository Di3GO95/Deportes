package jms.mensajes;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SubscriptorMensajes {
	private static TopicSubscriber topicSubscriber = null;
	private static TopicSession session = null;
	
	public static void registrarSubscriptor(String usuario, String selector) throws NamingException, JMSException {
		TopicConnection conn = null;
		Topic topic = null;
		
		InitialContext iniCtx = new InitialContext();
		Object tmp = iniCtx.lookup("ConnectionFactory");
		TopicConnectionFactory qcf = (TopicConnectionFactory) tmp;
		conn = qcf.createTopicConnection();
		
		topic = (Topic) iniCtx.lookup("topic/adApartado");
		
		session = conn.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		
		conn.start();
		
		if (topicSubscriber != null)
			topicSubscriber.close();
		topicSubscriber = session.createDurableSubscriber(topic, usuario, selector, false);
		
		topicSubscriber.setMessageListener(new OyenteMensajes());
	}
}
