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

import java.util.Objects;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.dao.EmployeeDao;
import org.veary.debs.facade.EmployeeFacade;
import org.veary.debs.model.Employee;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
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
    public Long createEmployee(Employee object) {
        LOG.trace(LOG_CALLED);
        return this.dao.createEmployee(validateInput(object));
    }

    private Employee validateInput(Employee object) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$
        Objects.requireNonNull(object.getFullname(), "");
        return object;
    }

}
