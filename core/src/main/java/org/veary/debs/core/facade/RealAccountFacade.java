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

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.Messages;
import org.veary.debs.core.model.AccountEntity;
import org.veary.debs.dao.AccountDao;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Account.Types;
import org.veary.debs.model.PersistentObject;

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

    private Account validateInput(Account object) {
        LOG.trace(LOG_CALLED);
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$

        if (object.getParentId().longValue() <= PersistentObject.DEFAULT_ID.longValue()) {
            throw new IllegalStateException(
                Messages.getString("RealAccountFacade.create.validateInput.noparentid")); //$NON-NLS-1$
        }

        return object;
    }

    @Override
    public void update(Account original, String name, String description, Long parentId,
        Types type) {
        Objects.requireNonNull(original, Messages.getParameterIsNull("original")); //$NON-NLS-1$

        AccountEntity updated = new AccountEntity(original);

    }
}
