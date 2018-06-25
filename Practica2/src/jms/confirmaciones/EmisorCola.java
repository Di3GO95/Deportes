package jms.confirmaciones;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EmisorCola {
	public static void enviar(String texto) throws NamingException, JMSException {
		InitialContext iniCtx = new InitialContext();
		Object tmp = iniCtx.lookup("ConnectionFactory_two");
		QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
		QueueConnection conn = qcf.createQueueConnection();
		Queue queue = (Queue) iniCtx.lookup("queue/adCola");
		QueueSession session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		TextMessage message = session.createTextMessage();
		message.setText(texto);
		QueueSender queueSender = session.createSender(queue);
		queueSender.send(message);
		conn.close();
	}
}
