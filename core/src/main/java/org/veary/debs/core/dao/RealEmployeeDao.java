/*
 * MIT License
 *
 * Copyright (c) 2019 ColonelBlimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.veary.debs.core.dao;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.dao.EmployeeDao;
import org.veary.debs.dao.Registry;
import org.veary.debs.model.Employee;
import org.veary.persist.PersistenceManagerFactory;
import org.veary.persist.SqlStatement;
import org.veary.persist.TransactionManager;
import org.veary.persist.exceptions.NoResultException;

/**
 * <b>Purpose:</b> Concrete implementation of the {@link EmployeeDao} interface.
 *
 * <p><b>Responsibility:</b> Handles all CRUD operations for the employee system.
 * 
 * <p><b>Note:</b> Annotated for JSR330.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class RealEmployeeDao extends AbstractDao<Employee> implements EmployeeDao {

    private static final Logger LOG = LogManager.getLogger(RealEmployeeDao.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$

    private final Registry registry;

    /**
     * Constructor.
     *
     * @param registry {@link Registry}
     * @param factory {@link PersistenceManagerFactory}
     */
    @Inject
    public RealEmployeeDao(Registry registry, PersistenceManagerFactory factory) {
        super(factory);
        LOG.trace(LOG_CALLED);
        this.registry = Objects.requireNonNull(registry,
            Messages.getParameterIsNull("registry")); //$NON-NLS-1$
    }

    @Override
    public Long createEmployee(Employee object) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$

        final SqlStatement insert = SqlStatement
            .newInstance(this.registry.getSql("createEmployee")); //$NON-NLS-1$
        insert.setParameter(1, object.getFullname());
        insert.setParameter(2, object.getNationalIdNumber());
        insert.setParameter(3, object.getContactNumber());

        final TransactionManager manager = this.factory.createTransactionManager();
        manager.begin();
        final Long newId = manager.persist(insert);
        manager.commit();

        return newId;
    }

    @Override
    public void updateEmployee(Employee original, Employee updated) {
        LOG.trace(LOG_CALLED);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(id, Messages.getParameterIsNull("id")); //$NON-NLS-1$

        final SqlStatement select = SqlStatement
            .newInstance(this.registry.getSql("getEmployeeById")); //$NON-NLS-1$
        select.setParameter(1, id);

        return executeAndReturnSingleResult(select, Employee.class);
    }

    @Override
    public Employee getEmployeeByIdentityNumber(String number) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(number, Messages.getParameterIsNull("number")); //$NON-NLS-1$

        final SqlStatement select = SqlStatement
            .newInstance(this.registry.getSql("getEmployeeByIdentityNumber")); //$NON-NLS-1$
        select.setParameter(1, number);

        return executeAndReturnSingleResult(select, Employee.class);
    }

    @Override
    public List<Employee> getAllEmployees(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);

        String key = "getAllEmployees"; //$NON-NLS-1$
        if (includeDeleted) {
            key = "getAllEmployeesIncludeDeleted"; //$NON-NLS-1$
        }

        return getEmployeeList(key);
    }

    private List<Employee> getEmployeeList(String key) {
        try {
            return executeAndReturnListResult(
                SqlStatement.newInstance(this.registry.getSql(key)), Employee.class);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
