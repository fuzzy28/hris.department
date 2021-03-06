package org.jrue.ems.department.service;

import java.util.Collection;

import org.jrue.ems.department.domain.Department;

/**
 * Department Service Interface
 * 
 * @author Joel F. Ruelos Jr.
 * @since 1.0
 */

public interface DepartmentService {

    Collection<Department> findAll();

    long countAll();

    Department findOne(Long id);

    Department save(Department persist);

    Department update(Department update);

    void delete(Long id);
}