package model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
		@NamedQuery(
			name="findPartidoByFechas",
			query="SELECT p FROM Partido p WHERE p.fecha >= :f1 AND p.fecha <= :f2"),
		@NamedQuery(
			name="findPartidosByUsuario",
			query="SELECT p FROM Partido p JOIN p.asistentes u WHERE u.usuario = :usuario")
})
public class Partido implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	@OneToMany(mappedBy="partido")
	private List<Alineacion> alineaciones;
	
	@ManyToMany
	private List<User> asistentes;
	
	@ManyToOne
	private Temporada temporada;
	
	public Partido() {
		alineaciones = new LinkedList<Alineacion>();
		asistentes = new LinkedList<User>();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public List<Alineacion> getAlineaciones() {
		return alineaciones;
	}
	
	public void setAlineaciones(List<Alineacion> alineaciones) {
		this.alineaciones = alineaciones;
	}
	
	public List<User> getAsistentes() {
		return asistentes;
	}
	
	public void setAsistentes(List<User> asistentes) {
		this.asistentes = asistentes;
	}
	
	public Temporada getTemporada() {
		return temporada;
	}
	
	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}
	
}
