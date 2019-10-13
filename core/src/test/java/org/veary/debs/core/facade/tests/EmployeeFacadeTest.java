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

package org.veary.debs.core.facade.tests;

import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.facade.RealEmployeeFacade;
import org.veary.debs.facade.EmployeeFacade;
import org.veary.debs.model.Employee;
import org.veary.debs.tests.JndiTestBase;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class EmployeeFacadeTest extends JndiTestBase {

    private Long id;

    @Test
    public void instantiation() {
        Assert.assertNotNull(new RealEmployeeFacade(this.employeeDao));
    }

    private static final String FULLNAME = "Test Name";
    private static final String NID = "ABC123";
    private static final String CONTACT_NUMBER = "0880000000";

    @Test
    public void createEmployee() {
        EmployeeFacade facade = this.injector.getInstance(EmployeeFacade.class);
        Assert.assertNotNull(facade);

        Employee object = Employee.newInstance(FULLNAME, NID, CONTACT_NUMBER);
        this.id = facade.create(object);
        Assert.assertNotNull(this.id);
        Assert.assertFalse(this.id.equals(Long.valueOf(0)));
    }

    @Test(dependsOnMethods = { "createEmployee" })
    public void getById() {
        EmployeeFacade facade = this.injector.getInstance(EmployeeFacade.class);
        Assert.assertNotNull(facade);

        Optional<Employee> result = facade.getById(this.id);
        Assert.assertTrue(result.isPresent());
        Employee object = result.get();
        Assert.assertEquals(object.getFullname(), FULLNAME);
        Assert.assertEquals(object.getNationalIdNumber(), NID);
        Assert.assertEquals(object.getContactNumber(), CONTACT_NUMBER);
    }

    @Test
    public void getByIdFail() {
        EmployeeFacade facade = this.injector.getInstance(EmployeeFacade.class);
        Assert.assertNotNull(facade);
        Optional<Employee> result = facade.getById(Long.valueOf(12345));
        Assert.assertFalse(result.isPresent());
    }

    @Test(dependsOnMethods = { "createEmployee" })
    public void getByName() {
        EmployeeFacade facade = this.injector.getInstance(EmployeeFacade.class);
        Assert.assertNotNull(facade);

        Optional<Employee> result = facade.getByName(FULLNAME);
        Assert.assertTrue(result.isPresent());
        Employee object = result.get();
        Assert.assertEquals(object.getFullname(), FULLNAME);
        Assert.assertEquals(object.getNationalIdNumber(), NID);
        Assert.assertEquals(object.getContactNumber(), CONTACT_NUMBER);
    }

    @Test
    public void getByNameFail() {
        EmployeeFacade facade = this.injector.getInstance(EmployeeFacade.class);
        Assert.assertNotNull(facade);
        Optional<Employee> result = facade.getByName("Unknown");
        Assert.assertFalse(result.isPresent());
    }
}
