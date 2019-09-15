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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.veary.debs.web.GuiceContextListener;
import org.veary.debs.web.internal.WebConstants;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;

public final class VoucherDownloadAction extends BaseAction
    implements ServletContextAware, SessionAware {
    private static final Logger LOG = LogManager.getLogger(VoucherDownloadAction.class);
    private static final String LOG_CALLED = "called";

    private ServletContext context;
    private InputStream inputStream;
    private String voucherFileName;
    private Map<String, Object> sessionMap;

    @Inject
    public VoucherDownloadAction(PageBean pageBean) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.voucherFileName = "TEST.pdf";
    }

    @Override
    protected String executeSubmitNull() {
        LOG.trace(LOG_CALLED);

        final Path voucherDir = (Path) this.context
            .getAttribute(GuiceContextListener.VOUCHER_DIR_KEY);

        final String voucherFilePath = voucherDir.toString() + File.separator
            + this.voucherFileName;

        if (!Files.exists(Paths.get(voucherFilePath))) {
            throw new AssertionError("Unknown voucher file: " + voucherFilePath);
        }

        try {
            this.inputStream = new FileInputStream(new File(voucherFilePath));
        } catch (FileNotFoundException e) {
            LOG.error(() -> e);
        }

        this.sessionMap.remove(WebConstants.VOUCHER_NAME_SESSION_KEY);

        return Action.SUCCESS;
    }

    public InputStream getFileInputStream() {
        return this.inputStream;
    }

    public String getVoucherFileName() {
        return this.voucherFileName;
    }

    public void setVoucherFileName(String voucherFileName) {
        this.voucherFileName = voucherFileName;
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
