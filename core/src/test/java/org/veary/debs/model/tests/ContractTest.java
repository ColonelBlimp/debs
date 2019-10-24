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

package org.veary.debs.model.tests;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.model.Contract;

public class ContractTest {

    private static final Long EMPLOYEE_ID = Long.valueOf(2019010100001L);
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate EXPIRY_DATE = START_DATE.plusMonths(12);
    private static final Money MONTHLY_SALARY = new Money(BigDecimal.valueOf(60000));

    @Test
    public void instantiation() {
        Contract object = Contract.newInstance(EMPLOYEE_ID, START_DATE, EXPIRY_DATE,
            MONTHLY_SALARY);
        Assert.assertNotNull(object);
        Assert.assertEquals(object.getEmployeeId(), EMPLOYEE_ID);
        Assert.assertEquals(object.getStartDate(), START_DATE);
        Assert.assertEquals(object.getExpiryDate(), EXPIRY_DATE);
        Assert.assertEquals(object.getMonthlySalary(), MONTHLY_SALARY);

        Contract other = Contract.newInstance(EMPLOYEE_ID, START_DATE, EXPIRY_DATE,
            MONTHLY_SALARY);

        Assert.assertTrue(object.equals(other));
    }
}
