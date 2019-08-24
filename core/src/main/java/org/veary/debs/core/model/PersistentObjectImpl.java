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

import java.time.LocalDateTime;
import java.util.Objects;

import org.veary.debs.Messages;
import org.veary.debs.model.PersistentObject;

/**
 * <b>Purpose:</b>
 *
 * <b>Responsibility:</b>
 *
 * @author Marc L. Veary
 * @since 1.0
 */
abstract class PersistentObjectImpl implements PersistentObject {

    /**
     * The default value for unique identifier fields.
     */
    public static final Long DEFAULT_ID = Long.valueOf(0);

    private Long id;
    private boolean isDeleted;
    private LocalDateTime created;

    PersistentObjectImpl() {
        this.id = DEFAULT_ID;
        this.created = LocalDateTime.now();
        this.isDeleted = false;
    }

    @Override
    public final Long getId() {
        return this.id;
    }

    @Override
    public final boolean isDeleted() {
        return this.isDeleted;
    }

    @Override
    public final LocalDateTime getCreationTimestamp() {
        return this.created;
    }

    public void setId(Long id) {
        this.id = Objects.requireNonNull(id, Messages.getParameterIsNull("id"));
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setCreationTimestamp(LocalDateTime created) {
        this.created = Objects.requireNonNull(created, Messages.getParameterIsNull("created"));
    }
}
