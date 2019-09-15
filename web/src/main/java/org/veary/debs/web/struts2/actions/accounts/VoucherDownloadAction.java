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
import java.nio.file.Paths;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.BaseAction;

public final class VoucherDownloadAction extends BaseAction {
    private static final Logger LOG = LogManager.getLogger(VoucherDownloadAction.class);
    private static final String LOG_CALLED = "called";

    private InputStream inputStream;
    private String voucherFileName;
    private String voucherFilePath;

    @Inject
    public VoucherDownloadAction(PageBean pageBean) {
        super(pageBean);
        LOG.trace(LOG_CALLED);

        this.voucherFileName = "TEST.pdf";
    }

    @Override
    protected String executeSubmitCreate() {
        LOG.trace(LOG_CALLED);

        if (!Files.exists(Paths.get(this.voucherFilePath))) {
            throw new AssertionError("Unknown voucher file: " + this.voucherFilePath);
        }
        final File voucherFile = new File(this.voucherFilePath);
        try {
            this.inputStream = new FileInputStream(voucherFile);
        } catch (FileNotFoundException e) {
            LOG.error(() -> e);
        }
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

    public String getVoucherFilePath() {
        return this.voucherFilePath;
    }

    public void setVoucherFilePath(String voucherFilePath) {
        this.voucherFilePath = voucherFilePath;
    }
}
