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

import java.io.File;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.dao.RealRegistry;

public class RegistryTest {

    private RealRegistry registry;

    @Test
    public void instantiationWithoutFilename() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sql").toURI());
        this.registry = new RealRegistry(file.toString());
    }

    @Test(dependsOnMethods = { "instantiationWithoutFilename" })
    public void getSqlMethod() {
        String sql = this.registry.getSql("createAccount"); //$NON-NLS-1$
        Assert.assertEquals(sql,
            "INSERT INTO DEBS.ACCOUNT(NAME,DESCRIPTION,PARENT_ID,ACCOUNT_TYPE) VALUES(?,?,?,?)"); //$NON-NLS-1$
    }

    @Test(
        dependsOnMethods = { "instantiationWithoutFilename" },
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Unknown key: badkey")
    public void missingKeyException() {
        this.registry.getSql("badkey"); //$NON-NLS-1$
    }
}
