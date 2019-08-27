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

package org.veary.debs.core.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import org.veary.debs.Messages;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class DaoUtils {

    /**
     * Takes a {@code java.sql.Timestamp} object and returns a {@code LocalDateTime} object.
     *
     * @param ts {@code java.sql.Timestamp}
     * @return {@code LocalDateTime}
     */
    public static LocalDateTime localDateTimeFromSqlTimestamp(Timestamp ts) {
        return LocalDateTime.ofInstant(
            Objects.requireNonNull(ts, Messages.getParameterIsNull("ts")).toInstant(), //$NON-NLS-1$
            ZoneOffset.ofHours(0));
    }

    /**
     * Takes a {@code java.sql.Date} object and returns a {@code LocalDate} object.
     *
     * @param date {@code java.sql.Date}
     * @return {@code LocalDate}
     */
    public static LocalDate localDateFromSqlDate(Date date) {
        return Objects.requireNonNull(date, Messages.getParameterIsNull("date")).toLocalDate(); //$NON-NLS-1$
    }

    /**
     * Private constructor.
     */
    private DaoUtils() {
    }
}
