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

import java.math.BigDecimal;
import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.facade.RealSystemFacade;
import org.veary.debs.core.model.EntryEntity;
import org.veary.debs.core.model.TransactionEntity;
import org.veary.debs.model.Account;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;
import org.veary.debs.tests.JndiTestBase;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class SystemFacadeTxUpdateTest extends JndiTestBase {

    @Test
    public void instantiation() {
        Assert.assertNotNull(new RealSystemFacade(this.transactionDao));
    }

    @Test
    public void fromGuice() {
        Assert.assertNotNull(this.systemFacade);
    }

    private Long txId;

    private static final LocalDate DATE = LocalDate.now();
    private static final String NARRATIVE = "Test Narrative"; //$NON-NLS-1$
    private static final String REFERENCE = "Test Reference"; //$NON-NLS-1$
    private static final Money AMOUNT = new Money(BigDecimal.valueOf(100000));

    @Test(dependsOnMethods = { "fromGuice" })
    public void postTransactionMethod() {
        createAccounts();

        Transaction transaction = Transaction.newInstance(DATE, NARRATIVE, REFERENCE, AMOUNT,
            false);
        Entry fromEntry = Entry.newInstance(Entry.Types.FROM, this.fromAccount);
        Entry toEntry = Entry.newInstance(Entry.Types.TO, this.toAccount);

        this.txId = this.systemFacade.postTransaction(transaction, fromEntry, toEntry);
        Assert.assertNotNull(this.txId);
        Assert.assertTrue(this.txId.longValue() > 0L);

        // Check balance on the involved accounts
        Account fromAcc = this.accountDao.getAccountByName(FROM_NAME);
        Assert.assertNotNull(fromAcc);
        Assert.assertNotNull(fromAcc.getBalance());
        Assert.assertTrue(fromAcc.getBalance().isMinus());
        Assert.assertTrue(fromAcc.getBalance().eq(AMOUNT.negate()));

        Account toAcc = this.accountDao.getAccountByName(TO_NAME);
        Assert.assertNotNull(toAcc);
        Assert.assertNotNull(toAcc.getBalance());
        Assert.assertTrue(toAcc.getBalance().isPlus());
        Assert.assertTrue(toAcc.getBalance().eq(AMOUNT));
    }

    @Test(dependsOnMethods = { "postTransactionMethod" })
    public void getTransactionByIdMethod() {
        java.util.Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction object = result.get();
        Assert.assertEquals(object.getDate(), DATE);
        Assert.assertEquals(object.getNarrative(), NARRATIVE);
        Assert.assertEquals(object.getReference(), REFERENCE);
        Assert.assertTrue(object.getToEntry().getAmount().eq(AMOUNT));
        Assert.assertTrue(object.getFromEntry().getAmount().eq(AMOUNT.negate()));

        Assert.assertFalse(((TransactionEntity) object).isCleared());
        Assert.assertFalse(object.getFromEntry().isCleared());
        Assert.assertFalse(object.getToEntry().isCleared());

        //System.out.println(result.get());
    }

    private static final LocalDate UPDATE_DATE = LocalDate.of(2019, 8, 20);
    private static final String UPDATE_NARRATIVE = "Updated Narrative"; //$NON-NLS-1$
    private static final String UPDATE_REFERENCE = "Updated Reference"; //$NON-NLS-1$
    private static final Money UPDATED_AMOUNT = new Money(BigDecimal.valueOf(123456));

    /**
     * Change all the transaction's details except the amount and entries.
     */
    @Test(dependsOnMethods = { "getTransactionByIdMethod" })
    public void updateTxNoEntryChanges() {
        java.util.Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction original = result.get();

        Transaction updated = Transaction.newInstance(UPDATE_DATE, UPDATE_NARRATIVE,
            UPDATE_REFERENCE, AMOUNT, false);

        this.systemFacade.updateTransaction(original, updated,
            new EntryEntity(original.getFromEntry()),
            new EntryEntity(original.getToEntry()));

        result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction fetched = result.get();

        Assert.assertEquals(fetched.getDate(), UPDATE_DATE);
        Assert.assertEquals(fetched.getNarrative(), UPDATE_NARRATIVE);
        Assert.assertEquals(fetched.getReference(), UPDATE_REFERENCE);
        Assert.assertTrue(fetched.getFromEntry().getAmount().eq(AMOUNT.negate()));
        Assert.assertTrue(fetched.getToEntry().getAmount().eq(AMOUNT));

        Account fromAccount = this.accountDao
            .getAccountById(original.getFromEntry().getAccountId());
        Assert.assertTrue(fromAccount.getBalance().eq(AMOUNT.negate()));

        Account toAccount = this.accountDao
            .getAccountById(original.getToEntry().getAccountId());
        Assert.assertTrue(toAccount.getBalance().eq(AMOUNT));
    }

    /**
     * Only change is the amount for the transaction.
     */
    @Test(dependsOnMethods = { "updateTxNoEntryChanges" })
    public void updateTxAmountChanged() {
        java.util.Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction original = result.get();

        Transaction updated = Transaction.newInstance(original.getDate(), original.getNarrative(),
            original.getReference(), UPDATED_AMOUNT, false);

        this.systemFacade.updateTransaction(original, updated,
            new EntryEntity(original.getFromEntry()),
            new EntryEntity(original.getToEntry()));

        Account fromAccount = this.accountDao
            .getAccountById(original.getFromEntry().getAccountId());
        Assert.assertTrue(fromAccount.getBalance().eq(UPDATED_AMOUNT.negate()));
        Account toAccount = this.accountDao
            .getAccountById(original.getToEntry().getAccountId());
        Assert.assertTrue(toAccount.getBalance().eq(UPDATED_AMOUNT));
    }

    /**
     * Only change is the FROM account.
     */
    @Test(dependsOnMethods = { "updateTxAmountChanged" })
    public void updateTxFromAccountChangeOnly() {
        java.util.Optional<Transaction> result = this.systemFacade.getTransactionById(this.txId);
        Assert.assertFalse(result.isEmpty());
        Transaction original = result.get();

        EntryEntity updatedToEntry = new EntryEntity(original.getToEntry());
        EntryEntity updatedFromEntry = new EntryEntity(original.getFromEntry());
        updatedFromEntry.setAccountId(this.updatedFromAccount.getId());
        this.systemFacade.updateTransaction(original, original, updatedFromEntry, updatedToEntry);

        Account toAccount = this.accountDao
            .getAccountById(original.getToEntry().getAccountId());
        Assert.assertTrue(toAccount.getBalance().eq(UPDATED_AMOUNT));

        Account fromAccount = this.accountDao
            .getAccountById(original.getFromEntry().getAccountId());
        Assert.assertTrue(fromAccount.getBalance().eq(new Money(BigDecimal.ZERO)));

        Account otherFromAccount = this.accountDao
            .getAccountById(updatedFromEntry.getAccountId());
        System.out.println("New FROM Account balance: " + otherFromAccount.getBalance());
        //        Assert.assertTrue(otherFromAccount.getBalance().eq(UPDATED_AMOUNT.negate()));
    }

    /* ****************************** PRIVATE ******************************* */

    private static final Long ASSETS_GROUP_ID = Long.valueOf(3);
    private static final String FROM_NAME = "Cash"; //$NON-NLS-1$
    private static final String FROM_DESC = "Cash Description"; //$NON-NLS-1$
    private static final String TO_NAME = "Fuel"; //$NON-NLS-1$
    private static final String TO_DESC = "Fuel Description"; //$NON-NLS-1$

    private static final String UPDATE_FROM_NAME = "Bank"; //$NON-NLS-1$
    private static final String UPDATED_FROM_DESC = "Bank Description"; //$NON-NLS-1$

    private Account fromAccount;
    private Account toAccount;
    private Account updatedFromAccount;

    private void createAccounts() {
        if (this.fromAccount == null) {
            this.accountDao
                .createAccount(Account.newInstance(FROM_NAME, FROM_DESC, ASSETS_GROUP_ID,
                    Account.Types.ASSET));
            this.fromAccount = this.accountDao.getAccountByName(FROM_NAME);
        }

        if (this.toAccount == null) {
            this.accountDao
                .createAccount(Account.newInstance(TO_NAME, TO_DESC, Long.valueOf(7),
                    Account.Types.EXPENSE));
            this.toAccount = this.accountDao.getAccountByName(TO_NAME);
        }

        if (this.updatedFromAccount == null) {
            this.accountDao.createAccount(Account.newInstance(UPDATE_FROM_NAME, UPDATED_FROM_DESC,
                ASSETS_GROUP_ID, Account.Types.ASSET));
            this.updatedFromAccount = this.accountDao.getAccountByName(UPDATE_FROM_NAME);
        }
    }
}
