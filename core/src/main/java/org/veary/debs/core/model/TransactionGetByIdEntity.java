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

package org.veary.debs.core.model;

import java.lang.reflect.Field;
import java.util.Map;

import org.veary.debs.core.Money;
import org.veary.debs.core.utils.Validator;
import org.veary.debs.model.Entry;

/**
 * <b>Purpose:</b> ?
 *
 * <p><b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class TransactionGetByIdEntity extends PersistentObjectImpl {

    enum Fields {
        ID("ID"), //$NON-NLS-1$
        CREATED("CREATED"), //$NON-NLS-1$
        DELETED("DELETED"), //$NON-NLS-1$
        REFERENCE("REFERENCE"), //$NON-NLS-1$
        NARRATIVE("NARRATIVE"), //$NON-NLS-1$
        FROM_ID("FROM_ID"), //$NON-NLS-1$
        FROM_AMOUNT("FROM_AMOUNT"), //$NON-NLS-1$
        FROM_ETYPE("FROM_ETYPE"), //$NON-NLS-1$
        FROM_ACCOUNT_ID("FROM_ACCOUNT_ID"), //$NON-NLS-1$
        TO_ID("TO_ID"), //$NON-NLS-1$
        TO_AMOUNT("TO_AMOUNT"), //$NON-NLS-1$
        TO_ETYPE("TO_ETYPE"), //$NON-NLS-1$
        TO_ACCOUNT_ID("TO_ACCOUNT_ID"); //$NON-NLS-1$

        private final String name;

        Fields(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private String reference;
    private String narrative;

    private Long fromEntryId;
    private Money fromEntryAmount;
    private Entry.Types fromEntryType;
    private Long fromAccountId;

    private Long toEntryId;
    private Money toEntryAmount;
    private Entry.Types toEntryType;
    private Long toAccountId;

    public TransactionGetByIdEntity(Map<String, Object> dataMap) {
        Validator.validateDataMap(dataMap,
            Validator.getEnumValuesAsStringArray(TransactionGetByIdEntity.Fields.class));
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNarrative() {
        return this.narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public Long getFromEntryId() {
        return this.fromEntryId;
    }

    public void setFromEntryId(Long fromEntryId) {
        this.fromEntryId = fromEntryId;
    }

    public Money getFromEntryAmount() {
        return this.fromEntryAmount;
    }

    public void setFromEntryAmount(Money fromEntryAmount) {
        this.fromEntryAmount = fromEntryAmount;
    }

    public Entry.Types getFromEntryType() {
        return this.fromEntryType;
    }

    public void setFromEntryType(Entry.Types fromEntryType) {
        this.fromEntryType = fromEntryType;
    }

    public Long getFromAccountId() {
        return this.fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToEntryId() {
        return this.toEntryId;
    }

    public void setToEntryId(Long toEntryId) {
        this.toEntryId = toEntryId;
    }

    public Money getToEntryAmount() {
        return this.toEntryAmount;
    }

    public void setToEntryAmount(Money toEntryAmount) {
        this.toEntryAmount = toEntryAmount;
    }

    public Entry.Types getToEntryType() {
        return this.toEntryType;
    }

    public void setToEntryType(Entry.Types toEntryType) {
        this.toEntryType = toEntryType;
    }

    public Long getToAccountId() {
        return this.toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public static final TransactionGetByIdEntity newInstance(Map<String, Object> dataMap) {
        return new TransactionGetByIdEntity(dataMap);
    }

    /**
     * For debugging purposes only.
     */
    @Override
    public String toString() {
        String NL = System.lineSeparator();
        Class<?> clazz = this.getClass();
        StringBuilder sb = new StringBuilder(NL).append(clazz.getSimpleName());
        sb.append(" {").append(NL); //$NON-NLS-1$

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isSynthetic()) {
                sb.append("  ").append(field.getName()).append(": "); //$NON-NLS-1$ //$NON-NLS-2$
                try {
                    Object value = field.get(this);
                    sb.append(value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    sb.append("{error}"); //$NON-NLS-1$
                }
                sb.append(NL);
            }
        }
        sb.append("}"); //$NON-NLS-1$
        return sb.toString();
    }
}
