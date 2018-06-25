package beans;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import controlador.Controlador;
import funciones.Utils;
import model.Alineacion;
import model.Partido;
import model.Temporada;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanGestionarResultados")
@ViewScoped
public class BeanGestionarResultados implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> temporadas;
	private String temporadaElegida;

	private List<String> partidos;
	private String partidoElegido;
	private boolean partidosDisabled;

	private List<Alineacion> alineaciones;

	private boolean buttonConfirmarDisabled;
	
	private List<Temporada> listaTemporadas;
	private Temporada temporadaElegidaT;
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

	public void setTemporadaElegida(String temporadaElegida) {
		this.temporadaElegida = temporadaElegida;
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

	public List<Alineacion> getAlineaciones() {
		return alineaciones;
	}

	public void setAlineaciones(List<Alineacion> alineaciones) {
		this.alineaciones = alineaciones;
	}

	public boolean isButtonConfirmarDisabled() {
		return buttonConfirmarDisabled;
	}

	public void setButtonConfirmarDisabled(boolean buttonConfirmarDisabled) {
		this.buttonConfirmarDisabled = buttonConfirmarDisabled;
	}

	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
		
		Utils.comprobarRegistroYMostrarMensaje("Resultados modificados correctamente");
		
		partidosDisabled = true;
		buttonConfirmarDisabled = true;

		listaTemporadas = Controlador.getInstancia().getTodasLasTemporadas();
		
		temporadas = new LinkedList<>();
		for (Temporada temporada : listaTemporadas) {
			temporadas.add(temporada.getNombre());
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
		buttonConfirmarDisabled = true;
		
		temporadaElegidaT = buscarTemporada();
		listaPartidos = Controlador.getInstancia().getPartidosDeTemporada(temporadaElegidaT.getId());

		partidos = new LinkedList<>();
		for (Partido p : listaPartidos) {
			if (p.getFecha().before(new Date()))
				partidos.add(p.getFecha().toString());
			else
				System.out.println("es despues: " + p.getId());
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
		partidoElegidoP = buscarPartido();
		
		alineaciones = Controlador.getInstancia().getAlineacionesFromPartido(partidoElegidoP.getId());
		
		buttonConfirmarDisabled = false;
	}

	public String registrarResultados() {
		for (Alineacion a : alineaciones) {
			Controlador.getInstancia().introducirResultado(a.getId(), a.getTanteo());
		}
		
		return Utils.resetView() + "&registrado=true";
	}

}
