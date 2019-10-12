package org.veary.debs.tests;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.veary.debs.core.facade.RealAccountFacade;
import org.veary.debs.core.facade.RealSystemFacade;
import org.veary.debs.dao.AccountDao;
import org.veary.debs.dao.EmployeeDao;
import org.veary.debs.dao.TransactionDao;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.AdminFacade;
import org.veary.debs.facade.SystemFacade;

import hthurow.tomcatjndi.TomcatJNDI;

public abstract class JndiTestBase {

    protected TomcatJNDI tomcatJndi;
    protected Injector injector;
    protected AccountDao accountDao;
    protected AccountFacade accountFacade;
    protected TransactionDao transactionDao;
    protected SystemFacade systemFacade;
    protected AdminFacade adminFacade;
    protected EmployeeDao employeeDao;

    @BeforeClass
    public void setUp() {
        final File contextXml = new File("src/test/resources/context.xml"); //$NON-NLS-1$
        this.tomcatJndi = new TomcatJNDI();
        this.tomcatJndi.processContextXml(contextXml);
        this.tomcatJndi.start();
        this.injector = Guice.createInjector(new GuicePersistTestModule());
        this.accountDao = this.injector.getInstance(AccountDao.class);
        this.accountFacade = this.injector.getInstance(RealAccountFacade.class);
        this.transactionDao = this.injector.getInstance(TransactionDao.class);
        this.systemFacade = this.injector.getInstance(RealSystemFacade.class);
        this.employeeDao = this.injector.getInstance(EmployeeDao.class);
        this.adminFacade = this.injector.getInstance(AdminFacade.class);
        this.adminFacade.initializeDatabase();
    }

    @AfterClass
    public void teardown() {
        this.tomcatJndi.tearDown();
    }
}
