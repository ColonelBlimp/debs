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
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Entry.Types;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> A system entity bean for {@link Transaction}.
 *
 * <p><b>Responsibility:</b> All SQL {@code SELECT} statements relating to Transactions pass
 * this class into the {@code org.veary.persist} library.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class TransactionEntitySelect extends PersistentObjectImpl {

    public enum Fields {
        ID("ID"), //$NON-NLS-1$
        CREATED("CREATED"), //$NON-NLS-1$
        DELETED("DELETED"), //$NON-NLS-1$
        TDATE("TDATE"), //$NON-NLS-1$
        REFERENCE("REFERENCE"), //$NON-NLS-1$
        NARRATIVE("NARRATIVE"), //$NON-NLS-1$

        FROM_ID("FROM_ID"), //$NON-NLS-1$
        FROM_CREATED("FROM_CREATED"), //$NON-NLS-1$
        FROM_DELETED("FROM_DELETED"), //$NON-NLS-1$
        FROM_AMOUNT("FROM_AMOUNT"), //$NON-NLS-1$
        FROM_ETYPE("FROM_ETYPE"), //$NON-NLS-1$
        FROM_ACCOUNT_ID("FROM_ACCOUNT_ID"), //$NON-NLS-1$
        FROM_CLEARED("FROM_CLEARED"), //$NON-NLS-1$
        FROM_CLEARED_TS("FROM_CLEARED_TS"), //$NON-NLS-1$

        TO_ID("TO_ID"), //$NON-NLS-1$
        TO_CREATED("TO_CREATED"), //$NON-NLS-1$
        TO_DELETED("TO_DELETED"), //$NON-NLS-1$
        TO_AMOUNT("TO_AMOUNT"), //$NON-NLS-1$
        TO_ETYPE("TO_ETYPE"), //$NON-NLS-1$
        TO_ACCOUNT_ID("TO_ACCOUNT_ID"), //$NON-NLS-1$
        TO_CLEARED("TO_CLEARED"), //$NON-NLS-1$
        TO_CLEARED_TS("TO_CLEARED_TS"); //$NON-NLS-1$

        private final String name;

        Fields(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private LocalDate date;
    private String reference;
    private String narrative;

    private Long fromId;
    private LocalDateTime fromCreatedTimestamp;
    private boolean fromDeleted;
    private Money fromAmount;
    private Entry.Types fromType = Entry.Types.FROM;
    private Long fromAccountId;
    private boolean fromCleared;
    private LocalDateTime fromClearedTimestamp;

    private Long toId;
    private LocalDateTime toCreatedTimestamp;
    private boolean toDeleted;
    private Money toAmount;
    private Entry.Types toType = Entry.Types.TO;
    private Long toAccountId;
    private boolean toCleared;
    private LocalDateTime toClearedTimestamp;

    /**
     * Default Constructor.
     *
     */
    public TransactionEntitySelect() {
    }

    /**
     * Constructor.
     *
     * @param dataMap {@code Map}
     */
    public TransactionEntitySelect(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(TransactionEntitySelect.Fields.class));

        setId((Long) dataMap.get(Fields.ID.toString()));
        setDeleted(((Boolean) dataMap.get(Fields.DELETED.toString())).booleanValue());
        setCreationTimestamp(
            ((Timestamp) dataMap.get(Fields.CREATED.toString())).toLocalDateTime());

        this.date = ((Date) dataMap.get(Fields.TDATE.toString())).toLocalDate();
        this.reference = (String) dataMap.get(Fields.REFERENCE.toString());
        this.narrative = (String) dataMap.get(Fields.NARRATIVE.toString());

        this.fromId = (Long) dataMap.get(Fields.FROM_ID.toString());
        this.fromCreatedTimestamp = ((Timestamp) dataMap.get(Fields.FROM_CREATED.toString()))
            .toLocalDateTime();
        this.fromDeleted = ((Boolean) dataMap.get(Fields.FROM_DELETED.toString())).booleanValue();
        setFromAmount(new Money((BigDecimal) dataMap.get(Fields.FROM_AMOUNT.toString())));
        this.fromType = Types.getType((Integer) dataMap.get(Fields.FROM_ETYPE.toString()));
        this.fromAccountId = (Long) dataMap.get(Fields.FROM_ACCOUNT_ID.toString());

        setFromCleared(((Boolean) dataMap.get(Fields.FROM_CLEARED.toString())).booleanValue());
        setFromClearedTimestamp(
            ((Timestamp) dataMap.get(Fields.FROM_CLEARED_TS.toString())).toLocalDateTime());

        this.toId = (Long) dataMap.get(Fields.TO_ID.toString());
        this.toCreatedTimestamp = ((Timestamp) dataMap.get(Fields.TO_CREATED.toString()))
            .toLocalDateTime();
        this.toDeleted = ((Boolean) dataMap.get(Fields.TO_DELETED.toString())).booleanValue();
        setToAmount(new Money((BigDecimal) dataMap.get(Fields.TO_AMOUNT.toString())));
        this.toType = Types.getType((Integer) dataMap.get(Fields.TO_ETYPE.toString()));
        this.toAccountId = (Long) dataMap.get(Fields.TO_ACCOUNT_ID.toString());

        setToCleared(((Boolean) dataMap.get(Fields.TO_CLEARED.toString())).booleanValue());
        setToClearedTimestamp(
            ((Timestamp) dataMap.get(Fields.TO_CLEARED_TS.toString())).toLocalDateTime());
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNarrative() {
        return this.narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public Long getToAccountId() {
        return this.toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    /**
     * Static method for creating a new {@code TransactionGetByIdEntity} object.
     *
     * @param dataMap {@code Map<String, Object>} as returned by {@code org.veary.persist}
     * @return {@link TransactionEntitySelect}
     */
    public static final TransactionEntitySelect newInstance(Map<String, Object> dataMap) {
        return new TransactionEntitySelect(dataMap);
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

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isFromCleared() {
        return this.fromCleared;
    }

    public void setFromCleared(boolean fromCleared) {
        this.fromCleared = fromCleared;
        if (fromCleared) {
            this.fromClearedTimestamp = LocalDateTime.now();
        } else {
            this.fromClearedTimestamp = EntryEntity.NOT_CLEARED_TIMESTAMP;
        }
    }

    public boolean isToCleared() {
        return this.toCleared;
    }

    public void setToCleared(boolean toCleared) {
        this.toCleared = toCleared;
        if (toCleared) {
            this.toClearedTimestamp = LocalDateTime.now();
        } else {
            this.toClearedTimestamp = EntryEntity.NOT_CLEARED_TIMESTAMP;
        }
    }

    public Long getFromId() {
        return this.fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Money getFromAmount() {
        return this.fromAmount;
    }

    /**
     * Set the {@code Money} amount for the FROM {@code Entry} for this {@code Transaction}.
     *
     * @param fromAmount {@code Money}
     */
    public void setFromAmount(Money fromAmount) {
        if (fromAmount.isPlus()) {
            throw new IllegalArgumentException(
                Messages.getString("TransactionGetByIdEntity.setFromAmount.wrongsign", //$NON-NLS-1$
                    fromAmount));
        }
        this.fromAmount = fromAmount;
    }

    public Entry.Types getFromType() {
        return this.fromType;
    }

    public Long getFromAccountId() {
        return this.fromAccountId;
    }

    /**
     * Sets the FROM account unique identifier for the FROM {@code Entry} for this
     * {@code Transaction}.
     *
     * @param fromAccountId {@code Long}
     */
    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public LocalDateTime getFromClearedTimestamp() {
        return this.fromClearedTimestamp;
    }

    public void setFromClearedTimestamp(LocalDateTime fromClearedTimestamp) {
        this.fromClearedTimestamp = fromClearedTimestamp;
        if (fromClearedTimestamp.equals(EntryEntity.NOT_CLEARED_TIMESTAMP)) {
            this.fromCleared = false;
        } else {
            this.fromCleared = true;
        }
    }

    public Long getToId() {
        return this.toId;
    }

    /**
     * Sets the unique identifier.
     *
     * @param toId {@code Long}
     */
    public void setToId(Long toId) {
        this.toId = toId;
    }

    public Money getToAmount() {
        return this.toAmount;
    }

    /**
     * Set the {@code Money} amount for the TO {@code Entry} for this {@code Transaction}.
     *
     * @param toAmount {@code Money}
     */
    public void setToAmount(Money toAmount) {
        if (toAmount.isMinus()) {
            throw new IllegalArgumentException(Messages
                .getString("TransactionGetByIdEntity.setToAmount.wrongsign",
                    toAmount)); //$NON-NLS-1$
        }
        this.toAmount = toAmount;
    }

    public Entry.Types getToType() {
        return this.toType;
    }

    public LocalDateTime getToClearedTimestamp() {
        return this.toClearedTimestamp;
    }

    /**
     * Sets the <i>cleared</i> timestamp. If the value given is anything other than
     * {@link EntryEntity#NOT_CLEARED_TIMESTAMP}, the {@code cleared} flag will also be set to
     * {@code true}. The reverse is also true.
     *
     * @param toClearedTimestamp {@code LocalDateTime}
     */
    public void setToClearedTimestamp(LocalDateTime toClearedTimestamp) {
        this.toClearedTimestamp = toClearedTimestamp;
        if (toClearedTimestamp.equals(EntryEntity.NOT_CLEARED_TIMESTAMP)) {
            this.toCleared = false;
        } else {
            this.toCleared = true;
        }
    }

    public LocalDateTime getFromCreatedTimestamp() {
        return this.fromCreatedTimestamp;
    }

    public void setFromCreatedTimestamp(LocalDateTime fromCreatedTimestamp) {
        this.fromCreatedTimestamp = fromCreatedTimestamp;
    }

    public LocalDateTime getToCreatedTimestamp() {
        return this.toCreatedTimestamp;
    }

    public void setToCreatedTimestamp(LocalDateTime toCreatedTimestamp) {
        this.toCreatedTimestamp = toCreatedTimestamp;
    }

    public boolean isFromDeleted() {
        return this.fromDeleted;
    }

    public void setFromDeleted(boolean fromDeleted) {
        this.fromDeleted = fromDeleted;
    }

    public boolean isToDeleted() {
        return this.toDeleted;
    }

    public void setToDeleted(boolean toDeleted) {
        this.toDeleted = toDeleted;
    }
}
