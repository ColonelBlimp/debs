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

package org.veary.debs.model.tests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.EntryEntity;
import org.veary.debs.core.model.TransactionEntity;
import org.veary.debs.core.model.TransactionEntitySelect;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class TransactionTest {

    private static final LocalDate DATE = LocalDate.now();
    private static final String NARRATIVE = "Test Narrative"; //$NON-NLS-1$
    private static final String REFERENCE = "Test Reference"; //$NON-NLS-1$
    private static final Money AMOUNT = new Money(BigDecimal.valueOf(123456));

    @Test
    public void instantiationNotCleared() {
        TransactionEntity entity = (TransactionEntity) Transaction.newInstance(DATE, NARRATIVE,
            REFERENCE, AMOUNT, false, false);
        Assert.assertNotNull(entity);
        Assert.assertEquals(entity.getNarrative(), NARRATIVE);
        Assert.assertEquals(entity.getReference(), REFERENCE);
        Assert.assertEquals(entity.getDate(), DATE);
        Assert.assertTrue(entity.getAmount().eq(AMOUNT));
        Assert.assertFalse(entity.isCleared());
        Assert.assertFalse(entity.isDeleted());
    }

    private static final Long ACOUNT_FROM_ID = Long.valueOf(11);
    private static final LocalDateTime CREATION = LocalDateTime.now();
    private static final Long FROM_ID = Long.valueOf(100);
    private static final Long ID = Long.valueOf(123);
    private static final Long TO_ID = Long.valueOf(200);
    private static final Long ACOUNT_TO_ID = Long.valueOf(22);

    @Test
    public void transactionGetByIdMethod() {
        TransactionEntitySelect object = new TransactionEntitySelect();
        object.setCreationTimestamp(CREATION);
        object.setDate(DATE);
        object.setDeleted(true);
        object.setFromAccountId(ACOUNT_FROM_ID);
        object.setFromAmount(AMOUNT.negate());
        object.setFromClearedTimestamp(EntryEntity.NOT_CLEARED_TIMESTAMP);
        object.setFromCreatedTimestamp(CREATION);
        object.setFromDeleted(false);
        object.setFromId(FROM_ID);
        object.setId(ID);
        object.setNarrative(NARRATIVE);
        object.setReference(REFERENCE);
        object.setToAccountId(ACOUNT_TO_ID);
        object.setToAmount(AMOUNT);
        object.setToClearedTimestamp(EntryEntity.NOT_CLEARED_TIMESTAMP);
        object.setToCreatedTimestamp(CREATION);
        object.setToDeleted(false);
        object.setToId(TO_ID);

        Assert.assertEquals(object.getCreationTimestamp(), CREATION);
        Assert.assertEquals(object.getDate(), DATE);
        Assert.assertTrue(object.isDeleted());
        Assert.assertEquals(object.getFromAccountId(), ACOUNT_FROM_ID);
        Assert.assertTrue(object.getFromAmount().eq(AMOUNT.negate()));
        Assert.assertEquals(object.getFromClearedTimestamp(), EntryEntity.NOT_CLEARED_TIMESTAMP);
        Assert.assertFalse(object.isFromCleared());
        Assert.assertEquals(object.getFromCreatedTimestamp(), CREATION);
        Assert.assertFalse(object.isFromDeleted());
        Assert.assertEquals(object.getFromId(), FROM_ID);
        Assert.assertEquals(object.getId(), ID);
        Assert.assertEquals(object.getNarrative(), NARRATIVE);
        Assert.assertEquals(object.getReference(), REFERENCE);
        Assert.assertEquals(object.getToAccountId(), ACOUNT_TO_ID);
        Assert.assertTrue(object.getToAmount().eq(AMOUNT));
        Assert.assertEquals(object.getToCreatedTimestamp(), CREATION);
        Assert.assertFalse(object.isToDeleted());
        Assert.assertEquals(object.getToId(), TO_ID);
        object.toString();
        //        System.out.println(object);

        object.setFromCleared(true);
        Assert.assertFalse(
            object.getFromClearedTimestamp().equals(EntryEntity.NOT_CLEARED_TIMESTAMP));

        object.setToCleared(true);
        Assert.assertFalse(
            object.getToClearedTimestamp().equals(EntryEntity.NOT_CLEARED_TIMESTAMP));
    }

    @Test
    public void setClearedTimestamp() {
        TransactionEntitySelect object = new TransactionEntitySelect();
        object.setFromCleared(false);
        Assert.assertFalse(object.isFromCleared());
        Assert.assertEquals(object.getFromClearedTimestamp(), EntryEntity.NOT_CLEARED_TIMESTAMP);
        object.setToCleared(false);
        Assert.assertFalse(object.isToCleared());
        Assert.assertEquals(object.getToClearedTimestamp(), EntryEntity.NOT_CLEARED_TIMESTAMP);

        object.setFromClearedTimestamp(CREATION);
        Assert.assertEquals(object.getFromClearedTimestamp(), CREATION);
        Assert.assertTrue(object.isFromCleared());

        object.setToClearedTimestamp(CREATION);
        Assert.assertEquals(object.getToClearedTimestamp(), CREATION);
        Assert.assertTrue(object.isToCleared());
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "The amount for the 'FROM' account must be a minus. Value: 123456.00")
    public void setFromMoneyException() {
        TransactionEntitySelect object = new TransactionEntitySelect();
        object.setFromAmount(AMOUNT);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "The amount for the 'TO' account must be plus. Value: -123456.00")
    public void setToMoneyException() {
        TransactionEntitySelect object = new TransactionEntitySelect();
        object.setToAmount(AMOUNT.negate());
    }
}
