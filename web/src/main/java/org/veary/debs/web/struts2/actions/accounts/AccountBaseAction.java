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

package org.veary.debs.web.struts2.actions.accounts;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.AccountBean;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public abstract class AccountBaseAction extends BaseAction {

    private static final Logger LOG = LogManager.getLogger(AccountBaseAction.class);
    private static final String LOG_CALLED = "called";

    private final Map<Integer, String> typeMap;
    private final Map<Long, String> parentMap;

    protected final AccountFacade accountFacade;
    protected AccountBean bean;

    /**
     * Constructor.
     *
     * @param pageBean
     */
    public AccountBaseAction(PageBean pageBean, AccountFacade accountFacade) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.accountFacade = accountFacade;

        this.typeMap = new HashMap<>();
        for (Account.Types type : Account.Types.values()) {
            this.typeMap.put(type.getId(), type.toString());
        }

        this.parentMap = new HashMap<>();
        for (Account parent : this.accountFacade.getGroupAccounts(false)) {
            this.parentMap.put(parent.getId(), parent.getName());
        }

        this.bean = new AccountBean();
    }

    protected boolean validateAccountBeanStringFields(AccountBean bean) {
        LOG.trace(LOG_CALLED);
        if ("".equals(bean.getName())) {
            addFieldError("name", "Invalid name");
            return false;
        }

        try {
            Validator.validateTextField(bean.getName());
        } catch (IllegalArgumentException e) {
            addFieldError("name", e.getMessage());
            return false;
        }

        try {
            Validator.validateTextField(bean.getDescription());
        } catch (IllegalArgumentException e) {
            addFieldError("description", e.getMessage());
            return false;
        }

        return true;
    }

    public AccountBean getBean() {
        return this.bean;
    }

    public void setBean(AccountBean bean) {
        this.bean = bean;
    }

    public Map<Integer, String> getTypeMap() {
        return this.typeMap;
    }

    public Map<Long, String> getParentMap() {
        return this.parentMap;
    }
}
