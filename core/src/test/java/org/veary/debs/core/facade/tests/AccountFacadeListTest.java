package org.veary.debs.core.facade.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.model.Account;
import org.veary.debs.tests.JndiTestBase;

public final class AccountFacadeListTest extends JndiTestBase {

	@Test
	public void getAllAccountsIncludeMethod_Empty() {
		List<Account> list = this.accountFacade.getAllAccounts(true);
		Assert.assertTrue(list.isEmpty());
	}
}
