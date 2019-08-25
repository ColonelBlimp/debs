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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.dao.DaoUtils;
import org.veary.debs.model.Account;
import org.veary.debs.model.Entry;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class EntryEntity extends PersistentObjectImpl implements Entry {

    private Types type;
    private Long accountId;
    private Money amount;
    private LocalDateTime clearedTimestamp;
    private boolean cleared;

    /**
     * Constructor.
     *
     * @param type
     * @param account
     * @param amount
     * @param isCleared
     */
    public EntryEntity(Types type, Account account, Money amount, boolean isCleared) {
        this.type = Objects.requireNonNull(type, Messages.getParameterIsNull("type")); //$NON-NLS-1$
        this.accountId = (Objects.requireNonNull(account, Messages.getParameterIsNull("account"))) //$NON-NLS-1$
            .getId();
        this.amount = Objects.requireNonNull(amount, Messages.getParameterIsNull("amount")); //$NON-NLS-1$
        if (isCleared) {
            this.clearedTimestamp = LocalDateTime.now();
        }
        this.cleared = isCleared;
        validateInput();
    }

    /**
     * Constructor.
     *
     * @param dataMap
     */
    public EntryEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(Entry.Fields.class));

        setId((Long) dataMap.get(Fields.ID.toString()));
        setDeleted((Boolean) dataMap.get(Fields.DELETED.toString()));
        setCreationTimestamp(DaoUtils
            .localDateTimeFromTimestamp((Timestamp) dataMap.get(Fields.CREATED.toString())));

        this.type = Entry.Types.getType((Integer) dataMap.get(Fields.ETYPE.toString()));
        this.accountId = (Long) dataMap.get(Fields.ACCOUNT_ID.toString());
        this.amount = new Money((BigDecimal) dataMap.get(Fields.AMOUNT.toString()));
        this.cleared = (Boolean) dataMap.get(Fields.CLEARED.toString());
        this.clearedTimestamp = DaoUtils
            .localDateTimeFromTimestamp((Timestamp) dataMap.get(Fields.CLEARED_TS.toString()));
        validateInput();
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
    public Optional<LocalDateTime> getClearedTimestamp() {
        return Optional.ofNullable(this.clearedTimestamp);
    }

    @Override
    public boolean isCleared() {
        return this.cleared;
    }

    private void validateInput() {
        if (this.amount.isPlus() && this.type.equals(Entry.Types.FROM)) {
            throw new IllegalStateException(
                Messages.getString("EntryEntry.validateInput.amount.fromexception", this.amount)); //$NON-NLS-1$
        }
        if (this.amount.isMinus() && this.type.equals(Entry.Types.TO)) {
            throw new IllegalStateException(
                Messages.getString("EntryEntry.validateInput.amount.toexception", this.amount)); //$NON-NLS-1$
        }
    }
}
