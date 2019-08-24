package org.veary.debs.tests;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.veary.debs.core.facade.RealAccountFacade;
import org.veary.debs.dao.AccountDao;
import org.veary.debs.facade.AccountFacade;

import com.google.inject.Guice;
import com.google.inject.Injector;

import hthurow.tomcatjndi.TomcatJNDI;

public abstract class JndiTestBase {

	protected TomcatJNDI tomcatJndi;
	protected Injector injector;
	protected AccountDao accountDao;
	protected AccountFacade accountFacade;

	@BeforeClass
	public void setUp() {
		final File contextXml = new File("src/test/resources/context.xml");
		this.tomcatJndi = new TomcatJNDI();
		this.tomcatJndi.processContextXml(contextXml);
		this.tomcatJndi.start();
		this.injector = Guice.createInjector(new GuicePersistTestModule());
		this.accountDao = this.injector.getInstance(AccountDao.class);
		this.accountFacade = this.injector.getInstance(RealAccountFacade.class);
	}

	@AfterClass
	public void teardown() {
		this.tomcatJndi.tearDown();
	}
}
