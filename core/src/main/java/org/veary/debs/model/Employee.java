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

import java.util.Map;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.model.EmployeeEntity;

/**
 * <b>Purpose:</b> Defines the contract for all employee objects.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public interface Employee extends PersistentObject {

    enum Fields {
        ID("ID"),
        CREATED("CREATED"),
        DELETED("DELETED"),
        FULLNAME("FULLNAME"),
        NID("NID"),
        CONTACT_NUMBER("CONTACT_NUMBER");

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
     * The fullname, includes firstname, middle names and family name. This is used for all
     * formal documents produced by the system.
     *
     * @return {@code String}
     */
    String getFullname();

    /**
     * The main contact number (mobile).
     *
     * @return {@code String}. A non-{@code null}, but can return an empty string.
     */
    String getContactNumber();

    /**
     * The national id number.
     *
     * @return {@code String}. A non-{@code null}, but can return an empty string.
     */
    String getNationalIdNumber();

    static Employee newInstance(String fullname, String nationalIdNumber, String contactNumber) {
        return new EmployeeEntity(fullname, nationalIdNumber, contactNumber);
    }

    static Employee newInstance(Map<String, Object> dataMap) {
        return new EmployeeEntity(dataMap);
    }
}
