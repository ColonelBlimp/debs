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

import org.veary.debs.model.Transaction;
import org.veary.debs.web.internal.WebConstants;

public final class TransactionBean {

    private String id;
    private String date;
    private String narrative;
    private String amount;
    private String fromAccountId;
    private String fromAccountName;
    private String toAccountId;
    private String toAccountName;
    private String reference;
    private boolean deleted;
    private boolean fromCleared;
    private boolean toCleared;

    public TransactionBean() {
    }

    public TransactionBean(Transaction object) {
        this.id = object.getId().toString();
        this.date = object.getDate().toString();
        this.narrative = object.getNarrative();
        this.fromAccountId = object.getFromEntry().getAccountId().toString();
        this.toAccountId = object.getToEntry().getAccountId().toString();
        this.amount = String.format(WebConstants.CURRENCY_DISPLAY_FORMAT,
            object.getToEntry().getAmount().getValue());
        this.reference = object.getReference();
        this.deleted = object.isDeleted();
        this.fromCleared = object.getFromEntry().isCleared();
        this.toCleared = object.getToEntry().isCleared();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromAccountId() {
        return this.fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
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

    public String getToAccountId() {
        return this.toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
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

    public String getFromAccountName() {
        return this.fromAccountName;
    }

    public void setFromAccountName(String fromAccountName) {
        this.fromAccountName = fromAccountName;
    }

    public String getToAccountName() {
        return this.toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public boolean isFromCleared() {
        return this.fromCleared;
    }

    public void setFromCleared(boolean fromCleared) {
        this.fromCleared = fromCleared;
    }

    public boolean isToCleared() {
        return this.toCleared;
    }

    public void setToCleared(boolean toCleared) {
        this.toCleared = toCleared;
    }
}
