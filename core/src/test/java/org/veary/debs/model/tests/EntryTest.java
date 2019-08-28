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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.AccountEntity;
import org.veary.debs.core.model.EntryEntity;
import org.veary.debs.model.Account;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Entry.Fields;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class EntryTest {

    private AccountEntity fromAccount;
    private AccountEntity toAccount;

    private static final Long FROM_ACC_ID = Long.valueOf(10);
    private static final Long TO_ACC_ID = Long.valueOf(11);

    @BeforeClass
    public void setUp() {
        this.fromAccount = new AccountEntity("Cash", "Test", Long.valueOf(3), //$NON-NLS-1$ //$NON-NLS-2$
            Account.Types.ASSET);
        this.fromAccount.setId(FROM_ACC_ID);
        this.toAccount = new AccountEntity("Fuel", "Test", Long.valueOf(7), //$NON-NLS-1$ //$NON-NLS-2$
            Account.Types.EXPENSE);
        this.toAccount.setId(TO_ACC_ID);
    }

    private static final Entry.Types FROM = Entry.Types.FROM;
    private static final Entry.Types TO = Entry.Types.TO;
    private static final Money AMOUNT = new Money(BigDecimal.valueOf(1000));

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp = "A 'FROM' entry must have a negative amount. Actual: 1000.00")
    public void instantiationDefault_BadFromAmountException() {
        EntryEntity entity = (EntryEntity) Entry.newInstance(FROM, this.fromAccount);
        entity.setAmount(AMOUNT);
    }

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp = "A 'TO' entry must have a positive amount. Actual: -1000.00")
    public void instantiationDefault_BadToAmountException() {
        EntryEntity entity = (EntryEntity) Entry.newInstance(TO, this.toAccount);
        entity.setAmount(AMOUNT.negate());
    }

    private static final Long SET_ACC_ID = Long.valueOf(101);
    private static final LocalDateTime SET_CLEARED_TS = LocalDateTime.now();

    @Test
    public void instantiationFromDefault() {
        Entry object = Entry.newInstance(FROM, this.fromAccount);
        Assert.assertNotNull(object);
        Assert.assertFalse(object.isCleared());
        Assert.assertEquals(object.getClearedTimestamp(), EntryEntity.NOT_CLEARED_TIMESTAMP);
        Assert.assertTrue(object.getType().equals(FROM));
        Assert.assertEquals(object.getAccountId(), FROM_ACC_ID);

        ((EntryEntity) object).setAccountId(SET_ACC_ID);
        Assert.assertEquals(object.getAccountId(), SET_ACC_ID);
        ((EntryEntity) object).setType(TO);
        Assert.assertTrue(object.getType().equals(TO));
        ((EntryEntity) object).setClearedTimestamp(SET_CLEARED_TS);
        Assert.assertEquals(object.getClearedTimestamp(), SET_CLEARED_TS);
        Assert.assertTrue(object.isCleared());
    }

    @Test
    public void instantiationToDefault() {
        Entry object = Entry.newInstance(TO, this.toAccount);
        Assert.assertNotNull(object);
        Assert.assertFalse(object.isCleared());
        Assert.assertEquals(object.getClearedTimestamp(), EntryEntity.NOT_CLEARED_TIMESTAMP);
        Assert.assertTrue(object.getType().equals(TO));
        Assert.assertEquals(object.getAccountId(), TO_ACC_ID);
    }

    private static final Long REAL_ID = Long.valueOf(2);
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    private static final Timestamp REAL_CREATION = Timestamp.valueOf(LOCAL_DATE_TIME);
    private static final Timestamp REAL_CLEARED_TS = Timestamp.valueOf(LOCAL_DATE_TIME);

    @Test
    public void instantiationToDataMap() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Fields.ID.toString(), REAL_ID);
        dataMap.put(Fields.CREATED.toString(), REAL_CREATION);
        dataMap.put(Fields.DELETED.toString(), Boolean.FALSE);
        dataMap.put(Fields.AMOUNT.toString(), AMOUNT.getValue());
        dataMap.put(Fields.ETYPE.toString(), Entry.Types.TO.getId());
        dataMap.put(Fields.ACCOUNT_ID.toString(), this.toAccount.getId());
        dataMap.put(Fields.CLEARED.toString(), Boolean.TRUE);
        dataMap.put(Fields.CLEARED_TS.toString(), REAL_CLEARED_TS);

        Entry object = Entry.newInstance(dataMap);
        Assert.assertNotNull(object);

        Assert.assertEquals(object.getId(), REAL_ID);
        Assert.assertNotNull(object.getCreationTimestamp());
        Assert.assertFalse(object.isDeleted());
        Assert.assertTrue(object.isCleared());
        Assert.assertTrue(object.getAmount().eq(AMOUNT));
        Assert.assertEquals(object.getType(), Entry.Types.TO);
        Assert.assertEquals(object.getAccountId(), this.toAccount.getId());
        //        Assert.assertEquals(object.getClearedTimestamp(), REAL_CLEARED_TS);
    }

    @Test
    public void Types() {
        Integer id = Entry.Types.FROM.getId();
        Assert.assertNotNull(id);
        Entry.Types type = Entry.Types.getType(id);
        Assert.assertEquals(type, Entry.Types.FROM);

        id = Entry.Types.TO.getId();
        Assert.assertNotNull(id);
        type = Entry.Types.getType(id);
        Assert.assertEquals(type, Entry.Types.TO);
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Unknown Entry Type: 20")
    public void TypesException() {
        Entry.Types.getType(Integer.valueOf(20));
    }
}
