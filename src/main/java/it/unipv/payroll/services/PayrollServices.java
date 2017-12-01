package it.unipv.payroll.services;

import javax.inject.Inject;
import javax.jws.WebService;

import it.unipv.payroll.controller.AdministratorController;
import it.unipv.payroll.controller.CommissionEmployeeController;
import it.unipv.payroll.controller.FlatEmployeeController;
import it.unipv.payroll.controller.PaymentMethodController;
import it.unipv.payroll.controller.SalesReceiptController;
import it.unipv.payroll.controller.ServiceChargeController;
import it.unipv.payroll.controller.TimeCardController;
import it.unipv.payroll.controller.TimeEmployeeController;
import it.unipv.payroll.controller.UnionController;
import it.unipv.payroll.model.CommissionEmployee;
import it.unipv.payroll.model.FlatEmployee;
import it.unipv.payroll.model.PaymentMethod;
import it.unipv.payroll.model.SalesReceipt;
import it.unipv.payroll.model.ServiceCharge;
import it.unipv.payroll.model.TimeCard;
import it.unipv.payroll.model.TimeEmployee;
import it.unipv.payroll.model.Union;

@WebService
public class PayrollServices {

	@Inject	AdministratorController AdminController;
	@Inject	TimeCardController CardController;
	@Inject	TimeEmployeeController TimeController;
	@Inject	FlatEmployeeController FlatController;
	@Inject	ServiceChargeController ServiceController;
	@Inject	UnionController UnionController;
	@Inject	PaymentMethodController Paycontroller;
	@Inject	CommissionEmployeeController commissionController;
	@Inject	SalesReceiptController SalesController;

	public void AddUnion(Union U) throws Exception {
		UnionController.add(U);
	}

	public void AddFlatemployee(FlatEmployee fe, int union_id) throws Exception {
		AdminController.addFlatEmployee(fe);
		Union u = null;
		if (union_id > 0)
			u = UnionController.find(union_id);
		AdminController.AssignUnion(fe, u);
	}

	public void AddCommissionEmloyee(CommissionEmployee ce, int union_id) throws Exception {
		AdminController.addCommissionEmployee(ce);
		Union u = null;
		if (union_id > 0)
			u = UnionController.find(union_id);
		AdminController.AssignUnion(ce, u);
	}

	public void AddTimeEmployee(TimeEmployee te, int union_id) throws Exception {
		AdminController.addTimeEmployee(te);
		Union u = null;
		if (union_id > 0)
			u = UnionController.find(union_id);
		AdminController.AssignUnion(te, u);
	}

	public void AddTimeCard(TimeCard tc, int te_id) throws Exception {

		TimeEmployee te = (TimeEmployee) TimeController.find(te_id);
		if (te != null)
			CardController.add(tc, te);
	}

	public void AddSaleReceipt(SalesReceipt sr, int ce_id) throws Exception {
		CommissionEmployee ce = (CommissionEmployee) commissionController.find(ce_id);
		if (ce != null)
			SalesController.add(sr, ce);
	}

	public void AddServiceCharge(int flatemployee_id, int union_id, ServiceCharge charge1) throws Exception {
		Union u = UnionController.find(union_id);
		if (u != null) {
			FlatEmployee fe = (FlatEmployee) FlatController.find(flatemployee_id);
			ServiceController.add(charge1, fe, u);
		}

	}

	public void ModifyPayment(PaymentMethod pm, int employee_id) throws Exception {
		FlatEmployee fe = (FlatEmployee) FlatController.find(employee_id);
		if (fe != null) {
			fe.getPaymentmethod().setAddress(pm.getAddress());
			fe.getPaymentmethod().setType(pm.getType());
			Paycontroller.update(fe.getPaymentmethod());
		}
	}

}
