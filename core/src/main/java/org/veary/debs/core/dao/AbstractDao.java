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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.model.Account;
import org.veary.persist.PersistenceManagerFactory;
import org.veary.persist.QueryManager;
import org.veary.persist.SqlStatement;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
abstract class AbstractDao<T> {

    private static final Logger LOG = LogManager.getLogger(AbstractDao.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$

    protected final PersistenceManagerFactory factory;

    /**
     * Constructor.
     *
     * @param factory {@link PersistenceManagerFactory}
     */
    protected AbstractDao(PersistenceManagerFactory factory) {
        this.factory = Objects.requireNonNull(factory, Messages.getParameterIsNull("factory")); //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")
    protected T executeAndReturnSingleResult(SqlStatement statement, Class<T> entityClass) {
        LOG.trace(LOG_CALLED);
        final QueryManager manager = this.factory.createQueryManager();
        return (T) manager.createQuery(statement, entityClass).execute()
            .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    protected List<T> executeAndReturnListResult(SqlStatement statement) {
        LOG.trace(LOG_CALLED);
        final QueryManager manager = this.factory.createQueryManager();
        final List<Object> results = manager.createQuery(statement, Account.class).execute()
            .getResultList();

        final List<T> list = new ArrayList<>(results.size());

        for (Object object : results) {
            list.add((T) object);
        }

        return Collections.unmodifiableList(list);
    }

}
