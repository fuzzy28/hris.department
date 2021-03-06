package org.jrue.ems.department.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.jrue.ems.department.domain.Department;
import org.jrue.ems.department.exception.DepartmentIdNotConsistentException;
import org.jrue.ems.department.exception.DepartmentNotFoundException;
import org.jrue.ems.department.exception.DepartmentNotPersistedException;
import org.jrue.ems.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The DepartmentController class is a RESTful web service controller. The
 * <code>@RestController</code> annotation informs Spring that each
 * <code>@RequestMapping</code> method returns a <code>@ResponseBody</code>
 * which, by default, contains a ResponseEntity converted into JSON with an
 * associated HTTP status code.
 * 
 * @author Joel F. Ruelos Jr.
 * @since 1.0
 */

@RequestMapping("/departments")
@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<Department>> getAllDepartments() {
	return new ResponseEntity<Collection<Department>>(
		departmentService.findAll(),
		HttpStatus.OK);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Department> getDepartmentById(
	    @PathVariable("id") Long departmentId) {

	Department department = departmentService.findOne(departmentId);
	ResponseEntity<Department> response = null;
	if (department == null) {
	    throw new DepartmentNotFoundException();
	} else {
	    response = new ResponseEntity<Department>(department, HttpStatus.OK);
	}
	return response;
    }

    @RequestMapping(
	    method = RequestMethod.POST,
	    consumes = MediaType.APPLICATION_JSON_VALUE,
	    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> saveDepartment(
	    @Valid @RequestBody Department persist) {

	if (persist == null || persist.getId() != null) {
	    throw new DepartmentNotPersistedException();
	}

	Department department = departmentService.save(persist);
	HttpHeaders headers = new HttpHeaders();
	headers.add("location", String.format("/departments/%d", department.getId()));
	return new ResponseEntity<Department>(department, headers, HttpStatus.CREATED);
    }

    @RequestMapping(
	    path = "/{id}",
	    method = RequestMethod.PUT,
	    consumes = MediaType.APPLICATION_JSON_VALUE,
	    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> updateDepartment(
	    @Valid @RequestBody Department persist,
	    @PathVariable("id") Long departmentId) {

	if (persist.getId() != null && persist.getId() != departmentId) {
	    throw new DepartmentIdNotConsistentException();
	}

	ResponseEntity<Department> response = null;
	Department department = departmentService.update(persist);
	if (department == null) {
	    throw new DepartmentNotFoundException();
	} else {
	    response = new ResponseEntity<Department>(department, HttpStatus.OK);
	}

	return response;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Department> deleteDepartmentById(
	    @PathVariable("id") Long departmentId) {

	ResponseEntity<Department> response = null;
	if (departmentService.findOne(departmentId) == null) {
	    throw new DepartmentNotFoundException();
	} else {
	    departmentService.delete(departmentId);
	    response = new ResponseEntity<Department>(HttpStatus.NO_CONTENT);
	}

	return response;
    }
}