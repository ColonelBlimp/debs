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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.Account;
import org.veary.debs.model.Entry;

/**
 * <b>Purpose:</b> Entity bean for {@code Entry} objects.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class EntryEntity extends PersistentObjectImpl implements Entry {

    /**
     * A {@link LocalDateTime} instance used to indicate that an {@code Entry} object is
     * <b>not</b> cleared.
     */
    public static final LocalDateTime NOT_CLEARED_TIMESTAMP = LocalDateTime.of(1, 1, 1, 0, 0, 0);

    private volatile int hashCode = 0;

    private Types type;
    private Long accountId;
    private Money amount;
    private LocalDateTime clearedTimestamp;
    private boolean cleared;

    /**
     * Constructor.
     *
     * @param type {@link Entry.Types}
     * @param account the associated {@link Account}
     */
    public EntryEntity(Types type, Account account) {
        this.type = Objects.requireNonNull(type,
            Messages.getParameterIsNull("type")); //$NON-NLS-1$
        this.accountId = (Objects.requireNonNull(account,
            Messages.getParameterIsNull("account"))) //$NON-NLS-1$
                .getId();
        this.amount = new Money(BigDecimal.ZERO);
        setCleared(false);
    }

    /**
     * Constructor.
     *
     * <p><b>Note:</b> This constructor is required by the {@code org.veary.persist} library.
     *
     * @param dataMap {@code Map<String, Object>}
     */
    public EntryEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(Entry.Fields.class));

        setId((Long) dataMap.get(Fields.ID.toString()));
        setDeleted((Boolean) dataMap.get(Fields.DELETED.toString()));
        setCreationTimestamp(
            ((Timestamp) dataMap.get(Fields.CREATED.toString())).toLocalDateTime());

        this.type = Entry.Types.getType((Integer) dataMap.get(Fields.ETYPE.toString()));
        this.accountId = (Long) dataMap.get(Fields.ACCOUNT_ID.toString());
        this.amount = new Money((BigDecimal) dataMap.get(Fields.AMOUNT.toString()));
        setCleared((boolean) dataMap.get(Fields.CLEARED.toString()));
        setClearedTimestamp(
            ((Timestamp) dataMap.get(Fields.CLEARED_TS.toString())).toLocalDateTime());
        validateInput();
    }

    /**
     * Special constructor.
     *
     * @param object {@link TransactionEntitySelect}
     * @param type {@code Entry.Types}
     */
    public EntryEntity(TransactionEntitySelect object, Types type) {
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$
        Objects.requireNonNull(type, Messages.getParameterIsNull("type")); //$NON-NLS-1$

        this.type = type;
        if (type.equals(Types.FROM)) {
            setId(Objects.requireNonNull(object.getFromId()));
            setDeleted(object.isFromDeleted());
            setCreationTimestamp(Objects.requireNonNull(object.getFromCreatedTimestamp()));

            this.amount = Objects.requireNonNull(object.getFromAmount());
            this.type = Objects.requireNonNull(object.getFromType());
            this.accountId = Objects.requireNonNull(object.getFromAccountId());
            // These two might cause some issues?
            this.cleared = object.isFromCleared();
            this.clearedTimestamp = Objects.requireNonNull(object.getFromClearedTimestamp());
        } else {
            setId(Objects.requireNonNull(object.getToId()));
            setDeleted(object.isToDeleted());
            setCreationTimestamp(Objects.requireNonNull(object.getToCreatedTimestamp()));

            this.amount = Objects.requireNonNull(object.getToAmount());
            this.type = Objects.requireNonNull(object.getToType());
            this.accountId = Objects.requireNonNull(object.getToAccountId());
            // These two might cause some issues?
            this.cleared = object.isToCleared();
            this.clearedTimestamp = Objects.requireNonNull(object.getToClearedTimestamp());
        }
    }

    /**
     * Copy constructor.
     *
     * @param object {@code Entry} object to copy
     */
    public EntryEntity(Entry object) {
        Objects.requireNonNull(object, Messages.getParameterIsNull("object")); //$NON-NLS-1$
        setId(Objects.requireNonNull(object.getId()));
        setDeleted(object.isDeleted());
        setCreationTimestamp(Objects.requireNonNull(object.getCreationTimestamp()));
        this.amount = Objects.requireNonNull(object.getAmount());
        this.type = Objects.requireNonNull(object.getType());
        this.accountId = Objects.requireNonNull(object.getAccountId());
        this.cleared = object.isCleared();
        this.clearedTimestamp = Objects.requireNonNull(object.getClearedTimestamp());
    }

    @Override
    public Types getType() {
        return this.type;
    }

    @Override
    public Long getAccountId() {
        return this.accountId;
    }

    @Override
    public Money getAmount() {
        return this.amount;
    }

    @Override
    public LocalDateTime getClearedTimestamp() {
        return this.clearedTimestamp;
    }

    @Override
    public boolean isCleared() {
        return this.cleared;
    }

    public void setType(Types type) {
        this.type = Objects.requireNonNull(type,
            Messages.getParameterIsNull("type")); //$NON-NLS-1$
        validateInput();
    }

    public void setAccountId(Long accountId) {
        this.accountId = Objects.requireNonNull(accountId,
            Messages.getParameterIsNull("accountId")); //$NON-NLS-1$
    }

    public void setAmount(Money amount) {
        this.amount = Objects.requireNonNull(amount,
            Messages.getParameterIsNull("amount")); //$NON-NLS-1$
        validateInput();
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
        if (cleared) {
            this.clearedTimestamp = LocalDateTime.now();
        } else {
            this.clearedTimestamp = NOT_CLEARED_TIMESTAMP;
        }
    }

    private void validateInput() {
        if (this.amount.isPlus() && this.type.equals(Entry.Types.FROM)) {
            throw new IllegalStateException(
                Messages.getString("EntryEntry.validateInput.amount.fromexception",
                    this.amount)); //$NON-NLS-1$
        }
        if (this.amount.isMinus() && this.type.equals(Entry.Types.TO)) {
            throw new IllegalStateException(
                Messages.getString("EntryEntry.validateInput.amount.toexception",
                    this.amount)); //$NON-NLS-1$
        }
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

    /**
     * Setting the cleared timestamp to anything other than {@link #NOT_CLEARED_TIMESTAMP} will
     * also set the {@code cleared} flag to {@code true}. The reverse is also true.
     *
     * @param clearedTimestamp {@link LocalDateTime}
     */
    public void setClearedTimestamp(LocalDateTime clearedTimestamp) {
        this.clearedTimestamp = clearedTimestamp;
        if (clearedTimestamp.equals(NOT_CLEARED_TIMESTAMP)) {
            this.cleared = false;
        } else {
            this.cleared = true;
        }
    }

    @Override
    public boolean equals(Object that) {

        if (!(that instanceof EntryEntity)) {
            return false;
        }
        if (this == that) {
            return true;
        }

        EntryEntity other = (EntryEntity) that;

        return getId().equals(other.getId())
            && isDeleted() == other.isDeleted()
            && getCreationTimestamp().equals(other.getCreationTimestamp())
            && this.type.equals(other.type)
            && this.accountId.equals(other.accountId)
            && this.amount.eq(other.amount)
            && this.clearedTimestamp.equals(other.clearedTimestamp)
            && this.cleared == other.cleared;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = Objects.hash(getId(), isDeleted(), getCreationTimestamp(), this.type,
                this.accountId, this.amount,
                this.clearedTimestamp,
                this.cleared);
        }
        return this.hashCode;
    }
}
