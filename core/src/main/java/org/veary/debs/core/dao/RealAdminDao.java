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
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.dao.AdminDao;
import org.veary.debs.dao.Registry;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.persist.PersistenceManagerFactory;
import org.veary.persist.SqlStatement;
import org.veary.persist.TransactionManager;
import org.veary.persist.exceptions.PersistenceException;

/**
 * <b>Purpose:</b> Concrete implementation of the {@link AdminDao} interface.
 *
 * <p><b>Responsibility:</b>
 *
 * <p><b>Note:</b> Annotated for JSR330.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
@Singleton
public final class RealAdminDao implements AdminDao {

    private static final Logger LOG = LogManager.getLogger(RealAdminDao.class);
    private static final String LOG_CALLED = "called";

    private final PersistenceManagerFactory factory;
    private final Registry registry;

    /**
     * Constructor.
     */
    @Inject
    public RealAdminDao(Registry registry, PersistenceManagerFactory factory) {
        LOG.trace(LOG_CALLED);

        this.registry = Objects.requireNonNull(registry,
            Messages.getParameterIsNull("registry")); //$NON-NLS-1$
        this.factory = Objects.requireNonNull(factory, Messages.getParameterIsNull("factory"));
    }

    @Override
    public void initializeDatabase() {
        LOG.trace(LOG_CALLED);
        try {
            final TransactionManager manager = this.factory.createTransactionManager();
            manager.begin();

            // Main accounting system
            createSchema(manager);
            createAccountTable(manager);
            createEntryTable(manager);
            createTransactionTable(manager);

            // Wages system
            createEmployeeTable(manager);

            // Default data
            createBalanceGroup(manager);

            manager.commit();
        } catch (PersistenceException e) {
            LOG.error(e);
            throw e;
        }
    }

    private void createSchema(TransactionManager manager) throws PersistenceException {
        LOG.trace(LOG_CALLED);

        final SqlStatement schema = SqlStatement.newInstance("CREATE SCHEMA IF NOT EXISTS DEBS");
        LOG.trace("Create SCHEMA result: {}", manager.persist(schema));
    }

    private void createAccountTable(TransactionManager manager) throws PersistenceException {
        LOG.trace(LOG_CALLED);
        final SqlStatement accountTable = SqlStatement
            .newInstance(this.registry.getSql("createAccountTable"));
        LOG.trace("Create ACCOUNT table result: {}", manager.persist(accountTable));
    }

    private void createEntryTable(TransactionManager manager) throws PersistenceException {
        LOG.trace(LOG_CALLED);
        final SqlStatement entryTable = SqlStatement
            .newInstance(this.registry.getSql("createEntryTable"));
        LOG.trace("Create ENTRY table result: {}", manager.persist(entryTable));
    }

    private void createTransactionTable(TransactionManager manager) throws PersistenceException {
        LOG.trace(LOG_CALLED);
        final SqlStatement txTable = SqlStatement
            .newInstance(this.registry.getSql("createTransactionTable"));
        LOG.trace("Create TRANSACTION table result: {}", manager.persist(txTable));
    }

    // This is done here rather than via the AccountFacade because this is the ONLY Account object
    // without a parentId
    private void createBalanceGroup(TransactionManager manager) throws PersistenceException {
        final SqlStatement createBalanceGroup = SqlStatement.newInstance(
            this.registry.getSql("createAccount"));
        createBalanceGroup.setParameter(1,
            AccountFacade.BuiltInAccounts.BALANCE_GROUP.toString());
        createBalanceGroup.setParameter(2, "Balance Group (Build In)");
        createBalanceGroup.setParameter(3, Long.valueOf(0));
        createBalanceGroup.setParameter(4, Account.Types.BALANCE_GROUP.getId());
        LOG.trace("Insert BALANCE GROUP account result: {}", manager.persist(createBalanceGroup));
    }

    private void createEmployeeTable(TransactionManager manager) throws PersistenceException {
        LOG.trace(LOG_CALLED);
        final SqlStatement employeeTable = SqlStatement
            .newInstance(this.registry.getSql("createEmployeeTable"));
        LOG.trace("Create EMPLOYEE table result: {}", manager.persist(employeeTable));
    }
}
