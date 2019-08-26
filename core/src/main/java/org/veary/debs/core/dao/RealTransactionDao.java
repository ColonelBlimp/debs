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

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class RealTransactionDao implements TransactionDao {

    private static final Logger LOG = LogManager.getLogger(RealTransactionDao.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$

    private final Registry registry;
    private final PersistenceManagerFactory factory;

    @Inject
    public RealTransactionDao(Registry registry, PersistenceManagerFactory factory) {
        LOG.trace(LOG_CALLED);
        this.registry = Objects.requireNonNull(registry, Messages.getParameterIsNull("registry")); //$NON-NLS-1$
        this.factory = Objects.requireNonNull(factory, Messages.getParameterIsNull("factory")); //$NON-NLS-1$
    }

    @Override
    public Long createTransaction(Transaction object) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$

        TransactionManager manager = this.factory.createTransactionManager();
        manager.begin();

        createTransactionEntry(manager, object.getFromEntry());
        createTransactionEntry(manager, object.getToEntry());

        final SqlStatement insertTx = SqlStatement
            .newInstance(this.registry.getSql("createTransaction")); //$NON-NLS-1$

        Long id = manager.persist(insertTx);
        manager.commit();

        return null;
    }

    private Long createTransactionEntry(TransactionManager manager, Entry object) {
        LOG.trace(LOG_CALLED);

        final SqlStatement insertTxEntry = SqlStatement
            .newInstance(this.registry.getSql("createTransactionEntry")); //$NON-NLS-1$
        insertTxEntry.setParameter(1, object.getAmount());
        insertTxEntry.setParameter(2, object.getType().getId());
        insertTxEntry.setParameter(3, object.getAccountId());
        insertTxEntry.setParameter(4, object.isCleared());
        if (object.isCleared()) {
            Optional<LocalDateTime> result = object.getClearedTimestamp();
            if (result.isEmpty()) {
                throw new AssertionError(
                    "Entry marked as cleared, but not cleared timestamp has been set.");
            }
            insertTxEntry.setParameter(5, result.get());
        } else {
            insertTxEntry.setParameter(5, null);
        }

        return manager.persist(insertTxEntry);
    }
}
