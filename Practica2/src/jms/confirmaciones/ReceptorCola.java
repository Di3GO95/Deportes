package jms.confirmaciones;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReceptorCola {
	public static void recibir() throws NamingException, JMSException {
		InitialContext iniCtx = new InitialContext();
		Object tmp = iniCtx.lookup("ConnectionFactory_two");
		QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
		QueueConnection conn = qcf.createQueueConnection();
		Queue queue = (Queue) iniCtx.lookup("queue/adCola");
		QueueSession session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		QueueReceiver queueReceiver = session.createReceiver(queue);
		conn.start();
		
		queueReceiver.setMessageListener(new OyenteCola());
	}
}
