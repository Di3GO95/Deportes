package beans;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

@SuppressWarnings("deprecation")
@ManagedBean(name = "beanLocale")
@SessionScoped
public class BeanLocale implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Locale locale;

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@PostConstruct
	public void init() {
		locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
	}

	public Locale getLocale() {
		return locale;
	}

	public String getLanguage() {
		return locale.getLanguage();
	}

	public void setLanguage(String language) {
		locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}
}
