package it.unipv.payroll.DAO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.TimeEmployee;

@Stateless
public class TimeEmployeeDAO extends EmployeeDAO {

	@PersistenceContext
	private EntityManager em;

	public List<TimeEmployee> SelectivefindAll() {

		List<TimeEmployee> te = (List<TimeEmployee>) em
				.createQuery("from " + TimeEmployee.class.getSimpleName(), TimeEmployee.class).getResultList();
		return te;
	}

	// This Method makes sure the Employee exists before deleting it.
	public boolean delete(TimeEmployee te) {
		TimeEmployee x = em.find(TimeEmployee.class, te.getId());
		if (x == null) {
			return false;
		} else {
			em.remove(em.contains(te) ? te : em.merge(te));
			return true;
		}
	}

}