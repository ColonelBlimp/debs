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

package org.veary.debs.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import org.veary.debs.Messages;

/**
 * <b>Purpose:</b> Represents a monetary value. Internally, the value is stored as a
 * {@link BigDecimal} with a <i>scale</i> of 2 and <i>rounding</i> set to
 * {@link RoundingMode#HALF_EVEN}.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class Money {

    private static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;
    private static final int SCALE = 2;
    private static final String THAT_PARAM = "that"; //$NON-NLS-1$

    private final BigDecimal value;

    /**
     * Constructor. The given {@link BigDecimal} will be have it's scale set to in the
     * constructor.
     *
     * @param value {@link BigDecimal}
     */
    public Money(BigDecimal value) {
        this.value = Objects.requireNonNull(value,
            Messages.getParameterIsNull("value")).setScale(SCALE, ROUNDING); //$NON-NLS-1$
    }

    /**
     * Return the value as a {@link BigDecimal}.
     *
     * @return {@link BigDecimal}
     */
    public BigDecimal getValue() {
        return this.value;
    }

    /**
     * Returns <code>true</code> <b>only</b> if the amount is <i>positive</i>.
     *
     * @return boolean
     */
    public boolean isPlus() {
        return this.value.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Returns <code>true</code> <b>only</b> if the amount is <i>negative</i>.
     *
     * @return boolean
     */
    public boolean isMinus() {
        return this.value.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Returns <code>true</code> <b>only</b> if the amount is <i>zero</i>.
     *
     * @return boolean
     */
    public boolean isZero() {
        return this.value.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Return the amount x (-1).
     *
     * @return a new <code>Money</code> object.
     */
    public Money negate() {
        return times(-1);
    }

    /**
     * Add {@code that} {@code Money} to this {@code Money}.
     *
     * @param that {@code Money}
     * @return a new {@code Money} object instance.
     */
    public Money plus(Money that) {
        return new Money(
            this.value.add(
                Objects.requireNonNull(that, Messages.getParameterIsNull(THAT_PARAM)).value));
    }

    /**
     * Subtract {@code that Money} from this {@code Money}.
     *
     * @param that {@code Money}
     * @return a new {@code Money} object instance.
     */
    public Money minus(Money that) {
        return new Money(
            this.value.subtract(
                Objects.requireNonNull(that, Messages.getParameterIsNull(THAT_PARAM)).value));
    }

    /**
     * Multiply this <code>Money</code> by an integral factor. The scale of the returned
     * <code>Money</code> is equal to the scale of 'this' <code>Money</code>.
     *
     * @param factor a double
     * @return a new <code>Money</code> object.
     */
    public Money times(double factor) {
        return new Money(this.value.multiply(BigDecimal.valueOf(factor)));
    }

    /**
     * Equals (sensitive to scale). This method is <em>not</em> synonymous with the
     * {@code equals} method.
     *
     * @param that {@code Money} object to compare
     * @return {@code true} if amounts are equal, otherwise {@code false}
     */
    public boolean eq(Money that) {
        return this.value.equals(
            Objects.requireNonNull(that, Messages.getParameterIsNull(THAT_PARAM)).value);
    }

    @Override
    public String toString() {
        return String.format("%.2f", this.value.doubleValue()); //$NON-NLS-1$
    }
}
