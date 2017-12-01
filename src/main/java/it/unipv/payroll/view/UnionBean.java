package it.unipv.payroll.view;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import it.unipv.payroll.controller.EmployeeController;
import it.unipv.payroll.controller.ServiceChargeController;
import it.unipv.payroll.controller.UnionController;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.Union;
import it.unipv.payroll.util.TimeManagUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "UnionBean")
@ViewScoped
public class UnionBean implements Serializable {

	@Inject	UnionController unioncontroller;
	@Inject	ServiceChargeController chargecontroller;
	@Inject	@Named	EmployeeController employeeController;

	private Union u;
	private List<ServiceCharge> charges;
	private List<Employee> employees;
	private float due = 0;
	private String timestamp = "";
	private float amount = 0;
	private String service = "";
	private String id;
	private UIComponent confirmButton;
	private String cf;

	@PostConstruct 
	public void init() {
		id = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
		u = unioncontroller.find(Integer.parseInt(id));
		charges = u.getServiceCharges();
		employees = u.getEmployee();
		due = u.getUniondue();
	}

	public String changedue() {
		if (due != 0) {
			u.setUniondue(due);
			unioncontroller.update(u);

		} else {
			message("Malformed input! please check and retry");
		}
		return null;
	}

	public String addCharge() {


		try {
			Employee e = employeeController.findCF(cf);
			if (e == null || e.getUnion().getId() != u.getId()) {
				message("That employee do not belong to your Union!");
			}

			if (!service.equals("") && amount > 0 && !timestamp.equals("") && TimeManagUtil.CheckFuture(timestamp)) {

				ServiceCharge newservicecharge = new ServiceCharge();
				newservicecharge.setChargeAmount(amount);
				newservicecharge.setTimeStamp(timestamp);
				newservicecharge.setService(service);
				newservicecharge.setEmployee(e);
				newservicecharge.setUnion(u);

				chargecontroller.add(newservicecharge, e, u);
				u = (Union) unioncontroller.find(Integer.parseInt(id));
				charges = u.getServiceCharges();
			} else {
				message("Malformed input! please check and retry");
			}
		} catch (ParseException e) {
			message("Timestamp is incorrect! please check it");
		} catch (NoResultException e2) {
			message("Emploeyy not found! please check the fiscal code");
		}
		return null;
	}

	public String getCf() {
		return cf;
	}

	public void settimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setamount(float amount) {
		this.amount = amount;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public float getAmount() {
		return amount;
	}

	public String getService() {
		return service;
	}

	public void setservice(String service) {
		this.service = service;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getid() {
		return String.valueOf(u.getId());
	}

	public String getName() {
		return u.getName();
	}

	public String getactualdue() {
		return String.valueOf(u.getUniondue());
	}

	public List<ServiceCharge> getCharges() {
		return charges;
	}

	public void setDue(float due) {
		this.due = due;
	}

	public float getDue() {
		return due;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
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

	public String Logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "Login?faces-redirect=true";
	}

}
