package beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import controlador.Controlador;
import funciones.Utils;
import model.Partido;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanPartidosPendientesConfirmacion")
@ViewScoped
public class BeanPartidosPendientesConfirmacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Partido> partidosPendientesDeConfirmar;
	private List<String> partidosPendientesDeConfirmarFechas;
	private List<String> partidosQueSeQuierenConfirmar;
	private boolean buttonConfirmarDisabled;
	

	public List<Partido> getPartidosPendientesDeConfirmar() {
		return partidosPendientesDeConfirmar;
	}

	public void setPartidosPendientesDeConfirmar(List<Partido> partidosPendientesDeConfirmar) {
		this.partidosPendientesDeConfirmar = partidosPendientesDeConfirmar;
	}

	public List<String> getPartidosPendientesDeConfirmarFechas() {
		return partidosPendientesDeConfirmarFechas;
	}

	public void setPartidosPendientesDeConfirmarFechas(List<String> partidosPendientesDeConfirmarFechas) {
		this.partidosPendientesDeConfirmarFechas = partidosPendientesDeConfirmarFechas;
	}

	public List<String> getPartidosQueSeQuierenConfirmar() {
		return partidosQueSeQuierenConfirmar;
	}

	public void setPartidosQueSeQuierenConfirmar(List<String> partidosQueSeQuierenConfirmar) {
		this.partidosQueSeQuierenConfirmar = partidosQueSeQuierenConfirmar;
	}


	public boolean isButtonConfirmarDisabled() {
		return buttonConfirmarDisabled;
	}

	public void setButtonConfirmarDisabled(boolean buttonConfirmarDisabled) {
		this.buttonConfirmarDisabled = buttonConfirmarDisabled;
	}
	
	private void inicializarPartidosPendientes() {
		partidosPendientesDeConfirmarFechas = new LinkedList<>();
		partidosPendientesDeConfirmar = Controlador.getInstancia().getPartidosPendientesDeConfirmarPorUsuario(Utils.getUsuarioActual());
		
		for (Partido p : partidosPendientesDeConfirmar) {
			partidosPendientesDeConfirmarFechas.add(p.getTemporada().getNombre() + " - " + p.getFecha());
		}
	}
	

	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
		
		Utils.comprobarRegistroYMostrarMensaje("Partidos confirmados correctamente");
		
		buttonConfirmarDisabled = true;
		
		inicializarPartidosPendientes();
	}
	
	public void elegirPartidos() {
		if (partidosQueSeQuierenConfirmar.isEmpty()) {
			buttonConfirmarDisabled = true;
			return;
		}
		
		buttonConfirmarDisabled = false;
	}
	
	public String confirmarPartidos() {
		List<Integer> idPartidosAConfirmar = new LinkedList<>();
		
		for (String string : partidosQueSeQuierenConfirmar) {

			String[] fechaCortada = string.split(" - "); /* Ejemplo de string: futbol - Wed Jun 06 00:00:00 CEST 2018 */
			String fechaStringConfirmar = fechaCortada[fechaCortada.length-1]; /* Nos quedamos con solo la fecha */
			
			int i = 0;
			boolean encontrado = false;
			while (i < partidosPendientesDeConfirmar.size() && !encontrado) {
				Partido p = partidosPendientesDeConfirmar.get(i);
				
				String fechaString = p.getFecha().toString();
				
				if (fechaString.equals(fechaStringConfirmar)) {
					encontrado = true;
					idPartidosAConfirmar.add(p.getId());
				}
				i++;
			}
		}
		
		for (Integer id : idPartidosAConfirmar) {
			Controlador.getInstancia().confirmarAsistenciaAPartido(Utils.getUsuarioActual(), id);
		}
		
		return Utils.resetView() + "&registrado=true";
	}

}
