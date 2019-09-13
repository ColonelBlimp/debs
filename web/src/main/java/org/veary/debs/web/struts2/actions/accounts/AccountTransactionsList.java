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

import com.opensymphony.xwork2.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Transaction;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.AccountTransactionBean;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class AccountTransactionsList extends BaseAction {

    private static final Logger LOG = LogManager.getLogger(AccountTransactionsList.class);
    private static final String LOG_CALLED = "called";

    private final SystemFacade systemFacade;
    private final AccountFacade accountFacade;

    private Long id;
    private List<AccountTransactionBean> transactions;

    /**
     * Constructor.
     *
     * @param pageBean
     */
    @Inject
    public AccountTransactionsList(PageBean pageBean, SystemFacade systemFacade,
        AccountFacade accountFacade) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.systemFacade = systemFacade;
        this.accountFacade = accountFacade;

        this.pageBean.setPageTitle(getText("AccountTransactionsList.pageTitle"));
        this.pageBean.setMainHeadingText(getText("AccountTransactionsList.mainHeader"));
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        if (this.id == null) {
            this.transactions = Collections.emptyList();
        } else {
            this.transactions = transactionListToBeanList(
                this.systemFacade.getAllTransactions(false));
        }

        return Action.INPUT;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AccountTransactionBean> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<AccountTransactionBean> transactions) {
        this.transactions = transactions;
    }

    private List<AccountTransactionBean>
        transactionListToBeanList(List<Transaction> transactions) {
        LOG.trace(LOG_CALLED);

        List<AccountTransactionBean> list = new ArrayList<>(transactions.size());

        for (Transaction obj : transactions) {
            AccountTransactionBean bean = new AccountTransactionBean(obj);

            if (obj.getFromEntry().getAccountId().equals(this.id)) {
                bean.setOtherAccountName(
                    getAccountFromId(obj.getToEntry().getAccountId()).getName());
                bean.setAmountFrom(obj.getFromEntry().getAmount().toString());
            } else {
                bean.setOtherAccountName(
                    getAccountFromId(obj.getFromEntry().getAccountId()).getName());
                bean.setAmountTo(obj.getToEntry().getAmount().toString());
            }
            list.add(bean);
        }

        return Collections.unmodifiableList(list);
    }

    private Account getAccountFromId(Long id) {
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
