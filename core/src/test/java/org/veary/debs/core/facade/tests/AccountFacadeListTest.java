package org.veary.debs.core.facade.tests;

import java.util.List;
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.model.Account;
import org.veary.debs.tests.JndiTestBase;
import org.veary.tree.TreeNode;

public final class AccountFacadeListTest extends JndiTestBase {

    private Long groupA;
    private Long groupAA;

    @Test
    public void getAllAccountsMethod() {
        this.groupA = this.accountFacade
            .create(Account.newInstance("Group A", "Group A Desc", Long.valueOf(1), //$NON-NLS-1$ //$NON-NLS-2$
                Account.Types.ASSETS_GROUP));
        this.groupAA = this.accountFacade
            .create(Account.newInstance("Group A-A", "Group A-A Desc", this.groupA, //$NON-NLS-1$ //$NON-NLS-2$
                Account.Types.ASSETS_GROUP));
        this.accountFacade.create(
            Account.newInstance("Group A-B", "Group A-B Desc", this.groupA, //$NON-NLS-1$//$NON-NLS-2$
                Account.Types.ASSETS_GROUP));

        List<Account> list = this.accountFacade.getAllAccounts(false);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 11);
    }

    @Test(dependsOnMethods = { "getAllAccountsMethod" })
    public void getAllAccountsIncludeMethod() {
        List<Account> list = this.accountFacade.getAllAccounts(false);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 11);

        Optional<Account> result = this.accountFacade.getByName("Group A-B"); //$NON-NLS-1$
        Assert.assertFalse(result.isEmpty());
        this.accountFacade.update(result.get(), null, null, null, null, true);

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
            .create(
                Account.newInstance("Cash", "Cash Account", this.groupAA, Account.Types.ASSET)); //$NON-NLS-1$ //$NON-NLS-2$
        List<Account> list = this.accountFacade.getAllAccounts(false);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 11);

        list = this.accountFacade.getActualAccounts(false);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 1);

        Optional<Account> result = this.accountFacade.getById(id);
        Assert.assertFalse(result.isEmpty());
        this.accountFacade.update(result.get(), null, null, null, null, true);

        list = this.accountFacade.getActualAccounts(false);
        Assert.assertFalse(list.isEmpty());

        list = this.accountFacade.getActualAccounts(true);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == 1);
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

    @Test(dependsOnMethods = { "getGroupAccountsMethod" })
    public void getChartOfAccountsMethod() {
        TreeNode<Account> chart = this.accountFacade.getChartOfAccounts();
        Assert.assertNotNull(chart);

        for (TreeNode<Account> node : chart) {
            int level = node.getLevel();
            if (level > 0) {
                System.out.println(
                    String.format("%" + node.getLevel() + "s", " ") + node.getData().getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            } else {
                System.out.println(node.getData().getName());
            }
        }
    }
}
