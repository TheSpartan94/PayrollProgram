package it.unipv.payroll.view;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import it.unipv.payroll.controller.UserController;

@ManagedBean(name = "LoginBean")
@ViewScoped

public class LoginBean implements Serializable {

	private static final long serialVersionUID = 19L;

	private String id, pwd;
	private UIComponent loginbutton;

	@Inject	UserController uc;

	@PostConstruct
	public void init() {
		id = "";
		pwd = "";
		
	}

	public String validate() throws NumberFormatException {
		if (Pattern.matches("[0-9]+", id)) {
			try {
				boolean result = uc.login(Integer.parseInt(id), pwd);
				if (result) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("id", id);
					String type = uc.getUserClass(Integer.parseInt(id));
					if (type.equals("Union")) {
						return "Union?faces-redirect=true";
					} else if (type.equals("TimeEmployee")) {
						return "TimeEmployee?faces-redirect=true";
					} else if (type.equals("FlatEmployee")) {
						return "FlatEmployee?faces-redirect=true";
					} else if (type.equals("CommissionEmployee")) {
						return "CommissionEmployee?faces-redirect=true";
					} else if (type.equals("Administrator")) {
						return "Administrator?faces-redirect=true";
					}

				} else {
					message("id or password not matching");
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} else {
			message("id is not valid");
			return null;
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public UIComponent getLoginbutton() {
		return loginbutton;
	}

	public void setLoginbutton(UIComponent loginbutton) {
		this.loginbutton = loginbutton;
	}

	public void message(String msg) {
		FacesMessage message = new FacesMessage(msg);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(loginbutton.getClientId(context), message);
	}

}
