package it.unipv.payroll.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import it.unipv.payroll.controller.AdministratorController;

@ManagedBean(name = "PayrollResultBean")
@ViewScoped

public class PayrollResult {

	@Inject	AdministratorController Controller;

	private List<String> result;
	private UIComponent datagrid;

	@PostConstruct
	public void init() {
		try {
			result = Controller.RunPayroll();

		} catch (Exception e) {
		}
	}

	public String GoBack() {
		return "Administrator?faces-redirect=true";
	}

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}

	public UIComponent getdatagrid() {
		return datagrid;
	}

	public void setdatagrid(UIComponent datagrid) {
		this.datagrid = datagrid;
	}

	public void message(String msg) {
		FacesMessage message = new FacesMessage(msg);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(datagrid.getClientId(context), message);
	}

}
