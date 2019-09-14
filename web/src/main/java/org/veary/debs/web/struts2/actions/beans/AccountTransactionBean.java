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

import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> To view transactions for a particular account.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class AccountTransactionBean {

    private String id;
    private String date;
    private String narrative;
    private String reference;
    private String amountFrom = "";
    private String amountTo = "";
    private String otherAccountName;
    private String otherAccountId;
    private boolean deleted;

    /**
     * Constructor.
     */
    public AccountTransactionBean() {
    }

    /**
     * Constructor.
     *
     * @param object
     */
    public AccountTransactionBean(Transaction object) {
        Objects.requireNonNull(object, Messages.getParameterIsNull("object"));
        this.id = Objects.requireNonNull(object.getId()).toString();
        this.date = Objects.requireNonNull(object.getDate()).toString();
        this.narrative = Objects.requireNonNull(object.getNarrative());
        this.reference = Objects.requireNonNull(object.getReference());
        this.deleted = object.isDeleted();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNarrative() {
        return this.narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getAmountFrom() {
        return this.amountFrom;
    }

    public void setAmountFrom(String amountFrom) {
        this.amountFrom = amountFrom;
    }

    public String getAmountTo() {
        return this.amountTo;
    }

    public void setAmountTo(String amountTo) {
        this.amountTo = amountTo;
    }

    public String getOtherAccountName() {
        return this.otherAccountName;
    }

    public void setOtherAccountName(String otherAccountName) {
        this.otherAccountName = otherAccountName;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getOtherAccountId() {
        return this.otherAccountId;
    }

    public void setOtherAccountId(String otherAccountId) {
        this.otherAccountId = otherAccountId;
    }
}
