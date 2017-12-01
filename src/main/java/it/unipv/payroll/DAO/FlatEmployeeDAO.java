package it.unipv.payroll.DAO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.FlatEmployee;

@Stateless
public class FlatEmployeeDAO extends EmployeeDAO {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings(value = "unchecked")
	public List<FlatEmployee> SelectivefindAll() {

		// JPQL Query
		List<FlatEmployee> fe = em.createQuery("select p from " + FlatEmployee.class.getName() + " p").getResultList();
		return fe;
	}

	// This Method makes sure the Employee exists before deleting it.

	public boolean delete(FlatEmployee fe) {
		FlatEmployee e = em.find(FlatEmployee.class, fe.getId());
		if (e == null) {
			return false;
		} else {
			em.remove(em.contains(fe) ? fe : em.merge(fe));
			return true;
		}
	}

}