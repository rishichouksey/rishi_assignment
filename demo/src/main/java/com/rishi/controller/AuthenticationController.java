package com.rishi.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.common.RequestStatus;
import com.rishi.common.Response;
import com.rishi.config.JwtTokenUtil;
import com.rishi.dao.ManagerDao;
import com.rishi.domain.Manager;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private ManagerDao managerDao;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public Response authenticate(@NotNull @RequestParam(value = "emailId", defaultValue = "") String emailId,
			@NotNull @RequestParam(value = "password", defaultValue = "") String password) {
		Response response = new Response();
		try {
			Manager manager = managerDao.findByEmailAndPassword(emailId, password);
			if (manager == null) {
				response.setStatus(RequestStatus.FAILURE);
				response.setMessage("Email or Password wrong");
				return response;
			} else {
				response.setStatus(RequestStatus.SUCCESS);
				response.setResponse(manager.getFirstName());
				final String token = jwtTokenUtil.generateToken(manager);
				response.setToken(token);
			}
		} catch (Exception e) {
			response.setStatus(RequestStatus.FAILURE);
			response.setMessage("Server Error ");
		}
		return response;
	}

	@RequestMapping(value = "/registerManager", method = RequestMethod.POST)
	public Response registerManager(@RequestParam(value = "emailId", defaultValue = "-1") String emailId,
			@RequestParam(value = "address", defaultValue = "-1") String address,
			@RequestParam(value = "lastName", defaultValue = "-1") String lastName,
			@RequestParam(value = "firstName", defaultValue = "-1") String firstName,
			@RequestParam(value = "password", defaultValue = "-1") String password) {
		Response response = new Response();
		try {
			Manager manager = managerDao.findByEmail(emailId);
			if (manager != null) {
				response.setStatus(RequestStatus.FAILURE);
				response.setMessage("This EmailId is Already Exists.");
				return response;
			}
			manager = new Manager();
			manager.setFirstName(firstName);
			manager.setLastName(lastName);
			manager.setEmail(emailId);
			manager.setAddress(address);
			manager.setPassword(password);
			managerDao.save(manager);
			response.setStatus(RequestStatus.SUCCESS);
			response.setMessage(firstName + " Created SuccessFully.");
		} catch (Exception e) {
			response.setStatus(RequestStatus.FAILURE);
			response.setMessage("Server Error");
		}
		return response;
	}
}
