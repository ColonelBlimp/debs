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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Entry.Types;
import org.veary.debs.model.Transaction;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.TransactionBean;
import org.veary.persist.exceptions.PersistenceException;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class TransactionEdit extends TransactionBaseAction {

    private static final Logger LOG = LogManager.getLogger(TransactionEdit.class);
    private static final String LOG_CALLED = "called";

    private Transaction original;
    private Long id;

    /**
     * Constructor.
     *
     * @param pageBean {@link PageBean}
     * @param systemFacade {@link SystemFacade}
     * @param accountFacade {@link AccountFacade}
     */
    @Inject
    public TransactionEdit(PageBean pageBean, SystemFacade systemFacade,
        AccountFacade accountFacade) {
        super(pageBean, systemFacade, accountFacade);
        LOG.trace(LOG_CALLED);

        this.pageBean.setPageTitle(getText("TransactionEdit.pageTitle"));
        this.pageBean.setMainHeadingText(getText("TransactionEdit.mainHeader"));
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        if (this.id == null) {
            LOG.error("The transaction ID has not been set");
            return BaseAction.ERROR;
        }

        Optional<Transaction> result = this.systemFacade.getTransactionById(this.id);
        if (result.isEmpty()) {
            LOG.error("Unknown transaction with ID: {}", this.id);
            return BaseAction.ERROR;
        }

        this.original = result.get();
        this.bean = new TransactionBean(this.original);

        return BaseAction.INPUT;
    }

    @Override
    protected String executeSubmitUpdate() {
        LOG.trace(LOG_CALLED);

        Optional<Transaction> result = this.systemFacade
            .getTransactionById(Long.valueOf(this.bean.getId()));
        if (result.isEmpty()) {
            LOG.error("Unknown transaction with ID: {}", this.id);
            return BaseAction.ERROR;
        }
        this.original = result.get();

        Transaction updated = Transaction.newInstance(
            LocalDate.parse(this.bean.getDate()),
            this.bean.getNarrative(),
            this.bean.getReference(),
            new Money(new BigDecimal(this.bean.getAmount())),
            false, this.bean.isDeleted());

        Entry updatedFromEntry = Entry.newInstance(Types.FROM,
            getAccountFromId(Long.valueOf(this.bean.getFromAccountId())));
        Entry updatedToEntry = Entry.newInstance(Types.TO,
            getAccountFromId(Long.valueOf(this.bean.getToAccountId())));

        try {
            this.systemFacade.updateTransaction(this.original, updated, updatedFromEntry,
                updatedToEntry);
        } catch (PersistenceException e) {
            LOG.error(e);
            return BaseAction.ERROR;
        }

        return BaseAction.SUCCESS;
    }

    @Override
    protected void validateSubmitUpdate() {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
