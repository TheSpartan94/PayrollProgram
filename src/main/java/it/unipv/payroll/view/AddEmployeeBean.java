package it.unipv.payroll.view;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import it.unipv.payroll.controller.AdministratorController;
import it.unipv.payroll.controller.PaymentMethodController;
import it.unipv.payroll.controller.UnionController;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.PaymentMethod;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.TimeEmployee;
import it.unipv.payroll.model.Union;
import it.unipv.payroll.util.TimeManagUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "AddEmployeeBean")
@ViewScoped

public class AddEmployeeBean implements Serializable {

	@Inject	UnionController unioncontroller;
	@Inject	PaymentMethodController paymenthcontroller;
	@Inject	AdministratorController admincontroller;
	
	private String CF, Fullname, PayAddress, UnionName, Daytobepaid, Employee_selected_type, choosen_pm_type, password;
	private float te_hourrate, ce_base, ce_amount;
	private int fe_salary;
	private List<PaymentType> pm_types;
	private List<String> Union_names = new ArrayList<>();
	private boolean display_ce, display_te, display_fe;
	private UIComponent confirmButton;


	@PostConstruct
	public void init() throws ParseException {
		display_ce = display_te = false;
		display_fe = true;
		pm_types = Arrays.asList(PaymentType.values());
		Daytobepaid = TimeManagUtil.CheckHoliday(TimeManagUtil.getLastDayOfMonth(Calendar.getInstance()));
		Union_names.add("unset");
		for (Union u : unioncontroller.findall())
			Union_names.add(u.getName());

	}

	@SuppressWarnings("deprecation")
	public void Display() throws ParseException {

		if (Employee_selected_type.equals("Flat employee")) {
			display_te = display_ce = false;
			Daytobepaid = TimeManagUtil.CheckHoliday(TimeManagUtil.getLastDayOfMonth(Calendar.getInstance()));
			display_fe = true;
		} else if (Employee_selected_type.equals("Time employee")) {
			Daytobepaid = TimeManagUtil.NextFriday(Calendar.getInstance());
			display_fe = display_ce = false;
			display_te = true;
		} else if (Employee_selected_type.equals("Commission employee")) {
			Daytobepaid = TimeManagUtil.NextFriday(Calendar.getInstance());
			display_fe = display_te = false;
			display_ce = true;
		}

	}

	public String AddEmployee() {
		try {
			if (!password.equals("") && !CF.equals("") && !Fullname.equals("") && !PayAddress.equals("")
					&& !Daytobepaid.equals("") && !TimeManagUtil.CheckFuture(Daytobepaid)) {
				PaymentType x = null;
				if (choosen_pm_type.equals("Check")) {
					x = PaymentType.Check;
				} else if (choosen_pm_type.equals("Transfer")) {
					x = PaymentType.Transfer;
				} else if (choosen_pm_type.equals("BankAccount")) {
					x = PaymentType.BankAccount;
				}
				PaymentMethod pm = new PaymentMethod(PayAddress, x);

				Union u = null;
				if (!UnionName.equals("unset"))
					u = unioncontroller.find(UnionName);

				if (Employee_selected_type.equals("Flat employee")) {
					if (fe_salary > 0) {
						FlatEmployee newemployee = new FlatEmployee(password, CF, Fullname, fe_salary, pm, Daytobepaid);
						admincontroller.AssignUnion(newemployee, u);
						admincontroller.addFlatEmployee(newemployee);
						return "Administrator?faces-redirect=true";

					} else {
						message("Malformed input! please check and retry");
					}
				} else if (Employee_selected_type.equals("Time employee")) {
					if (te_hourrate > 0) {
						TimeEmployee newemployee = new TimeEmployee(password, CF, Fullname, te_hourrate, pm,
								Daytobepaid);
						admincontroller.AssignUnion(newemployee, u);
						admincontroller.addTimeEmployee(newemployee);
						return "Administrator?faces-redirect=true";
					} else {
						message("Malformed input! please check and retry");
					}
				} else if (Employee_selected_type.equals("Commission employee")) {
					if (ce_amount > 0 && ce_base > 0) {
						CommissionEmployee newemployee = new CommissionEmployee(password, CF, pm, Fullname, ce_amount,
								ce_base, Daytobepaid);
						admincontroller.AssignUnion(newemployee, u);
						admincontroller.addCommissionEmployee(newemployee);
						return "Administrator?faces-redirect=true";
					} else {
						message("Malformed input! please check and retry");
					}
				}
			} else {
				message("Malformed input! please check and retry");
			}
		} catch (ParseException e) {
			message("Timestamp is incorrect! please check it");
		} catch (NoSuchAlgorithmException e) {
			message("Ops...we some trouble with the pass " + e.getMessage());
		} catch (Exception e) {
			message(e.getMessage());
		}
		return null;
	}

	public String getCF() {
		return CF;
	}

	public void setCF(String cF) {
		CF = cF;
	}

	public String getFullname() {
		return Fullname;
	}

	public void setFullname(String fullname) {
		Fullname = fullname;
	}

	public String getPayAddress() {
		return PayAddress;
	}

	public void setPayAddress(String payAddress) {
		PayAddress = payAddress;
	}

	public String getEmployee_selected_type() {
		return Employee_selected_type;
	}

	public void setEmployee_selected_type(String employeetype) {
		Employee_selected_type = employeetype;
	}

	public String getUnionName() {
		return UnionName;
	}

	public void setUnionName(String unionName) {
		UnionName = unionName;
	}

	public String getDaytobepaid() {
		return Daytobepaid;
	}

	public void setDaytobepaid(String daytobepaid) {
		Daytobepaid = daytobepaid;
	}

	public int getFe_salary() {
		return fe_salary;
	}

	public void setFe_salary(int fe_salary) {
		this.fe_salary = fe_salary;
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

	public List<PaymentType> getPm_types() {
		return pm_types;
	}

	public void setPm_types(List<PaymentType> pm_types) {
		this.pm_types = pm_types;
	}

	public List<String> getUnion_names() {
		return Union_names;
	}

	public void setUnion_names(List<String> union_names) {
		Union_names = union_names;
	}

	public String getChoosen_pm_type() {
		return choosen_pm_type;
	}

	public void setChoosen_pm_type(String choosen_pm_type) {
		this.choosen_pm_type = choosen_pm_type;
	}

	public boolean isDisplay_fe() {
		return display_fe;
	}

	public void setDisplay_fe(boolean display_fe) {
		this.display_fe = display_fe;
	}

	public boolean isDisplay_ce() {
		return display_ce;
	}

	public void setDisplay_ce(boolean dsplay_ce) {
		this.display_ce = dsplay_ce;
	}

	public boolean isDisplay_te() {
		return display_te;
	}

	public void setDisplay_te(boolean display_te) {
		this.display_te = display_te;
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

	public UIComponent getconfirmButton() {
		return confirmButton;
	}

	public void setconfirmButton(UIComponent x) {
		confirmButton = x;
	}

}
