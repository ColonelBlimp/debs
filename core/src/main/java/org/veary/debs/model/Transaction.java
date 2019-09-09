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

import java.time.LocalDate;

import org.veary.debs.core.Money;
import org.veary.debs.core.model.TransactionEntity;

/**
 * <b>Purpose:</b> Defines the contract for an accounting transaction.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface Transaction extends PersistentObject {

    /**
     * Returns the transaction's date.
     *
     * @return {@link LocalDate}
     */
    LocalDate getDate();

    /**
     * Returns the narrative (description) for this transaction.
     *
     * @return {@code String}
     */
    String getNarrative();

    /**
     * Returns the reference for this transaction.
     *
     * @return {@code String}
     */
    String getReference();

    /**
     * Returns the Entry describing where the value <b>came from</b>.
     *
     * @return {@link Entry}
     */
    Entry getFromEntry();

    /**
     * Returns the Entry describing where the value <b>went to</b>.
     *
     * @return {@link Entry}
     */
    Entry getToEntry();

    /**
     * Static method for creating a new Transaction object. Fields other than those referenced
     * are set to their default values.
     *
     * @param date {@link LocalDate}
     * @param narrative {@code String}
     * @param reference {@code String}
     * @param amount {@link Money}
     * @param cleared {@code true} if the whole transaction should be marked as <i>cleared</i>,
     *     otherwise {@code false}
     * @param deleted {@code true} if the whole transaction should be marked as <i>deleted</i>,
     *     otherwise {@code false}
     * @return a new unpersisted {@code Transaction} object
     */
    static Transaction newInstance(LocalDate date, String narrative, String reference,
        Money amount, boolean cleared, boolean deleted) {
        return new TransactionEntity(date, narrative, reference, amount, cleared);
    }
}
