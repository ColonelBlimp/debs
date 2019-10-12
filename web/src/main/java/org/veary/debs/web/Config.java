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

package org.veary.debs.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.veary.debs.exceptions.DebsException;

/**
 * <b>Purpose:</b> Global (web app) configuration utility.
 *
 * <p><b>Responsibility:</b> Generally, to provide default settings.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class Config {

    private final Properties props;

    /**
     * Constructor.
     */
    public Config() {
        this.props = new Properties();
        try (InputStream in = Config.class.getResourceAsStream("defaults.properties")) {
            this.props.load(in);
        } catch (IOException e) {
            throw new DebsException(e);
        }
    }

    /**
     * Returns a String associated with the given {@code key}. If the key is not found, an empty
     * string is returned.
     *
     * @param key the key
     * @return a {@code String}
     */
    public String get(String key) {
        return this.props.getProperty(key, "");
    }
}
