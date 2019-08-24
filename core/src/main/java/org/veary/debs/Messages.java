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

package org.veary.debs;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <b>Purpose:</b> Remove string messages from code.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class Messages {

    private static final String PARAMETER_NULL = "global.parameter.error.null"; //$NON-NLS-1$

    private static final String BUNDLE_NAME = "org.veary.debs.messages"; //$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Static method to lookup a messsage.
     *
     * @param key key to lookup in the {@code messages.properties} file
     * @param vars list of {@code String} variables
     * @return {@code String}
     */
    public static String getString(String key, Object... vars) {
        try {
            return String.format(RESOURCE_BUNDLE.getString(key), vars);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * Helper method for particular use with {@code Objects.requireNonNull}.
     *
     * @param parameterName the parameter name
     * @return {@code String}
     */
    public static String getParameterIsNull(String parameterName) {
        return getString(PARAMETER_NULL, parameterName);
    }

    /**
     * Private Constructor.
     */
    private Messages() {
    }
}
