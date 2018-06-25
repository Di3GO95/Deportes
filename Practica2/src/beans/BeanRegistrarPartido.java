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
import model.Temporada;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanRegistrarPartido")
@ViewScoped
public class BeanRegistrarPartido implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> listaTemporadas;
	private String temporadaElegida;
	private Date fecha;
	private boolean buttonConfirmarDisabled;
	
	private List<Temporada> temporadas;
	
	public List<String> getListaTemporadas() {
		return listaTemporadas;
	}

	public void setListaTemporadas(List<String> listaTemporadas) {
		this.listaTemporadas = listaTemporadas;
	}

	public String getTemporadaElegida() {
		return temporadaElegida;
	}

	public void setTemporadaElegida(String temporadaElegida) {
		this.temporadaElegida = temporadaElegida;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	
	public boolean isButtonConfirmarDisabled() {
		return buttonConfirmarDisabled;
	}

	public void setButtonConfirmarDisabled(boolean buttonConfirmarDisabled) {
		this.buttonConfirmarDisabled = buttonConfirmarDisabled;
	}
	
	public List<Temporada> getTemporadas() {
		return temporadas;
	}
	
	public void setTemporadas(List<Temporada> temporadas) {
		this.temporadas = temporadas;
	}

	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
		
		Utils.comprobarRegistroYMostrarMensaje("Partido registrado correctamente");
		
		listaTemporadas = new LinkedList<>();
		temporadas = Controlador.getInstancia().getTodasLasTemporadas();
		for (Temporada temporada : temporadas) {
			listaTemporadas.add(temporada.getNombre());
		}
		
		buttonConfirmarDisabled = true;
	}


	public void elegirTemporada() {
		if (temporadaElegida != null && fecha != null)
			buttonConfirmarDisabled = false;
		else
			buttonConfirmarDisabled = true;
	}

	public String registrarPartido() {
		int i = 0;
		boolean encontrado = false;
		Temporada t = null;
		while (i < temporadas.size() && !encontrado) {
			Temporada tmp = temporadas.get(i);
			if (tmp.getNombre().equals(temporadaElegida)) {
				t = tmp;
				encontrado = true;
			}
			i++;
		}
		if (encontrado) {
			Controlador.getInstancia().registrarPartido(t.getId(), fecha);
			
			return Utils.resetView() + "&registrado=true";
		}
		
		return Utils.resetView();
	}
	
}
