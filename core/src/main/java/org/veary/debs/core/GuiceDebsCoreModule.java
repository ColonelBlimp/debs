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

package org.veary.debs.core;

import com.google.inject.AbstractModule;

import org.veary.debs.core.dao.RealAccountDao;
import org.veary.debs.core.dao.RealRegistry;
import org.veary.debs.core.dao.RealTransactionDao;
import org.veary.debs.core.facade.RealAccountFacade;
import org.veary.debs.core.facade.RealSystemFacade;
import org.veary.debs.dao.AccountDao;
import org.veary.debs.dao.Registry;
import org.veary.debs.dao.TransactionDao;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.persist.PersistenceManagerFactory;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class GuiceDebsCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PersistenceManagerFactory.class);
        bind(Registry.class).to(RealRegistry.class);
        bind(AccountDao.class).to(RealAccountDao.class);
        bind(TransactionDao.class).to(RealTransactionDao.class);
        bind(AccountFacade.class).to(RealAccountFacade.class);
        bind(SystemFacade.class).to(RealSystemFacade.class);
    }
}
