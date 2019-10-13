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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.model.Employee;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class EmployeeTest {

    private static final String FULLNAME = "Test Fullname";
    private static final String NID = "Test1234567890";
    private static final String CONTACT_NUMBER = "0000000000";

    @Test(expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Parameter 'fullname' cannot be null")
    public void fullnameNullPointerException() {
        Employee.newInstance(null, NID, CONTACT_NUMBER);
    }

    @Test(expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Parameter 'nationalIdNumber' cannot be null")
    public void nidEmptyException() {
        Employee.newInstance(FULLNAME, null, CONTACT_NUMBER);
    }

    @Test(expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Parameter 'contactNumber' cannot be null")
    public void contactNumberNullPointerException() {
        Employee.newInstance(FULLNAME, NID, null);
    }

    public void instantiation() {
        Employee object = Employee.newInstance(FULLNAME, NID, CONTACT_NUMBER);
        Assert.assertNotNull(object);
        Assert.assertEquals(object.getFullname(), FULLNAME);
        Assert.assertEquals(object.getNationalIdNumber(), NID);
        Assert.assertEquals(object.getContactNumber(), CONTACT_NUMBER);
    }
}
