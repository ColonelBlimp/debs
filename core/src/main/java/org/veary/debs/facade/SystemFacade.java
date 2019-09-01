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

package org.veary.debs.facade;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.veary.debs.model.Account;
import org.veary.debs.model.Entry;
import org.veary.debs.model.Transaction;

/**
 * <b>Purpose:</b> Defines the contract for accessing the main accounting system.
 *
 * <p><b>Responsibility:</b> Hides the complexity of the subsystem making it easier to use.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface SystemFacade {

    /**
     * Posts an accounting transaction to the system.
     *
     * @param transaction {@link Transaction}
     * @param fromEntry {@link Entry} describing where the value came from
     * @param toEntry {@link Entry} describing where the value went to
     * @return {@code Long} the unique identifier of the new transaction
     */
    Long postTransaction(Transaction transaction, Entry fromEntry, Entry toEntry);

    /**
     * Updates the referenced <i>original</i> transaction, with data from the referenced
     * <i>updated</i> transaction and updated <i>updatedFromEntry</i> and <i>updatedToEntry</i>.
     *
     * <p><b>Note:</b> Only certain field in the {@code original} can be modified.
     *
     * @param original {@link Transaction}
     * @param updated {@link Transaction}
     * @param updatedFromEntry {@link Entry}
     * @param updatedToEntry {@link Entry}
     */
    void updateTransaction(Transaction original, Transaction updated, Entry updatedFromEntry,
        Entry updatedToEntry);

    /**
     * Marks a {@code Transaction} and its accociated {@code Entry} objects as deleted. The
     * actual entries are not deleted, but simply marked as such.
     *
     * @param object {@code Transaction} to be marked as deleted
     */
    void deleteTransaction(Transaction object);

    /**
     * Fetch a {@code Transaction} from persistent storage.
     *
     * @param id the unique identifier of the transaction to be retrieved
     * @return {@code Optional<Transaction>}
     */
    Optional<Transaction> getTransactionById(Long id);

    /**
     * Returns a {@code List} of all the {@code Transaction} objects in the system.
     *
     * @param status indicates which records to include in the results
     * @return an unmodifiable {@code List}. Cannot be {@code null}.
     */
    List<Transaction> getAllTransactions(Status status);

    /**
     * Returns a {@code List} of all the {@link Transaction} objects posted during the
     * referenced {@link YearMonth}.
     *
     * @param period {@link YearMonth} defining the year and month
     * @param status indicates which records to include in the results
     * @return an unmodifiable {@code List}. Cannot be {@code null}.
     */
    List<Transaction> getAllTransactionsOverPeriod(YearMonth period, Status status);

    /**
     * Returns a {@code List} of all the {@code Transaction} objects relating to the referenced
     * {@code Account}.
     *
     * @param account the {@code Account}
     * @param status indicates which records to include in the results
     * @return an unmodifiable {@code List}. Cannot be {@code null}.
     */
    List<Transaction> getTransactionsForAccount(Account account, Status status);

    /**
     * Returns a {@code List} of all the {@code Transaction} objects relating to the referenced
     * {@code Account} posted during the referenced {@link YearMonth}.
     * 
     * @param period {@link YearMonth} defining the year and month
     * @param account the {@code Account}
     * @param status indicates which records to include in the results
     * @return an unmodifiable {@code List}. Cannot be {@code null}.
     */
    List<Transaction> getTransactionsForAccountOverPeriod(YearMonth period, Account account,
        Status status);
}
