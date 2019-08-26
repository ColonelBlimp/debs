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
import org.veary.debs.dao.Registry;
import org.veary.debs.dao.TransactionDao;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;
import org.veary.persist.PersistenceManagerFactory;
import org.veary.persist.SqlStatement;
import org.veary.persist.TransactionManager;
import org.veary.persist.exceptions.NoResultException;

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

    /**
     * Constructor.
     *
     * @param registry {@link Registry}
     * @param factory {@link PersistenceManagerFactory}
     */
    @Inject
    public RealTransactionDao(Registry registry, PersistenceManagerFactory factory) {
        super(factory);
        LOG.trace(LOG_CALLED);
        this.registry = Objects.requireNonNull(registry, Messages.getParameterIsNull("registry")); //$NON-NLS-1$
    }

    @Override
    public Long createTransaction(Transaction object) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$

        TransactionManager manager = this.factory.createTransactionManager();
        manager.begin();

        Long fromId = createTransactionEntry(manager,
            Objects.requireNonNull(object.getFromEntry(),
                Messages.getString("RealTransactionDao.createTransaction.fromEntry.null"))); //$NON-NLS-1$
        Long toId = createTransactionEntry(manager,
            Objects.requireNonNull(object.getToEntry(),
                Messages.getString("RealTransactionDao.createTransaction.toEntry.null"))); //$NON-NLS-1$

        final SqlStatement insertTx = SqlStatement
            .newInstance(this.registry.getSql("createTransaction")); //$NON-NLS-1$
        insertTx.setParameter(1, object.getNarrative());
        insertTx.setParameter(2, object.getReference());
        insertTx.setParameter(3, fromId);
        insertTx.setParameter(4, toId);

        Long id = manager.persist(insertTx);
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

        throw new NoResultException("");
        //        return null;
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

        return manager.persist(insertTxEntry);
    }
}
