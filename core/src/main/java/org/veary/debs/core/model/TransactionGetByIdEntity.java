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
import org.veary.debs.core.utils.DaoUtils;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Entry.Types;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class TransactionGetByIdEntity extends PersistentObjectImpl {

    enum Fields {
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
    public TransactionGetByIdEntity() {
    }

    /**
     * Constructor.
     *
     * @param dataMap
     */
    public TransactionGetByIdEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(TransactionGetByIdEntity.Fields.class));

        setId((Long) dataMap.get(Fields.ID.toString()));
        setDeleted((Boolean) dataMap.get(Fields.DELETED.toString()));
        setCreationTimestamp(DaoUtils
            .localDateTimeFromSqlTimestamp((Timestamp) dataMap.get(Fields.CREATED.toString())));

        this.date = DaoUtils
            .localDateFromSqlDate((Date) dataMap.get(Fields.TDATE.toString()));
        this.reference = (String) dataMap.get(Fields.REFERENCE.toString());
        this.narrative = (String) dataMap.get(Fields.NARRATIVE.toString());

        this.fromId = (Long) dataMap.get(Fields.FROM_ID.toString());
        this.fromCreatedTimestamp = DaoUtils.localDateTimeFromSqlTimestamp(
            (Timestamp) dataMap.get(Fields.FROM_CREATED.toString()));
        this.fromDeleted = (boolean) dataMap.get(Fields.FROM_DELETED.toString());
        setFromAmount(new Money((BigDecimal) dataMap.get(Fields.FROM_AMOUNT.toString())));
        this.fromType = Types.getType((Integer) dataMap.get(Fields.FROM_ETYPE.toString()));
        this.fromAccountId = (Long) dataMap.get(Fields.FROM_ACCOUNT_ID.toString());
        setFromCleared((boolean) dataMap.get(Fields.FROM_CLEARED.toString()));
        setFromClearedTimestamp(DaoUtils.localDateTimeFromSqlTimestamp(
            (Timestamp) dataMap.get(Fields.FROM_CLEARED_TS.toString())));

        this.toId = (Long) dataMap.get(Fields.TO_ID.toString());
        this.toCreatedTimestamp = DaoUtils
            .localDateTimeFromSqlTimestamp((Timestamp) dataMap.get(Fields.TO_CREATED.toString()));
        this.toDeleted = (boolean) dataMap.get(Fields.TO_DELETED.toString());
        setToAmount(new Money((BigDecimal) dataMap.get(Fields.TO_AMOUNT.toString())));
        this.toType = Types.getType((Integer) dataMap.get(Fields.TO_ETYPE.toString()));
        this.toAccountId = (Long) dataMap.get(Fields.TO_ACCOUNT_ID.toString());
        setToCleared((boolean) dataMap.get(Fields.TO_CLEARED.toString()));
        setToClearedTimestamp(DaoUtils.localDateTimeFromSqlTimestamp(
            (Timestamp) dataMap.get(Fields.TO_CLEARED_TS.toString())));
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
     * @return {@link TransactionGetByIdEntity}
     */
    public static final TransactionGetByIdEntity newInstance(Map<String, Object> dataMap) {
        return new TransactionGetByIdEntity(dataMap);
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
            this.fromClearedTimestamp = LocalDateTime.MIN;
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
            this.toClearedTimestamp = LocalDateTime.MIN;
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

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public LocalDateTime getFromClearedTimestamp() {
        return this.fromClearedTimestamp;
    }

    public void setFromClearedTimestamp(LocalDateTime fromClearedTimestamp) {
        this.fromClearedTimestamp = fromClearedTimestamp;
        if (fromClearedTimestamp.equals(LocalDateTime.MIN)) {
            this.fromCleared = false;
        } else {
            this.fromCleared = true;
        }
    }

    public Long getToId() {
        return this.toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public Money getToAmount() {
        return this.toAmount;
    }

    public void setToAmount(Money toAmount) {
        if (toAmount.isMinus()) {
            throw new IllegalArgumentException(
                Messages.getString("TransactionGetByIdEntity.setToAmount.wrongsign", toAmount)); //$NON-NLS-1$
        }
        this.toAmount = toAmount;
    }

    public Entry.Types getToType() {
        return this.toType;
    }

    public LocalDateTime getToClearedTimestamp() {
        return this.toClearedTimestamp;
    }

    public void setToClearedTimestamp(LocalDateTime toClearedTimestamp) {
        this.toClearedTimestamp = toClearedTimestamp;
        if (toClearedTimestamp.equals(LocalDateTime.MIN)) {
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
