package it.unipv.payroll.DAO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.Administrator;

@Stateless
public class AdministratorDAO {

	@PersistenceContext
	private EntityManager em;

	public void add(Administrator a) {
		em.persist(a);
	}

	// This Method makes sure the Administrator exists before deleting it.
	public boolean delete(Administrator a) {
		Administrator x = em.find(Administrator.class, a.getId());
		if (x == null) {
			return false;
		} else {
			em.remove(em.contains(a) ? a : em.merge(a));
			return true;
		}
	}

	public List<Administrator> findAll() {
		List<Administrator> a = (List<Administrator>) em
				.createQuery("select p from " + Administrator.class.getName() + " p", Administrator.class)
				.getResultList();
		return a;
	}
}
