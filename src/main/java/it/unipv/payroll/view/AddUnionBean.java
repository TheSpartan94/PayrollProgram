package it.unipv.payroll.view;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import it.unipv.payroll.controller.AdministratorController;
import it.unipv.payroll.controller.UnionController;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@ManagedBean(name = "AddUnionBean")
@ViewScoped

public class AddUnionBean implements Serializable {

	@Inject	UnionController unioncontroller;
	@Inject	AdministratorController admincontroller;

	private String unionName, password;
	private float unionDue;
	private UIComponent confirmButton;

	public String AddUnion() {

		if (!password.equals("") && !unionName.equals("") && unionDue > 0) {
			try {
				Union u = new Union(unionName, password, unionDue);
				admincontroller.addUnion(u);
				return "Administrator?faces-redirect=true";
			}

			catch (NoSuchAlgorithmException e) {
				message("Ops...we some trouble with the pass " + e.getMessage());
			}

			catch (Exception e) {
				message(e.getMessage());
			}
		} else {
			message("Malformed input! please check and retry");
		}
		return null;
	}

	public String getUnionName() {
		return unionName;
	}

	public void setUnionName(String unionName) {
		this.unionName = unionName;
	}

	public float getUnionDue() {
		return unionDue;
	}

	public void setUnionDue(float unionDue) {
		this.unionDue = unionDue;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void message(String msg) {
		FacesMessage message = new FacesMessage(msg);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(confirmButton.getClientId(context), message);
	}

	public UIComponent getConfirmButton() {
		return confirmButton;
	}

	public void setConfirmButton(UIComponent x) {
		confirmButton = x;
	}

}
