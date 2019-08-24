package org.veary.debs.core.facade.tests;

import java.util.List;
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.model.Account;
import org.veary.debs.tests.JndiTestBase;

public final class AccountFacadeListTest extends JndiTestBase {

	@Test
	public void getAllAccountsMethod() {
		Long groupAId = this.accountFacade
				.create(Account.newInstance("Group A", "Group A Desc", Long.valueOf(1), Account.Types.GROUP));
		this.accountFacade.create(Account.newInstance("Group A-A", "Group A-A Desc", groupAId, Account.Types.GROUP));
		this.accountFacade.create(Account.newInstance("Group A-B", "Group A-B Desc", groupAId, Account.Types.GROUP));

		List<Account> list = this.accountFacade.getAllAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 4);
	}

	@Test(dependsOnMethods = { "getAllAccountsMethod" })
	public void getAllAccountsIncludeMethod() {
		List<Account> list = this.accountFacade.getAllAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 4);

		Optional<Account> result = this.accountFacade.getByName("Group A-B");
		Assert.assertFalse(result.isEmpty());
		this.accountFacade.delete(result.get());

		list = this.accountFacade.getAllAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 3);

		list = this.accountFacade.getAllAccounts(true);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 4);
	}
}
