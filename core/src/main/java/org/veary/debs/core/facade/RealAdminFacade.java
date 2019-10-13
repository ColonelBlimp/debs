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
import java.util.Optional;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.dao.AdminDao;
import org.veary.debs.exceptions.DebsException;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.AdminFacade;
import org.veary.debs.model.Account;

/**
 * <b>Purpose:</b> Concrete implementation of the {@link AdminFacade} interface.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class RealAdminFacade implements AdminFacade {

    private static final Logger LOG = LogManager.getLogger(RealAdminFacade.class);
    private static final String LOG_CALLED = "called";

    private final AdminDao adminDao;
    private final AccountFacade accountFacade;

    /**
     * Constructor.
     */
    @Inject
    public RealAdminFacade(AdminDao adminDao, AccountFacade accountFacade) {
        LOG.trace(LOG_CALLED);
        this.accountFacade = Objects.requireNonNull(accountFacade,
            Messages.getParameterIsNull("accountFacade"));
        this.adminDao = Objects.requireNonNull(adminDao, Messages.getParameterIsNull("adminDao"));
    }

    @Override
    public void checkDatabase() {
        LOG.trace(LOG_CALLED);

    }

    @Override
    public void initializeDatabase() {
        LOG.trace(LOG_CALLED);
        this.adminDao.initializeDatabase();

        Optional<Account> result = this.accountFacade
            .getByName(AccountFacade.BuiltInAccounts.BALANCE_GROUP.toString());
        if (result.isEmpty()) {
            throw new DebsException("Unable to fetch the "
                + AccountFacade.BuiltInAccounts.BALANCE_GROUP.toString() + " account!");
        }
        final Long netWorthGroupId = createNetWorthGroup(result.get().getId());
        final Long assetsGroupId = createAssetsGroup(netWorthGroupId);
        createLoansGroup(netWorthGroupId);
        final Long incAndExpGroupId = createIncomeAndExpensesGroup(result.get().getId());
        createIncomeGroup(incAndExpGroupId);
        createExpensesGroup(incAndExpGroupId);
        createOpeningBalanceAccount(result.get().getId());
        createCashAccount(assetsGroupId);
    }

    private Long createNetWorthGroup(Long parentId) {
        Account object = Account.newInstance(
            AccountFacade.BuiltInAccounts.NET_WORTH_GROUP.toString(),
            "Net Worth Group (Build In)",
            parentId,
            Account.Types.NETWORTH_GROUP);
        return this.accountFacade.create(object);
    }

    private Long createAssetsGroup(Long parentId) {
        Account object = Account.newInstance(
            AccountFacade.BuiltInAccounts.ASSETS_GROUP.toString(),
            "Assets Group (Build In)",
            parentId,
            Account.Types.ASSETS_GROUP);
        return this.accountFacade.create(object);
    }

    private Long createLoansGroup(Long parentId) {
        Account object = Account.newInstance(
            AccountFacade.BuiltInAccounts.LOANS_GROUP.toString(),
            "Loans Group (Build In)",
            parentId,
            Account.Types.LOANS_GROUP);
        return this.accountFacade.create(object);
    }

    private Long createIncomeAndExpensesGroup(Long parentId) {
        Account object = Account.newInstance(
            AccountFacade.BuiltInAccounts.INCOME_AND_EXPENSES_GROUP.toString(),
            "Income & Expenses Group (Build In)",
            parentId,
            Account.Types.REVENUE_GROUP);
        return this.accountFacade.create(object);
    }

    private Long createIncomeGroup(Long parentId) {
        Account object = Account.newInstance(
            AccountFacade.BuiltInAccounts.INCOME_GROUP.toString(),
            "Income Group (Build In)",
            parentId,
            Account.Types.INCOME_GROUP);
        return this.accountFacade.create(object);
    }

    private Long createExpensesGroup(Long parentId) {
        Account object = Account.newInstance(
            AccountFacade.BuiltInAccounts.EXPENSES_GROUP.toString(),
            "Expense Group (Build In)",
            parentId,
            Account.Types.EXPENSE_GROUP);
        return this.accountFacade.create(object);
    }

    private Long createOpeningBalanceAccount(Long parentId) {
        Account object = Account.newInstance(
            AccountFacade.BuiltInAccounts.OPENING_BALANCE.toString(),
            "Opening Balance (Build In)",
            parentId,
            Account.Types.RETAINED_EARNINGS);
        return this.accountFacade.create(object);
    }

    private Long createCashAccount(Long parentId) {
        Account object = Account.newInstance("Cash",
            "Cash",
            parentId,
            Account.Types.ASSET);
        return this.accountFacade.create(object);
    }
}
