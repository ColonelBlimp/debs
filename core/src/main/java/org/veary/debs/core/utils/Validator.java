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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.veary.debs.Messages;

/**
 * <b>Purpose:</b> Various validator utilities.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class Validator {

    private static final Pattern money = Pattern
        .compile("^(?:0|[1-9]\\d{0,3})(\\,)?(\\d{3})?(\\,)?(\\d{3})?([.]\\d{2})?");

    private static final List<Character> acceptedChars = Arrays.asList(
        Character.valueOf('('),
        Character.valueOf(')'),
        Character.valueOf('-'),
        Character.valueOf(','),
        Character.valueOf('.'));

    /**
     * Checks the given currency string is not null and is correctly formatted.
     *
     * @param monetaryString a string representation of the currency to be checked.
     * @return {@code true} if valid, otherwise {@code false}.
     */
    public static boolean checkMonetaryFormat(final String monetaryString) {
        if ("".equals(
            Objects.requireNonNull(monetaryString, "Monetary string cannot be null."))) {
            throw new IllegalArgumentException("Monetary string must be non-empty.");
        }
        return money.matcher(monetaryString).matches();
    }

    /**
     * Validate the state of the given {@code Map}.
     *
     * <p>This method ensures that the given {@code Map} is not {@code null} or {@code empty},
     * contains the right number of elements and all the required keys are present. It also checks
     * that the map contains no {@code null} values.
     *
     * @param dataMap the {@code Map} object
     * @param fields an String array of map keys
     */
    public static void validateDataMap(Map<String, Object> dataMap, String[] fields) {
        Objects.requireNonNull(dataMap, Messages.getParameterIsNull("dataMap")); //$NON-NLS-1$
        Objects.requireNonNull(fields, Messages.getParameterIsNull("fields")); //$NON-NLS-1$

        if (dataMap.size() != fields.length) {
            throw new IllegalStateException(Messages
                .getString("Validator.validateDataMap.entrycount", //$NON-NLS-1$
                    Integer.valueOf(fields.length)));
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
     * Checks the given string is not {@code null} and is non-empty.
     *
     * @param str the {@code String} to be checked
     * @return {@code String}
     */
    public static String requireNonEmpty(String str, String message) {
        Objects.requireNonNull(str, message);
        if (str.equals("")) {
            throw new IllegalArgumentException(message);
        }
        return str;
    }

    /**
     * Checks the given string is not {@code null} or {@code empty} and has only allowed
     * characters.
     *
     * @param str the {@code String} to be checked.
     * @return the validated string
     * @throws IllegalArgumentException is the string contains unauthorized characters or is
     *     {@code empty}
     * @throws NullPointerException if the string is {@code null}
     */
    public static String validateTextField(String str) {
        final StringCharacterIterator iterator = new StringCharacterIterator(
            requireNonEmpty(str, "A text field cannot be be null or empty"));
        char ch = iterator.current();
        while (ch != CharacterIterator.DONE) {
            final boolean validChar = (Character.isLetterOrDigit(ch)
                || Character.isSpaceChar(ch)
                || acceptedChars.contains(Character.valueOf(ch)));

            if (!validChar) {
                throw new IllegalArgumentException(
                    "A text field can contain only: a-zA-Z0-9 ()-");
            }
            ch = iterator.next();
        }
        return str;
    }

    /**
     * Validates a Malawi Citizen Identification number.
     *
     * @param id {@code String}
     * @return the validates {@code String}
     */
    public static String validateCitizenId(String id) {
        final StringCharacterIterator iterator = new StringCharacterIterator(
            requireNonEmpty(id, "A text field cannot be be null or empty"));
        char ch = iterator.current();
        while (ch != CharacterIterator.DONE) {
            if (!Character.isUpperCase(ch) && !Character.isDigit(ch)) {
                throw new IllegalArgumentException(
                    "The ID field can contain only digits or UPPERCASE letters");
            }
            ch = iterator.next();
        }
        return id;
    }

    /**
     * Private constructor.
     */
    private Validator() {
    }
}
