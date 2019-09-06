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

package org.veary.debs.model;

import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.AccountEntity;

/**
 * <b>Purpose:</b> Defines the contract for all account objects. Where an account has a
 * {@code parentId} the parent account is termed a <b>Group Account</b> and itself has no
 * <b>transactions</b>. Accounts which have <b>transactions</b> are termed an <b>Actual
 * Account</b>.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface Account extends PersistentObject {

    /**
     * <b>Purpose:</b> Enum defining the field (column) names from persistent storage. These are
     * mapped to data fields when a new object is create using the {@link #newInstance} method.
     *
     * <p><b>Responsibility:</b> Type-safe persistent storage field names.
     *
     * @author Marc L. Veary
     * @since 1.0
     */
    enum Fields {
        ID("ID"), //$NON-NLS-1$
        CREATED("CREATED"), //$NON-NLS-1$
        BALANCE("BALANCE"), //$NON-NLS-1$
        DELETED("DELETED"), //$NON-NLS-1$
        NAME("NAME"), //$NON-NLS-1$
        DESCRIPTION("DESCRIPTION"), //$NON-NLS-1$
        PARENT_ID("PARENT_ID"), //$NON-NLS-1$
        ACCOUNT_TYPE("ACCOUNT_TYPE"); //$NON-NLS-1$

        private final String name;

        /**
         * Constructor.
         *
         * @param name the persistent storage field (column) name
         */
        Fields(String name) {
            this.name = Objects.requireNonNull(name,
                Messages.getParameterIsNull("name")); //$NON-NLS-1$
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * <b>Purpose:</b> Enum which defines all the account types.
     *
     * <p><b>Responsibility:</b> Type-safe account types.
     *
     * @author Marc L. Veary
     * @since 1.0
     */
    enum Types {
        ASSET(1),
        EXPENSE(2),
        LIABILITY(3),
        INCOME(4),
        EQUITY(5),
        RETAINED_EARNINGS(6),
        CONTROL(7),
        GROUP(8);

        private final Integer id;

        /**
         * Constructor.
         *
         * @param id Type's unique identifier
         */
        Types(int id) {
            this.id = Integer.valueOf(id);
        }

        /**
         * Returns the {@code id} for the a particular {@code Account.Type}
         *
         * @return {@code Integer}
         */
        public Integer getId() {
            return this.id;
        }

        /**
         * Returns the {@code Account.Type} for the referenced {@code id}.
         *
         * @param id {@code Integer}
         * @return {@code Account.Type}
         * @throws IllegalArgumentException if the referenced {@code id} does not represent an
         *     {@code Account.Type}.
         */
        public static Types getType(Integer id) {
            Objects.requireNonNull(id, Messages.getParameterIsNull("id")); //$NON-NLS-1$
            for (Types type : Types.values()) {
                if (type.id.equals(id)) {
                    return type;
                }
            }
            throw new IllegalArgumentException(
                Messages.getString("Account.Types.getType.exception", id)); //$NON-NLS-1$
        }
    }

    /**
     * Static method for creating a new Account object. Fields other than those referenced are
     * set to their default values.
     *
     * @param name a unique name for this account
     * @param description a description for this account
     * @param parentId the unique identifer of the parent account. The value
     *     {@code Long.valueOf(0)} if there is no parent.
     * @param type the account's type
     * @return a new unpersisted {@code Account} object
     */
    static Account newInstance(String name, String description, Long parentId, Types type) {
        return new AccountEntity(name, description, parentId, type);
    }

    /**
     * Static method for creating a new Account object from persisted data.
     *
     * <p><b>Note:</b> this method is required by the {@code org.veary.persist} library.
     *
     * @param dataMap a {@code Map<String, Object>} holding the data with which to populate the
     *     new {@code Account} instance.
     * @return {@code Account} new instance. Non-{@code null}.
     */
    static Account newInstance(Map<String, Object> dataMap) {
        return new AccountEntity(dataMap);
    }

    /**
     * Returns the unique name for this account.
     *
     * @return {@code String}. Non-{@code null}.
     */
    String getName();

    /**
     * Returns the description of this account.
     *
     * @return {@code String}. Non-{@code null}.
     */
    String getDescription();

    /**
     * Returns this account's type.
     *
     * @return {@code Account.Types}
     * @see Account.Types
     */
    Types getType();

    /**
     * Returns the current balance for this account.
     *
     * @return {@link Money}. Non-{@code null}.
     */
    Money getBalance();

    /**
     * Returns the account's parent unique identifier.
     *
     * @return {@code Long}. Non-{@code null}.
     */
    Long getParentId();
}
