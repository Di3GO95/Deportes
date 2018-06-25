package beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import controlador.Controlador;
import funciones.Utils;
import model.Color;
import model.Partido;
import model.Temporada;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanRegistrarAlineacion")
@ViewScoped
public class BeanRegistrarAlineacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> temporadas;
	private String temporadaElegida;
	private Temporada temporadaElegidaT;

	private List<String> partidos;
	private String partidoElegido;
	private boolean partidosDisabled;

	private String nombreAlineacion;
	private Color[] posiblesColores;
	private Color colorEquipaciones;
	private List<String> jugadoresPosibles;
	private List<String> jugadoresElegidos;
	private boolean jugadoresDisabled;

	private boolean buttonConfirmarDisabled;

	private List<Temporada> listaTemporadas;
	private List<Partido> listaPartidos;
	private Partido partidoElegidoP;

	public List<String> getTemporadas() {
		return temporadas;
	}

	public void setTemporadas(List<String> temporadas) {
		this.temporadas = temporadas;
	}

	public String getTemporadaElegida() {
		return temporadaElegida;
	}

	public List<String> getPartidos() {
		return partidos;
	}

	public void setPartidos(List<String> partidos) {
		this.partidos = partidos;
	}

	public String getPartidoElegido() {
		return partidoElegido;
	}

	public void setPartidoElegido(String partidoElegido) {
		this.partidoElegido = partidoElegido;
	}

	public boolean isPartidosDisabled() {
		return partidosDisabled;
	}

	public void setPartidosDisabled(boolean partidosDisabled) {
		this.partidosDisabled = partidosDisabled;
	}

	public void setTemporadaElegida(String temporadaElegida) {
		this.temporadaElegida = temporadaElegida;
	}

	public String getNombreAlineacion() {
		return nombreAlineacion;
	}

	public void setNombreAlineacion(String nombreAlineacion) {
		this.nombreAlineacion = nombreAlineacion;
	}

	public Color[] getPosiblesColores() {
		return posiblesColores;
	}

	public void setPosiblesColores(Color[] posiblesColores) {
		this.posiblesColores = posiblesColores;
	}

	public Color getColorEquipaciones() {
		return colorEquipaciones;
	}

	public void setColorEquipaciones(Color colorEquipaciones) {
		this.colorEquipaciones = colorEquipaciones;
	}

	public List<String> getJugadoresPosibles() {
		return jugadoresPosibles;
	}

	public void setJugadoresPosibles(List<String> jugadoresPosibles) {
		this.jugadoresPosibles = jugadoresPosibles;
	}

	public List<String> getJugadoresElegidos() {
		return jugadoresElegidos;
	}

	public void setJugadoresElegidos(List<String> jugadoresElegidos) {
		this.jugadoresElegidos = jugadoresElegidos;
	}

	public boolean isJugadoresDisabled() {
		return jugadoresDisabled;
	}

	public void setJugadoresDisabled(boolean jugadoresDisabled) {
		this.jugadoresDisabled = jugadoresDisabled;
	}

	public boolean isButtonConfirmarDisabled() {
		return buttonConfirmarDisabled;
	}

	public void setButtonConfirmarDisabled(boolean buttonConfirmarDisabled) {
		this.buttonConfirmarDisabled = buttonConfirmarDisabled;
	}

	public List<Temporada> getListaTemporadas() {
		return listaTemporadas;
	}

	public void setListaTemporadas(List<Temporada> listaTemporadas) {
		this.listaTemporadas = listaTemporadas;
	}

	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
		
		Utils.comprobarRegistroYMostrarMensaje("Alineación registrada correctamente");
		
		partidosDisabled = true;
		buttonConfirmarDisabled = true;
		posiblesColores = Color.values();

		listaTemporadas = Controlador.getInstancia().getTodasLasTemporadas();
		temporadas = new LinkedList<>();
		for (Temporada t : listaTemporadas) {
			temporadas.add(t.getNombre());
		}
	}
	
	private Temporada buscarTemporada() {
		int i = 0;
		while (i < listaTemporadas.size()) {
			Temporada tmp = listaTemporadas.get(i);
			if (tmp.getNombre().equals(temporadaElegida)) {
				return tmp;
			}
			i++;
		}
		return null;
	}

	public void elegirTemporada() {
		partidosDisabled = false;
		
		temporadaElegidaT = buscarTemporada();
		
		listaPartidos = Controlador.getInstancia().getPartidosPendientesDeRegistrarAlineacion(temporadaElegidaT.getId());
		
		partidos = new LinkedList<>();
		for (Partido p : listaPartidos) {
			partidos.add(p.getFecha().toString());
		}
	}
	
	private Partido buscarPartido() {
		int i = 0;
		while (i < listaPartidos.size()) {
			Partido tmp = listaPartidos.get(i);
			if (tmp.getFecha().toString().equals(partidoElegido)) {
				return tmp;
			}
			i++;
		}
		return null;
	}

	public void elegirPartido() {
		jugadoresDisabled = false;
		
		partidoElegidoP = buscarPartido();
		
		jugadoresPosibles = Controlador.getInstancia().getJugadoresDisponiblesEnPartido(partidoElegidoP.getId());
	}

	public void elegirJugadores() {
		if (jugadoresElegidos == null || jugadoresElegidos.isEmpty() || colorEquipaciones == null) {
			buttonConfirmarDisabled = true;
			return;
		}
		buttonConfirmarDisabled = false;
	}

	public String registrarAlineacion() {
		Controlador.getInstancia().registrarAlineacion(nombreAlineacion, colorEquipaciones, partidoElegidoP.getId(), jugadoresElegidos);

		return Utils.resetView() + "&registrado=true";
	}

	public List<Partido> getListaPartidos() {
		return listaPartidos;
	}

	public void setListaPartidos(List<Partido> listaPartidos) {
		this.listaPartidos = listaPartidos;
	}

	public Temporada getTemporadaElegidaT() {
		return temporadaElegidaT;
	}

	public void setTemporadaElegidaT(Temporada temporadaElegidaT) {
		this.temporadaElegidaT = temporadaElegidaT;
	}

}
