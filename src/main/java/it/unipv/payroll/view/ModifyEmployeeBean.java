package it.unipv.payroll.view;

import java.io.Serializable;
import java.util.ArrayList;
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
import it.unipv.payroll.controller.EmployeeController;
import it.unipv.payroll.controller.UnionController;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.Employee;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.TimeEmployee;
import it.unipv.payroll.model.Union;

@SuppressWarnings("serial")
@ManagedBean(name = "ModifyEmployeeBean")
@ViewScoped
public class ModifyEmployeeBean implements Serializable {

	@Inject	UnionController unioncontroller;
	@Inject	@Named	EmployeeController employeeController;
	@Inject	AdministratorController admincontroller;

	private String unionName;
	private float te_hourrate, ce_base, ce_amount;
	private int fe_salary, employeeId;
	private List<String> Union_names = new ArrayList<>();
	private boolean display_ce, display_te, display_fe;
	private String EmployeeType;
	private UIComponent confirmButton;

	@PostConstruct
	public void init() {
		employeeId = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("employeeid");
		Employee e = employeeController.find(employeeId);
		EmployeeType = e.getClass().getSimpleName();

		if (EmployeeType.equals(FlatEmployee.class.getSimpleName())) {
			fe_salary=((FlatEmployee) e).getSalary();
			display_te = display_ce = false;
			display_fe = true;
		} else if (EmployeeType.equals(TimeEmployee.class.getSimpleName())) {
			te_hourrate=((TimeEmployee) e).getHourRate();
			display_fe = display_ce = false;
			display_te = true;
			
		} else if (EmployeeType.equals(CommissionEmployee.class.getSimpleName())) {
			ce_amount=(float) ((CommissionEmployee) e).getCommissionAmount();
			ce_base=(float) ((CommissionEmployee) e).getCommissionBase();

			display_fe = display_te = false;
			display_ce = true;
		}

		Union_names.add("unset");
		for (Union u : unioncontroller.findall())
			Union_names.add(u.getName());
	}

	public String Modify() throws Exception {

		Employee empl = employeeController.find(employeeId);
		Union u = null;
		if (!unionName.equals("unset")) {
			u = unioncontroller.find(unionName);
		}

		admincontroller.AssignUnion(empl, u);
		if (!unionName.equals("unset")) {
			unioncontroller.update(u);
		}
		employeeController.update(empl);

		if (EmployeeType.equals(FlatEmployee.class.getSimpleName())) {
			if (fe_salary > 0) {
				admincontroller.setSalary(employeeId, fe_salary);
				employeeController.update(empl);
				return "Administrator?faces-redirect=true";
			} else {
				message("Malformed input! please check and retry");
			}

		} else if (EmployeeType.equals(TimeEmployee.class.getSimpleName())) {
			if (te_hourrate > 0) {
				admincontroller.setHourRate(employeeId, te_hourrate);
				employeeController.update(empl);
				return "Administrator?faces-redirect=true";
			} else {
				message("Malformed input! please check and retry");
			}

		} else if (EmployeeType.equals(CommissionEmployee.class.getSimpleName())) {
			if (ce_amount > 0 && ce_base > 0) {
				admincontroller.setCommissionAmount(employeeId, ce_amount);
				admincontroller.setCommissionBase(employeeId, ce_base);
				employeeController.update(empl);
				return "Administrator?faces-redirect=true";
			} else {
				message("Malformed input! please check and retry");
			}
		}
		return null;
	}

	public String getUnionName() {
		return unionName;
	}

	public void setUnionName(String unioName) {
		this.unionName = unioName;
	}

	public float getTe_hourrate() {
		return te_hourrate;
	}

	public void setTe_hourrate(float te_hourrate) {
		this.te_hourrate = te_hourrate;
	}

	public float getCe_base() {
		return ce_base;
	}

	public void setCe_base(float ce_base) {
		this.ce_base = ce_base;
	}

	public float getCe_amount() {
		return ce_amount;
	}

	public void setCe_amount(float ce_amount) {
		this.ce_amount = ce_amount;
	}

	public int getFe_salary() {
		return fe_salary;
	}

	public void setFe_salary(int fe_salary) {
		this.fe_salary = fe_salary;
	}

	public List<String> getUnion_names() {
		return Union_names;
	}

	public void setUnion_names(List<String> union_names) {
		Union_names = union_names;
	}

	public boolean isDisplay_ce() {
		return display_ce;
	}

	public void setDisplay_ce(boolean display_ce) {
		this.display_ce = display_ce;
	}

	public boolean isDisplay_te() {
		return display_te;
	}

	public void setDisplay_te(boolean display_te) {
		this.display_te = display_te;
	}

	public boolean isdisplay_fe() {
		return display_fe;
	}

	public void setdisplay_fe(boolean display_fe) {
		this.display_fe = display_fe;
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
