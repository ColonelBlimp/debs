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
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.AccountEntity;
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
    private Account toAccount;

    private static final Long FROM_ACC_ID = Long.valueOf(10);

    @BeforeClass
    public void setUp() {
        this.fromAccount = new AccountEntity("Cash", "Test", Long.valueOf(3), //$NON-NLS-1$ //$NON-NLS-2$
            Account.Types.ASSET);
        this.fromAccount.setId(FROM_ACC_ID);
        this.toAccount = new AccountEntity("Fuel", "Test", Long.valueOf(7), //$NON-NLS-1$ //$NON-NLS-2$
            Account.Types.EXPENSE);
    }

    private static final Entry.Types FROM = Entry.Types.FROM;
    private static final Entry.Types TO = Entry.Types.TO;
    private static final Money AMOUNT = new Money(BigDecimal.valueOf(1000));

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp = "A 'FROM' entry must have a negative amount. Actual: 1000.00")
    public void instantiationDefault_BadFromAmountException() {
        Entry.newInstance(FROM, this.fromAccount, AMOUNT, false);
    }

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp = "A 'TO' entry must have a positive amount. Actual: -1000.00")
    public void instantiationDefault_BadToAmountException() {
        Entry.newInstance(TO, this.toAccount, AMOUNT.negate(), false);
    }

    @Test
    public void instantiationDefaultNotCleared() {
        Entry object = Entry.newInstance(FROM, this.fromAccount, AMOUNT.negate(), false);
        Assert.assertNotNull(object);
    }

    @Test
    public void instantiationDefaultCleared() {
        Entry object = Entry.newInstance(FROM, this.fromAccount, AMOUNT.negate(), true);
        Assert.assertNotNull(object);
        Assert.assertTrue(object.isCleared());
        Assert.assertNotNull(object.getClearedTimestamp());
        Assert.assertTrue(object.getType().equals(FROM));
        Assert.assertEquals(object.getAccountId(), FROM_ACC_ID);
        Assert.assertTrue(object.getAmount().eq(AMOUNT.negate()));
    }

    private static final Long REAL_ID = Long.valueOf(2);
    private static final Timestamp REAL_CREATION = new Timestamp(System.currentTimeMillis());
    private static final Timestamp REAL_CLEARED_TS = new Timestamp(System.currentTimeMillis());

    @Test
    public void instantiationToDataMap() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Fields.ID.toString(), REAL_ID);
        dataMap.put(Fields.CREATED.toString(), REAL_CREATION);
        dataMap.put(Fields.DELETED.toString(), Boolean.FALSE);
        dataMap.put(Fields.AMOUNT.toString(), AMOUNT.getValue());
        dataMap.put(Fields.ETYPE.toString(), Entry.Types.TO.getId());
        dataMap.put(Fields.ACCOUNT_ID.toString(), this.toAccount.getId());
        dataMap.put(Fields.CLEARED.toString(), Boolean.FALSE);
        dataMap.put(Fields.CLEARED_TS.toString(), REAL_CLEARED_TS);

        Entry object = Entry.newInstance(dataMap);
        Assert.assertNotNull(object);
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
