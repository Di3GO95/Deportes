package beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import controlador.Controlador;
import funciones.Utils;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanTemporada")
@ViewScoped
public class BeanTemporada implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String lugarCelebracion;
	private int minParticipantesPorPartido;
	private boolean hayParticipantes;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getLugarCelebracion() {
		return lugarCelebracion;
	}
	public void setLugarCelebracion(String lugarCelebracion) {
		this.lugarCelebracion = lugarCelebracion;
	}
	public int getMinParticipantesPorPartido() {
		return minParticipantesPorPartido;
	}
	public void setMinParticipantesPorPartido(int minParticipantesPorPartido) {
		this.minParticipantesPorPartido = minParticipantesPorPartido;
	}
	public boolean isHayParticipantes() {
		return hayParticipantes;
	}
	public void setHayParticipantes(boolean hayParticipantes) {
		this.hayParticipantes = hayParticipantes;
	}
	
	@PostConstruct
	public void init() {
		if (Utils.getUsuarioActual() == null)
			Utils.redirigirALogin();
		
		setMinParticipantesPorPartido(2);
		
		Utils.comprobarRegistroYMostrarMensaje("Temporada registrada correctamente");
	}
	
	public String crearTemporada() {
		String destino = "";
		
		Controlador.getInstancia().registrarTemporada(nombre, lugarCelebracion, minParticipantesPorPartido);
		if (hayParticipantes) {
			destino = "gestionUsuariosEnTemporadas";
		}else {
			destino = Utils.resetView() + "&registrado=true";
		}
		
		return destino;
	}
	
	
}
