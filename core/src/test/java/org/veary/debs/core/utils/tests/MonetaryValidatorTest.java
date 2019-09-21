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

package org.veary.debs.core.utils.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.utils.Validator;

public class MonetaryValidatorTest {

    @Test
    public void checkMonetaryFormat() {
        Assert.assertTrue(Validator.checkMonetaryFormat("100,000,000.00"));
        Assert.assertTrue(Validator.checkMonetaryFormat("100,000,000"));
        Assert.assertTrue(Validator.checkMonetaryFormat("1,000,000.00"));
        Assert.assertTrue(Validator.checkMonetaryFormat("1,000,000"));
        Assert.assertTrue(Validator.checkMonetaryFormat("100,000.00"));
        Assert.assertTrue(Validator.checkMonetaryFormat("100,000"));
        Assert.assertTrue(Validator.checkMonetaryFormat("10,000.00"));
        Assert.assertTrue(Validator.checkMonetaryFormat("10,000"));
        Assert.assertTrue(Validator.checkMonetaryFormat("1,000.00"));
        Assert.assertTrue(Validator.checkMonetaryFormat("1,000"));
        Assert.assertTrue(Validator.checkMonetaryFormat("100.00"));
        Assert.assertTrue(Validator.checkMonetaryFormat("100"));
        Assert.assertTrue(Validator.checkMonetaryFormat("10.00"));
        Assert.assertTrue(Validator.checkMonetaryFormat("10"));
        Assert.assertTrue(Validator.checkMonetaryFormat("1.00"));
        Assert.assertTrue(Validator.checkMonetaryFormat("1"));

        //        Assert.assertTrue(Validator.checkMonetaryFormat("-100,000,000.00"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-100,000,000"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-1,000,000.00"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-1,000,000"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-100,000.00"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-100,000"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-10,000.00"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-10,000"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-1,000.00"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-1,000"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-100.00"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-100"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-10.00"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-10"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-1.00"));
        //        Assert.assertTrue(Validator.checkMonetaryFormat("-1"));

        Assert.assertFalse(Validator.checkMonetaryFormat("1."));
        Assert.assertFalse(Validator.checkMonetaryFormat("10."));
        Assert.assertFalse(Validator.checkMonetaryFormat("100."));
        Assert.assertFalse(Validator.checkMonetaryFormat("1000."));
        Assert.assertFalse(Validator.checkMonetaryFormat("1.0"));
        Assert.assertFalse(Validator.checkMonetaryFormat("1.000"));
        Assert.assertFalse(Validator.checkMonetaryFormat("10.0"));
        Assert.assertFalse(Validator.checkMonetaryFormat("A"));
        Assert.assertFalse(Validator.checkMonetaryFormat("1,000,0"));
        Assert.assertFalse(Validator.checkMonetaryFormat("1,000,00"));
        Assert.assertFalse(Validator.checkMonetaryFormat(".11"));
        Assert.assertFalse(Validator.checkMonetaryFormat("*.##"));
        Assert.assertFalse(Validator.checkMonetaryFormat("MWK100"));

    }

    @Test(
        expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Monetary string cannot be null.")
    public void checkMonetaryFormatExceptionOne() {
        Validator.checkMonetaryFormat(null);
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Monetary string must be non-empty.")
    public void checkMonetaryFormatExceptionTwo() {
        Validator.checkMonetaryFormat("");
    }

}
