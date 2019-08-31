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

package org.veary.debs.core.facade.tests;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.veary.debs.core.Money;
import org.veary.debs.model.Account;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;
import org.veary.debs.tests.JndiTestBase;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class AbstractSystemFacadeTestBase extends JndiTestBase {

    protected static final Long BALANCE_GROUP_ID = Long.valueOf(1);
    protected static final Long NETWORTH_GROUP_ID = Long.valueOf(2);
    protected static final Long ASSETS_GROUP_ID = Long.valueOf(3);
    protected static final Long LIABILITIES_GROUP_ID = Long.valueOf(4);
    protected static final Long INCOME_AND_EXPENSES_GROUP_ID = Long.valueOf(5);
    protected static final Long INCOME_GROUP_ID = Long.valueOf(6);
    protected static final Long EXPENSES_GROUP_ID = Long.valueOf(7);

    protected Account fromAccount;
    protected Account toAccount;
    protected Account otherFromAccount;
    protected Account otherToAccount;

    protected static final String CASH_ACC_NAME = "Cash";
    protected static final String CASH_ACC_DESC = "Cash Description";
    protected static final String FUEL_ACC_NAME = "Fuel";
    protected static final String FUEL_ACC_DESC = "Fuel Description";
    protected static final String BANK_ACC_NAME = "Bank";
    protected static final String BANK_ACC_DESC = "Bank Description";
    protected static final String FOOD_ACC_NAME = "Food";
    protected static final String FOOD_ACC_DESC = "Food Description";

    protected static final LocalDate TX_DATE = LocalDate.now();
    protected static final String TX_NARRATIVE = "Test Narrative";
    protected static final String TX_REFERENCE = "Test Reference";
    protected static final Money TX_AMOUNT = new Money(BigDecimal.valueOf(100000));

    protected Long txId;

    @BeforeClass
    @Override
    public void setUp() {
        super.setUp();
        createAccounts();

        Transaction transaction = Transaction.newInstance(TX_DATE, TX_NARRATIVE, TX_REFERENCE,
            TX_AMOUNT, false);
        Entry fromEntry = Entry.newInstance(Entry.Types.FROM, this.fromAccount);
        Entry toEntry = Entry.newInstance(Entry.Types.TO, this.toAccount);

        this.txId = this.systemFacade.postTransaction(transaction, fromEntry, toEntry);
        Assert.assertNotNull(this.txId);
        Assert.assertTrue(this.txId.longValue() > 0L);

        Account fromAcc = this.accountDao.getAccountByName(CASH_ACC_NAME);
        Assert.assertNotNull(fromAcc);
        Assert.assertNotNull(fromAcc.getBalance());
        Assert.assertTrue(fromAcc.getBalance().isMinus());
        Assert.assertTrue(fromAcc.getBalance().eq(TX_AMOUNT.negate()));

        Account toAcc = this.accountDao.getAccountByName(FUEL_ACC_NAME);
        Assert.assertNotNull(toAcc);
        Assert.assertNotNull(toAcc.getBalance());
        Assert.assertTrue(toAcc.getBalance().isPlus());
        Assert.assertTrue(toAcc.getBalance().eq(TX_AMOUNT));
    }

    @AfterClass
    @Override
    public void teardown() {
        super.teardown();
    }

    private void createAccounts() {
        if (this.fromAccount == null) {
            this.accountDao
                .createAccount(Account.newInstance(CASH_ACC_NAME, CASH_ACC_DESC, ASSETS_GROUP_ID,
                    Account.Types.ASSET));
            this.fromAccount = this.accountDao.getAccountByName(CASH_ACC_NAME);
        }

        if (this.toAccount == null) {
            this.accountDao
                .createAccount(
                    Account.newInstance(FUEL_ACC_NAME, FUEL_ACC_DESC, EXPENSES_GROUP_ID,
                        Account.Types.EXPENSE));
            this.toAccount = this.accountDao.getAccountByName(FUEL_ACC_NAME);
        }

        if (this.otherFromAccount == null) {
            this.accountDao.createAccount(Account.newInstance(BANK_ACC_NAME, BANK_ACC_DESC,
                ASSETS_GROUP_ID, Account.Types.ASSET));
            this.otherFromAccount = this.accountDao.getAccountByName(BANK_ACC_NAME);
        }

        if (this.otherToAccount == null) {
            this.accountDao.createAccount(Account.newInstance(FOOD_ACC_NAME, FOOD_ACC_DESC,
                EXPENSES_GROUP_ID, Account.Types.EXPENSE));
            this.otherToAccount = this.accountDao.getAccountByName(FOOD_ACC_NAME);
        }
    }
}
