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

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.veary.debs.core.facade.RealAccountFacade;
import org.veary.debs.dao.AccountDao;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Account.Types;
import org.veary.debs.tests.GuicePersistTestModule;

import hthurow.tomcatjndi.TomcatJNDI;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class AccountFacadeTest {

    private static final String NAME = "Fuel";
    private static final String DESC = "Desc";
    private static final Long PARENT_ID = Long.valueOf(2);

    private TomcatJNDI tomcatJndi;
    private Injector injector;
    private AccountDao dao;

    @BeforeClass
    public void setUp() {
        final File contextXml = new File("src/test/resources/context.xml");
        this.tomcatJndi = new TomcatJNDI();
        this.tomcatJndi.processContextXml(contextXml);
        this.tomcatJndi.start();
        this.injector = Guice.createInjector(new GuicePersistTestModule());
        this.dao = this.injector.getInstance(AccountDao.class);
    }

    @AfterClass
    public void teardown() {
        this.tomcatJndi.tearDown();
    }

    @Test
    public void instantiation() {
        Assert.assertNotNull(new RealAccountFacade(this.dao));
    }

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp = "The Account object does not have a parent set!")
    public void createMethodNoParentId() {
        AccountFacade facade = new RealAccountFacade(this.dao);
        Assert.assertNotNull(facade);

        Account object = Account.newInstance(NAME, DESC, Long.valueOf(0), Types.EXPENSE);
        facade.create(object);
    }

    @Test
    public void createMethod() {
        AccountFacade facade = new RealAccountFacade(this.dao);
        Assert.assertNotNull(facade);

        Account object = Account.newInstance(NAME, DESC, PARENT_ID, Types.EXPENSE);

        Long id = facade.create(object);
        Assert.assertNotNull(id);
    }
}
