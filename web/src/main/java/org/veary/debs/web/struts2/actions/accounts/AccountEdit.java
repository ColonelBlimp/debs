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

package org.veary.debs.web.struts2.actions.accounts;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.AccountBean;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class AccountEdit extends AccountBaseAction {
    private static final Logger LOG = LogManager.getLogger(AccountEdit.class);
    private static final String LOG_CALLED = "called";

    private Account account;
    private Long id;

    /**
     * Constructor.
     *
     * @param pageBean
     */
    @Inject
    public AccountEdit(PageBean pageBean, AccountFacade accountFacade) {
        super(pageBean, accountFacade);
        LOG.trace(LOG_CALLED);

        this.pageBean.setPageTitle(getText("AccountEdit.pageTitle"));
        this.pageBean.setMainHeadingText(getText("AccountEdit.mainHeader"));
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        if (this.id == null) {
            LOG.error("The account ID has not been set");
            return BaseAction.ERROR;
        }

        Optional<Account> result = this.accountFacade.getById(this.id);
        if (result.isEmpty()) {
            LOG.error("Unknown account with ID: {}", this.id);
            return BaseAction.ERROR;
        }

        this.account = result.get();
        this.bean = new AccountBean(this.account);

        return BaseAction.INPUT;
    }

    @Override
    protected String executeSubmitUpdate() {
        LOG.trace(LOG_CALLED);

        return BaseAction.SUCCESS;
    }

    @Override
    protected void validateSubmitUpdate() {
        LOG.trace(LOG_CALLED);

        if (!validateAccountBeanStringFields(this.bean)) {
            return;
        }

        if (!this.account.getName().equals(this.bean.getName())) {
            Optional<Account> result = this.accountFacade.getByName(this.bean.getName());
            if (result.isPresent()) {
                addFieldError("name", getText("AccountEdit.account.name.notunique"));
            }
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
