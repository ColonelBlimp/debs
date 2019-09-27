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
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.exceptions.DebsException;
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

    protected final Map<Integer, String> typeMap;
    //    protected final Map<Long, String> groupMap;

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
            if (type.getId().intValue() >= 20) {
                this.typeMap.put(type.getId(), type.toString());
            }
        }

        //        this.groupMap = new HashMap<>();
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

    //    public Map<Long, String> getGroupMap() {
    //        return this.groupMap;
    //    }

    /**
     * Handles all the logic for which account types are available for the referenced (parent)
     * Account Group.
     *
     * @param parentId the account group id
     * @return {@code Map}
     */
    //TODO: Finish
    protected void getGroupsForType(Account.Types type) {
        LOG.trace(LOG_CALLED);

        LOG.trace("Type: {}", () -> type);
        /*
        this.groupMap.clear();
        
        switch (type) {
            case ASSET:
                Account assetAcc = getAccountByName(
                    AccountFacade.BuiltInAccounts.ASSETS_GROUP.toString());
                this.groupMap.put(assetAcc.getId(), assetAcc.getName());
                assetAcc = getAccountByName(
                    AccountFacade.BuiltInAccounts.NET_WORTH_GROUP.toString());
                this.groupMap.put(assetAcc.getId(), assetAcc.getName());
                break;
            case EXPENSE:
                final Account expAcc = getAccountByName(
                    AccountFacade.BuiltInAccounts.EXPENSES_GROUP.toString());
                this.groupMap.put(expAcc.getId(), expAcc.getName());
                break;
            case LIABILITY:
                final Account liabAcc = getAccountByName(
                    AccountFacade.BuiltInAccounts.LOANS_GROUP.toString());
                this.groupMap.put(liabAcc.getId(), liabAcc.getName());
                break;
            case INCOME:
                final Account incAcc = getAccountByName(
                    AccountFacade.BuiltInAccounts.INCOME_GROUP.toString());
                this.groupMap.put(incAcc.getId(), incAcc.getName());
                break;
            case EQUITY:
            case RETAINED_EARNINGS:
            case CONTROL:
            default:
        }
        */
    }

    private Account getAccountByName(String name) {
        Optional<Account> result = this.accountFacade
            .getByName(name);
        if (result.isEmpty()) {
            //TODO: Add Messages
            throw new DebsException("????");
        }
        return result.get();
    }
}
