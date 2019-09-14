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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.core.Money;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Transaction;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.AccountTransactionBean;

/**
 * <b>Purpose:</b> Struts action.
 *
 * <p><b>View:</b> {@code /WEB-INF/templates/accounts/txlist.ftl}
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
    private BigDecimal fromColumnTotal = BigDecimal.ZERO;
    private BigDecimal toColumnTotal = BigDecimal.ZERO;
    private Account account;

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
            Optional<Account> result = this.accountFacade.getById(this.id);
            if (result.isEmpty()) {
                LOG.error("Unable to fetch account with ID: {}", () -> this.id);
                return Action.ERROR;
            }
            this.account = result.get();
            this.transactions = transactionListToBeanList(
                this.systemFacade.getTransactionsForAccount(this.account, false));
        }

        return Action.SUCCESS;
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

    public String getFromColumnTotal() {
        if (this.fromColumnTotal.signum() == -1) {
            return new Money(this.fromColumnTotal).negate().toString();
        }
        return new Money(this.fromColumnTotal).toString();
    }

    public String getToColumnTotal() {
        return new Money(this.toColumnTotal).toString();
    }

    public Account getAccount() {
        return this.account;
    }

    private List<AccountTransactionBean>
        transactionListToBeanList(List<Transaction> transactions) {
        LOG.trace(LOG_CALLED);

        BigDecimal fromTotal = BigDecimal.ZERO;
        BigDecimal toTotal = BigDecimal.ZERO;
        List<AccountTransactionBean> list = new ArrayList<>(transactions.size());

        for (Transaction obj : transactions) {
            AccountTransactionBean bean = new AccountTransactionBean(obj);

            if (obj.getFromEntry().getAccountId().equals(this.id)) {
                bean.setOtherAccountName(
                    getAccountFromId(obj.getToEntry().getAccountId()).getName());
                bean.setAmountFrom(obj.getFromEntry().getAmount().toString());
                fromTotal = fromTotal.add(obj.getFromEntry().getAmount().getValue());
            } else {
                bean.setOtherAccountName(
                    getAccountFromId(obj.getFromEntry().getAccountId()).getName());
                bean.setAmountTo(obj.getToEntry().getAmount().toString());
                toTotal = toTotal.add(obj.getToEntry().getAmount().getValue());
            }
            list.add(bean);
        }

        this.fromColumnTotal = fromTotal;
        this.toColumnTotal = toTotal;

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
