package it.unipv.payroll.DAO;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.unipv.payroll.model.SalesReceipt;

@Stateless
public class SalesReceiptDAO {

	@PersistenceContext
	private EntityManager em;

	public void add(SalesReceipt sr) {
		em.persist(sr);
	}

	@SuppressWarnings(value = "unchecked")
	public List<SalesReceipt> findAll() {
		List<SalesReceipt> sr = (List<SalesReceipt>) em
				.createQuery("select p from " + SalesReceipt.class.getName() + " p").getResultList();
		return sr;
	}

	public SalesReceipt find(int id) {
		try {
			return em.find(SalesReceipt.class, id);
		} catch (NoResultException e) {
			return null;
		}
	}

	// This Method makes sure the Sales Receipt exists before deleting it.
	public boolean delete(SalesReceipt salereceipt) {
		SalesReceipt sr = em.find(SalesReceipt.class, salereceipt.getId());
		if (sr == null) {
			return false;
		} else {
			em.remove(em.contains(salereceipt) ? salereceipt : em.merge(salereceipt));
			return true;
		}
	}

}
