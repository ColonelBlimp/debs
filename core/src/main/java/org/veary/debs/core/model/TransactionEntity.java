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

    private volatile int hashCode = 0;

    private Money amount;

    private LocalDate date;
    private String narrative;
    private String reference;
    private Entry fromEntry;
    private Entry toEntry;
    private boolean cleared;

    /**
     * Constructor.
     *
     * @param date {@link LocalDate}
     * @param narrative {@code String}
     * @param reference {@code String}
     * @param amount {@link Money}
     * @param cleared {@code true} if <b>both</b> {@code Entry} object should be marked as
     *     <i>cleared</i>, otherwise {@code false}. <b>Note:</b> The {@code Transaction} object
     *     itself does does not have a <i>cleared</i> field.
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

    /**
     * Special Constructor.
     *
     * @param object {@link TransactionGetByIdEntity}
     */
    public TransactionEntity(TransactionGetByIdEntity object) {
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$

        setId(Objects.requireNonNull(object.getId()));
        setDeleted(object.isDeleted());
        setCreationTimestamp(Objects.requireNonNull(object.getCreationTimestamp()));

        this.date = Objects.requireNonNull(object.getDate());
        this.reference = Objects.requireNonNull(object.getReference());
        this.narrative = Objects.requireNonNull(object.getNarrative());
        this.amount = Objects.requireNonNull(object.getToAmount());

        this.fromEntry = new EntryEntity(object, Entry.Types.FROM);
        this.toEntry = new EntryEntity(object, Entry.Types.TO);
    }

    /**
     * Copy constructor. Returns a new instance (state is equal, but identity is not).
     *
     * <p><b>Note:</b> Associated {@code Entry} objects are also copied (i.e. state is equal, but
     * identity is not).
     *
     * @param object the {@code Transaction} object to be copied.
     */
    public TransactionEntity(Transaction object) {
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$

        setId(Objects.requireNonNull(object.getId()));
        setDeleted(object.isDeleted());
        setCreationTimestamp(Objects.requireNonNull(object.getCreationTimestamp()));

        this.date = Objects.requireNonNull(object.getDate()); //Do We need to copy this?
        this.reference = Objects.requireNonNull(object.getReference());
        this.narrative = Objects.requireNonNull(object.getNarrative());

        this.fromEntry = new EntryEntity(object.getFromEntry());
        this.toEntry = new EntryEntity(object.getToEntry());
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

    public void setEntries(Entry fromEntry, Entry toEntry) {
        Objects.requireNonNull(fromEntry, Messages.getParameterIsNull("fromEntry")); //$NON-NLS-1$
        Objects.requireNonNull(toEntry, Messages.getParameterIsNull("toEntry")); //$NON-NLS-1$

        ((EntryEntity) fromEntry).setCleared(this.cleared);
        ((EntryEntity) toEntry).setCleared(this.cleared);

        ((EntryEntity) fromEntry).setAmount(this.amount.negate());
        ((EntryEntity) toEntry).setAmount(this.amount);

        this.fromEntry = fromEntry;
        this.toEntry = toEntry;
    }

    /**
     * For debugging purposes only.
     */
    @Override
    public String toString() {
        String newLine = System.lineSeparator();
        Class<?> clazz = this.getClass();
        StringBuilder sb = new StringBuilder(newLine).append(clazz.getSimpleName());
        sb.append(" {").append(newLine); //$NON-NLS-1$

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
                sb.append(newLine);
            }
        }
        sb.append("}"); //$NON-NLS-1$
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof TransactionEntity)) {
            return false;
        }

        TransactionEntity other = (TransactionEntity) that;

        return this.amount.eq(other.amount) &&
            this.date.equals(other.date) &&
            this.narrative.equals(other.narrative) &&
            this.reference.contentEquals(other.reference) &&
            this.fromEntry.equals(other.fromEntry) &&
            this.toEntry.equals(other.toEntry) &&
            this.cleared == other.cleared;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = Objects.hash(this.amount, this.date, this.narrative, this.reference,
                this.fromEntry, this.toEntry, this.cleared);
        }
        return this.hashCode;
    }
}
