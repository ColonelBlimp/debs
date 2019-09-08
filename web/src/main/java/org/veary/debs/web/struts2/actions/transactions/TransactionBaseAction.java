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

package org.veary.debs.web.struts2.actions.transactions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Account;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.TransactionBean;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public abstract class TransactionBaseAction extends BaseAction {

    private static final Logger LOG = LogManager.getLogger(TransactionBaseAction.class);
    private static final String LOG_CALLED = "called";

    private final Map<String, String> accountsMap;

    protected final AccountFacade accountFacade;
    protected final SystemFacade systemFacade;
    protected TransactionBean bean;

    /**
     * Constructor.
     *
     * @param pageBean
     */
    public TransactionBaseAction(PageBean pageBean,
        SystemFacade systemFacade, AccountFacade accountFacade) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.accountFacade = Objects.requireNonNull(accountFacade);
        this.systemFacade = Objects.requireNonNull(systemFacade);

        this.accountsMap = new HashMap<>();
        for (Account obj : this.accountFacade.getActualAccounts(false)) {
            this.accountsMap.put(obj.getId().toString(), obj.getName());
        }
    }

    public Map<String, String> getAccountsMap() {
        return this.accountsMap;
    }

    public TransactionBean getBean() {
        return this.bean;
    }

    public void setBean(TransactionBean bean) {
        this.bean = bean;
    }

    protected Account getAccountFromId(Long id) {
        LOG.trace(LOG_CALLED);
        Optional<Account> result = this.accountFacade.getById(id);
        if (result.isEmpty()) {
            throw new AssertionError(
                String.format(
                    "AccountFacade.getById() returned an empty result for ID: %s", id));
        }
        return result.get();
    }
}
