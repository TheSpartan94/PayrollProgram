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

import it.unipv.payroll.controller.CommissionEmployeeController;
import it.unipv.payroll.controller.SalesReceiptController;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.PaymentMethod.PaymentType;
import it.unipv.payroll.model.SalesReceipt;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.Union;
import it.unipv.payroll.util.TimeManagUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "CommissionEmployeeBean")
@ViewScoped
public class CommissionEmployeeBean implements Serializable {

	@Inject	CommissionEmployeeController commissionemployeecontroller;
	@Inject	SalesReceiptController salesReceiptcontroller;

	private CommissionEmployee ce;
	private List<SalesReceipt> salesReceipts;
	private SalesReceipt salesReceipt;
	private List<ServiceCharge> servicecharges;

	private String id, Unioname, Timestamp = "";
	private float amount = 0;
	private double camount = 0;
	private double base = 0;
	private UIComponent confirmButton;

	@PostConstruct
	public void init() {
		id = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("id");
		ce = (CommissionEmployee) commissionemployeecontroller.find(Integer.parseInt(id));
		salesReceipts = ce.getSalesRecepit();
		Union U = ce.getUnion();
		this.base = ce.getCommissionBase();
		this.camount = ce.getCommissionAmount();
		servicecharges = ce.getServiceCharges();
		if (U == null)
			Unioname = "Not specified";
		else
			Unioname = U.getName();
	}

	public String getId() {
		return id;
	}

	public String getUnioname() {
		return Unioname;
	}

	public SalesReceipt getSalesReceipt() {
		return salesReceipt;
	}

	public void setSalesReceipt(SalesReceipt salesReceipt) {
		this.salesReceipt = salesReceipt;
	}

	public CommissionEmployee getCe() {
		return ce;
	}

	public List<SalesReceipt> getSalesReceipts() {
		return salesReceipts;
	}

	public String getfullname() {
		return ce.getFullName();
	}

	public List<ServiceCharge> getServicecharges() {
		return servicecharges;
	}

	public void setServicecharges(List<ServiceCharge> servicecharges) {
		this.servicecharges = servicecharges;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public float getAmount() {
		return amount;
	}

	public double getBase() {
		return base;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getCamount() {
		return  Math.round(camount*100.0)/100.0;
	}

	public void setCamount(double camount) {
		this.camount = camount;
	}

	public String getPaymentMethodname() {
		return ce.getPaymentmethod().getAddress();
	}

	public String getPaymentMethodType() {
		if (ce.getPaymentmethod().getType() == PaymentType.BankAccount)
			return "Bank Account";
		else if (ce.getPaymentmethod().getType() == PaymentType.Check)
			return "Check";
		return "Transfer";
	}

	public String AddSaleReceipt() throws Exception {
		try {
			if (amount > 0 && !Timestamp.equals("") && TimeManagUtil.CheckFuture(Timestamp)) {
				SalesReceipt receipt = new SalesReceipt();
				receipt.setSaleAmount(amount);
				receipt.setTimeStamp(Timestamp);
				receipt.setEmployee(ce);

				salesReceiptcontroller.add(receipt, ce);
				ce = (CommissionEmployee) commissionemployeecontroller.find(ce.getId());
				salesReceipts = ce.getSalesRecepit();

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

	public void setConfirmButton(UIComponent x) {
		confirmButton = x;
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
