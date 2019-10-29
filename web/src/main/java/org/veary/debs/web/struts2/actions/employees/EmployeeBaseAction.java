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

package org.veary.debs.web.struts2.actions.employees;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.facade.EmployeeFacade;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.EmployeeBean;

abstract class EmployeeBaseAction extends BaseAction {

    private static final Logger LOG = LogManager.getLogger(EmployeeBaseAction.class);
    private static final String LOG_CALLED = "called";

    protected final EmployeeFacade employeeFacade;
    protected EmployeeBean bean;

    public EmployeeBaseAction(PageBean pageBean, EmployeeFacade employeeFacade) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.employeeFacade = Objects.requireNonNull(employeeFacade,
            Messages.getParameterIsNull("employeeFacade"));
    }

    protected boolean validateBeanStringFields(EmployeeBean bean) {
        LOG.trace(LOG_CALLED);
        if ("".equals(bean.getFullname())) {
            addFieldError("fullname", "Invalid fullname");
            return false;
        }

        try {
            Validator.validateTextField(bean.getFullname());
        } catch (IllegalArgumentException e) {
            addFieldError("fullname", e.getMessage());
            return false;
        }

        if ("".equals(bean.getNationalIdNumber())) {
            addFieldError("nid", "Invalid ID");
            return false;
        }

        if ("".equals(bean.getContactNumber())) {
            addFieldError("phone", "Invalid Contact number");
            return false;
        }

        return true;
    }

    public EmployeeBean getBean() {
        return this.bean;
    }

    public void setBean(EmployeeBean bean) {
        this.bean = bean;
    }
}
