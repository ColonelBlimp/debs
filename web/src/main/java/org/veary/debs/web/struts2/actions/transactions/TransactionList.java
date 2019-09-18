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

package org.veary.debs.web.struts2.actions.transactions;

import com.opensymphony.xwork2.Action;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Transaction;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.beans.TransactionBean;

/**
 * <b>Purpose:</b> .
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class TransactionList extends TransactionBaseAction implements SessionAware {

    /**
     * Used to retain the current listView across requests.
     */
    public static final String LIST_VIEW_SESSION_KEY = "org.veary.debs.web.struts2.actions.transactions.TransactionList";

    private static final String LIST_VIEW_THIS_MONTH = "this_month";
    private static final String LIST_VIEW_LAST_MONTH = "last_month";
    private static final String LIST_VIEW_ALL = "all";

    private static final Logger LOG = LogManager.getLogger(TransactionList.class);
    private static final String LOG_CALLED = "called";

    private final Map<String, String> viewMap;
    private String id;

    private List<TransactionBean> transactions;
    private String listView;
    private Boolean includeDeleted;
    private Map<String, Object> sessionMap;

    /**
     * Constructor.
     *
     * @param pageBean {@link PageBean}
     * @param systemFacade {@link SystemFacade}
     */
    @Inject
    public TransactionList(PageBean pageBean, SystemFacade systemFacade,
        AccountFacade accountFacade) {
        super(pageBean, systemFacade, accountFacade);
        LOG.trace(LOG_CALLED);

        this.viewMap = new HashMap<>();
        this.viewMap.put(LIST_VIEW_THIS_MONTH, getText("TransactionList.listView.this_month"));
        this.viewMap.put(LIST_VIEW_LAST_MONTH, getText("TransactionList.listView.last_month"));
        this.viewMap.put(LIST_VIEW_ALL, getText("TransactionList.listView.all"));
        this.listView = LIST_VIEW_THIS_MONTH;
        this.includeDeleted = Boolean.FALSE;

        this.pageBean.setPageTitle(getText("TransactionList.pageTitle"));
        this.pageBean.setMainHeadingText(getText("TransactionList.mainHeader"));
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        if (this.sessionMap.containsKey(TransactionList.LIST_VIEW_SESSION_KEY)) {
            this.listView = (String) this.sessionMap.get(TransactionList.LIST_VIEW_SESSION_KEY);
        }

        if (this.listView.equals(LIST_VIEW_ALL)) {
            this.transactions = transactionListToBeanList(
                this.systemFacade.getAllTransactions(this.includeDeleted.booleanValue()));
        } else {
            YearMonth period = getSelectedPeriod();
            this.transactions = transactionListToBeanList(
                this.systemFacade.getAllTransactionsOverPeriod(period,
                    this.includeDeleted.booleanValue()));

        }

        return Action.SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    public List<TransactionBean> getTransactions() {
        return this.transactions;
    }

    public Map<String, String> getViewMap() {
        return this.viewMap;
    }

    public String getListView() {
        return this.listView;
    }

    public void setListView(String listView) {
        this.listView = listView;
        this.sessionMap.put(TransactionList.LIST_VIEW_SESSION_KEY, listView);
    }

    public Boolean isIncludeDeleted() {
        return this.includeDeleted;
    }

    public void setIncludeDeleted(Boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }

    private List<TransactionBean> transactionListToBeanList(List<Transaction> transactions) {
        LOG.trace(LOG_CALLED);

        List<TransactionBean> list = new ArrayList<>(transactions.size());

        for (Transaction obj : transactions) {
            TransactionBean bean = new TransactionBean(obj);
            list.add(bean);
            bean.setFromAccountName(
                getAccountFromId(obj.getFromEntry().getAccountId()).getName());
            bean.setToAccountName(getAccountFromId(obj.getToEntry().getAccountId()).getName());
        }

        return Collections.unmodifiableList(list);
    }

    private YearMonth getSelectedPeriod() {
        LOG.trace(LOG_CALLED);

        YearMonth period = YearMonth.now();
        final Month month = period.getMonth();

        if (this.listView.equals(LIST_VIEW_LAST_MONTH)) {
            period = YearMonth.of(period.getYear(), month.minus(1));
        }

        return period;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
