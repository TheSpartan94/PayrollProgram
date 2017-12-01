package it.unipv.payroll.DAO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.CommissionEmployee;

@Stateless
public class CommissionEmployeeDAO extends EmployeeDAO {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings(value = "unchecked")
	public List<CommissionEmployee> SelectivefindAll() {
		try {
			List<CommissionEmployee> ce = (List<CommissionEmployee>) em
					.createQuery("select p from " + CommissionEmployee.class.getName() + " p").getResultList();
			return ce;
		} catch (NoResultException e) {
			return null;
		}
	}

	// This Method makes sure the Employee exists before deleting it.

	public boolean delete(CommissionEmployee ce) {
		CommissionEmployee e = em.find(CommissionEmployee.class, ce.getId());
		if (e == null) {
			return false;
		} else {
			em.remove(em.contains(ce) ? ce : em.merge(ce));
			return true;
		}
	}
}
