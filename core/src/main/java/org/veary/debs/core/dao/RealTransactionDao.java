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

package org.veary.debs.core.dao;

import java.util.Objects;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.TransactionEntity;
import org.veary.debs.core.model.TransactionGetByIdEntity;
import org.veary.debs.dao.AccountDao;
import org.veary.debs.dao.Registry;
import org.veary.debs.dao.TransactionDao;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;
import org.veary.persist.PersistenceManagerFactory;
import org.veary.persist.Query;
import org.veary.persist.QueryManager;
import org.veary.persist.SqlStatement;
import org.veary.persist.TransactionManager;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class RealTransactionDao extends AbstractDao<Transaction> implements TransactionDao {

    private static final Logger LOG = LogManager.getLogger(RealTransactionDao.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$

    private final Registry registry;
    private final AccountDao accountDao;

    /**
     * Constructor.
     *
     * @param registry {@link Registry}
     * @param factory {@link PersistenceManagerFactory}
     */
    @Inject
    public RealTransactionDao(Registry registry, PersistenceManagerFactory factory,
        AccountDao accountDao) {
        super(factory);
        LOG.trace(LOG_CALLED);
        this.registry = Objects.requireNonNull(registry, Messages.getParameterIsNull("registry")); //$NON-NLS-1$
        this.accountDao = accountDao;
    }

    @Override
    public Long createTransaction(Transaction object) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$
        Objects.requireNonNull(object.getFromEntry(),
            Messages.getString("RealTransactionDao.createTransaction.fromEntry.null"));
        Objects.requireNonNull(object.getToEntry(),
            Messages.getString("RealTransactionDao.createTransaction.toEntry.null"));

        Money newFromBalance = getCurrentBalanceForAccount(object.getFromEntry().getAccountId());
        newFromBalance = newFromBalance.plus(object.getFromEntry().getAmount());
        Money newToBalance = getCurrentBalanceForAccount(object.getToEntry().getAccountId());
        newToBalance = newToBalance.plus(object.getToEntry().getAmount());

        TransactionManager manager = this.factory.createTransactionManager();
        manager.begin();

        Long fromId = createTransactionEntry(manager, object.getFromEntry());
        Long toId = createTransactionEntry(manager, object.getToEntry());

        final SqlStatement insertTx = SqlStatement
            .newInstance(this.registry.getSql("createTransaction")); //$NON-NLS-1$
        insertTx.setParameter(1, object.getDate());
        insertTx.setParameter(2, object.getReference());
        insertTx.setParameter(3, object.getNarrative());
        insertTx.setParameter(4, fromId);
        insertTx.setParameter(5, toId);

        Long id = manager.persist(insertTx);

        updateAccountBalance(manager, object.getFromEntry(), newFromBalance);
        updateAccountBalance(manager, object.getToEntry(), newToBalance);

        manager.commit();

        return id;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(id, Messages.getParameterIsNull("id")); //$NON-NLS-1$

        final SqlStatement select = SqlStatement
            .newInstance(this.registry.getSql("getTransactionById")); //$NON-NLS-1$
        select.setParameter(1, id);

        QueryManager manager = this.factory.createQueryManager();
        Query query = manager.createQuery(select, TransactionGetByIdEntity.class);
        query.execute();

        return new TransactionEntity((TransactionGetByIdEntity) query.getSingleResult());
    }

    /**
     * A separate call will need to be made to set this item as cleared!
     *
     * @param manager {@code TransactionManager}
     * @param object {@code Entry}
     * @return the unique identifier of the new {@code Entry}
     */
    private Long createTransactionEntry(TransactionManager manager, Entry object) {
        LOG.trace(LOG_CALLED);

        final SqlStatement insertTxEntry = SqlStatement
            .newInstance(this.registry.getSql("createTransactionEntry")); //$NON-NLS-1$
        insertTxEntry.setParameter(1, object.getAmount().getValue());
        insertTxEntry.setParameter(2, object.getType().getId());
        insertTxEntry.setParameter(3, object.getAccountId());
        insertTxEntry.setParameter(4, object.isCleared());
        insertTxEntry.setParameter(5, object.getClearedTimestamp());

        return manager.persist(insertTxEntry);
    }

    private void updateAccountBalance(TransactionManager manager, Entry entry, Money newBalance) {
        LOG.trace(LOG_CALLED);

        LOG.trace("Account Id: {}, new balance: {}", entry.getAccountId(), newBalance); //$NON-NLS-1$

        final SqlStatement update = SqlStatement
            .newInstance(this.registry.getSql("updateAccountBalance")); //$NON-NLS-1$
        update.setParameter(1, newBalance.getValue());
        update.setParameter(2, entry.getAccountId());
        manager.persist(update);
    }

    private Money getCurrentBalanceForAccount(Long accountId) {
        return this.accountDao.getAccountById(accountId).getBalance();
    }
}
