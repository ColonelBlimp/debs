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

import java.util.List;

import org.veary.debs.core.Money;
import org.veary.debs.model.Account;
import org.veary.persist.exceptions.NoResultException;

/**
 * <b>Purpose:</b> Defined the contract from CRUD operations on {@link Account} objects.
 *
 * <p><b>Responsibility:</b> All DAO for {@code Account} objects are handled by any
 * implementation.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface AccountDao {

    /**
     * Create an new {@code Account} and persist.
     *
     * @param object new {@link Account} object created using
     *     {@link Account#newInstance(String, String, Long, Account.Types)}.
     * @return {@code Long} the unique identifier
     */
    Long createAccount(Account object);

    /**
     * Updates the referenced <b>original</b> {@code Account} with the values from the
     * <b>update</b> {@code Account}.
     *
     * <p><b>Note:</b> The <i>CREATION</i>, <i>ID</i>, <i>BALANCE</i> and <i>DELETED</i> fields
     * (columns) of the <b>original</b> are <b>not</b> modified by this method.
     *
     * @param original {@code Account} to be updated
     * @param updated {@code Account} holding the new values
     */
    void updateAccount(Account original, Account updated);

    /**
     * Updates the referenced {@code Account}'s balance by adding the referenced {@code amount}.
     *
     * @param object {@link Account}
     * @param amount the value to update (add) to the account's current balance
     */
    void updateAccountBalance(Account object, Money amount);

    /**
     * Deletes the referened {@code Account}.
     *
     * <p><b>Note:</b> An account can only be deleted if it has not transactions assigned to it.
     *
     * @param object {@code Account} object to be deleted
     */
    void deleteAccount(Account object);

    /**
     * Retrieve an {@code Account} matching the referenced unique identifier.
     *
     * <p><b>Note:</b> This will also return an {@code Account} which is marked as <b>deleted</b>.
     *
     * @param id the unique identifier
     * @return {@code Account}. Non-{@code null}.
     * @throws NoResultException if there is no record matching the referenced unique identifer
     */
    Account getAccountById(Long id);

    /**
     * Retrieve an {@code Account} matching the references unique name.
     *
     * @param name a unique name
     * @return {@code Account}. Non-{@code null}.
     * @throws NoResultException if there is no record matching the referenced unique identifer
     */
    Account getAccountByName(String name);

    /**
     * Return a list of all {@code Account} objects.
     *
     * <p><b>Note:</b> the result includes deleted accounts and group accounts.
     *
     * @return {@code List<Account>}. Can be empty, but not {@code null}.
     */

    /**
     * Return a list of all {@code Account} objects.
     *
     * <p><b>Note:</b> the result includes group accounts.
     *
     * @param includeDeleted {@code true} if deleted accounts should be included in the list
     * @return {@code List<Account>}. Can be empty, but not {@code null}.
     */
    List<Account> getAllAccounts(boolean includeDeleted);

    /**
     * Return a list of all Group {@code Account} objects.
     *
     * @param includeDeleted {@code true} if deleted accounts should be included in the list
     * @return {@code List<Account>}. Can be empty, but not {@code null}.
     */
    List<Account> getGroupAccounts(boolean includeDeleted);

    /**
     * Return a list of all Actual {@code Account} objects.
     *
     * @param includeDeleted {@code true} if deleted accounts should be included in the list
     * @return {@code List<Account>}. Can be empty, but not {@code null}.
     */
    List<Account> getActualAccounts(boolean includeDeleted);
}
