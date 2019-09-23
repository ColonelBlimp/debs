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
import org.veary.debs.dao.AdminDao;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.persist.PersistenceManagerFactory;
import org.veary.persist.SqlStatement;
import org.veary.persist.TransactionManager;
import org.veary.persist.exceptions.PersistenceException;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class RealAdminDao implements AdminDao {

    private static final Logger LOG = LogManager.getLogger(RealAdminDao.class);
    private static final String LOG_CALLED = "called";

    private final PersistenceManagerFactory factory;

    /**
     * Constructor.
     *
     */
    @Inject
    public RealAdminDao(PersistenceManagerFactory factory) {
        LOG.trace(LOG_CALLED);
        this.factory = Objects.requireNonNull(factory, Messages.getParameterIsNull("factory"));
    }

    @Override
    public void initializeDatabase() {
        LOG.trace(LOG_CALLED);
        try {
            final TransactionManager manager = this.factory.createTransactionManager();
            manager.begin();

            createSchema(manager);
            createAccountTable(manager);
            createEntryTable(manager);
            createTransactionTable(manager);
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
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS DEBS.ACCOUNT (")
            .append("ID IDENTITY GENERATED ALWAYS AS IDENTITY,")
            .append("CREATED SMALLDATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP()),")
            .append("BALANCE DECIMAL NOT NULL DEFAULT(0.00),")
            .append("DELETED BOOLEAN NOT NULL DEFAULT(FALSE),")
            .append("NAME VARCHAR(50) UNIQUE NOT NULL,")
            .append("DESCRIPTION VARCHAR(255) NOT NULL,")
            .append("PARENT_ID BIGINT NOT NULL,")
            .append("ACCOUNT_TYPE INT NOT NULL)");

        final SqlStatement accountTable = SqlStatement.newInstance(sb.toString());
        LOG.trace("Create ACCOUNT table result: {}", manager.persist(accountTable));
    }

    private void createEntryTable(TransactionManager manager) throws PersistenceException {
        LOG.trace(LOG_CALLED);
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS DEBS.ENTRY (")
            .append("ID IDENTITY GENERATED ALWAYS AS IDENTITY,")
            .append("CREATED SMALLDATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP()),")
            .append("DELETED BOOLEAN NOT NULL DEFAULT(FALSE),")
            .append("AMOUNT DECIMAL NOT NULL DEFAULT(0.00),")
            .append("ETYPE INT CHECK(ETYPE >= 1 AND ETYPE <= 2),")
            .append("ACCOUNT_ID BIGINT NOT NULL CHECK(ACCOUNT_ID > 0),")
            .append("CLEARED BOOLEAN NOT NULL DEFAULT(FALSE),")
            .append("CLEARED_TS SMALLDATETIME NOT NULL,")
            .append("FOREIGN KEY(ACCOUNT_ID) REFERENCES DEBS.ACCOUNT(ID))");

        final SqlStatement entryTable = SqlStatement.newInstance(sb.toString());
        LOG.trace("Create ENTRY table result: {}", manager.persist(entryTable));
    }

    private void createTransactionTable(TransactionManager manager) throws PersistenceException {
        LOG.trace(LOG_CALLED);
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS DEBS.TRANSACTION (")
            .append("ID IDENTITY GENERATED ALWAYS AS IDENTITY,")
            .append("CREATED SMALLDATETIME NOT NULL DEFAULT(CURRENT_TIMESTAMP()),")
            .append("DELETED BOOLEAN NOT NULL DEFAULT(FALSE),")
            .append("TDATE DATE NOT NULL,")
            .append("REFERENCE VARCHAR(255) NOT NULL,")
            .append("NARRATIVE VARCHAR(255) NOT NULL,")
            .append("EID_FROM BIGINT NOT NULL,")
            .append("EID_TO BIGINT NOT NULL,")
            .append("CHECK(EID_FROM > 0),")
            .append("CHECK(EID_TO > 0),")
            .append("CHECK(EID_FROM != EID_TO),")
            .append("FOREIGN KEY(EID_FROM) REFERENCES DEBS.ENTRY(ID),")
            .append("FOREIGN KEY(EID_TO) REFERENCES DEBS.ENTRY(ID))");

        final SqlStatement txTable = SqlStatement.newInstance(sb.toString());
        LOG.trace("Create TRANSACTION table result: {}", manager.persist(txTable));
    }

    // This is done here rather than via the AccountFacade because this is the ONLY Account object
    // without a parentId
    private void createBalanceGroup(TransactionManager manager) throws PersistenceException {
        StringBuilder sb = new StringBuilder()
            .append("INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES('")
            .append(AccountFacade.BuiltInAccounts.BALANCE_GROUP.toString())
            .append("','Balance Group',")
            .append(Long.valueOf(0).toString())
            .append(",")
            .append(Account.Types.BALANCE_GROUP.getId().toString())
            .append(")");

        final SqlStatement createBalanceGroup = SqlStatement.newInstance(sb.toString());
        LOG.trace("Insert BALANCE GROUP account result: {}", manager.persist(createBalanceGroup));
    }
}
