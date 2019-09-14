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

package org.veary.debs.web.struts2.actions;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Validateable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.web.struts2.PageBean;

/**
 * <b>Purpose:</b> Struts2 base action class. All Struts2 action classes should {@code extend}
 * this class.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class BaseAction extends ValidationAwareImpl implements Action, Validateable {

    public static final String SUBMIT_CREATE = "create";
    public static final String SUBMIT_UPDATE = "update";
    public static final String SUBMIT_DELETE = "delete";
    public static final String SUBMIT_CANCEL = "cancel";
    public static final String DOWNLOAD = "download";
    public static final String PROCESS = "process";
    public static final String VOUCHER = "voucher";
    public static final String PRINT = "print";
    public static final String ACTION_MESSAGE = "ACTION_MESSAGE";

    private static final Logger LOG = LogManager.getLogger(BaseAction.class);
    private static final String LOG_CALLED = "called";

    /**
     * The methods on this bean are used by Freemarker macros.
     */
    protected final PageBean pageBean;

    /**
     * Class variable holding the name of the submit button (if any).
     */
    protected String submitType;

    /**
     * Flag used by Freemarker templates.
     */
    protected boolean showTransactionsSelector;

    /**
     * Flag used in Freemarker templates toggling showing the Accounts Selector.
     *
     * @see fragments.ftl
     */
    protected boolean showAccountsSelector;

    /**
     * Constructor.
     *
     * @param pageBean a {@link PageBean} object
     */
    public BaseAction(PageBean pageBean) {
        this.pageBean = pageBean;
        this.showTransactionsSelector = true;
    }

    @Override
    public final String execute() throws Exception {
        LOG.trace(LOG_CALLED);

        if (this.submitType == null) {
            return executeSubmitNull();
        }

        String retval;
        switch (this.submitType.toLowerCase()) {
            case SUBMIT_CREATE:
                retval = executeSubmitCreate();
                break;
            case SUBMIT_UPDATE:
                retval = executeSubmitUpdate();
                break;
            case SUBMIT_DELETE:
                retval = executeSubmitDelete();
                break;
            case SUBMIT_CANCEL:
                retval = executeSubmitCancel();
                break;
            default:
                retval = executeSubmitOther();
        }

        LOG.trace("Returning: {}", retval);
        return retval;
    }

    @Override
    public final void validate() {
        LOG.trace(LOG_CALLED);

        if (this.submitType == null || this.submitType.equalsIgnoreCase(SUBMIT_CANCEL)) {
            LOG.trace("Returning early, submit [{}]", () -> this.submitType);
            return;
        }

        switch (this.submitType.toLowerCase()) {
            case SUBMIT_CREATE:
                validateSubmitCreate();
                break;
            case SUBMIT_UPDATE:
                validateSubmitUpdate();
                break;
            case SUBMIT_DELETE:
                validateSubmitDelete();
                break;
            default:
                validateSubmitOther();
        }
    }

    public final PageBean getPageBean() {
        return this.pageBean;
    }

    public final void setSubmitType(String submitType) {
        this.submitType = submitType;
    }

    public boolean isShowTransactionsSelector() {
        return this.showTransactionsSelector;
    }

    public boolean isShowAccountsSelector() {
        return this.showAccountsSelector;
    }

    /**
     * Called when {@code this.submitType} class variable equals {@code null}. This variable
     * will generally equal {@code null} when this class is activated from a HTTP REQUEST which
     * was <b>NOT</b> produced by a 'submit' button in a HTML 'form' element.
     *
     * <p> Generally, this method should be <i>Overriden</i> and used to populate the class with
     * data required by the view.
     *
     * @return the default is {@code SUCCESS}.
     */
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);
        return SUCCESS;
    }

    protected String executeSubmitCreate() {
        LOG.trace(LOG_CALLED);
        return SUCCESS;
    }

    protected String executeSubmitUpdate() {
        LOG.trace(LOG_CALLED);
        return SUCCESS;
    }

    protected String executeSubmitDelete() {
        LOG.trace(LOG_CALLED);
        return SUCCESS;
    }

    protected String executeSubmitCancel() {
        LOG.trace(LOG_CALLED);
        return SUCCESS;
    }

    protected String executeSubmitOther() {
        LOG.trace(LOG_CALLED);
        return SUCCESS;
    }

    protected void validateSubmitCreate() {
        LOG.trace(LOG_CALLED);
    }

    protected void validateSubmitUpdate() {
        LOG.trace(LOG_CALLED);
    }

    protected void validateSubmitDelete() {
        LOG.trace(LOG_CALLED);
    }

    protected void validateSubmitOther() {
        LOG.trace(LOG_CALLED);
    }
}
