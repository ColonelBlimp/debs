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

package org.veary.debs.core.facade;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.AccountEntity;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.dao.AccountDao;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Account.Types;
import org.veary.persist.exceptions.NoResultException;
import org.veary.tree.TreeNode;

/**
 * <b>Purpose:</b> Concrete implementation of the {@code AccountFacade} interface.
 *
 * <p><b>Responsibility:</b>
 *
 * <p><b>Notes:</b> annotated for JSR330
 *
 * @author Marc L. Veary
 * @since 1.0
 */
@Singleton
public final class RealAccountFacade implements AccountFacade {

    private static final Logger LOG = LogManager.getLogger(RealAccountFacade.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$
    private static final Long DEFAULT_ID = Long.valueOf(0);

    private final AccountDao dao;

    /**
     * Constructor.
     *
     * @param dao {@link AccountDao}
     */
    @Inject
    public RealAccountFacade(AccountDao dao) {
        this.dao = Objects.requireNonNull(dao, Messages.getParameterIsNull("dao")); //$NON-NLS-1$
    }

    @Override
    public Long create(Account object) {
        LOG.trace(LOG_CALLED);
        return this.dao.createAccount(validateInput(object));
    }

    @Override
    public void update(Account original, String name, String description, Long parentId,
        Types type, boolean isDeleted) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(original, Messages.getParameterIsNull("original")); //$NON-NLS-1$

        for (String builtIn : Validator.getEnumValuesAsStringArray(BuiltInAccounts.class)) {
            if (builtIn.equals(name)) {
                throw new IllegalArgumentException(
                    Messages.getString("RealAccountFacade.update.error.builtin",
                        name)); //$NON-NLS-1$
            }
        }

        final AccountEntity updated = new AccountEntity(original);

        if (name != null) {
            updated.setName(name);
        }
        if (description != null) {
            updated.setDescription(description);
        }
        if (parentId != null) {
            updated.setParentId(parentId);
        }
        if (type != null) {
            updated.setType(type);
        }

        updated.setDeleted(isDeleted);

        this.dao.updateAccount(original, updated);
    }

    @Override
    public void updateBalance(Account object, Money balance) {
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$
        Objects.requireNonNull(balance, Messages.getParameterIsNull("balance")); //$NON-NLS-1$
        this.dao.updateAccountBalance(object, balance);
    }

    @Override
    public Optional<Account> getById(Long id) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(id, Messages.getParameterIsNull("id")); //$NON-NLS-1$

        try {
            return Optional.of(this.dao.getAccountById(id));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Account> getByName(String name) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(name, Messages.getParameterIsNull("name")); //$NON-NLS-1$

        try {
            return Optional.of(this.dao.getAccountByName(name));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Account> getAllAccounts(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);
        return this.dao.getAllAccounts(includeDeleted);
    }

    @Override
    public List<Account> getActualAccounts(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);
        return this.dao.getActualAccounts(includeDeleted);
    }

    @Override
    public List<Account> getGroupAccounts(boolean includeDeleted) {
        LOG.trace(LOG_CALLED);
        return this.dao.getGroupAccounts(includeDeleted);
    }

    @Override
    public TreeNode<Account> getChartOfAccounts() {
        LOG.trace(LOG_CALLED);
        return buildChartOfAccounts(getRootNode());
    }

    /**
     * The {@code AccountDao} allows the {@code parentId} parameter to be zero. However, the
     * facade does <b>not</b> allow this as a value of zero is the 'Balance' Group Account.
     *
     * @param object the {@code Account} object to be validated
     * @return {@code Account}
     */
    private Account validateInput(Account object) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$

        if (object.getParentId().longValue() <= DEFAULT_ID.longValue()) {
            throw new IllegalStateException(
                Messages
                    .getString("RealAccountFacade.create.validateInput.noparentid")); //$NON-NLS-1$
        }

        return object;
    }

    private TreeNode<Account> getRootNode() {
        LOG.trace(LOG_CALLED);
        try {
            Account root = this.dao.getAccountByName(BuiltInAccounts.BALANCE_GROUP.toString());
            return new TreeNode<>(root);
        } catch (NoResultException e) {
            throw new AssertionError(
                Messages.getString("RealAccountFacade.chart.assert.norootnode", //$NON-NLS-1$
                    BuiltInAccounts.BALANCE_GROUP.toString()));
        }
    }

    private TreeNode<Account> buildChartOfAccounts(TreeNode<Account> balance) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(balance, Messages.getParameterIsNull("balance")); //$NON-NLS-1$

        for (Account account : this.dao.getAllAccounts(false)) {
            if (account.getName().equals(BuiltInAccounts.BALANCE_GROUP.toString())) {
                continue;
            }

            Optional<TreeNode<Account>> result = balance.findNode(
                elementData -> elementData.getId().equals(account.getParentId()));

            if (result.isPresent()) {
                result.get().addChild(account);
            } else {
                throw new AssertionError(Messages.getString(
                    "RealAccountFacade.chart.assert.unknownparent",
                    account.getName())); //$NON-NLS-1$
            }
        }

        return balance;
    }
}
