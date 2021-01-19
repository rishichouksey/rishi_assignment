package com.rishi.dao;

import org.springframework.data.repository.CrudRepository;

import com.rishi.domain.Manager;

public interface ManagerDao extends CrudRepository<Manager, Long> {

	public Manager findByEmailAndPassword(String email,String password);
	
	public Manager findByEmail(String email);
}
