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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.dao.AccountDao;
import org.veary.debs.dao.Registry;
import org.veary.debs.model.Account;
import org.veary.persist.PersistenceManagerFactory;
import org.veary.persist.QueryManager;
import org.veary.persist.SqlStatement;
import org.veary.persist.TransactionManager;
import org.veary.persist.exceptions.NoResultException;

/**
 * <b>Purpose:</b> Concrete implementation of the {@code AccountDao} interface.
 *
 * <p> <b>Responsibility:</b> Handling all CRUD actions for {@link Account} object.
 *
 * <p> <b>Note:</b> Annotated for JSR330.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
@Singleton
public final class RealAccountDao implements AccountDao {

    private static final Logger LOG = LogManager.getLogger(RealAccountDao.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$
    private static final String PARAM_OBJECT = "object"; //$NON-NLS-1$

    private final Registry registry;
    private final PersistenceManagerFactory factory;

    /**
     * Constructor.
     *
     * @param registry
     * @param factory
     */
    @Inject
    public RealAccountDao(Registry registry, PersistenceManagerFactory factory) {
        LOG.trace(LOG_CALLED);
        this.registry = Objects.requireNonNull(registry, Messages.getParameterIsNull("registry")); //$NON-NLS-1$
        this.factory = Objects.requireNonNull(factory, Messages.getParameterIsNull("factory")); //$NON-NLS-1$
    }

    @Override
    public Long createAccount(Account object) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(object, Messages.getParameterIsNull(PARAM_OBJECT));

        final SqlStatement insert = SqlStatement
            .newInstance(this.registry.getSql("createAccount")); //$NON-NLS-1$
        insert.setParameter(1, object.getName());
        insert.setParameter(2, object.getDescription());
        insert.setParameter(3, object.getParentId());
        insert.setParameter(4, object.getType().getId());

        final TransactionManager manager = this.factory.createTransactionManager();
        manager.begin();
        final Long newId = manager.persist(insert);
        manager.commit();

        return newId;
    }

    @Override
    public void updateAccount(Account original, Account updated) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(original, Messages.getParameterIsNull("original")); //$NON-NLS-1$
        Objects.requireNonNull(updated, Messages.getParameterIsNull("updated")); //$NON-NLS-1$

        final SqlStatement update = SqlStatement
            .newInstance(this.registry.getSql("updateAccount")); //$NON-NLS-1$
        update.setParameter(1, updated.getName());
        update.setParameter(2, updated.getDescription());
        update.setParameter(3, updated.getParentId());
        update.setParameter(4, updated.getType().getId());
        update.setParameter(5, original.getId());

        final TransactionManager manager = this.factory.createTransactionManager();
        manager.begin();
        manager.persist(update);
        manager.commit();
    }

    @Override
    public void updateAccountBalance(Account object, Money newBalance) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(object, Messages.getParameterIsNull(PARAM_OBJECT));
        Objects.requireNonNull(newBalance, Messages.getParameterIsNull("newBalance")); //$NON-NLS-1$

        final SqlStatement update = SqlStatement
            .newInstance(this.registry.getSql("updateAccountBalance"));
        update.setParameter(1, newBalance.getValue());
        update.setParameter(2, object.getId());

        final TransactionManager manager = this.factory.createTransactionManager();
        manager.begin();
        manager.persist(update);
        manager.commit();
    }

    @Override
    public void deleteAccount(Account object) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(object, Messages.getParameterIsNull(PARAM_OBJECT));
        final SqlStatement update = SqlStatement
            .newInstance(this.registry.getSql("deleteAccount")); //$NON-NLS-1$
        update.setParameter(1, object.getId());

        final TransactionManager manager = this.factory.createTransactionManager();
        manager.begin();
        manager.persist(update);
        manager.commit();
    }

    @Override
    public Account getAccountById(Long id) {
        LOG.trace(LOG_CALLED);

        Objects.requireNonNull(id, Messages.getParameterIsNull("id")); //$NON-NLS-1$

        final SqlStatement select = SqlStatement
            .newInstance(this.registry.getSql("getAccountById")); //$NON-NLS-1$
        select.setParameter(1, id);

        return executeAndReturnSingleResult(select);
    }

    @Override
    public Account getAccountByName(String name) {
        Objects.requireNonNull(name, Messages.getParameterIsNull("name")); //$NON-NLS-1$

        final SqlStatement select = SqlStatement
            .newInstance(this.registry.getSql("getAccountByName")); //$NON-NLS-1$
        select.setParameter(1, name);

        return executeAndReturnSingleResult(select);
    }

    @Override
    public List<Account> getAllAccounts(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);

        String key = "getAllAccounts";
        if (includeDeleted) {
            key = "getAllAccountsIncludeDeleted";
        }

        return getAccountsList(key);
    }

    @Override
    public List<Account> getGroupAccounts(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);

        String key = "getGroupAccounts";
        if (includeDeleted) {
            key = "getGroupAccountsIncludeDeleted";
        }

        return getAccountsList(key);
    }

    @Override
    public List<Account> getActualAccounts(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);

        String key = "getActualAccounts";
        if (includeDeleted) {
            key = "getActualAccountsIncludeDeleted";
        }

        return getAccountsList(key);
    }

    private Account executeAndReturnSingleResult(SqlStatement statement) {
        LOG.trace(LOG_CALLED);
        final QueryManager manager = this.factory.createQueryManager();
        return (Account) manager.createQuery(statement, Account.class).execute()
            .getSingleResult();
    }

    private List<Account> executeAndReturnListResult(SqlStatement statement) {
        LOG.trace(LOG_CALLED);
        final QueryManager manager = this.factory.createQueryManager();
        final List<Object> results = manager.createQuery(statement, Account.class).execute()
            .getResultList();

        final List<Account> list = new ArrayList<>(results.size());

        for (Object object : results) {
            list.add((Account) object);
        }

        return Collections.unmodifiableList(list);
    }

    private List<Account> getAccountsList(String key) {
        try {
            return executeAndReturnListResult(
                SqlStatement.newInstance(this.registry.getSql(key)));
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
