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

import com.itextpdf.text.DocumentException;
import com.opensymphony.xwork2.Action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.veary.debs.core.Money;
import org.veary.debs.facade.AccountFacade;
import org.veary.debs.facade.SystemFacade;
import org.veary.debs.model.Account;
import org.veary.debs.model.Transaction;
import org.veary.debs.web.GuiceContextListener;
import org.veary.debs.web.internal.VoucherEntryBean;
import org.veary.debs.web.internal.WebConstants;
import org.veary.debs.web.struts2.DocumentGenerator;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;
import org.veary.debs.web.struts2.actions.beans.AccountTransactionBean;

/**
 * <b>Purpose:</b> Struts action.
 *
 * <p><b>View:</b> {@code /WEB-INF/templates/accounts/txlist.ftl}
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class AccountTransactionsList extends BaseAction
    implements ServletContextAware, SessionAware {

    private static final Logger LOG = LogManager.getLogger(AccountTransactionsList.class);
    private static final String LOG_CALLED = "called";

    private final SystemFacade systemFacade;
    private final AccountFacade accountFacade;
    private final boolean showVoucherModal;
    private final DocumentGenerator documentGenerator;

    private ServletContext context;
    private Long id;
    private List<AccountTransactionBean> transactions;
    private BigDecimal fromColumnTotal = BigDecimal.ZERO;
    private BigDecimal toColumnTotal = BigDecimal.ZERO;
    private Account account;
    private String voucherDate;
    private String voucherNumber;
    private Map<String, Object> sessionMap;

    /**
     * Constructor.
     *
     * @param pageBean
     */
    @Inject
    public AccountTransactionsList(PageBean pageBean, SystemFacade systemFacade,
        AccountFacade accountFacade, DocumentGenerator documentGenerator) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.systemFacade = systemFacade;
        this.accountFacade = accountFacade;
        this.showVoucherModal = true;
        this.documentGenerator = documentGenerator;

        this.pageBean.setPageTitle(getText("AccountTransactionsList.pageTitle"));
        this.pageBean.setMainHeadingText(getText("AccountTransactionsList.mainHeader"));
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        if (this.id == null) {
            LOG.error("Account ID has not been set");
            this.transactions = Collections.emptyList();
        } else {
            Optional<Account> result = this.accountFacade.getById(this.id);
            if (result.isEmpty()) {
                LOG.error("Unable to fetch account with ID: {}", () -> this.id);
                return Action.ERROR;
            }
            this.account = result.get();
            this.transactions = transactionListToBeanList(
                this.systemFacade.getTransactionsForAccount(this.account, false));
        }

        return Action.SUCCESS;
    }

    @Override
    protected String executeSubmitCreate() {
        LOG.trace(LOG_CALLED);

        LOG.trace("LIST: {}", () -> this.transactions);
        LOG.trace("ID: {}", () -> this.id);

        if (this.id == null) {
            LOG.error("Account ID has not been set");
            return Action.ERROR;
        }

        Optional<Account> result = this.accountFacade.getById(this.id);
        if (result.isEmpty()) {
            LOG.error("Unable to fetch account with ID: {}", () -> this.id);
            return Action.ERROR;
        }

        final Account account = result.get();
        final List<VoucherEntryBean> data = transactionListToVoucherBeanList(
            this.systemFacade.getTransactionsForAccount(account, false));

        final String voucherFileName = this.voucherNumber + ".pdf";

        Path voucherDir = (Path) this.context.getAttribute(GuiceContextListener.VOUCHER_DIR_KEY);
        File voucherFile = new File(
            voucherDir.toString() + File.separator + voucherFileName);

        try (FileOutputStream fos = new FileOutputStream(voucherFile)) {

            this.documentGenerator
                .generateVoucher(this.voucherNumber, this.voucherDate, account.getName(), data)
                .writeTo(fos);

        } catch (FileNotFoundException e) {
            LOG.error(() -> e);
            return Action.ERROR;
        } catch (IOException e) {
            LOG.error(() -> e);
            return Action.ERROR;
        } catch (DocumentException e) {
            LOG.error(() -> e);
            return Action.ERROR;
        }

        this.sessionMap.put(WebConstants.VOUCHER_NAME_SESSION_KEY, voucherFileName);

        return Action.SUCCESS;
    }

    @Override
    protected void validateSubmitCreate() {
        LOG.trace(LOG_CALLED);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AccountTransactionBean> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<AccountTransactionBean> transactions) {
        this.transactions = transactions;
    }

    public String getFromColumnTotal() {
        if (this.fromColumnTotal.signum() == -1) {
            return new Money(this.fromColumnTotal).negate().toString();
        }
        return new Money(this.fromColumnTotal).toString();
    }

    public String getToColumnTotal() {
        return new Money(this.toColumnTotal).toString();
    }

    public Account getAccount() {
        return this.account;
    }

    public boolean isShowVoucherModal() {
        return this.showVoucherModal;
    }

    public String getVoucherDate() {
        return this.voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherNumber() {
        return this.voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    private List<AccountTransactionBean>
        transactionListToBeanList(List<Transaction> transactions) {
        LOG.trace(LOG_CALLED);

        BigDecimal fromTotal = BigDecimal.ZERO;
        BigDecimal toTotal = BigDecimal.ZERO;
        List<AccountTransactionBean> list = new ArrayList<>(transactions.size());

        for (Transaction obj : transactions) {
            AccountTransactionBean bean = new AccountTransactionBean(obj);

            if (obj.getFromEntry().getAccountId().equals(this.id)) {
                bean.setOtherAccountName(
                    getAccountFromId(obj.getToEntry().getAccountId()).getName());
                bean.setAmountFrom(obj.getFromEntry().getAmount().toString());
                bean.setOtherAccountId(obj.getToEntry().getAccountId().toString());
                fromTotal = fromTotal.add(obj.getFromEntry().getAmount().getValue());
            } else {
                bean.setOtherAccountName(
                    getAccountFromId(obj.getFromEntry().getAccountId()).getName());
                bean.setAmountTo(obj.getToEntry().getAmount().toString());
                bean.setOtherAccountId(obj.getFromEntry().getAccountId().toString());
                toTotal = toTotal.add(obj.getToEntry().getAmount().getValue());
            }
            list.add(bean);
        }

        this.fromColumnTotal = fromTotal;
        this.toColumnTotal = toTotal;

        return Collections.unmodifiableList(list);
    }

    private Account getAccountFromId(Long id) {
        LOG.trace(LOG_CALLED);
        Optional<Account> result = this.accountFacade.getById(id);
        if (result.isEmpty()) {
            throw new AssertionError(
                String.format(
                    "AccountFacade.getById() returned an empty result for ID: %s", id));
        }
        return result.get();
    }

    private List<VoucherEntryBean>
        transactionListToVoucherBeanList(List<Transaction> transactions) {
        LOG.trace(LOG_CALLED);

        final List<VoucherEntryBean> voucherList = new ArrayList<>(transactions.size());

        for (Transaction obj : transactions) {

            VoucherEntryBean bean = new VoucherEntryBean(
                obj.getNarrative(),
                obj.getReference(),
                new Money(BigDecimal.ZERO));

            voucherList.add(bean);
        }

        return Collections.unmodifiableList(voucherList);
    }

    @Override
    public void setServletContext(ServletContext context) {
        this.context = context;
    }

    @Override
    public void setSession(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
}
