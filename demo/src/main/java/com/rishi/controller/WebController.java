package com.rishi.controller;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.common.RequestStatus;
import com.rishi.common.Response;
import com.rishi.dao.EmployeeDao;
import com.rishi.dao.ManagerDao;
import com.rishi.domain.Employee;
import com.rishi.domain.Manager;

@RestController
@RequestMapping("/web")
public class WebController {

	@Autowired
	private ManagerDao managerDao;

	@Autowired
	private EmployeeDao employeeDao;

	@RequestMapping(value = "/getEmployeeData", method = RequestMethod.POST)
	public Response getEmployeeData(@RequestParam(value = "managerId", defaultValue = "") String managerId) {
		Response response = new Response();
		try {
			Manager manager = managerDao.findByEmail(managerId);
			List<Employee> empList = employeeDao.findByManager(manager);
			response.setResponse(empList);
			response.setStatus(RequestStatus.SUCCESS);
		} catch (Exception e) {
			response.setStatus(RequestStatus.FAILURE);
			response.setMessage("Server Error.");
			return response;
		}
		return response;
	}

	@RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
	public Response deleteEmployee(@RequestParam(value = "id", defaultValue = "") long id) {
		Response response = new Response();
		try {
			Employee employee = employeeDao.findOne(id);
			if (employee == null) {
				response.setStatus(RequestStatus.FAILURE);
				response.setMessage("Wrong Information Provide.");
				return response;
			} else {
				employeeDao.delete(employee);
				response.setStatus(RequestStatus.SUCCESS);
				response.setMessage("Delete SuccessFully.");
			}
		} catch (Exception e) {
			response.setStatus(RequestStatus.FAILURE);
			response.setMessage("Server Error.");
			return response;
		}
		return response;
	}

	@RequestMapping(value = "/saveEmployeeData", method = RequestMethod.POST)
	public Response saveEmployeeData(@NotNull @RequestParam(value = "managerId", defaultValue = "-1") String managerId,
			@NotNull @RequestParam(value = "firstName", defaultValue = "") String firstName,
			@NotNull @RequestParam(value = "lastName", defaultValue = "") String lastName,
			@NotNull @RequestParam(value = "city", defaultValue = "") String city,
			@NotNull @RequestParam(value = "mobile", defaultValue = "") String mobile,
			@NotNull @RequestParam(value = "dob", defaultValue = "") long dob,
			@NotNull @RequestParam(value = "emailId", defaultValue = "") String emailId,
			@NotNull @RequestParam(value = "address", defaultValue = "") String address,
			@NotNull @RequestParam(value = "id", defaultValue = "-1") long id) {
		Response response = new Response();
		try {
			Manager manager = managerDao.findByEmail(managerId);
			if (manager == null) {
				response.setStatus(RequestStatus.FAILURE);
				response.setMessage("Please Use Valid Login Id.");
				return response;
			}
			Employee employee = employeeDao.findOne(id);
			if (employee == null) {
				employee = new Employee();
				Employee email = employeeDao.findByEmailId(emailId);
				if (email != null) {
					response.setStatus(RequestStatus.FAILURE);
					response.setMessage("This EmailId is Already Exists.");
					return response;
				}
				employee.setEmailId(emailId);
				employee.setManager(manager);
				employee.setFirstName(firstName);
				employee.setLastName(lastName);
				employee.setCity(city);
				employee.setMobile(mobile);
				employee.setAddress(address);
				if (dob != -1) {
					Date date = new Date(dob);
					employee.setDob(date);
				}
			} else {
				employee.setManager(manager);
				employee.setFirstName(firstName);
				employee.setLastName(lastName);
				employee.setCity(city);
				employee.setMobile(mobile);
				employee.setAddress(address);
				if (dob != -1) {
					Date date = new Date(dob);
					employee.setDob(date);
				}
			}
			employeeDao.save(employee);
			response.setStatus(RequestStatus.SUCCESS);
		} catch (Exception e) {
			response.setStatus(RequestStatus.FAILURE);
			response.setMessage("Server Error");
		}
		return response;
	}
}
