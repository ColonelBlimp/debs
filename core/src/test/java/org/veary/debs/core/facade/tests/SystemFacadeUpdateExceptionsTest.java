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
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.EntryEntity;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class SystemFacadeUpdateExceptionsTest extends AbstractSystemFacadeTestBase {
    //private static final Logger LOG = LogManager.getLogger(SystemFacadeUpdateExceptionsTest.class);

    private static final Money UPDATED_AMOUNT = new Money(BigDecimal.valueOf(123456L));

    @Test(expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "The original 'FROM' entry must not be the same instance as the updated 'FROM' entry. Use the EntryEntity copy constructor.")
    public void fromEntityIdentityException() {
        Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction original = result.get();

        Transaction updated = Transaction.newInstance(original.getDate(), original.getNarrative(),
            original.getReference(), UPDATED_AMOUNT, false, false);

        this.systemFacade.updateTransaction(original, updated,
            original.getFromEntry(), new EntryEntity(original.getToEntry()));
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "The original 'TO' entry must not be the same instance as the updated 'TO' entry. Use the EntryEntity copy constructor.")
    public void toEntityIdentityException() {
        Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction original = result.get();

        Transaction updated = Transaction.newInstance(original.getDate(), original.getNarrative(),
            original.getReference(), UPDATED_AMOUNT, false, false);

        this.systemFacade.updateTransaction(original, updated,
            new EntryEntity(original.getFromEntry()), original.getToEntry());
    }
}
