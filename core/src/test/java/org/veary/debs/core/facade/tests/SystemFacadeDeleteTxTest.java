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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.model.Account;
import org.veary.debs.model.Transaction;

public class SystemFacadeDeleteTxTest extends AbstractSystemFacadeTestBase {
    //    private static final Logger LOG = LogManager.getLogger(SystemFacadeDeleteTxTest.class);

    private static final Money ZERO = new Money(BigDecimal.ZERO);

    @Test
    public void deleteMethod() {
        java.util.Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction object = result.get();

        this.systemFacade.deleteTransaction(object);

        result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction fetched = result.get();

        Assert.assertTrue(fetched.isDeleted());
        Assert.assertTrue(fetched.getFromEntry().isDeleted());
        Assert.assertTrue(fetched.getToEntry().isDeleted());

        Account fromAccount = this.accountDao
            .getAccountById(fetched.getFromEntry().getAccountId());
        Assert.assertTrue(fromAccount.getBalance().eq(ZERO));

        Account toAccount = this.accountDao
            .getAccountById(fetched.getToEntry().getAccountId());
        Assert.assertTrue(toAccount.getBalance().eq(ZERO));
    }
}
