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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.dao.RealAccountDao;
import org.veary.debs.core.model.AccountEntity;
import org.veary.debs.model.Account;
import org.veary.debs.model.Account.Types;
import org.veary.debs.tests.JndiTestBase;

/**
 * <b>Purpose:</b> Test the {@link RealAccountDao}.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class AccountDaoTest extends JndiTestBase {

	private static final String NAME = "Fuel";
	private static final String DESC = "Desc";
	private static final Long PARENT_ID = Long.valueOf(0);

	private static final String UPDATED_NAME = "Updated Fuel";
	private static final String UPDATED_DESC = "Updated Desc";
	private static final Long UPDATED_PARENT_ID = Long.valueOf(4);
	private static final Money UPDATED_BALANCE = new Money(BigDecimal.valueOf(100000000));

	@Test
	public void createMethod() {
		Account object = Account.newInstance(NAME, DESC, PARENT_ID, Types.EXPENSE);
		Long id = this.accountDao.createAccount(object);
		Assert.assertNotNull(id);

		Account fetched = this.accountDao.getAccountById(id);
		Assert.assertNotNull(fetched);

		Assert.assertEquals(id, fetched.getId());
		Assert.assertEquals(NAME, fetched.getName());
		Assert.assertEquals(DESC, fetched.getDescription());
		Assert.assertEquals(PARENT_ID, fetched.getParentId());
		Assert.assertEquals(Types.EXPENSE, fetched.getType());
		Assert.assertFalse(fetched.isDeleted());
		Assert.assertNotNull(fetched.getCreationTimestamp());
		Assert.assertTrue(fetched.getBalance().eq(new Money(BigDecimal.ZERO.setScale(2))));
	}

	@Test(dependsOnMethods = { "createMethod" })
	public void updateMethod() {
		Account original = this.accountDao.getAccountByName(NAME);
		Assert.assertNotNull(original);

		AccountEntity updated = new AccountEntity(original);
		Assert.assertNotNull(updated);
		updated.setName(UPDATED_NAME);
		updated.setDescription(UPDATED_DESC);
		updated.setType(Types.GROUP);
		updated.setParentId(UPDATED_PARENT_ID);

		// Field which cannot be updated with the updateAccount method
		LocalDateTime updatedCreation = LocalDateTime.now();
		updated.setId(Long.valueOf(1000));
		updated.setBalance(UPDATED_BALANCE);
		updated.setDeleted(Boolean.TRUE);
		updated.setCreationTimestamp(updatedCreation);

		this.accountDao.updateAccount(original, updated);

		Account fetched = this.accountDao.getAccountByName(UPDATED_NAME);
		Assert.assertNotNull(fetched);
		Assert.assertFalse(fetched.getId().equals(Long.valueOf(1000)));
		Assert.assertFalse(fetched.getBalance().eq(UPDATED_BALANCE));
		Assert.assertFalse(fetched.isDeleted());
		Assert.assertFalse(fetched.getCreationTimestamp().equals(updatedCreation));
	}

	@Test(dependsOnMethods = { "updateMethod" })
	public void updateBalanceMethod() {
		Account account = this.accountDao.getAccountByName(UPDATED_NAME);
		Assert.assertNotNull(account);

		this.accountDao.updateAccountBalance(account, UPDATED_BALANCE);

		Account updated = this.accountDao.getAccountById(account.getId());
		Assert.assertNotNull(updated);
		Assert.assertTrue(updated.getBalance().eq(UPDATED_BALANCE));
	}

	@Test(dependsOnMethods = { "updateBalanceMethod" })
	public void deleteAccount() {
		Account account = this.accountDao.getAccountByName(UPDATED_NAME);
		Assert.assertNotNull(account);
		this.accountDao.deleteAccount(account);
	}
}
