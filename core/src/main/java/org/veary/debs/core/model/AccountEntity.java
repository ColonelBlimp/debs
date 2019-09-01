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
import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.Account;

/**
 * <b>Purpose:</b>
 *
 * <b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class AccountEntity extends PersistentObjectImpl implements Account {

    private volatile int hashCode = 0;

    private String name;
    private String description;
    private Account.Types type;
    private Money balance;
    private Long parentId;

    /**
     * Constructor.
     *
     * @param name a unique name for this account
     * @param description a description for this account
     * @param parentId the unique identifer of the parent account. The value
     *     {@code Long.valueOf(0)} if there is no parent.
     * @param type the account's type
     */
    public AccountEntity(String name, String description, Long parentId, Account.Types type) {
        this.name = Objects.requireNonNull(name, Messages.getParameterIsNull("name")); //$NON-NLS-1$
        this.description = Objects.requireNonNull(description,
            Messages.getParameterIsNull("description")); //$NON-NLS-1$
        this.parentId = Objects.requireNonNull(parentId, Messages.getParameterIsNull("parentId")); //$NON-NLS-1$
        this.type = Objects.requireNonNull(type, Messages.getParameterIsNull("type")); //$NON-NLS-1$
        this.balance = new Money(BigDecimal.ZERO);
    }

    /**
     * Constructor.
     *
     * <p><b>Note:</b> This constructor is required by the {@code org.veary.persist} library.
     *
     * @param dataMap {@code Map<String, Object>}
     */
    public AccountEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(Account.Fields.class));

        setId((Long) dataMap.get(Fields.ID.toString()));
        setDeleted((Boolean) dataMap.get(Fields.DELETED.toString()));
        setCreationTimestamp(
            ((Timestamp) dataMap.get(Fields.CREATED.toString())).toLocalDateTime());

        this.name = (String) dataMap.get(Fields.NAME.toString());
        this.description = (String) dataMap.get(Fields.DESCRIPTION.toString());
        this.type = Account.Types.getType((Integer) dataMap.get(Fields.ACCOUNT_TYPE.toString()));
        this.balance = new Money((BigDecimal) dataMap.get(Fields.BALANCE.toString()));
        this.parentId = (Long) dataMap.get(Fields.PARENT_ID.toString());
    }

    /**
     * Constructor. Creates this object from an {@code Account} object.
     *
     * @param original {@link Account}
     */
    public AccountEntity(Account original) {
        Objects.requireNonNull(original, Messages.getParameterIsNull("original")); //$NON-NLS-1$
        setId(original.getId());
        setDeleted(original.isDeleted());
        setCreationTimestamp(original.getCreationTimestamp());
        this.name = Objects.requireNonNull(original.getName());
        this.description = Objects.requireNonNull(original.getDescription());
        this.type = Objects.requireNonNull(original.getType());
        this.balance = Objects.requireNonNull(original.getBalance());
        this.parentId = Objects.requireNonNull(original.getParentId());
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, Messages.getParameterIsNull("name")); //$NON-NLS-1$
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description,
            Messages.getParameterIsNull("description")); //$NON-NLS-1$
    }

    @Override
    public Types getType() {
        return this.type;
    }

    public void setType(Types type) {
        this.type = Objects.requireNonNull(type, Messages.getParameterIsNull("type")); //$NON-NLS-1$
    }

    @Override
    public Money getBalance() {
        return this.balance;
    }

    public void setBalance(Money balance) {
        this.balance = Objects.requireNonNull(balance, Messages.getParameterIsNull("balance")); //$NON-NLS-1$
    }

    @Override
    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = Objects.requireNonNull(parentId, Messages.getParameterIsNull("parentId")); //$NON-NLS-1$
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

        if (!(that instanceof AccountEntity)) {
            return false;
        }

        if (this == that) {
            return true;
        }

        AccountEntity other = (AccountEntity) that;

        return getId().equals(other.getId())
            && isDeleted() == other.isDeleted()
            && getCreationTimestamp().equals(other.getCreationTimestamp())
            && this.name.equals(other.name)
            && this.description.equals(other.description)
            && this.type.equals(other.type)
            && this.balance.eq(other.balance)
            && this.parentId.equals(other.parentId);
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = Objects.hash(getId(), isDeleted(), getCreationTimestamp(), this.name,
                this.description, this.type, this.balance,
                this.parentId);
        }
        return this.hashCode;
    }
}
