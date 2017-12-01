package it.unipv.payroll.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import it.unipv.payroll.controller.FlatEmployeeController;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@ManagedBean(name = "FlatEmployeeBean")
@ViewScoped
public class FlatEmployeeBean implements Serializable {

	@Inject	FlatEmployeeController flatemployeecontroller;

	private String id, Unioname;
	private FlatEmployee fe;
	private List<ServiceCharge> servicecharges;
	private int salary;

	@PostConstruct
	public void init() {
		id = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
		fe = (FlatEmployee) flatemployeecontroller.find(Integer.parseInt(id));
		servicecharges = fe.getServiceCharges();
		Union U = fe.getUnion();
		salary = fe.getSalary();
		if (U == null)
			Unioname = "Not specified";
		else
			Unioname = U.getName();
	}

	public String getId() {
		return id;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnioname() {
		return Unioname;
	}

	public void setUnioname(String unioname) {
		Unioname = unioname;
	}

	public List<ServiceCharge> getServicecharges() {
		return servicecharges;
	}

	public void setServicecharges(List<ServiceCharge> servicecharges) {
		this.servicecharges = servicecharges;
	}

	public String getfullname() {
		return fe.getFullName();
	}

	public String getPaymentMethodname() {
		return fe.getPaymentmethod().getAddress();
	}

	public String getPaymentMethodType() {
		if (fe.getPaymentmethod().getType() == PaymentType.BankAccount)
			return "Bank Account";
		else if (fe.getPaymentmethod().getType() == PaymentType.Check)
			return "Check";
		return "Transfer";
	}

	public String Logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "Login?faces-redirect=true";
	}

	public String ChangePM() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("return_url",
				FacesContext.getCurrentInstance().getViewRoot().getViewId());
		return "ChangePM?faces-redirect=true";
	}
}
