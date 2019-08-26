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
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.facade.RealSystemFacade;
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
public class SystemFacadeTest extends JndiTestBase {

    @Test
    public void instantiation() {
        Assert.assertNotNull(new RealSystemFacade(this.transactionDao));
    }

    @Test
    public void fromGuice() {
        Assert.assertNotNull(this.systemFacade);
    }

    private Account fromAccount;
    private Account toAccount;

    private static final LocalDate DATE = LocalDate.now();
    private static final String NARRATIVE = "Test Narrative"; //$NON-NLS-1$
    private static final String REFERENCE = "Test Reference"; //$NON-NLS-1$
    private static final Money AMOUNT = new Money(BigDecimal.valueOf(100000));

    @Test(dependsOnMethods = { "fromGuice" })
    public void postTransactionMethod() {
        createAccounts();
        Transaction transaction = Transaction.newInstance(DATE, NARRATIVE, REFERENCE, AMOUNT,
            false);
        Entry fromEntry = Entry.newInstance(Entry.Types.FROM, this.fromAccount);
        Entry toEntry = Entry.newInstance(Entry.Types.TO, this.toAccount);
    }

    private static final String FROM_NAME = "Cash"; //$NON-NLS-1$
    private static final String FROM_DESC = "Cash Description"; //$NON-NLS-1$
    private static final String TO_NAME = "Fuel"; //$NON-NLS-1$
    private static final String TO_DESC = "Fuel Description"; //$NON-NLS-1$

    private void createAccounts() {
        if (this.fromAccount == null) {
            this.accountDao
                .createAccount(Account.newInstance(FROM_NAME, FROM_DESC, Long.valueOf(3),
                    Account.Types.ASSET));
            this.fromAccount = this.accountDao.getAccountByName(FROM_NAME);
        }

        if (this.toAccount == null) {
            this.accountDao
                .createAccount(Account.newInstance(TO_NAME, TO_DESC, Long.valueOf(7),
                    Account.Types.EXPENSE));
            this.toAccount = this.accountDao.getAccountByName(TO_NAME);
        }
    }
}
