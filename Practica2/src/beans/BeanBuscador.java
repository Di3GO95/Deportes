package beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import controlador.Controlador;
import funciones.Utils;
import model.Temporada;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanBuscador")
@ViewScoped
public class BeanBuscador implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String temporadaElegida;
	private List<String> temporadas;

	private String jugador;
	private List<String> jugadores;

	private Date fechaInicio;
	private Date fechaFin;

	private boolean buscarDisabled;
	
	private List<Temporada> listaTemporadas;

	public String getTemporadaElegida() {
		return temporadaElegida;
	}

	public void setTemporadaElegida(String temporadaElegida) {
		this.temporadaElegida = temporadaElegida;
	}

	public List<String> getTemporadas() {
		return temporadas;
	}

	public void setTemporadas(List<String> temporadas) {
		this.temporadas = temporadas;
	}

	public String getJugador() {
		return jugador;
	}

	public void setJugador(String jugador) {
		this.jugador = jugador;
	}

	public List<String> getJugadores() {
		return jugadores;
	}

	public void setJugadores(List<String> jugadores) {
		this.jugadores = jugadores;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean isBuscarDisabled() {
		return buscarDisabled;
	}

	public void setBuscarDisabled(boolean buscarDisabled) {
		this.buscarDisabled = buscarDisabled;
	}

	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
		
		buscarDisabled = false;
		
		listaTemporadas = Controlador.getInstancia().getTodasLasTemporadas();

		temporadas = new LinkedList<>();
		for (Temporada temporada : listaTemporadas) {
			temporadas.add(temporada.getNombre());
		}
		
		jugadores = Controlador.getInstancia().getTodosLosUsuarios();
	}

	public void buscar() throws IOException {
		String url = "buscadorMostrarResultados.xhtml?";
		url += "temporada=" + temporadaElegida;
		url += "&jugador=" + jugador;
		url += "&fechaInicio=" + fechaInicio;
		url += "&fechaFin=" + fechaFin;

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(url);
	}

	public void elegirFecha() {
		if (fechaInicio == null && fechaFin != null)
			buscarDisabled = true;
		else if (fechaInicio != null && fechaFin == null)
			buscarDisabled = true;
		else
			buscarDisabled = false;
	}

}
