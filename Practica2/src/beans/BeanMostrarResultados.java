package beans;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import controlador.Controlador;
import funciones.Utils;
import model.Alineacion;
import model.Color;
import model.Partido;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanMostrarResultados")
@ViewScoped
public class BeanMostrarResultados implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String temporada;
	String jugador;
	Date fechaInicio;
	Date fechaFin;

	private List<Partido> listaPartidos;
	private Partido partido;
	private List<String> listaPartidosFecha;
	private String partidoFecha;

	private List<Alineacion> listaAlineaciones;
	private Alineacion alineacion;

	private List<String> listaAlineacionesS;
	private String alineacionS;

	private List<String> jugadores;

	public String getAlineacionS() {
		return alineacionS;
	}

	public void setAlineacionS(String alineacionS) {
		this.alineacionS = alineacionS;
	}

	public List<String> getJugadores() {
		return jugadores;
	}

	public void setJugadores(List<String> jugadores) {
		this.jugadores = jugadores;
	}

	public List<String> getListaAlineacionesS() {
		return listaAlineacionesS;
	}

	public void setListaAlineacionesS(List<String> listaAlineacionesS) {
		this.listaAlineacionesS = listaAlineacionesS;
	}

	public List<String> getListaPartidosFecha() {
		return listaPartidosFecha;
	}

	public void setListaPartidosFecha(List<String> listaPartidosFecha) {
		this.listaPartidosFecha = listaPartidosFecha;
	}

	public String getPartidoFecha() {
		return partidoFecha;
	}

	public void setPartidoFecha(String partidoFecha) {
		this.partidoFecha = partidoFecha;
	}

	public String getTemporada() {
		return temporada;
	}

	public void setTemporada(String temporada) {
		this.temporada = temporada;
	}

	public String getJugador() {
		return jugador;
	}

	public void setJugador(String jugador) {
		this.jugador = jugador;
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

	public List<Partido> getListaPartidos() {
		return listaPartidos;
	}

	public void setListaPartidos(List<Partido> listaPartidos) {
		this.listaPartidos = listaPartidos;
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public List<Alineacion> getListaAlineaciones() {
		return listaAlineaciones;
	}

	public void setListaAlineaciones(List<Alineacion> listaAlineaciones) {
		this.listaAlineaciones = listaAlineaciones;
	}

	public Alineacion getAlineacion() {
		return alineacion;
	}

	public void setAlineacion(Alineacion alineacion) {
		this.alineacion = alineacion;
	}

	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
		
		listaPartidosFecha = new LinkedList<>();

		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

		temporada = params.get("temporada");
		jugador = params.get("jugador");
		String fechaInicioString = params.get("fechaInicio");
		String fechaFinString = params.get("fechaFin");
		if (fechaInicioString != null && fechaFinString != null) {
			fechaInicio = Utils.fromStringToDate(fechaInicioString);
			fechaFin = Utils.fromStringToDate(fechaFinString);
		}

		listaPartidos = Controlador.getInstancia().filtrarPartidos(temporada, fechaInicio, fechaFin, jugador);
		
		for (Partido p : listaPartidos) {
			listaPartidosFecha.add(p.getFecha().toString());
		}
	}

	private Partido buscarPartido() {
		int i = 0;
		while (i < listaPartidos.size()) {
			Partido tmp = listaPartidos.get(i);
			if (tmp.getFecha().toString().equals(partidoFecha)) {
				return tmp;
			}
			i++;
		}
		return null;
	}

	public void elegirPartido() {
		partido = buscarPartido();

		listaAlineaciones = partido.getAlineaciones();
		listaAlineacionesS = new LinkedList<>();
		for (Alineacion alineacion : listaAlineaciones) {
			String alineacionS = "";
			if (alineacion.getNombre() != null)
				alineacionS += alineacion.getNombre() + " - ";
			alineacionS += alineacion.getColor() + " - ";
			alineacionS += alineacion.getTanteo();

			listaAlineacionesS.add(alineacionS);
		}
	}

	public void elegirAlineacion() {		
		String[] alineacionSplit = alineacionS.split(" - "); /* Formato: alineacion0 - ROJO - 1 */
		int index = 0;
		String nombreAlineacion = null;
		if (alineacionSplit.length == 3) { /* tiene nombre de alineacion */
			nombreAlineacion = alineacionSplit[index];
			index++;
		}
		String colorString = alineacionSplit[index];
		index++;
		String tanteoString = alineacionSplit[index];
		
		Color color = Color.valueOf(colorString);
		int tanteo = Integer.valueOf(tanteoString);
		
		boolean alineacionEncontrada = false;
		index = 0;
		while (index < listaAlineaciones.size() && !alineacionEncontrada) {
			Alineacion a = listaAlineaciones.get(index);
			if (a.getColor().equals(color)) {
				if (a.getTanteo() == tanteo) {
					if (nombreAlineacion == null)
						alineacionEncontrada = true;
					else if (nombreAlineacion != null && a.getNombre().equals(nombreAlineacion))
						alineacionEncontrada = true;
					
					if (alineacionEncontrada) {
						jugadores = a.getJugadores();
					}
				}
			}
			index++;
		}

		
	}

}
