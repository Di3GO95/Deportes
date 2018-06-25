package beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import controlador.Controlador;
import funciones.Utils;
import model.Partido;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanPartidosPendientesCelebracion")
@RequestScoped
public class BeanPartidosPendientesCelebracion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Partido> listaPartidos;

	public List<Partido> getListaPartidos() {
		return listaPartidos;
	}

	public void setListaPartidos(List<Partido> listaPartidos) {
		this.listaPartidos = listaPartidos;
	}

	@PostConstruct
	public void init() {
		String usuarioActual = Utils.getUsuarioActual();
		if (usuarioActual == null) {
			Utils.redirigirALogin();
		}else {
			listaPartidos = Controlador.getInstancia().getPartidosPendientesDeCelebrar(usuarioActual);
		}
	}

}
