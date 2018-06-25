package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String usuario;
	private String clave;
	private String mail;
	private String telefono;
	private byte[] fotoPerfil;
	
	private boolean administrador;
	
	private boolean validado;
	
	@ManyToMany
	private List<Alineacion> alineaciones;
	
	@ManyToMany (mappedBy="asistentes")
	private List<Partido> partidos;
	
	@ManyToMany
	private List<Temporada> temporadas;
	
	public User() {
		alineaciones = new LinkedList<Alineacion>();
		partidos = new LinkedList<Partido>();
		
		temporadas = new LinkedList<Temporada>();
		
		fotoPerfil = null;
		
		administrador = false;
		
		validado = false;
	}
	
	public User(String usuario, String clave, String mail, String telefono) {
		this();
		
		this.usuario = usuario;
		this.clave = clave;
		this.mail = mail;
		this.telefono = telefono;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public List<Alineacion> getAlineaciones() {
		return alineaciones;
	}
	
	public void setAlineaciones(List<Alineacion> alineaciones) {
		this.alineaciones = alineaciones;
	}
	
	public byte[] getFotoPerfil() {
		return fotoPerfil;
	}
	
	public void setFotoPerfil(byte[] fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}
	
	public List<Partido> getPartidos() {
		return partidos;
	}
	
	public void setPartidos(List<Partido> partidos) {
		this.partidos = partidos;
	}
	
	public List<Temporada> getTemporadas() {
		return temporadas;
	}
	
	public void setTemporadas(List<Temporada> temporadas) {
		this.temporadas = temporadas;
	}
	
	public void setValidado(boolean validado) {
		this.validado = validado;
	}
	
	public boolean isValidado() {
		return validado;
	}
	
}
