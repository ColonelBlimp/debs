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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.LocaleProviderFactory;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class TextProviderImpl implements TextProvider, LocaleProvider {

    private static final Logger log = LogManager.getLogger(TextProviderImpl.class);

    private TextProvider textProvider;
    private LocaleProvider localeProvider;

    private Container container;

    @Override
    public boolean hasKey(String key) {
        return getTextProvider().hasKey(key);
    }

    @Override
    public String getText(String key) {
        return getTextProvider().getText(key);
    }

    @Override
    public String getText(String key, String defaultValue) {
        return getTextProvider().getText(key, defaultValue);
    }

    @Override
    public String getText(String key, String defaultValue, String obj) {
        return getTextProvider().getText(key, defaultValue, obj);
    }

    @Override
    public String getText(String key, List<?> args) {
        return getTextProvider().getText(key, args);
    }

    @Override
    public String getText(String key, String[] args) {
        return getTextProvider().getText(key, args);
    }

    @Override
    public String getText(String key, String defaultValue, List<?> args) {
        return getTextProvider().getText(key, defaultValue, args);
    }

    @Override
    public String getText(String key, String defaultValue, String[] args) {
        return getTextProvider().getText(key, defaultValue, args);
    }

    @Override
    public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
        return getTextProvider().getText(key, defaultValue, args, stack);
    }

    @Override
    public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
        return getTextProvider().getText(key, defaultValue, args, stack);
    }

    @Override
    public ResourceBundle getTexts(String bundleName) {
        return getTextProvider().getTexts(bundleName);
    }

    @Override
    public ResourceBundle getTexts() {
        return getTextProvider().getTexts();
    }

    protected TextProvider getTextProvider() {
        if (this.textProvider == null) {
            final TextProviderFactory tpf = getContainer().getInstance(TextProviderFactory.class);
            this.textProvider = tpf.createInstance(getClass());
        }
        return this.textProvider;
    }

    protected LocaleProvider getLocaleProvider() {
        if (this.localeProvider == null) {
            final LocaleProviderFactory localeProviderFactory = getContainer()
                .getInstance(LocaleProviderFactory.class);
            this.localeProvider = localeProviderFactory.createLocaleProvider();
        }
        return this.localeProvider;
    }

    protected Container getContainer() {
        if (this.container == null) {
            log.warn(
                "Container is null, action was created manually? Fallback to ActionContext.");
            this.container = Objects.requireNonNull(
                ActionContext.getContext().getContainer(),
                "Container is null, action was created out of ActionContext scope?!?");
        }
        return this.container;
    }

    @Inject
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Locale getLocale() {
        return getLocaleProvider().getLocale();
    }

    @Override
    public boolean isValidLocaleString(String localeStr) {
        return getLocaleProvider().isValidLocaleString(localeStr);
    }

    @Override
    public boolean isValidLocale(Locale locale) {
        return getLocaleProvider().isValidLocale(locale);
    }
}
