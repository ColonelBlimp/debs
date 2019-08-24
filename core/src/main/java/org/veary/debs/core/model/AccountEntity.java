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
import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.dao.DaoUtils;
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

    private String name;
    private String description;
    private Types type;
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
    public AccountEntity(String name, String description, Long parentId, Types type) {
        this.name = Objects.requireNonNull(name, Messages.getParameterIsNull("name"));
        this.description = Objects.requireNonNull(description,
            Messages.getParameterIsNull("description"));
        this.parentId = Objects.requireNonNull(parentId, Messages.getParameterIsNull("parentId"));
        this.type = Objects.requireNonNull(type, Messages.getParameterIsNull("type"));
        this.balance = new Money(BigDecimal.ZERO);
    }

    /**
     * Constructor.
     *
     * @param dataMap {@code Map<String, Object>}
     */
    public AccountEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap, Validator.getEnumValuesAsStringArray(Fields.class));

        setId((Long) dataMap.get(Fields.ID.toString()));
        setDeleted((Boolean) dataMap.get(Fields.DELETED.toString()));
        setCreationTimestamp(DaoUtils
            .localDateTimeFromTimestamp((Timestamp) dataMap.get(Fields.CREATED.toString())));

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
        Objects.requireNonNull(original, Messages.getParameterIsNull("original"));
        setId(original.getId());
        setDeleted(original.isDeleted());
        setCreationTimestamp(original.getCreationTimestamp());
        this.name = original.getName();
        this.description = original.getDescription();
        this.type = original.getType();
        this.balance = original.getBalance();
        this.parentId = original.getParentId();
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, Messages.getParameterIsNull("name"));
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description,
            Messages.getParameterIsNull("description"));
    }

    @Override
    public Types getType() {
        return this.type;
    }

    public void setType(Types type) {
        this.type = Objects.requireNonNull(type, Messages.getParameterIsNull("type"));
    }

    @Override
    public Money getBalance() {
        return this.balance;
    }

    public void setBalance(Money balance) {
        this.balance = Objects.requireNonNull(balance, Messages.getParameterIsNull("balance"));
    }

    @Override
    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = Objects.requireNonNull(parentId, Messages.getParameterIsNull("parentId"));
    }
}
