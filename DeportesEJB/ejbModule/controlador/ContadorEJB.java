package controlador;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton(name="Contador")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
public class ContadorEJB {
	private int contador;
	
	public ContadorEJB() {
		contador = 0;
	}
	
	public int siguienteValor() {
		return ++contador;
	}
	
	@Lock(LockType.READ)
	public int valorActual() {
		return contador;
	}
}
