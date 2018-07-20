package beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import controlador.Controlador;
import funciones.Mensajes_Estados;
import funciones.Utils;
import jms.mensajes.SubscriptorMensajes;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanMensajes")
@SessionScoped
public class BeanJMSMensajes implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> mensajesRecibidos = new LinkedList<>();
	private String[] mensajesRecibidosSeleccionados = null;
	private boolean nuevosMensajes = false;
	
	
	public boolean isNuevosMensajes() {
		return nuevosMensajes;
	}

	public void setNuevosMensajes(boolean nuevosMensajes) {
		this.nuevosMensajes = nuevosMensajes;
	}

	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
	}
	
	public void reiniciarNotificacionMensajes() {
		nuevosMensajes = false;
	}
	
	public void recibirMensajes() {
		try {
			String usuario = Utils.getUsuarioActual();
			
			List<Integer> listaTemporadasDeUsuario = Controlador.getInstancia().getTemporadasDeUsuario(usuario);
			
			if (listaTemporadasDeUsuario.size() > 0) {
				String selector = "(temporada = " + listaTemporadasDeUsuario.get(0) + ")";
		
				for (int i = 1; i < listaTemporadasDeUsuario.size(); i++) {
					selector += " OR (temporada = " + listaTemporadasDeUsuario.get(i) + ")";
				}
				
				Mensajes_Estados mensajesInicializados = Utils.inicializarMensajes(selector);
				if (mensajesInicializados.equals(Mensajes_Estados.NO_EXISTE)) {
					SubscriptorMensajes.registrarSubscriptor(usuario, selector); /* Primera subscripcion */
				}else {
					if (mensajesInicializados.equals(Mensajes_Estados.EXISTE_DISTINTO)) { /* Se ha añadido/borrado al usuario de una temporada */
						SubscriptorMensajes.registrarSubscriptor(usuario, selector);
					}else {
						SubscriptorMensajes.registrarSubscriptor(usuario, selector);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getMensajesRecibidosSeleccionados() {
		return mensajesRecibidosSeleccionados;
	}

	public void setMensajesRecibidosSeleccionados(String[] mensajesRecibidosSeleccionados) {
		this.mensajesRecibidosSeleccionados = mensajesRecibidosSeleccionados;
	}

	public List<String> getMensajesRecibidos() {
		return mensajesRecibidos;
	}

	public void setMensajesRecibidos(List<String> mensajesRecibidos) {
		this.mensajesRecibidos = mensajesRecibidos;
	}
	
	public String irAConfirmarPartidos() {
		String destino = "partidosPendientesConfirmacion";
		
		return destino;
	}
}
