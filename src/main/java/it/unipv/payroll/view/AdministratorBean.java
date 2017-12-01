package it.unipv.payroll.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.unipv.payroll.controller.AdministratorController;
import it.unipv.payroll.controller.CommissionEmployeeController;
import it.unipv.payroll.controller.EmployeeController;
import it.unipv.payroll.controller.FlatEmployeeController;
import it.unipv.payroll.controller.TimeEmployeeController;
import it.unipv.payroll.controller.UnionController;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@ManagedBean(name = "AdministratorBean")
@ViewScoped
public class AdministratorBean implements Serializable {

	@Inject	UnionController unioncontroller;
	@Inject	@Named	EmployeeController employeeController;
	@Inject	FlatEmployeeController fc;
	@Inject	CommissionEmployeeController cc;
	@Inject	TimeEmployeeController tc;
	@Inject	AdministratorController admincontroller;

	private List<Union> unions;
	private List<Employee> employees;
	private UIComponent confirmButton;

	private String id;

	@PostConstruct
	public void init() {
		id = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
		unions = unioncontroller.findall();
		employees = employeeController.findAll();

	}

	public void DeleteUnion(Union u) {
		unioncontroller.delete(u);
		unions = unioncontroller.findall();
		employees = employeeController.findAll();
	}

	public String ModifyEmployee(Employee e) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("employeeid", e.getId());
		return "ModifyEmployee?faces-redirect=true";
	}

	public void DeleteEmployee(Employee e) {
		try {
			admincontroller.deleteEmployee(e);
			employees = employeeController.findAll();
		} catch (Exception exception) {
			message(exception.getMessage());
		}

	}

	public String UnionName(Employee e) {
		if (e.getUnion() == null)
			return "not set";
		else
			return e.getUnion().getName();

	}

	public List<Union> getUnions() {
		return unions;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public String getId() {
		return id;
	}

	public String getType(Employee e) {
		String type = e.getClass().getSimpleName().toLowerCase().split("employee")[0];
		return type +" employee";
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

	public String AddEmployee() {
		return "AddEmployee?faces-redirect=true";
	}

	public String RunPayroll() {
		return "PayrollResult?faces-redirect=true";
	}

	public String AddUnion() {
		return "AddUnion?faces-redirect=true";
	}
}
