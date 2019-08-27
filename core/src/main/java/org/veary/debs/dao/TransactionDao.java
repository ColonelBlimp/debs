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

package org.veary.debs.dao;

import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;
import org.veary.persist.exceptions.NoResultException;

/**
 * <b>Purpose:</b> Defined the contract from CRUD operations on {@link Transaction} and
 * {@link Entry} objects.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface TransactionDao {

    /**
     * Create a new {@link Transaction} and persist it.
     *
     * @param object {@link Transaction}
     * @return {@code Long} the unique identifier
     */
    Long createTransaction(Transaction object);

    /**
     * Retrieve an {@code Transaction} matching the referenced unique identifier.
     *
     * <p><b>Note:</b> This will also return an {@code Transaction} which is marked as
     * <b>deleted</b>.
     *
     * @param id the unique identifier
     * @return {@code Transaction}. Non-{@code null}.
     * @throws NoResultException if there is no record matching the referenced unique identifer
     */
    Transaction getTransactionById(Long id);
}
