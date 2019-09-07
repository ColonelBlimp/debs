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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.TransactionBean;

/**
 * <b>Purpose:</b> .
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class TransactionList extends BaseAction {

    private static final String LIST_VIEW_THIS_MONTH = "this_month";
    private static final String LIST_VIEW_LAST_MONTH = "last_month";
    private static final String LIST_VIEW_ALL = "all";

    private static final Logger LOG = LogManager.getLogger(TransactionList.class);
    private static final String LOG_CALLED = "called";

    private final SystemFacade systemFacade;
    private final Map<String, String> viewMap;
    private List<TransactionBean> transactions;
    private String listView;

    public TransactionList(PageBean pageBean, SystemFacade systemFacade) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.systemFacade = Objects.requireNonNull(systemFacade);
        this.viewMap = new HashMap<>();
        this.viewMap.put(LIST_VIEW_THIS_MONTH, getText("TransactionList.listView.this_month"));
        this.viewMap.put(LIST_VIEW_LAST_MONTH, getText("TransactionList.listView.last_month"));
        this.viewMap.put(LIST_VIEW_ALL, getText("TransactionList.listView.all"));
        this.listView = LIST_VIEW_THIS_MONTH;

        this.pageBean.setPageTitle(getText("TransactionList.pageTitle"));
        this.pageBean.setMainHeadingText(getText("TransactionList.mainHeader"));
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        return BaseAction.SUCCESS;
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
    }
}
