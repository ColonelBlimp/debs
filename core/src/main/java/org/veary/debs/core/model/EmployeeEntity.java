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

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.Employee;

public class EmployeeEntity extends PersistentObjectImpl implements Employee {

    private final String fullname;
    private final String contactNumber;
    private final String nationalIdNumber;

    public EmployeeEntity(String fullname, String nationalIdNumber, String contactNumber) {
        this.fullname = Objects.requireNonNull(fullname, Messages.getParameterIsNull("fullname"));
        this.contactNumber = Objects.requireNonNull(contactNumber,
            Messages.getParameterIsNull("contactNumber"));
        this.nationalIdNumber = Objects.requireNonNull(nationalIdNumber,
            Messages.getParameterIsNull("nationalIdNumber"));
    }

    public EmployeeEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(Employee.Fields.class));

        setId((Long) dataMap.get(Employee.Fields.ID.toString()));
        setDeleted(((Boolean) dataMap.get(Employee.Fields.DELETED.toString())).booleanValue());
        setCreationTimestamp(
            ((Timestamp) dataMap.get(Employee.Fields.CREATED.toString())).toLocalDateTime());

        this.fullname = (String) dataMap.get(Employee.Fields.FULLNAME.toString());
        this.contactNumber = (String) dataMap.get(Employee.Fields.CONTACT_NUMBER.toString());
        this.nationalIdNumber = (String) dataMap.get(Employee.Fields.NID.toString());
    }

    @Override
    public String getFullname() {
        return this.fullname;
    }

    @Override
    public String getContactNumber() {
        return this.contactNumber;
    }

    @Override
    public String getNationalIdNumber() {
        return this.nationalIdNumber;
    }
}
