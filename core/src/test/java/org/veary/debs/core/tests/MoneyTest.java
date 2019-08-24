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

package org.veary.debs.core.tests;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;

public class MoneyTest {

    private static final BigDecimal VALUE = BigDecimal.valueOf(100000L);

    @Test
    public void money() {
        Money money = new Money(VALUE);
        Assert.assertNotNull(money);
        Assert.assertTrue(money.getValue().equals(VALUE.setScale(2)));
        Assert.assertTrue(money.eq(new Money(VALUE)));
        Assert.assertTrue(money.isPlus());
        Assert.assertFalse(money.isMinus());
        Assert.assertFalse(money.isZero());
        money = money.minus(new Money(VALUE));
        Assert.assertTrue(money.isZero());
        Assert.assertFalse(money.isPlus());
        Assert.assertFalse(money.isMinus());
        money = money.minus(new Money(VALUE));
        Assert.assertTrue(money.isMinus());
        money = money.plus(new Money(VALUE));
        Assert.assertTrue(money.isZero());
        money = money.plus(new Money(VALUE)).negate();
        Assert.assertTrue(money.isMinus());
        Assert.assertTrue(money.eq(new Money(BigDecimal.valueOf(-100000L))));
    }
}
