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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.model.Account;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
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
    private final List<Account> accounts;
    private final Map<String, String> viewMap;

    private boolean includeDeleted;
    private String listView;

    /**
     * Constructor.
     *
     * @param pageBean
     */
    @Inject
    public AccountList(PageBean pageBean, AccountFacade accountFacade) {
        super(pageBean);
        this.accountFacade = accountFacade;
        this.accounts = this.accountFacade.getActualAccounts(this.includeDeleted);
        this.viewMap = new HashMap<>();

        this.viewMap.put(LIST_VIEW_ACTUAL, "Actual Accounts");
        this.viewMap.put(LIST_VIEW_GROUP, "Group Accounts");
        this.viewMap.put(LIST_VIEW_ALL, "All Accounts");
        this.listView = LIST_VIEW_ACTUAL;

        this.pageBean.setPageTitle("DEBS :: Account List");
        this.pageBean.setMainHeadingText("Account List");
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        return BaseAction.SUCCESS;
    }

    public List<Account> getAccounts() {
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
}
