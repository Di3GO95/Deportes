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
@ManagedBean(name = "beanGestionUsuarioEnTemporadas")
@ViewScoped
public class BeanGestionUsuarioEnTemporadas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> listaTemporadas;
	private String temporadaElegida;
	private boolean buttonDisabled;
	private boolean listaParticipantesDisabled;

	private DualListModel<String> participantes;

	private boolean usuarioEnTemporada;
	private String usuarioActual;
	
	public boolean isUsuarioEnTemporada() {
		return usuarioEnTemporada;
	}

	public void setUsuarioEnTemporada(boolean usuarioEnTemporada) {
		this.usuarioEnTemporada = usuarioEnTemporada;
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
		else
			usuarioActual = Utils.getUsuarioActual();
		
		Utils.comprobarRegistroYMostrarMensaje("Usuario añadido/eliminado correctamente");
		
		listaTemporadas = Controlador.getInstancia().getNombreDeTodasLasTemporadas();
		buttonDisabled = true;
		listaParticipantesDisabled = true;

		participantes = new DualListModel<String>();
		usuarioEnTemporada = false;
	}

	public void elegirTemporada() {
		int idTemporada = Controlador.getInstancia().getIdTemporadaFromNombre(temporadaElegida);
		usuarioEnTemporada = Controlador.getInstancia().isUserParticipanteInTemporada(idTemporada, usuarioActual);

		List<String> listaInicialCandidato = new LinkedList<>();
		List<String> listaInicialParticipante = new LinkedList<>();
		
		if (usuarioEnTemporada)
			listaInicialParticipante.add(usuarioActual);
		else
			listaInicialCandidato.add(usuarioActual);

		participantes.setSource(listaInicialCandidato);
		participantes.setTarget(listaInicialParticipante);

		listaParticipantesDisabled = false;
	}

	public void transfer(TransferEvent event) {
		buttonDisabled = false;
	}

	public String confirmar() {
		int idTemporadaElegida = Controlador.getInstancia().getIdTemporadaFromNombre(temporadaElegida);
		
		if (!usuarioEnTemporada) { // Usuario no estaba en la temporada...
			for (String usuario : participantes.getTarget()) { //... y se quiere añadir a la temporada
				Controlador.getInstancia().addParticipanteATemporada(idTemporadaElegida, usuario);
			}
		}else { // Usuario ya estaba en la temporada...
			for (String usuario : participantes.getSource()) { //... y se quiere quitar de la temporada
				Controlador.getInstancia().removeParticipanteATemporada(idTemporadaElegida, usuario);
			}
		}

		return Utils.resetView() + "&registrado=true";
	}

}
