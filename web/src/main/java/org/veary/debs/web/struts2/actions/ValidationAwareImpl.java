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

import com.opensymphony.xwork2.ValidationAwareSupport;
import com.opensymphony.xwork2.interceptor.ValidationAware;

import java.util.Collection;
import java.util.List;
import java.util.Map;

abstract class ValidationAwareImpl extends TextProviderImpl implements ValidationAware {

    private final ValidationAwareSupport validationAware = new ValidationAwareSupport();

    @Override
    public void setActionErrors(Collection<String> errorMessages) {
        this.validationAware.setActionErrors(errorMessages);
    }

    @Override
    public Collection<String> getActionErrors() {
        return this.validationAware.getActionErrors();
    }

    @Override
    public void setActionMessages(Collection<String> messages) {
        this.validationAware.setActionMessages(messages);
    }

    @Override
    public Collection<String> getActionMessages() {
        return this.validationAware.getActionMessages();
    }

    @Override
    public void setFieldErrors(Map<String, List<String>> errorMap) {
        this.validationAware.setFieldErrors(errorMap);
    }

    @Override
    public Map<String, List<String>> getFieldErrors() {
        return this.validationAware.getFieldErrors();
    }

    @Override
    public void addActionError(String anErrorMessage) {
        this.validationAware.addActionError(anErrorMessage);
    }

    @Override
    public void addActionMessage(String aMessage) {
        this.validationAware.addActionMessage(aMessage);
    }

    @Override
    public void addFieldError(String fieldName, String errorMessage) {
        this.validationAware.addFieldError(fieldName, errorMessage);
    }

    @Override
    public boolean hasActionErrors() {
        return this.validationAware.hasActionErrors();
    }

    @Override
    public boolean hasActionMessages() {
        return this.validationAware.hasActionMessages();
    }

    @Override
    public boolean hasErrors() {
        return this.validationAware.hasErrors();
    }

    @Override
    public boolean hasFieldErrors() {
        return this.validationAware.hasFieldErrors();
    }
}
