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

package org.veary.debs.core.dao.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
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
public class AccountDaoListTest extends JndiTestBase {

    //    private static final Long DEFAULT_ID = Long.valueOf(0);

    @Test
    public void getAllAccountsMethod() {
        final List<Account> list = this.accountDao.getAllAccounts(false);
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 9);
    }

    @Test(dependsOnMethods = { "getAllAccountsMethod" })
    public void getAllAccountsIncludeDeleteMethod() {
        final List<Account> list = this.accountDao.getAllAccounts(true);
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 9);
    }

    @Test(dependsOnMethods = { "getAllAccountsMethod" })
    public void getActualAcccountsMethod() {
        final List<Account> list = this.accountDao.getActualAccounts(false);
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 2);
    }

    @Test(dependsOnMethods = { "getAllAccountsMethod" })
    public void getActualAccountsIncludeDeletedMethod() {
        final List<Account> list = this.accountDao.getActualAccounts(true);
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 2);
    }

    @Test(dependsOnMethods = { "getAllAccountsMethod" })
    public void getGroupAccountsMethod() {
        final List<Account> list = this.accountDao.getGroupAccounts(false);
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 7);
    }

    @Test(dependsOnMethods = { "getAllAccountsMethod" })
    public void getGroupAccountsIncludeDeletedMethod() {
        final List<Account> list = this.accountDao.getGroupAccounts(true);
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 7);
    }

    /*
    private static final String DESC = "In-build"; //$NON-NLS-1$

    @Test(dependsOnMethods = { "getAllAccountsMethod", "getAllAccountsIncludeDeleteMethod" })
    public void listResults() {
        Account object = Account.newInstance("BALANCE", DESC, DEFAULT_ID, //$NON-NLS-1$
            Types.BALANCE_GROUP);
        Long balanceId = this.accountDao.createAccount(object);
        object = Account.newInstance("NET WORTH", DESC, balanceId, Types.NETWORTH_GROUP); //$NON-NLS-1$
        Long netWorthId = this.accountDao.createAccount(object);

        Assert.assertTrue(this.accountDao.getGroupAccounts(false).size() == 9);
        object = this.accountDao.getAccountById(netWorthId);
        Assert.assertTrue(this.accountDao.getGroupAccounts(false).size() == 8);
    }
    */

    @Test(dependsOnMethods = { "getAllAccountsMethod", "getAllAccountsIncludeDeleteMethod" })
    public void accountsByTypeAsset() {
        List<Account> assetAccounts = this.accountDao.getAccountsByType(Types.ASSETS_GROUP);
        Assert.assertNotNull(assetAccounts);
        Assert.assertFalse(assetAccounts.isEmpty());
        System.out.println(">>> " + assetAccounts.size());
        Assert.assertTrue(assetAccounts.size() == 1);
        final Account account = assetAccounts.get(0);
        Assert.assertTrue(account.getType().equals(Types.ASSETS_GROUP));
    }
}
