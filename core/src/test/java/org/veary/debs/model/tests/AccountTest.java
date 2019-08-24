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
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.AccountEntity;
import org.veary.debs.model.Account;
import org.veary.debs.model.Account.Types;

/**
 *
 * <b>Purpose:</b> Tests the {@code Account} and {@code AccountEntity} from the perspective of
 * the database.
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class AccountTest {

    private static final Long DEFAULT_ID = Long.valueOf(0);
    private static final Long REAL_ID = Long.valueOf(2);
    private static final Timestamp REAL_CREATION = new Timestamp(System.currentTimeMillis());
    private static final BigDecimal REAL_AMOUNT = BigDecimal.valueOf(100000);
    private static final Long REAL_PARENT_ID = Long.valueOf(1);

    private static final Long UPDATED_ID = Long.valueOf(20);
    private static final String UPDATED_NAME = "Cash Book";
    private static final String UPDATED_DESCRIPTION = "Updated Description";
    private static final Long UPDATED_PARENT_ID = Long.valueOf(7);

    private static final String REAL_NAME = "Cash";
    private static final String REAL_DESC = "Description";
    private static final BigDecimal DEFAULT_BALANCE = BigDecimal.ZERO;

    @Test
    public void instantiationDefault() {
        Account object = Account.newInstance(REAL_NAME, REAL_DESC, REAL_PARENT_ID,
            Account.Types.ASSET);
        Assert.assertNotNull(object);
        Assert.assertEquals(object.getName(), REAL_NAME);
        Assert.assertEquals(object.getDescription(), REAL_DESC);
        Assert.assertEquals(object.getType(), Account.Types.ASSET);
        Assert.assertEquals(object.getId(), DEFAULT_ID);
        Assert.assertFalse(object.isDeleted());
        Assert.assertNotNull(object.getCreationTimestamp());
        Assert.assertTrue(object.getBalance().getValue().equals(DEFAULT_BALANCE.setScale(2)));
        Assert.assertEquals(object.getParentId(), REAL_PARENT_ID);

        AccountEntity entity = (AccountEntity) object;
        Assert.assertNotNull(entity);
        Assert.assertEquals(entity.getName(), REAL_NAME);
        Assert.assertEquals(entity.getDescription(), REAL_DESC);
        Assert.assertEquals(entity.getType(), Account.Types.ASSET);
        Assert.assertEquals(entity.getId(), DEFAULT_ID);
        Assert.assertFalse(entity.isDeleted());
        Assert.assertEquals(entity.getCreationTimestamp(), object.getCreationTimestamp());
        Assert.assertTrue(LocalDateTime.class.isInstance(entity.getCreationTimestamp()));
        Assert.assertTrue(entity.getBalance().getValue().equals(DEFAULT_BALANCE.setScale(2)));
        Assert.assertEquals(entity.getParentId(), REAL_PARENT_ID);
    }

    @Test
    public void instantiationDataMap() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Account.Fields.ID.toString(), REAL_ID);
        dataMap.put(Account.Fields.CREATED.toString(), REAL_CREATION);
        dataMap.put(Account.Fields.BALANCE.toString(), REAL_AMOUNT);
        dataMap.put(Account.Fields.DELETED.toString(), Boolean.FALSE);
        dataMap.put(Account.Fields.NAME.toString(), REAL_NAME);
        dataMap.put(Account.Fields.DESCRIPTION.toString(), REAL_DESC);
        dataMap.put(Account.Fields.PARENT_ID.toString(), REAL_PARENT_ID);
        dataMap.put(Account.Fields.ACCOUNT_TYPE.toString(), Account.Types.ASSET.getId());

        Account object = Account.newInstance(dataMap);
        Assert.assertNotNull(object);
    }

    @Test
    public void setters() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Account.Fields.ID.toString(), REAL_ID);
        dataMap.put(Account.Fields.CREATED.toString(), REAL_CREATION);
        dataMap.put(Account.Fields.BALANCE.toString(), DEFAULT_BALANCE);
        dataMap.put(Account.Fields.DELETED.toString(), Boolean.FALSE);
        dataMap.put(Account.Fields.NAME.toString(), REAL_NAME);
        dataMap.put(Account.Fields.DESCRIPTION.toString(), REAL_DESC);
        dataMap.put(Account.Fields.PARENT_ID.toString(), REAL_PARENT_ID);
        dataMap.put(Account.Fields.ACCOUNT_TYPE.toString(), Account.Types.ASSET.getId());

        Account object = Account.newInstance(dataMap);

        AccountEntity entity = (AccountEntity) object;
        Assert.assertNotNull(entity);
        Assert.assertEquals(entity.getName(), REAL_NAME);
        Assert.assertEquals(entity.getDescription(), REAL_DESC);
        Assert.assertEquals(entity.getType(), Account.Types.ASSET);
        Assert.assertEquals(entity.getId(), REAL_ID);
        Assert.assertFalse(entity.isDeleted());
        Assert.assertEquals(entity.getCreationTimestamp(), object.getCreationTimestamp());
        Assert.assertTrue(LocalDateTime.class.isInstance(entity.getCreationTimestamp()));
        Assert.assertTrue(entity.getBalance().getValue().equals(DEFAULT_BALANCE.setScale(2)));
        Assert.assertEquals(entity.getParentId(), REAL_PARENT_ID);

        entity.setId(UPDATED_ID);
        Assert.assertEquals(entity.getId(), UPDATED_ID);
        entity.setBalance(new Money(REAL_AMOUNT));
        Assert.assertTrue(entity.getBalance().getValue().equals(REAL_AMOUNT.setScale(2)));
        entity.setName(UPDATED_NAME);
        Assert.assertEquals(entity.getName(), UPDATED_NAME);
        entity.setDescription(UPDATED_DESCRIPTION);
        Assert.assertEquals(entity.getDescription(), UPDATED_DESCRIPTION);
        entity.setParentId(UPDATED_PARENT_ID);
        Assert.assertEquals(entity.getParentId(), UPDATED_PARENT_ID);
        entity.setType(Types.EXPENSE);
        Assert.assertEquals(entity.getType(), Types.EXPENSE);
    }

    @Test
    public void Types() {
        Integer id = Account.Types.ASSET.getId();
        Assert.assertNotNull(id);
        Account.Types type = Account.Types.getType(id);
        Assert.assertEquals(type, Account.Types.ASSET);

        id = Account.Types.EXPENSE.getId();
        Assert.assertNotNull(id);
        type = Account.Types.getType(id);
        Assert.assertEquals(type, Account.Types.EXPENSE);

        id = Account.Types.LIABILITY.getId();
        Assert.assertNotNull(id);
        type = Account.Types.getType(id);
        Assert.assertEquals(type, Account.Types.LIABILITY);

        id = Account.Types.INCOME.getId();
        Assert.assertNotNull(id);
        type = Account.Types.getType(id);
        Assert.assertEquals(type, Account.Types.INCOME);

        id = Account.Types.EQUITY.getId();
        Assert.assertNotNull(id);
        type = Account.Types.getType(id);
        Assert.assertEquals(type, Account.Types.EQUITY);

        id = Account.Types.RETAINED_EARNINGS.getId();
        Assert.assertNotNull(id);
        type = Account.Types.getType(id);
        Assert.assertEquals(type, Account.Types.RETAINED_EARNINGS);

        id = Account.Types.CONTROL.getId();
        Assert.assertNotNull(id);
        type = Account.Types.getType(id);
        Assert.assertEquals(type, Account.Types.CONTROL);

        id = Account.Types.GROUP.getId();
        Assert.assertNotNull(id);
        type = Account.Types.getType(id);
        Assert.assertEquals(type, Account.Types.GROUP);
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Unknown Account Type: 20")
    public void TypesException() {
        Account.Types.getType(Integer.valueOf(20));
    }

    @Test(
        expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Parameter 'name' cannot be null")
    public void createMethodNullNameException() {
        Account.newInstance(null, REAL_DESC, REAL_PARENT_ID, Types.EXPENSE);
    }

    @Test(
        expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Parameter 'description' cannot be null")
    public void createMethodNullDescriptionException() {
        Account.newInstance(REAL_NAME, null, REAL_PARENT_ID, Types.EXPENSE);
    }

    @Test(
        expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Parameter 'parentId' cannot be null")
    public void createMethodNullParentIdException() {
        Account.newInstance(REAL_NAME, REAL_DESC, null, Types.EXPENSE);
    }

    @Test(
        expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Parameter 'type' cannot be null")
    public void createMethodNullTypeException() {
        Account.newInstance(REAL_NAME, REAL_DESC, REAL_PARENT_ID, null);
    }
}
