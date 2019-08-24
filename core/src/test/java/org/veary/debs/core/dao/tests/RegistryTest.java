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

package org.veary.debs.core.dao.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.dao.RealRegistry;
import org.veary.debs.exceptions.DebsException;

public class RegistryTest {

    private RealRegistry registry;

    @Test(expectedExceptions = DebsException.class)
    public void instantationException() {
        new RealRegistry("unknown.xml");
    }

    @Test
    public void instantiationWithoutFilename() {
        this.registry = new RealRegistry();
    }

    @Test(dependsOnMethods = { "instantiationWithoutFilename" })
    public void getSqlMethod() {
        String sql = this.registry.getSql("createAccount");
        Assert.assertEquals(sql,
            "INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES(?,?,?,?)");
    }

    @Test(
        dependsOnMethods = { "instantiationWithoutFilename" },
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Unknown key: badkey")
    public void missingKeyException() {
        this.registry.getSql("badkey");
    }
}
