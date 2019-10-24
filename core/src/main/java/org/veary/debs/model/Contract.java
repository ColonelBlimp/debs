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
import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.model.ContractEntity;

/**
 * <b>Purpose:</b> Defines the contract for all employee contract objects.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface Contract extends PersistentObject {

    enum Fields {
        ID("ID"),
        DELETED("DELETED"),
        CREATED("CREATED"),
        EMPLOYEE_ID("EMPLOYEE_ID"),
        START_DATE("START_DATE"),
        END_DATE("EXPIRY_DATE"),
        MONTHLY_SALARY("MONTHLY_SALARY");

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
     * Returns the unique identifier of the {@link Employee} object linked to this
     * {@code Contract}.
     *
     * @return {@code Long}
     */
    Long getEmployeeId();

    /**
     * Returns the start date of this contact.
     *
     * @return {@link LocalDate}
     */
    LocalDate getStartDate();

    /**
     * Returns the end date of this contact.
     *
     * @return {@link LocalDate}
     */
    LocalDate getExpiryDate();

    /**
     * Returns the monthly salary for this contact.
     *
     * @return {@link Money}
     */
    Money getMonthlySalary();

    /**
     * Static method for creating a new Contract object.
     * 
     * @param employeeId
     * @param startDate
     * @param expiryDate
     * @param monthlySalary
     * @return
     */
    static Contract newInstance(Long employeeId, LocalDate startDate, LocalDate expiryDate,
        Money monthlySalary) {
        return new ContractEntity(employeeId, startDate, expiryDate, monthlySalary);
    }

    /**
     * Static method for creating a new Contract object from persisted data.
     *
     * <p><b>Note:</b> this method is required by the {@code org.veary.persist} library.
     *
     * @param dataMap a {@code Map<String, Object>} holding the data with which to populate the
     *     new {@code Contract} instance.
     * @return {@code Contract} new instance. Non-{@code null}.
     */
    static Contract newInstance(Map<String, Object> dataMap) {
        return new ContractEntity(dataMap);
    }
}
