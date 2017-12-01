package it.unipv.payroll.DAO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.Union;

@Stateless
public class UnionDAO {

	@PersistenceContext
	private EntityManager em;

	public void add(Union union) {
		em.persist(union);
	}

	// This Method makes sure the Union exists before deleting it.

	public boolean delete(Union union) {
		Union x = em.find(Union.class, union.getId());
		if (x == null) {
			return false;
		} else {
			em.remove(em.contains(union) ? union : em.merge(union));
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Union> findall() {
		List<Union> u = (List<Union>) em.createQuery("select p from " + Union.class.getName() + " p").getResultList();
		return u;

	}

	public Union find(int id) {
		try {
			return (Union) em.createQuery("select p from " + Union.class.getName() + " p where id='" + id + "'")
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Union find(String name) {
		try {
			return (Union) em.createQuery("select p from " + Union.class.getName() + " p where name='" + name + "'")
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// Needed when an operation that changes some of the attributes of a union
	// is called. It forwards the modifications to the database.

	public void update(Union u) {
		em.merge(u);
	}

}
