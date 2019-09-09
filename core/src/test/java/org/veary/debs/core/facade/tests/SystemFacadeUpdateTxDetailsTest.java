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

import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.model.EntryEntity;
import org.veary.debs.model.Account;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class SystemFacadeUpdateTxDetailsTest extends AbstractSystemFacadeTestBase {

    private static final LocalDate UPDATE_DATE = LocalDate.of(2019, 8, 20);
    private static final String UPDATE_NARRATIVE = "Updated Narrative";
    private static final String UPDATE_REFERENCE = "Updated Reference";

    /**
     * Updates the details of the transaction object. The Entries' details are left unchanged.
     */
    @Test
    public void updateTransactionObjectDetailsOnly() {
        java.util.Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction original = result.get();

        Transaction updated = Transaction.newInstance(UPDATE_DATE, UPDATE_NARRATIVE,
            UPDATE_REFERENCE, TX_AMOUNT, false, false);

        this.systemFacade.updateTransaction(original, updated,
            new EntryEntity(original.getFromEntry()),
            new EntryEntity(original.getToEntry()));

        result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction fetched = result.get();

        Assert.assertEquals(fetched.getDate(), UPDATE_DATE);
        Assert.assertEquals(fetched.getNarrative(), UPDATE_NARRATIVE);
        Assert.assertEquals(fetched.getReference(), UPDATE_REFERENCE);
        Assert.assertTrue(fetched.getFromEntry().getAmount().eq(TX_AMOUNT.negate()));
        Assert.assertTrue(fetched.getToEntry().getAmount().eq(TX_AMOUNT));

        Account fromAccount = this.accountDao
            .getAccountById(original.getFromEntry().getAccountId());
        Assert.assertTrue(fromAccount.getBalance().eq(TX_AMOUNT.negate()));

        Account toAccount = this.accountDao
            .getAccountById(original.getToEntry().getAccountId());
        Assert.assertTrue(toAccount.getBalance().eq(TX_AMOUNT));
    }
}
