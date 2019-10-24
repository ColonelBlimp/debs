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

package org.veary.debs.core.facade;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.dao.EmployeeDao;
import org.veary.debs.facade.EmployeeFacade;
import org.veary.debs.model.Employee;
import org.veary.persist.exceptions.NoResultException;

/**
 * <b>Purpose:</b> Concrete implementation of the {@code EmployeeFacade} interface.
 *
 * <p><b>Responsibility:</b>
 *
 * <p><b>Note:</b> Annotated for JSR330.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
@Singleton
public final class RealEmployeeFacade implements EmployeeFacade {

    private static final Logger LOG = LogManager.getLogger(RealEmployeeFacade.class);
    private static final String LOG_CALLED = "called";

    private final EmployeeDao dao;

    /**
     * Constructor.
     *
     */
    @Inject
    public RealEmployeeFacade(EmployeeDao dao) {
        this.dao = Objects.requireNonNull(dao, Messages.getParameterIsNull("dao"));
    }

    @Override
    public Long create(Employee object) {
        LOG.trace(LOG_CALLED);
        return this.dao.createEmployee(validateInput(object));
    }

    @Override
    public Optional<Employee> getById(Long id) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(id, Messages.getParameterIsNull("id")); //$NON-NLS-1$

        try {
            return Optional.of(this.dao.getEmployeeById(id));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Employee> getByIdentityNumber(String identityNumber) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(identityNumber,
            Messages.getParameterIsNull("identityNumber")); //$NON-NLS-1$

        try {
            return Optional.of(this.dao.getEmployeeByIdentityNumber(identityNumber));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Employee> getAllEmployees(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);
        return this.dao.getAllEmployees(includeDeleted);
    }

    private Employee validateInput(Employee object) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$
        Objects.requireNonNull(object.getFullname(), "Fullname property cannot be null");
        Objects.requireNonNull(object.getContactNumber(),
            "ContactNumber property cannot be null");
        return object;
    }
}
