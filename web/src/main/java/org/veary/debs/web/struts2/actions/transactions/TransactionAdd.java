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

import com.opensymphony.xwork2.Action;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.beans.TransactionBean;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class TransactionAdd extends TransactionBaseAction implements SessionAware {

    private static final Logger LOG = LogManager.getLogger(TransactionAdd.class);
    private static final String LOG_CALLED = "called";
    private static final String SESSION_DATE_KEY = "sessionDateKey";

    private String sessionDate;
    private Map<String, Object> sessionMap;

    /**
     * Constructor.
     *
     * @param pageBean
     */
    @Inject
    public TransactionAdd(PageBean pageBean, AccountFacade accountFacade,
        SystemFacade systemFacade) {
        super(pageBean, systemFacade, accountFacade);
        LOG.trace(LOG_CALLED);

        this.pageBean.setPageTitle(getText("TransactionAdd.pageTitle"));
        this.pageBean.setMainHeadingText(getText("TransactionAdd.mainHeader"));
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        this.bean = new TransactionBean();

        if (!this.sessionMap.containsKey(SESSION_DATE_KEY)) {
            this.sessionMap.put(SESSION_DATE_KEY, LocalDate.now().toString());
        }

        this.sessionDate = (String) this.sessionMap.get(SESSION_DATE_KEY);

        return Action.INPUT;
    }

    @Override
    protected String executeSubmitCreate() {
        LOG.trace(LOG_CALLED);

        final Account fromAccount = getAccountFromId(Long.valueOf(this.bean.getFromAccountId()));
        final Account toAccount = getAccountFromId(Long.valueOf(this.bean.getToAccountId()));
        final Money amount = new Money(
            BigDecimal.valueOf(Long.valueOf(this.bean.getAmount()).doubleValue()));

        final Entry fromEntry = Entry.newInstance(Entry.Types.FROM, fromAccount);
        final Entry toEntry = Entry.newInstance(Entry.Types.TO, toAccount);

        final Transaction transaction = Transaction.newInstance(
            calculateDate(this.bean.getDate()),
            this.bean.getNarrative(),
            this.bean.getReference(),
            amount, false, false);

        LOG.info("New transaction created: {}",
            this.systemFacade.postTransaction(transaction, fromEntry, toEntry));

        this.sessionMap.put(SESSION_DATE_KEY, this.bean.getDate());

        return Action.SUCCESS;
    }

    @Override
    protected void validateSubmitCreate() {
        LOG.trace(LOG_CALLED);

        if ("".equals(this.bean.getDate())) {
            addFieldError("date", "Invalid date");
            return;
        }

        try {
            LocalDate.parse(this.bean.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            addFieldError("date", "Invalid date");
            return;
        }

        if ("".equals(this.bean.getNarrative())) {
            addFieldError("narrative", "Narrative cannot be empty");
            return;
        }

        try {
            if (!Validator.checkMonetaryFormat(this.bean.getAmount())) {
                addFieldError("amount", "Amount invalid");
                return;
            }
        } catch (IllegalArgumentException e) {
            addFieldError("amount", "Amount invalid");
            return;
        }

        if (this.bean.getFromAccountId().equals(this.bean.getToAccountId())) {
            addFieldError("to", "Cannot be the same as the From Account");
        }
    }

    private LocalDate calculateDate(String isoLocalDate) {
        return LocalDate.parse(isoLocalDate, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.sessionMap = session;
    }

    public String getSessionDate() {
        return this.sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }
}
