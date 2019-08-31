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
import org.veary.debs.core.model.EntryEntity;
import org.veary.debs.model.Account;
import org.veary.debs.model.Transaction;

public class SystemFacadeUpdateTxFromAccAndAmountTest extends AbstractSystemFacadeTestBase {

    //    private static final Logger LOG = LogManager.getLogger(SystemFacadeUpdateTxFromAccTest.class);

    private static final Money UPDATED_AMOUNT = new Money(BigDecimal.valueOf(789123L));

    /**
     * Updates the current FROM account to another account plus the amount for the transaction.
     */
    @Test
    public void updateTransactionFromAccount() {
        java.util.Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction original = result.get();

        Transaction updated = Transaction.newInstance(original.getDate(), original.getNarrative(),
            original.getReference(), UPDATED_AMOUNT, false);

        EntryEntity updatedFromEntry = new EntryEntity(original.getFromEntry());
        updatedFromEntry.setAccountId(this.otherFromAccount.getId());

        this.systemFacade.updateTransaction(original, updated,
            updatedFromEntry, new EntryEntity(original.getToEntry()));

        result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction fetched = result.get();

        Assert.assertEquals(fetched.getDate(), TX_DATE);
        Assert.assertEquals(fetched.getNarrative(), TX_NARRATIVE);
        Assert.assertEquals(fetched.getReference(), TX_REFERENCE);
        Assert.assertTrue(fetched.getFromEntry().getAmount().eq(UPDATED_AMOUNT.negate()));
        Assert.assertTrue(fetched.getToEntry().getAmount().eq(UPDATED_AMOUNT));

        Assert.assertEquals(fetched.getFromEntry().getAccountId(),
            this.otherFromAccount.getId());
        Assert.assertEquals(fetched.getToEntry().getAccountId(),
            this.toAccount.getId());

        Account fromAccount = this.accountDao
            .getAccountById(fetched.getFromEntry().getAccountId());
        Assert.assertTrue(fromAccount.getBalance().eq(UPDATED_AMOUNT.negate()));

        Account toAccount = this.accountDao
            .getAccountById(original.getToEntry().getAccountId());
        Assert.assertTrue(toAccount.getBalance().eq(UPDATED_AMOUNT));

        Account oldFromAccount = this.accountDao
            .getAccountById(original.getFromEntry().getAccountId());
        Assert.assertEquals(original.getFromEntry().getAccountId(),
            this.fromAccount.getId());
        Assert.assertTrue(oldFromAccount.getBalance().eq(new Money(BigDecimal.ZERO)));
    }
}
