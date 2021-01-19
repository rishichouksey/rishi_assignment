package com.rishi.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rishi.dao.ManagerDao;
import com.rishi.domain.Manager;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private ManagerDao managerDao;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Manager manager=managerDao.findByEmail(email);
		if(manager==null) {
			throw new UsernameNotFoundException("User not found with username: " + email);
		}
		if (manager.getEmail().equals(email)) {
			return new User("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + email);
		}
	}

}