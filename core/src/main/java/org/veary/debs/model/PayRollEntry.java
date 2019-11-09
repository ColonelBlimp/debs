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
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.core.Money;

public interface PayRollEntry extends PersistentObject {

    enum Fields {
        ID("ID"), //$NON-NLS-1$
        CREATED("CREATED"), //$NON-NLS-1$
        DELETED("DELETED"), //$NON-NLS-1$
        EMPLOYEE_ID("EMPLOYEE_ID"),
        TDATE("TDATE"),
        REFERENCE("REFERENCE"),
        GROSS("GROSS"),
        TAX("TAX"),
        NET("NET");

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

    Long getEmployeeId();

    LocalDate getDate();

    String getReference();

    Money getGross();

    Money getTax();

    Money getNet();
}
