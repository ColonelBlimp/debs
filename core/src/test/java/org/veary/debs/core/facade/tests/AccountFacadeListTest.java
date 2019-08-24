package org.veary.debs.core.facade.tests;

import java.util.List;
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.model.Account;
import org.veary.debs.tests.JndiTestBase;

public final class AccountFacadeListTest extends JndiTestBase {

	private Long groupA;
	private Long groupAA;

	@Test
	public void getAllAccountsMethod() {
		this.groupA = this.accountFacade
				.create(Account.newInstance("Group A", "Group A Desc", Long.valueOf(1), Account.Types.GROUP));
		this.groupAA = this.accountFacade
				.create(Account.newInstance("Group A-A", "Group A-A Desc", this.groupA, Account.Types.GROUP));
		this.accountFacade.create(Account.newInstance("Group A-B", "Group A-B Desc", this.groupA, Account.Types.GROUP));

		List<Account> list = this.accountFacade.getAllAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 11);
	}

	@Test(dependsOnMethods = { "getAllAccountsMethod" })
	public void getAllAccountsIncludeMethod() {
		List<Account> list = this.accountFacade.getAllAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 11);

		Optional<Account> result = this.accountFacade.getByName("Group A-B");
		Assert.assertFalse(result.isEmpty());
		this.accountFacade.delete(result.get());

		list = this.accountFacade.getAllAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 10);

		list = this.accountFacade.getAllAccounts(true);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 11);
	}

	@Test(dependsOnMethods = { "getAllAccountsIncludeMethod" })
	public void getActualAccountsMethod() {
		Long id = this.accountFacade
				.create(Account.newInstance("Cash", "Cash Account", this.groupAA, Account.Types.ASSET));
		List<Account> list = this.accountFacade.getAllAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 11);

		list = this.accountFacade.getActualAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 2);

		Optional<Account> result = this.accountFacade.getById(id);
		Assert.assertFalse(result.isEmpty());
		this.accountFacade.delete(result.get());

		list = this.accountFacade.getActualAccounts(false);
		Assert.assertFalse(list.isEmpty());

		list = this.accountFacade.getActualAccounts(true);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 2);
	}

	@Test(dependsOnMethods = { "getActualAccountsMethod" })
	public void getGroupAccountsMethod() {
		List<Account> list = this.accountFacade.getGroupAccounts(false);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 9);

		list = this.accountFacade.getGroupAccounts(true);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 10);

		list = this.accountFacade.getAllAccounts(true);
		Assert.assertFalse(list.isEmpty());
		Assert.assertTrue(list.size() == 12);
	}
}
