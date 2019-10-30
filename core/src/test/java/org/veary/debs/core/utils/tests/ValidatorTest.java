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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.Account;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class ValidatorTest {

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp = "Parameter 'dataMap' must contain exactly 8 entries")
    public void entryCountException() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Account.Fields.ID.toString(), Long.valueOf(1));
        dataMap.put(Account.Fields.CREATED.toString(), LocalDateTime.now());
        dataMap.put(Account.Fields.BALANCE.toString(), new Money(BigDecimal.valueOf(100000L)));
        dataMap.put(Account.Fields.DELETED.toString(), Boolean.FALSE);
        dataMap.put(Account.Fields.NAME.toString(), "Test"); //$NON-NLS-1$
        dataMap.put(Account.Fields.DESCRIPTION.toString(), "Desc"); //$NON-NLS-1$
        dataMap.put(Account.Fields.PARENT_ID.toString(), Long.valueOf(1));
        Account.newInstance(dataMap);
    }

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp = "Parameter 'dataMap' does not contain the required keys")
    public void missingKeyException() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Account.Fields.ID.toString(), Long.valueOf(1));
        dataMap.put(Account.Fields.CREATED.toString(), LocalDateTime.now());
        dataMap.put(Account.Fields.BALANCE.toString(), new Money(BigDecimal.valueOf(100000L)));
        dataMap.put(Account.Fields.DELETED.toString(), Boolean.FALSE);
        dataMap.put(Account.Fields.NAME.toString(), "Test"); //$NON-NLS-1$
        dataMap.put(Account.Fields.DESCRIPTION.toString(), "Desc"); //$NON-NLS-1$
        dataMap.put(Account.Fields.PARENT_ID.toString(), Long.valueOf(1));
        dataMap.put("Bad Key", Account.Types.ASSET); //$NON-NLS-1$
        Account.newInstance(dataMap);
    }

    @Test(
        expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Parameter 'dataMap' cannot contain null values")
    public void nullValueException() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Account.Fields.ID.toString(), Long.valueOf(1));
        dataMap.put(Account.Fields.CREATED.toString(), LocalDateTime.now());
        dataMap.put(Account.Fields.BALANCE.toString(), new Money(BigDecimal.valueOf(100000L)));
        dataMap.put(Account.Fields.DELETED.toString(), Boolean.FALSE);
        dataMap.put(Account.Fields.NAME.toString(), "Test"); //$NON-NLS-1$
        dataMap.put(Account.Fields.DESCRIPTION.toString(), null);
        dataMap.put(Account.Fields.PARENT_ID.toString(), Long.valueOf(1));
        dataMap.put(Account.Fields.ACCOUNT_TYPE.toString(), Account.Types.ASSET);
        Account.newInstance(dataMap);
    }

    @Test(
        expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "Test Message Null")
    public void requireNonEmptyNull() {
        Validator.requireNonEmpty(null, "Test Message Null");
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Test Message Empty")
    public void requireNonEmptyEmpty() {
        Validator.requireNonEmpty("", "Test Message Empty");
    }

    @Test
    public void requireNonEmptyMethod() {
        Assert.assertEquals(Validator.requireNonEmpty("Test", "Error"), "Test");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void validateTextFieldIllegalChar() {
        Validator.validateTextField("#");
    }

    @Test(expectedExceptions = NullPointerException.class,
        expectedExceptionsMessageRegExp = "A text field cannot be be null or empty")
    public void validateTextFieldNull() {
        Validator.validateTextField(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "A text field cannot be be null or empty")
    public void validateTextFieldEmpty() {
        Validator.validateTextField("");
    }

    @Test
    public void validateTextField() {
        Assert.assertEquals(Validator.validateTextField("Test1 ()-"), "Test1 ()-");
    }

    @Test
    public void validateNationalIdField() {
        Assert.assertEquals(Validator.validateCitizenID("QAS1EDFR"), "QAS1EDFR");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "The ID field can contain only digits or UPPERCASE letters")
    public void validateNationalIdFieldExceptionA() {
        Validator.validateCitizenID("QAS-EDFR");
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "The ID field can contain only digits or UPPERCASE letters")
    public void validateNationalIdFieldExceptionB() {
        Validator.validateCitizenID("QASzEDFR");
    }
}
