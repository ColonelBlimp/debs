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
import org.veary.debs.core.facade.RealAccountFacade;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Account.Types;
import org.veary.debs.tests.JndiTestBase;

/**
 * <b>Purpose:</b> ?
 *
 * <p> <b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class AccountFacadeTest extends JndiTestBase {

    private static final String NAME = "Fuel"; //$NON-NLS-1$
    private static final String DESC = "Desc"; //$NON-NLS-1$
    private static final Long PARENT_ID = Long.valueOf(2);

    @Test
    public void instantiation() {
        Assert.assertNotNull(new RealAccountFacade(this.accountDao));
    }

    @Test(expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp = "The Account object does not have a parent set!")
    public void createMethodNoParentId() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);
        Account object = Account.newInstance(NAME, DESC, Long.valueOf(0), Types.EXPENSE);
        facade.create(object);
    }

    @Test
    public void createMethod() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);
        Account object = Account.newInstance(NAME, DESC, PARENT_ID, Types.EXPENSE);
        Long id = facade.create(object);
        Assert.assertNotNull(id);

        Optional<Account> result = facade.getById(id);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void getByIdMethod() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);
        Optional<Account> result = facade.getById(Long.valueOf(200000));
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void getByNameMethod() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);
        Optional<Account> result = facade.getByName("Unknown Name"); //$NON-NLS-1$
        Assert.assertTrue(result.isEmpty());
    }

    private static final String NEW_NAME = "Car Fuel"; //$NON-NLS-1$
    private static final String NEW_DESC = "Fuel for a car"; //$NON-NLS-1$
    private static final Long NEW_PARENT_ID = Long.valueOf(8);

    @Test(dependsOnMethods = { "createMethod" })
    public void updateMethod_Name() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);

        Optional<Account> result = facade.getByName(NAME);
        Assert.assertFalse(result.isEmpty());
        Account original = result.get();

        facade.update(original, NEW_NAME, null, null, null, false);

        result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Account updated = result.get();
        Assert.assertEquals(updated.getName(), NEW_NAME);
        Assert.assertEquals(updated.getDescription(), DESC);
        Assert.assertTrue(updated.getParentId().equals(PARENT_ID));
        Assert.assertTrue(updated.getType().equals(Types.EXPENSE));
    }

    @Test(dependsOnMethods = { "createMethod", "updateMethod_Name" })
    public void updateMethod_Desc() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);

        Optional<Account> result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Account original = result.get();

        facade.update(original, null, NEW_DESC, null, null, false);

        result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Account updated = result.get();
        Assert.assertEquals(updated.getName(), NEW_NAME);
        Assert.assertEquals(updated.getDescription(), NEW_DESC);
        Assert.assertTrue(updated.getParentId().equals(PARENT_ID));
        Assert.assertTrue(updated.getType().equals(Types.EXPENSE));
    }

    @Test(dependsOnMethods = { "createMethod", "updateMethod_Name" })
    public void updateMethod_Parent() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);

        Optional<Account> result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Account original = result.get();

        facade.update(original, null, null, NEW_PARENT_ID, null, false);

        result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Account updated = result.get();
        Assert.assertEquals(updated.getName(), NEW_NAME);
        Assert.assertEquals(updated.getDescription(), NEW_DESC);
        Assert.assertTrue(updated.getParentId().equals(NEW_PARENT_ID));
        Assert.assertTrue(updated.getType().equals(Types.EXPENSE));
    }

    @Test(dependsOnMethods = { "createMethod", "updateMethod_Name" })
    public void updateMethod_Type() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);

        Optional<Account> result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Account original = result.get();

        facade.update(original, null, null, null, Types.GROUP, false);

        result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Account updated = result.get();
        Assert.assertEquals(updated.getName(), NEW_NAME);
        Assert.assertEquals(updated.getDescription(), NEW_DESC);
        Assert.assertTrue(updated.getParentId().equals(NEW_PARENT_ID));
        Assert.assertTrue(updated.getType().equals(Types.GROUP));
    }

    private static final Money MONEY = new Money(BigDecimal.valueOf(100000));

    @Test(dependsOnMethods = { "updateMethod_Type" })
    public void updateBalanceMethod() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);

        Optional<Account> result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.get().getBalance().eq(new Money(BigDecimal.valueOf(0.00))));

        facade.updateBalance(result.get(), MONEY);

        result = facade.getByName(NEW_NAME);
        Assert.assertFalse(result.isEmpty());
        Assert.assertTrue(result.get().getBalance().eq(MONEY));
    }
}
