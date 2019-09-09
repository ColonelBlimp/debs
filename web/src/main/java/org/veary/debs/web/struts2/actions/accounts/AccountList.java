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

import com.opensymphony.xwork2.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
 * <b>Purpose:</b> Struts2 Action class for {@code /WEB-INF/templates/accounts/list.ftl}
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class AccountList extends BaseAction {

    private static final String LIST_VIEW_ACTUAL = "actual";
    private static final String LIST_VIEW_GROUP = "group";
    private static final String LIST_VIEW_ALL = "all";

    private static final Logger LOG = LogManager.getLogger(AccountList.class);
    private static final String LOG_CALLED = "called";

    private final AccountFacade accountFacade;
    private final Map<String, String> viewMap;

    private List<AccountBean> accounts;
    private String listView;
    private Boolean includeDeleted;

    /**
     * Constructor.
     *
     * @param pageBean instance of {@link PageBean}
     * @param accountFacade instance of {@link AccountFacade}
     */
    @Inject
    public AccountList(PageBean pageBean, AccountFacade accountFacade) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.includeDeleted = Boolean.FALSE;
        this.accountFacade = Objects.requireNonNull(accountFacade);
        this.viewMap = new HashMap<>();

        this.viewMap.put(LIST_VIEW_ACTUAL, getText("AccountList.listView.actual"));
        this.viewMap.put(LIST_VIEW_GROUP, getText("AccountList.listView.group"));
        this.viewMap.put(LIST_VIEW_ALL, getText("AccountList.listView.all"));
        this.listView = LIST_VIEW_ACTUAL;
        this.includeDeleted = Boolean.FALSE;

        this.pageBean.setPageTitle(getText("AccountList.pageTitle"));
        this.pageBean.setMainHeadingText(getText("AccountList.mainHeader"));
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        switch (this.listView) {
            case LIST_VIEW_ALL:
                this.accounts = accountListToBeanList(
                    this.accountFacade.getAllAccounts(this.includeDeleted.booleanValue()));
                break;
            case LIST_VIEW_GROUP:
                this.accounts = accountListToBeanList(
                    this.accountFacade.getGroupAccounts(this.includeDeleted.booleanValue()));
                break;
            default:
                this.accounts = accountListToBeanList(
                    this.accountFacade.getActualAccounts(this.includeDeleted.booleanValue()));
        }

        return Action.SUCCESS;
    }

    public List<AccountBean> getAccounts() {
        return this.accounts;
    }

    public Map<String, String> getViewMap() {
        return this.viewMap;
    }

    public void setListView(String listView) {
        this.listView = listView;
    }

    public String getListView() {
        return this.listView;
    }

    private List<AccountBean> accountListToBeanList(List<Account> accounts) {
        LOG.trace(LOG_CALLED);

        List<AccountBean> list = new ArrayList<>(accounts.size());

        for (Account acc : accounts) {
            AccountBean bean = new AccountBean(acc);

            Optional<Account> parent = this.accountFacade.getById(acc.getParentId());
            if (parent.isEmpty()) {
                bean.setParentName("-");
            } else {
                bean.setParentName(parent.get().getName());
            }

            list.add(bean);
        }

        return Collections.unmodifiableList(list);
    }

    public Boolean isIncludeDeleted() {
        return this.includeDeleted;
    }

    public void setIncludeDeleted(Boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }
}
