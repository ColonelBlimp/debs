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

import java.util.Objects;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.core.model.EntryEntity;
import org.veary.debs.core.model.TransactionEntity;
import org.veary.debs.dao.TransactionDao;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class RealSystemFacade implements SystemFacade {

    private static final Logger LOG = LogManager.getLogger(RealSystemFacade.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$

    private final TransactionDao transactionDao;

    @Inject
    public RealSystemFacade(TransactionDao transactionDao) {
        this.transactionDao = Objects.requireNonNull(transactionDao,
            Messages.getParameterIsNull("transactionDao")); //$NON-NLS-1$
    }

    @Override
    public Long postTransaction(Transaction transaction, Entry fromEntry, Entry toEntry) {
        LOG.trace(LOG_CALLED);

        TransactionEntity transactionEntity = (TransactionEntity) Objects.requireNonNull(
            transaction, Messages.getParameterIsNull("transaction")); //$NON-NLS-1$
        EntryEntity fromEntryEntity = (EntryEntity) Objects.requireNonNull(fromEntry,
            Messages.getParameterIsNull("fromEntry")); //$NON-NLS-1$
        EntryEntity toEntryEntity = (EntryEntity) Objects.requireNonNull(toEntry,
            Messages.getParameterIsNull("toEntry")); //$NON-NLS-1$

        transactionEntity.setEntries(fromEntryEntity, toEntryEntity);
        LOG.debug(transactionEntity);

        return this.transactionDao.createTransaction(transactionEntity);
    }
}
