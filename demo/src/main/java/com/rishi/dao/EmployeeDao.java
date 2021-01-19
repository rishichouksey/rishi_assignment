package com.rishi.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.rishi.domain.Employee;
import com.rishi.domain.Manager;

public interface EmployeeDao extends CrudRepository<Employee, Long> {

	public List<Employee> findByManager(Manager manager);
	
	public Employee findByEmailId(String emailId);
}
