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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.jndi.JndiIntegration;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.inject.struts2.Struts2GuicePluginModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;
import org.veary.debs.core.GuiceDebsCoreModule;
import org.veary.debs.exceptions.DebsException;
import org.veary.debs.web.internal.PdfDocumentGenerator;
import org.veary.debs.web.struts2.DocumentGenerator;
import org.veary.debs.web.struts2.PageBean;
import org.veary.debs.web.struts2.actions.beans.RealPageBean;

/**
 * <p> Purpose:</b> Extension of {@link GuiceServletContextListener} for integration with Tomcat
 * and Struts2.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public final class GuiceContextListener extends GuiceServletContextListener {

    public static final String VOUCHER_DIR_KEY = "VOUCHER_DIR";

    private static final Logger LOG = LogManager.getLogger(GuiceContextListener.class);
    private static final String LOG_CALLED = "called";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        LOG.trace(LOG_CALLED);

        String voucherDir = servletContextEvent.getServletContext().getRealPath("vouchers");
        LOG.trace("Voucher Directory: {}", () -> voucherDir);

        try {
            Path vDir = Files.createDirectories(Paths.get(voucherDir));
            servletContextEvent.getServletContext().setAttribute(VOUCHER_DIR_KEY, vDir);
        } catch (IOException e) {
            throw new DebsException(e);
        }
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
            new GuiceDebsCoreModule(),
            new AbstractModule() {

                @Override
                protected void configure() {
                    bind(Context.class).to(InitialContext.class);
                    bind(DataSource.class)
                        .toProvider(JndiIntegration.fromJndi(DataSource.class,
                            "java:/comp/env/jdbc/debs"));
                    bind(PageBean.class).to(RealPageBean.class);
                    bind(DocumentGenerator.class).to(PdfDocumentGenerator.class);
                }
            },
            new Struts2GuicePluginModule(),
            new ServletModule() {

                @Override
                protected void configureServlets() {
                    bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
                    filter("/*").through(StrutsPrepareAndExecuteFilter.class);
                }
            });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOG.trace(LOG_CALLED);

        super.contextDestroyed(servletContextEvent);
    }
}
