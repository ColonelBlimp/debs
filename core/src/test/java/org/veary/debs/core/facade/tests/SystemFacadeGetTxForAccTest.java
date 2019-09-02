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
import java.util.List;
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.facade.Status;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class SystemFacadeGetTxForAccTest extends AbstractSystemFacadeTestBase {
    //private static final Logger LOG = LogManager.getLogger(SystemFacadeGetTxForAccTest.class);

    private static final Money AMOUNT_ONE = new Money(BigDecimal.valueOf(100000L));
    private static final Money AMOUNT_TWO = new Money(BigDecimal.valueOf(200000L));
    private static final Money AMOUNT_THREE = new Money(BigDecimal.valueOf(300000L));

    @Test
    public void getForAccount() {
        Transaction transaction = Transaction.newInstance(TX_DATE, TX_NARRATIVE, TX_REFERENCE,
            AMOUNT_ONE, false);
        Entry fromEntry = Entry.newInstance(Entry.Types.FROM, this.fromAccount);
        Entry toEntry = Entry.newInstance(Entry.Types.TO, this.toAccount);
        this.systemFacade.postTransaction(transaction, fromEntry, toEntry);

        transaction = Transaction.newInstance(TX_DATE, TX_NARRATIVE, TX_REFERENCE,
            AMOUNT_TWO, false);
        this.systemFacade.postTransaction(transaction, fromEntry, toEntry);

        transaction = Transaction.newInstance(TX_DATE, TX_NARRATIVE, TX_REFERENCE,
            AMOUNT_THREE, false);
        Long id = this.systemFacade.postTransaction(transaction, fromEntry, toEntry);
        Optional<Transaction> result = this.systemFacade.getTransactionById(id);
        Assert.assertFalse(result.isEmpty());

        this.systemFacade.deleteTransaction(result.get());

        List<Transaction> listNonDeleted = this.systemFacade
            .getTransactionsForAccount(this.fromAccount, Status.NON_DELETED);
        Assert.assertNotNull(listNonDeleted);
        Assert.assertFalse(listNonDeleted.isEmpty());
        Assert.assertTrue(listNonDeleted.size() == 3);

        List<Transaction> listDeleted = this.systemFacade
            .getTransactionsForAccount(this.fromAccount, Status.DELETED);
        Assert.assertNotNull(listDeleted);
        Assert.assertFalse(listDeleted.isEmpty());
        Assert.assertTrue(listDeleted.size() == 1);

        List<Transaction> listBoth = this.systemFacade.getTransactionsForAccount(this.fromAccount,
            Status.BOTH);
        Assert.assertNotNull(listBoth);
        Assert.assertFalse(listBoth.isEmpty());
        Assert.assertTrue(listBoth.size() == 4);
    }
}