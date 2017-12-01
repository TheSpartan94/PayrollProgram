package it.unipv.payroll.model;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
@Table(name = "union_tb")
@Inheritance(strategy = InheritanceType.JOINED)

/*
 * Union class, based on the user class. Defines two OneToMany relationship,
 * since it has to store the informations about the employees that are
 * subscribed to it and all the Service Charges assessed (the recipient is a
 * field in the Service Charge class).
 */

public class Union extends User {

	@Column(unique = true)
	String Name;
	float uniondue = 0;

	@OneToMany(targetEntity = ServiceCharge.class, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "union")
	List<ServiceCharge> ServiceCharges;
	@OneToMany(targetEntity = Employee.class, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "union")
	List<Employee> Employee;

	
	// getters and setters

	public List<Employee> getEmployee() {
		return Employee;
	}

	public void removeServiceCharge(ServiceCharge sc) {
		ServiceCharges.remove(sc);
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public float getUniondue() {
		return uniondue;
	}

	public void setUniondue(float uniondue) {
		this.uniondue = uniondue;
	}

	public List<ServiceCharge> getServiceCharges() {
		return ServiceCharges;
	}

	public void addServiceCharge(ServiceCharge sc) {
		ServiceCharges.add(sc);
	}

	public Union(String name, String password, float uniondue) throws NoSuchAlgorithmException {
		super(password);
		ServiceCharges = new ArrayList<>();
		Employee = new ArrayList<>();
		this.Name = name;
		this.uniondue = uniondue;
	}

	public Union() {
	}

}
