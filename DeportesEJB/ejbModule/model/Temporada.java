package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Temporada implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4834964269927899605L;

	@Id
	@GeneratedValue
	private int id;
	
	private String nombre;
	private String lugarCelebracion;
	private int minParticipantesPorPartido;
	
	@OneToMany (mappedBy="temporada")
	private List<Partido> partidos;
	
	@ManyToMany (mappedBy="temporadas")
	private List<User> usuarios;
	
	public Temporada() {
		partidos = new LinkedList<Partido>();
		usuarios = new LinkedList<User>();
	}
	
	public Temporada(String nombre, String lugarCelebracion, int minParticipantesPorPartido) {
		this();
		this.nombre = nombre;
		this.lugarCelebracion = lugarCelebracion;
		this.minParticipantesPorPartido = minParticipantesPorPartido;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
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

	public List<Partido> getPartidos() {
		return partidos;
	}
	
	public void setPartidos(List<Partido> partidos) {
		this.partidos = partidos;
	}
	
	public List<User> getUsuarios() {
		return usuarios;
	}
	
	public void setUsuarios(List<User> usuarios) {
		this.usuarios = usuarios;
	}
}
