package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Alineacion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private int id;
	
	private String nombre;
	private int tanteo;
	@Enumerated(EnumType.STRING)
	private Color color;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "JPA_TABLA_NOTAS")
	private List<String> notas;
	
	@ManyToOne
	private Partido partido;
	
	
	@ManyToMany(mappedBy="alineaciones")
	private List<User> usuarios;
	
	public Alineacion() {
		notas = new LinkedList<String>();
		usuarios = new LinkedList<User>();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getTanteo() {
		return tanteo;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setTanteo(int tanteo) {
		this.tanteo = tanteo;
	}

	public List<String> getNotas() {
		return notas;
	}
	public void setNotas(List<String> notas) {
		this.notas = notas;
	}
	
	public List<User> getUsuarios() {
		return usuarios;
	}
	
	public void setUsuarios(List<User> usuarios) {
		this.usuarios = usuarios;
	}
	
	public Partido getPartido() {
		return partido;
	}
	
	public void setPartido(Partido partido) {
		this.partido = partido;
	}
	
	public List<String> getJugadores(){
		if (usuarios == null)
			return null;
		
		List<String> jugadores = new LinkedList<>();
		for (User user : usuarios) {
			jugadores.add(user.getUsuario());
		}
		return jugadores;
	}
	
}
