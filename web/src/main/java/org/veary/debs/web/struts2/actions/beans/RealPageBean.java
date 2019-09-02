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

package org.veary.debs.web.struts2.actions.beans;

import org.veary.debs.web.struts2.PageBean;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class RealPageBean implements PageBean {

    private static final String NOT_SET = "{NOT SET}";

    private String formDate;
    private String pageTitle = NOT_SET;
    private String mainHeadingText = NOT_SET;

    @Override
    public String getPageTitle() {
        return this.pageTitle;
    }

    @Override
    public String getMainHeadingText() {
        return this.mainHeadingText;
    }

    @Override
    public String getSiteUrl() {
        return "http://localhost:8080";
    }

    @Override
    public String getSiteLocale() {
        return "en";
    }

    @Override
    public String getFormDate() {
        return this.formDate;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    public void setMainHeadingText(String text) {
        this.mainHeadingText = text;
    }

    @Override
    public void setFormDate(String dateString) {
        this.formDate = dateString;
    }
}
