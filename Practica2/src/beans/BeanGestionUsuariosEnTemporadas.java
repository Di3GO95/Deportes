package beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import controlador.Controlador;
import funciones.Utils;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanGestionUsuariosEnTemporadas")
@ViewScoped
public class BeanGestionUsuariosEnTemporadas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5322350143482663313L;
	private List<String> listaTemporadas;
	private String temporadaElegida;
	private boolean buttonDisabled;
	private boolean listaParticipantesDisabled;

	private DualListModel<String> participantes;

	private List<String> listaInicialUsuariosEnTemporada; /* Usuarios que ya están añadidos en una temporada */
	private List<String> listaInicialPosiblesJugadores; /* todos los usuarios menos los que ya estan en la temporada */

	public List<String> getListaInicialUsuariosEnTemporada() {
		return listaInicialUsuariosEnTemporada;
	}

	public void setListaInicialUsuariosEnTemporada(List<String> listaInicialUsuariosEnTemporada) {
		this.listaInicialUsuariosEnTemporada = listaInicialUsuariosEnTemporada;
	}

	public List<String> getListaInicialPosiblesJugadores() {
		return listaInicialPosiblesJugadores;
	}

	public void setListaInicialPosiblesJugadores(List<String> listaInicialPosiblesJugadores) {
		this.listaInicialPosiblesJugadores = listaInicialPosiblesJugadores;
	}

	public DualListModel<String> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(DualListModel<String> participantes) {
		this.participantes = participantes;
	}

	public String getTemporadaElegida() {
		return temporadaElegida;
	}

	public void setTemporadaElegida(String temporadaElegida) {
		this.temporadaElegida = temporadaElegida;
	}

	public List<String> getListaTemporadas() {
		return listaTemporadas;
	}

	public void setListaTemporadas(List<String> listaTemporadas) {
		this.listaTemporadas = listaTemporadas;
	}

	public boolean isListaParticipantesDisabled() {
		return listaParticipantesDisabled;
	}

	public void setListaParticipantesDisabled(boolean listaParticipantesDisabled) {
		this.listaParticipantesDisabled = listaParticipantesDisabled;
	}

	public boolean isButtonDisabled() {
		return buttonDisabled;
	}

	public void setButtonDisabled(boolean buttonDisabled) {
		this.buttonDisabled = buttonDisabled;
	}

	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
		
		Utils.comprobarRegistroYMostrarMensaje("Usuarios añadidos/eliminados correctamente");

		listaTemporadas = Controlador.getInstancia().getNombreDeTodasLasTemporadas();
		buttonDisabled = true;
		listaParticipantesDisabled = true;

		participantes = new DualListModel<String>();
	}

	public void elegirTemporada() {
		listaInicialUsuariosEnTemporada = Controlador.getInstancia().getParticipantesEnTemporada(temporadaElegida);

		listaInicialPosiblesJugadores = new LinkedList<>();
		List<String> listaTodosLosUsuarios = Controlador.getInstancia().getTodosLosUsuarios();
		for (String usuario : listaTodosLosUsuarios) {
			if (!listaInicialUsuariosEnTemporada.contains(usuario)) {
				listaInicialPosiblesJugadores.add(usuario);
			}
		}

		participantes.setSource(listaInicialPosiblesJugadores);
		participantes.setTarget(listaInicialUsuariosEnTemporada);

		listaParticipantesDisabled = false;
	}

	public void transfer(TransferEvent event) {
		buttonDisabled = false;
	}

	public String confirmar() {
		int idTemporadaElegida = Controlador.getInstancia().getIdTemporadaFromNombre(temporadaElegida);
		
		for (String usuario : participantes.getSource()) {
			if (!listaInicialPosiblesJugadores.contains(usuario)) {
				Controlador.getInstancia().removeParticipanteATemporada(idTemporadaElegida, usuario);
			}
		}
		for (String usuario : participantes.getTarget()) {
			if (!listaInicialUsuariosEnTemporada.contains(usuario)) {
				Controlador.getInstancia().addParticipanteATemporada(idTemporadaElegida, usuario);
			}
		}
		

		return Utils.resetView() + "&registrado=true";
	}

}
