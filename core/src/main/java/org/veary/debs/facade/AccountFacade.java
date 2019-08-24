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

import java.util.List;
import java.util.Optional;

import org.veary.debs.core.Money;
import org.veary.debs.model.Account;
import org.veary.tree.TreeNode;

/**
 * <b>Purpose:</b> Encapsulates the account subsystem in a simple interface.
 *
 * <p> <b>Responsibility:</b> Hides the complexity of the subsystem making it easier to use.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface AccountFacade {

    /**
     * Creates an {@code Account} within the system from the referenced object. The
     * {@code Account} object must be created using
     * {@link Account#newInstance(String, String, org.veary.debs.model.Account.Types)}.
     *
     * @param object {@link Account}
     * @return {@code Long} the unique identifier of the new {@code Account}. Non-{@code null}.
     */
    Long create(Account object);

    /**
     * Update the referenced {@code Account} with any or all of the give parameters.
     *
     * @param original the {@code Account} to be updated
     * @param name a new unique name, or {@code null} if this field is not to be updated
     * @param description a new description, or {@code null} if this field is not to be updated
     * @param parentId a new unique parent identifier, or {@code null} if this field is not to
     *     be updated
     * @param type a different type, or {@code null} if this field is not to be updated
     */
    void update(Account original, String name, String description, Long parentId,
        Account.Types type);

    /**
     * Update the referenced {@code Account} with the given {@code Money}'s value.
     *
     * @param object {@code Account}
     * @param balance {@code Money}
     */
    void updateBalance(Account object, Money balance);

    /**
     * Deletes the referenced {@code Account}.
     *
     * @param object the {@code Account} to be deleted
     */
    void delete(Account object);

    /**
     * Fetch an {@code Account} from persistent storage.
     *
     * @param id the unique identifier of the account to be retrieved
     * @return {@code Optional<Account>}
     */
    Optional<Account> getById(Long id);

    /**
     * Fetch an {@code Account} from persistent storage.
     *
     * @param name the unique name of the account to be retrieved
     * @return {@code Optional<Account>}
     */
    Optional<Account> getByName(String name);

    /**
     * Returns a {@code List<Account>} of all the accounts.
     *
     * @param includeDeleted {@code true} if accounts marked as <i>deleted</i> should be
     *     included
     * @return {@code List<Account>}
     */
    List<Account> getAllAccounts(boolean includeDeleted);

    /**
     * Returns a {@code List<Account>} of all Actual accounts.
     *
     * @param includeDeleted {@code true} if accounts marked as <i>deleted</i> should be
     *     included
     * @return {@code List<Account>}
     */
    List<Account> getActualAccounts(boolean includeDeleted);

    /**
     * Returns a {@code List<Account>} of all Group accounts.
     *
     * @param includeDeleted {@code true} if accounts marked as <i>deleted</i> should be
     *     included
     * @return {@code List<Account>}
     */
    List<Account> getGroupAccounts(boolean includeDeleted);

    /**
     * Returns a {@code TreeNode<Account>} which is a hierachical tree of the chart of accounts.
     *
     * @return {@link TreeNode<Account>}
     */
    TreeNode<Account> getChartOfAccounts();
}
