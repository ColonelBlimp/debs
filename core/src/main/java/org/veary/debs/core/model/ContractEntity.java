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
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.Contract;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class ContractEntity extends PersistentObjectImpl implements Contract {

    private volatile int hashCode = 0;

    private final Long employeeId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Money monthlySalary;

    /**
     * Constructor.
     *
     * @param employeeId unique identifier of the associated Employee
     * @param startDate {@link LocalDate}
     * @param endDate {@link LocalDate}
     * @param monthlySalary the monthly salary of this contract
     */
    public ContractEntity(Long employeeId, LocalDate startDate, LocalDate endDate,
        Money monthlySalary) {
        this.employeeId = Objects.requireNonNull(employeeId,
            Messages.getParameterIsNull("employeeId"));
        this.startDate = Objects.requireNonNull(startDate,
            Messages.getParameterIsNull("startDate"));
        this.endDate = Objects.requireNonNull(endDate, Messages.getParameterIsNull("endDate"));
        this.monthlySalary = Objects.requireNonNull(monthlySalary,
            Messages.getParameterIsNull("monthlySalary"));
    }

    /**
     * Constructor.
     *
     * <p><b>Note:</b> This constructor is required by the {@code org.veary.persist} library.
     *
     * @param dataMap {@code Map<String, Object>}
     */
    public ContractEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(Fields.class));

        setId((Long) dataMap.get(Fields.ID.toString()));
        setDeleted(((Boolean) dataMap.get(Fields.DELETED.toString())).booleanValue());
        setCreationTimestamp(
            ((Timestamp) dataMap.get(Fields.CREATED.toString())).toLocalDateTime());

        this.employeeId = (Long) dataMap.get(Fields.EMPLOYEE_ID.toString());
        this.startDate = (LocalDate) dataMap.get(Fields.START_DATE.toString());
        this.endDate = (LocalDate) dataMap.get(Fields.END_DATE.toString());
        this.monthlySalary = new Money(
            (BigDecimal) dataMap.get(Fields.MONTHLY_SALARY.toString()));
    }

    @Override
    public Long getEmployeeId() {
        return this.employeeId;
    }

    @Override
    public LocalDate getStartDate() {
        return this.startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return this.endDate;
    }

    @Override
    public Money getMonthlySalary() {
        return this.monthlySalary;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = Objects.hash(
                getId(),
                Boolean.valueOf(isDeleted()),
                getCreationTimestamp(),
                this.employeeId,
                this.startDate,
                this.endDate,
                this.monthlySalary);
        }
        return this.hashCode;
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof ContractEntity)) {
            return false;
        }

        if (this == that) {
            return true;
        }

        final ContractEntity other = (ContractEntity) that;

        return getId().equals(other.getId())
            && isDeleted() == other.isDeleted()
            && getCreationTimestamp().equals(other.getCreationTimestamp())
            && this.employeeId.equals(other.employeeId)
            && this.startDate.equals(other.startDate)
            && this.endDate.equals(other.endDate)
            && this.monthlySalary.eq(other.monthlySalary);
    }
}
