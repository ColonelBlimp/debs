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

package org.veary.debs.web.struts2;

/**
 * <b>Purpose:</b> Holds data relevant to every view rendered by Struts2. The various methods
 * are invoked in the rendering of freemarker templates.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface PageBean {

    /**
     * Returns the browser's page title.
     *
     * @return {@code String}
     */
    String getPageTitle();

    /**
     * Returns the main heading for the view being rendered.
     *
     * @return {@code String}
     */
    String getMainHeadingText();

    /**
     * Returns the configured site url. The syntax is [protocol]://[FQDN], a '/' should NOT be
     * added to the end.
     *
     * @return {@code String}
     */
    String getSiteUrl();

    /**
     * The configured locale. The default is 'en'.
     *
     * @return {@code String}
     */
    String getSiteLocale();

    /**
     * Gets the date entered into a form.
     *
     * @return {@code String}
     */
    String getFormDate();

    /**
     * Set the page title to be displayed in the browser.
     *
     * @param pageTitle a {@code String}
     */
    void setPageTitle(String pageTitle);

    /**
     * Set the main heading text
     *
     * @param text the text to be used in in the main H1 element
     */
    void setMainHeadingText(String text);

    /**
     * Sets the form date into this bean.
     *
     * @param dateString a {@code String}
     */
    void setFormDate(String dateString);
}
