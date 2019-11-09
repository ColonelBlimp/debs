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
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.veary.debs.Messages;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.PayRollEntry;

public final class PayRollEntryEntity extends PersistentObjectImpl implements PayRollEntry {

    private Long employeeId;
    private LocalDate date;
    private String reference;
    private Money gross;
    private Money tax;
    private Money net;

    public PayRollEntryEntity(Long employeeId, LocalDate date, String reference) {
        this.employeeId = Objects.requireNonNull(employeeId,
            Messages.getParameterIsNull("employeeId"));
        this.date = Objects.requireNonNull(date, Messages.getParameterIsNull("date"));
        if (StringUtils.isBlank(reference)) {
            throw new IllegalArgumentException("");
        }
    }

    public PayRollEntryEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(Fields.class));

        setId((Long) dataMap.get(Fields.ID.toString()));
        setDeleted(((Boolean) dataMap.get(Fields.DELETED.toString())).booleanValue());
        setCreationTimestamp(
            ((Timestamp) dataMap.get(Fields.CREATED.toString())).toLocalDateTime());

    }

    @Override
    public Long getEmployeeId() {
        return this.employeeId;
    }

    @Override
    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public String getReference() {
        return this.reference;
    }

    @Override
    public Money getGross() {
        return this.gross;
    }

    @Override
    public Money getTax() {
        return this.tax;
    }

    @Override
    public Money getNet() {
        return this.net;
    }
}
