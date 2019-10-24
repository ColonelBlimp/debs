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

import com.google.inject.Injector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.veary.debs.facade.AdminFacade;

/**
 * <b>Purpose:</b> Application specific {@link ServletContextListener}.
 *
 * <p><b>Responsibility:</b> Checks if the {@code WEB-INF/db} directory exists, if it does not,
 * then it is created and the database initialized.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
public class DebsContextListener implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger(DebsContextListener.class);
    private static final String LOG_CALLED = "called";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.trace(LOG_CALLED);
        if (sce.getServletContext().getAttribute("INIT_DB") != null) {
            final Injector injector = (Injector) sce.getServletContext()
                .getAttribute(Injector.class.getName());
            LOG.info("Initializing the database");
            final AdminFacade facade = injector.getInstance(AdminFacade.class);
            facade.initializeDatabase();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.trace(LOG_CALLED);
        final Injector injector = (Injector) sce.getServletContext()
            .getAttribute(Injector.class.getName());
        final AdminFacade facade = injector.getInstance(AdminFacade.class);
        facade.backupDatabase(
            (String) sce.getServletContext().getAttribute(GuiceContextListener.BACKUP_DIR_KEY));
    }
}
