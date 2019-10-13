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

import org.veary.debs.model.Employee;

/**
 * <b>Purpose:</b> Encapsulates the employee subsystem into one simple interface.
 *
 * <p><b>Responsibility:</b> Hides the complexity of the subsystem making it easier to use.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface EmployeeFacade {

    /**
     * Add a new employee to the system.
     *
     * @param object {@link Employee} object
     * @return {@code Long} the unique identifier
     */
    Long create(Employee object);

    /**
     * Fetch an {@code Employee} from persistent storage.
     *
     * @param id the unique identifier of the employee to be retrieved
     * @return {@code Optional<Employee>}
     */
    Optional<Employee> getById(Long id);

    /**
     * Fetch an {@code Employee} from persistent storage.
     *
     * @param fullname the unique fullname of the employee to be retrieved
     * @return {@code Optional<Employee>}
     */
    Optional<Employee> getByName(String fullname);

    /**
     * Returns a list of all the employees.
     *
     * @param includeDeleted flag indicating if <i>deleted</i> employee records should be
     *     included
     * @return {@code List}. Non-{@code null}.
     */
    List<Employee> getAllEmployees(boolean includeDeleted);
}
