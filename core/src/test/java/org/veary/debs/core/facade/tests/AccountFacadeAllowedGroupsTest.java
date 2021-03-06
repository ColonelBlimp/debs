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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.facade.RealAccountFacade;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Account.Types;
import org.veary.debs.tests.JndiTestBase;

public class AccountFacadeAllowedGroupsTest extends JndiTestBase {

    @Test
    public void expenseAccountGroups() {
        AccountFacade facade = new RealAccountFacade(this.accountDao);
        Assert.assertNotNull(facade);
        Account object = Account.newInstance("Expense", "Expense", Long.valueOf(2),
            Types.EXPENSE);
        facade.create(object);
    }
}
