package it.unipv.payroll.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.unipv.payroll.controller.EmployeeController;
import it.unipv.payroll.controller.PaymentMethodController;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@ManagedBean(name = "ChangepmBean")
@ViewScoped
public class ChangepmBean implements Serializable {

	@Inject	@Named	EmployeeController employeeController;
	@Inject	PaymentMethodController paycontroller;

	private Employee e;
	private String Unioname;
	private String selectedtype;
	private String newaddress;
	private List<PaymentType> types;
	private String id;
	private UIComponent confirmButton;

	@PostConstruct
	public void init() {

		id = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
		types = Arrays.asList(PaymentType.values());
		e = employeeController.find(Integer.parseInt(id));
		Union U = e.getUnion();
		if (U == null)
			Unioname = "Not specified";
		else
			Unioname = U.getName();

	}

	public String change() {

		if (!newaddress.equals("")) {
			try {
				PaymentType x = null;
				if (selectedtype.equals("Check")) {
					x = PaymentType.Check;
				} else if (selectedtype.equals("Transfer")) {
					x = PaymentType.Transfer;
				} else if (selectedtype.equals("BankAccount")) {
					x = PaymentType.BankAccount;
				}

				e.getPaymentmethod().setAddress(newaddress);
				e.getPaymentmethod().setType(x);
				paycontroller.update(e.getPaymentmethod());
				return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.get("return_url");

			} catch (Exception e1) {
				message(e1.getMessage());
			}
		} else {
			message("Malformed address! please check and retry");
		}
		return null;
	}

	public List<String> getTypes() {
		List<String> tmp = new ArrayList<String>();
		for (PaymentType x : types) {
			tmp.add(x.toString());
		}
		return tmp;
	}

	public String getSelectedtype() {
		return selectedtype;
	}

	public void setSelectedtype(String selectedtype) {
		this.selectedtype = selectedtype;
	}

	public String getid() {
		return String.valueOf(e.getId());
	}

	public String getNewaddress() {
		return newaddress;
	}

	public String getfullname() {
		return e.getFullName();
	}

	public String getUnioname() {
		return Unioname;
	}

	public void setUnioname(String unioname) {
		Unioname = unioname;
	}

	public String getPaymentMethodname() {
		return e.getPaymentmethod().getAddress();
	}

	public String getPaymentMethodType() {
		if (e.getPaymentmethod().getType() == PaymentType.BankAccount)
			return "Bank Account";
		else if (e.getPaymentmethod().getType() == PaymentType.Check)
			return "Check";
		return "Transfer";
	}

	public void setNewaddress(String newaddress) {
		this.newaddress = newaddress;
	}

	public String getFullName() {
		return e.getFullName();

	}

	public String getPMType() {
		return e.getPaymentmethod().getType().toString();
	}

	public String getAddress() {
		return e.getPaymentmethod().getAddress();

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
