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

import it.unipv.payroll.controller.TimeCardController;
import it.unipv.payroll.controller.TimeEmployeeController;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.TimeCard;
import it.unipv.payroll.model.TimeEmployee;
import it.unipv.payroll.model.Union;
import it.unipv.payroll.util.TimeManagUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "TimeEmployeeBean")
@ViewScoped

public class TimeEmployeeBean implements Serializable {

	@Inject	TimeEmployeeController timeemployeecontroller;
	@Inject	TimeCardController timecardcontroller;

	private TimeEmployee te;
	private List<TimeCard> timecards;
	private List<ServiceCharge> servicecharges;

	private String id;
	private String Unioname = "";
	private int minuteworked = 0;
	private String timestamp = "";
	private UIComponent confirmButton;

	@PostConstruct
	public void init() {
		id = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
		te = (TimeEmployee) timeemployeecontroller.find(Integer.parseInt(id));
		timecards = te.getTimescards();
		hourrate = te.getHourRate();
		Union U = te.getUnion();
		servicecharges = te.getServiceCharges();
		if (U == null)
			Unioname = "Not specified";
		else
			Unioname = U.getName();
	}

	public TimeEmployee getTe() {
		return te;
	}

	public String getPaymentMethodname() {
		return te.getPaymentmethod().getAddress();
	}

	public String getPaymentMethodType() {
		if (te.getPaymentmethod().getType() == PaymentType.BankAccount)
			return "Bank Account";
		else if (te.getPaymentmethod().getType() == PaymentType.Check)
			return "Check";
		return "Transfer";
	}

	public List<ServiceCharge> getServicecharges() {
		return servicecharges;
	}

	public void setServicecharges(List<ServiceCharge> servicecharges) {
		this.servicecharges = servicecharges;
	}

	public String getid() {
		return String.valueOf(te.getId());
	}

	public String getfullname() {
		return te.getFullName();
	}

	public float getHourrate() {
		return hourrate;
	}

	public void setHourrate(float hourrate) {
		this.hourrate = hourrate;
	}

	private float hourrate;

	public List<TimeCard> getTimecards() {
		return timecards;
	}

	public void setMinuteworked(int minuteworked) {
		this.minuteworked = minuteworked;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getMinuteworked() {
		return minuteworked;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getUnioname() {
		return Unioname;
	}

	public String AddTimeCard() {
		try {
			if (minuteworked > 0 && minuteworked < 960 && !timestamp.equals("")
					&& TimeManagUtil.CheckFuture(timestamp)) {
				TimeCard timecard = new TimeCard();
				timecard.setMinuteWorked(minuteworked);
				timecard.setTimeStamp(timestamp);
				timecardcontroller.add(timecard, te);
				te = (TimeEmployee) timeemployeecontroller.find(te.getId());
				timecards = te.getTimescards();

			} else {
				message("Malformed input! please check and retry");
			}
		}

		catch (ParseException e) {
			message("Timestamp is incorrect! please check it");
		} catch (Exception e) {
			message(e.getMessage());
		}

		return null;
	}

	public void message(String msg) {
		FacesMessage message = new FacesMessage(msg);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(confirmButton.getClientId(context), message);
	}

	public UIComponent getConfirmButton() {
		return confirmButton;
	}

	public void setConfirmButton(UIComponent confirmButton) {
		this.confirmButton = confirmButton;
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
