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

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.EntryEntity;
import org.veary.debs.core.model.TransactionEntity;
import org.veary.debs.dao.TransactionDao;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;
import org.veary.persist.exceptions.NoResultException;

/**
 * <b>Purpose:</b> Concrete implementation of the {@link SystemFacade} interface.
 *
 * <p><b>Responsibility:</b> Handling all the processes for manipulating {@code Transaction}
 * within the system.
 *
 * <p><b>Notes:</b> annotated for JSR330
 *
 * @author Marc L. Veary
 * @since 1.0
 */
@Singleton
public final class RealSystemFacade implements SystemFacade {

    private static final Logger LOG = LogManager.getLogger(RealSystemFacade.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$

    private final TransactionDao transactionDao;
    private final AccountFacade accountFacade;

    /**
     * Constructor.
     *
     * @param transactionDao {@link TransactionDao}
     */
    @Inject
    public RealSystemFacade(TransactionDao transactionDao, AccountFacade accountFacade) {
        this.transactionDao = Objects.requireNonNull(
            transactionDao,
            Messages.getParameterIsNull("transactionDao")); //$NON-NLS-1$
        this.accountFacade = Objects.requireNonNull(accountFacade,
            Messages.getParameterIsNull("accountFacade"));
    }

    @Override
    public Long postTransaction(Transaction transaction, Entry fromEntry, Entry toEntry) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(transaction,
            Messages.getParameterIsNull("transaction")); //$NON-NLS-1$
        Objects.requireNonNull(fromEntry, Messages.getParameterIsNull("fromEntry")); //$NON-NLS-1$
        Objects.requireNonNull(toEntry, Messages.getParameterIsNull("toEntry")); //$NON-NLS-1$

        TransactionEntity transactionEntity = (TransactionEntity) transaction;
        transactionEntity.setEntries(fromEntry, toEntry);

        Long id = this.transactionDao.createTransaction(transactionEntity);

        // Update the parent hierarchy balances
        updateParentBalance(fromEntry.getAccountId(), fromEntry.getAmount());
        updateParentBalance(toEntry.getAccountId(), toEntry.getAmount());
        return id;
    }

    @Override
    public void updateTransaction(Transaction original, Transaction updated,
        Entry updatedFromEntry, Entry updatedToEntry) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(original, Messages.getParameterIsNull("original")); //$NON-NLS-1$
        Objects.requireNonNull(updated, Messages.getParameterIsNull("updated")); //$NON-NLS-1$
        Objects.requireNonNull(updatedFromEntry,
            Messages.getParameterIsNull("updatedFromEntry")); //$NON-NLS-1$
        Objects.requireNonNull(updatedToEntry,
            Messages.getParameterIsNull("updatedToEntry")); //$NON-NLS-1$

        // Compare identity not state
        if (original.getFromEntry() == updatedFromEntry) {
            throw new IllegalArgumentException(
                Messages.getString("RealSystemFacade.updateTransaction.fromexception"));
        }

        // Compare identity not state
        if (original.getToEntry() == updatedToEntry) {
            throw new IllegalArgumentException(
                Messages.getString("RealSystemFacade.updateTransaction.toexception"));
        }

        ((EntryEntity) updatedFromEntry).setId(original.getFromEntry().getId());
        ((EntryEntity) updatedToEntry).setId(original.getToEntry().getId());

        TransactionEntity transactionEntity = (TransactionEntity) updated;
        transactionEntity.setEntries(updatedFromEntry, updatedToEntry);

        this.transactionDao.updateTransaction(original, updated);

        // Undo the original balance
        updateParentBalance(original.getFromEntry().getAccountId(),
            original.getToEntry().getAmount());
        updateParentBalance(original.getToEntry().getAccountId(),
            original.getFromEntry().getAmount());

        // Update the parent hierarchy balances
        if (!updated.isDeleted()) {
            updateParentBalance(updatedFromEntry.getAccountId(), updatedFromEntry.getAmount());
            updateParentBalance(updatedToEntry.getAccountId(), updatedToEntry.getAmount());
        }
    }

    @Override
    public void deleteTransaction(Transaction object) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$

        this.transactionDao.deleteTransaction(object);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(id, Messages.getParameterIsNull("id")); //$NON-NLS-1$

        try {
            return Optional.of(this.transactionDao.getTransactionById(id));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> getAllTransactions(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);
        return this.transactionDao.getAllTransactions(includeDeleted);
    }

    @Override
    public List<Transaction> getAllTransactionsOverPeriod(YearMonth period,
        boolean includeDeleted) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(period, Messages.getParameterIsNull("period"));

        return this.transactionDao.getAllTransactionsOverPeriod(period, includeDeleted);
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Account account, boolean includeDeleted) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(account, Messages.getParameterIsNull("account"));

        return this.transactionDao.getTransactionsForAccount(account, includeDeleted);
    }

    @Override
    public List<Transaction> getTransactionsForAccountOverPeriod(YearMonth period,
        Account account, boolean includeDeleted) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(period, Messages.getParameterIsNull("period"));
        Objects.requireNonNull(account, Messages.getParameterIsNull("account"));

        return this.transactionDao.getTransactionsForAccountOverPeriod(period, account,
            includeDeleted);
    }

    private void updateParentBalance(Long accountId, final Money amount) {
        LOG.trace(LOG_CALLED);

        Optional<Account> result = this.accountFacade.getById(accountId);
        if (result.isEmpty()) {
            throw new AssertionError("Unable to fetch account with ID: " + accountId);
        }
        Account account = result.get();

        Long parentId = account.getParentId();
        if (!parentId.equals(Long.valueOf(0))) {
            updateParentBalance(parentId, amount);
        }

        if (account.getType().toString().endsWith("_GROUP")) {
            LOG.trace("Update Balance for Account: {}", () -> account.getName());
            this.accountFacade.updateBalance(account, amount);
        }
    }
}
