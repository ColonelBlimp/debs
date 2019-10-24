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

package org.veary.debs.tests;

import com.google.inject.jndi.JndiIntegration;
import com.google.inject.name.Names;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.veary.debs.core.GuiceDebsCoreModule;

public class GuicePersistTestModule extends GuiceDebsCoreModule {

    @Override
    protected void configure() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sql").getFile());

        bindConstant().annotatedWith(Names.named("SQL_DIR")).to(file.getAbsolutePath());
        bind(Context.class).to(InitialContext.class);
        bind(DataSource.class)
            .toProvider(JndiIntegration.fromJndi(DataSource.class, "java:/comp/env/jdbc/debs")); //$NON-NLS-1$
        super.configure();
    }
}
