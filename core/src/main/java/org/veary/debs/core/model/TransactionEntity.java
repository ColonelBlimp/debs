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

package org.veary.debs.core.model;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b> To assembly all the constituent parts of a transaction, ensure the
 * integrity of the transaction and its constituent parts and all CRUD operations relating to
 * transactions.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class TransactionEntity extends PersistentObjectImpl implements Transaction {

    private final Money amount;

    private LocalDate date;
    private String narrative;
    private String reference;
    private Entry fromEntry;
    private Entry toEntry;
    private boolean cleared;

    /**
     * Constructor.
     *
     * @param date
     * @param narrative
     * @param reference
     * @param amount
     * @param cleared
     */
    public TransactionEntity(LocalDate date, String narrative, String reference, Money amount,
        boolean cleared) {
        this.date = Objects.requireNonNull(date, Messages.getParameterIsNull("date")); //$NON-NLS-1$
        this.narrative = Objects.requireNonNull(narrative,
            Messages.getParameterIsNull("narrative")); //$NON-NLS-1$
        this.reference = Objects.requireNonNull(reference,
            Messages.getParameterIsNull("reference")); //$NON-NLS-1$
        this.amount = Objects.requireNonNull(amount, Messages.getParameterIsNull("amount")); //$NON-NLS-1$
        this.cleared = cleared;
    }

    @Override
    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public String getNarrative() {
        return this.narrative;
    }

    @Override
    public String getReference() {
        return this.reference;
    }

    @Override
    public Entry getFromEntry() {
        return this.fromEntry;
    }

    @Override
    public Entry getToEntry() {
        return this.toEntry;
    }

    public Money getAmount() {
        return this.amount;
    }

    public boolean isCleared() {
        return this.cleared;
    }

    public void setEntries(EntryEntity fromEntry, EntryEntity toEntry) {
        Objects.requireNonNull(fromEntry, Messages.getParameterIsNull("fromEntry")); //$NON-NLS-1$
        Objects.requireNonNull(toEntry, Messages.getParameterIsNull("toEntry")); //$NON-NLS-1$

        fromEntry.setAmount(this.amount.negate());
        fromEntry.setCleared(this.cleared);
        toEntry.setAmount(this.amount);
        toEntry.setCleared(this.cleared);

        this.fromEntry = fromEntry;
        this.toEntry = toEntry;
    }

    /**
     * For debugging purposes only.
     */
    @Override
    public String toString() {
        String NL = System.lineSeparator();
        Class<?> clazz = this.getClass();
        StringBuilder sb = new StringBuilder(NL).append(clazz.getSimpleName());
        sb.append(" {").append(NL); //$NON-NLS-1$

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isSynthetic()) {
                sb.append("  ").append(field.getName()).append(": "); //$NON-NLS-1$ //$NON-NLS-2$
                try {
                    Object value = field.get(this);
                    sb.append(value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    sb.append("{error}"); //$NON-NLS-1$
                }
                sb.append(NL);
            }
        }
        sb.append("}"); //$NON-NLS-1$
        return sb.toString();
    }
}
