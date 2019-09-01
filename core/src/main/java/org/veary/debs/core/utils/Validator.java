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

import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;

/**
 * <b>Purpose:</b> Various validator utilities.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class Validator {

    /**
     * Validate the state of the given {@code Map}.
     *
     * <p>This method ensures that the given {@code Map} is not {@code null} or {@code empty},
     * contains the right number of elements and all the required keys are present. It also
     * checks that the map contains no {@code null} values.
     *
     * @param dataMap the {@code Map} object
     * @param fields an String array of map keys
     */
    public static void validateDataMap(Map<String, Object> dataMap, String[] fields) {
        Objects.requireNonNull(dataMap, Messages.getParameterIsNull("dataMap")); //$NON-NLS-1$
        Objects.requireNonNull(fields, Messages.getParameterIsNull("fields")); //$NON-NLS-1$

        if (dataMap.size() != fields.length) {
            throw new IllegalStateException(
                Messages.getString("Validator.validateDataMap.entrycount", fields.length)); //$NON-NLS-1$
        }

        for (String fieldName : fields) {
            if (!dataMap.containsKey(fieldName)) {
                throw new IllegalStateException(
                    Messages.getString("Validator.validateDataMap.missingkey")); //$NON-NLS-1$
            }
            if (dataMap.get(fieldName) == null) {
                throw new NullPointerException(
                    Messages.getString("Validator.validateDataMap.nullvalue")); //$NON-NLS-1$
            }
        }
    }

    /**
     * Returns a {@code String[]} containing all the values of the given Enum Class.
     *
     * @param <E> The type
     * @param enumClass the Enum class
     * @return an array of Strings
     */
    public static <E extends Enum<E>> String[] getEnumValuesAsStringArray(Class<E> enumClass) {
        final E[] list = enumClass.getEnumConstants();
        final String[] fields = new String[list.length];
        int index = 0;
        for (final E entry : list) {
            fields[index++] = entry.toString();
        }
        return fields;
    }

    /**
     * Private constructor.
     */
    private Validator() {
    }
}
