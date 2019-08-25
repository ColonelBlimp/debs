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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.EntryEntity;

/**
 * <b>Purpose:</b> Defines the contacts the entries associated with a {@link Transaction}. Every
 * transaction has at least two entries.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface Entry extends PersistentObject {

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
        DELETED("DELETED"), //$NON-NLS-1$
        AMOUNT("AMOUNT"), //$NON-NLS-1$
        ETYPE("ETYPE"), //$NON-NLS-1$
        ACCOUNT_ID("ACCOUNT_ID"), //$NON-NLS-1$
        CLEARED("CLEARED"), //$NON-NLS-1$
        CLEARED_TS("CLEARED_TS"); //$NON-NLS-1$

        private final String name;

        /**
         * Constructor.
         *
         * @param name the persistent storage field (column) name
         */
        Fields(String name) {
            this.name = Objects.requireNonNull(name, Messages.getParameterIsNull("name")); //$NON-NLS-1$
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * <b>Purpose:</b> Enum defining the movement of value.
     *
     * <p>{@link #FROM} means this {@code Entry} reprensents a value which came <b>from</b> the
     * associated account ({@link Entry#getAccountId}).
     *
     * <p>{@link #TO} means this {@code Entry} reprensents a value which when <b>to</b> the
     * associated account ({@link Entry#getAccountId}).
     *
     * @author Marc L. Veary
     * @since 1.0
     */
    enum Types {
        FROM(1),
        TO(2);

        private final Integer id;

        Types(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return this.id;
        }

        public static Types getType(Integer id) {
            Objects.requireNonNull(id, Messages.getParameterIsNull("id")); //$NON-NLS-1$
            for (Types type : Types.values()) {
                if (type.id.equals(id)) {
                    return type;
                }
            }
            throw new IllegalArgumentException(
                Messages.getString("Entry.Types.getType.exception", id)); //$NON-NLS-1$
        }
    }

    /**
     * Returns the <i>type</i> of this entry. This can only be one of two values:
     * {@link Types#FROM} indicating that the {@code Entry} represents a value that came
     * <b>FROM</b> the associated {@code Account}. Or {@link Types#TO} indicating that the
     * {@code Entry} represents a value that went <b>TO</b> the associated {@code Account}.
     *
     * @return {@link Types}
     */
    Types getType();

    /**
     * Returns the unique identifier of the {@code Account} associated with this {@code Entry}.
     *
     * @return {@code Long}
     */
    Long getAccountId();

    /**
     * Returns the {@link Money} object representing the value of this entry.
     *
     * @return {@code Money}
     */
    Money getAmount();

    /**
     * Returns a {@link LocalDateTime} representing the timestamp when this {@code Entry} was
     * marked as {@code cleared}.
     *
     * <p><b>Note:</b> The returned value will be empty if this entry has not been cleared.
     *
     * @return {@link Optional<LocalDateTime>}
     */
    Optional<LocalDateTime> getClearedTimestamp();

    /**
     * Is the entry cleared.
     *
     * @return {@code boolean}
     */
    boolean isCleared();

    /**
     * Static method for creating a new Entry object from persisted data.
     *
     * <p><b>Note:</b> this method is required by the {@code org.veary.persist} library.
     *
     * @param dataMap a {@code Map<String, Object>} holding the data with which to populate the
     *     new {@code Entry} instance.
     * @return {@code Entry} new instance. Non-{@code null}.
     */
    static Entry newInstance(Map<String, Object> dataMap) {
        return new EntryEntity(dataMap);
    }

    /**
     * Static factory method.
     *
     * @param type {@link Entry.Type}
     * @param account {@link Entry}
     * @param amount {@link Money}
     * @param isCleared boolean indicating if the {@code Entry} object is cleared or not
     * @return new instance of {@code Entry}
     */
    static Entry newInstance(Types type, Account account, Money amount, boolean isCleared) {
        return new EntryEntity(type, account, amount, isCleared);
    }
}
