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

package org.veary.debs.core.dao;

import com.google.inject.name.Named;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.veary.debs.Messages;
import org.veary.debs.dao.Registry;
import org.veary.debs.exceptions.DebsException;

/**
 * <b>Purpose:</b> Provides a central place for holding the system's SQL statements.
 *
 * <p><b>Responsibility:</b> Handles loading externally stored SQL.
 *
 * <p><b>Note:</b> Annotated for JSR330.
 *
 * @author Marc L. Veary
 * @since 1.0
 */
@Singleton
public final class RealRegistry implements Registry {

    /**
     * <b>Purpose:</b> Function interface.
     *
     * @author Marc L. Veary
     * @since 1.0
     */
    public interface NodeParser {
        void parse(Node node);
    }

    private static final Logger LOG = LogManager.getLogger(RealRegistry.class);
    private static final String LOG_CALLED = "called"; //$NON-NLS-1$
    private static final String XPATH_ATTRIB_NAME = "@name"; //$NON-NLS-1$

    private final Map<String, String> systemRegistry;

    /**
     * Constructor. Assumes that the file containing the system's SQL statements is named
     * {@code system.xml} and is located on the classpath.
     *
     * @param srcDir source directory for the XML files containing the system's SQL statements
     */
    @Inject
    public RealRegistry(@Named("SQL_DIR") String srcDir) {
        LOG.trace(LOG_CALLED);
        this.systemRegistry = new HashMap<>();
        init(srcDir); //$NON-NLS-1$
    }

    @Override
    public String getSql(String key) {
        LOG.trace(LOG_CALLED);
        if (!this.systemRegistry.containsKey(Objects.requireNonNull(key))) {
            throw new IllegalArgumentException(
                Messages.getString("RealRegistry.getSql.missingkey", key)); //$NON-NLS-1$
        }
        return Objects.requireNonNull(this.systemRegistry.get(key));
    }

    private void init(String srcDir) {
        LOG.trace(LOG_CALLED);

        for (String fileName : getSqlFiles(Path.of(srcDir))) {
            LOG.debug("Parsing: {}", () -> fileName);
            readXmlFile(fileName, "system", "/system", node -> { //$NON-NLS-1$ //$NON-NLS-2$
                for (final Node method : node.selectNodes("method")) { //$NON-NLS-1$
                    parseMethodNode(this.systemRegistry, method);
                }
            });
        }
    }

    private void readXmlFile(String fileName, String element, String nodeSelector,
        NodeParser parser) {
        final Document doc = readFileAndGetDocument(Objects.requireNonNull(fileName));
        final Element modelsElement = doc.getRootElement();
        if (modelsElement.getName().equals(Objects.requireNonNull(element))) {
            final List<Node> modelNodes = doc.selectNodes(Objects.requireNonNull(nodeSelector));
            for (final Iterator<Node> it = modelNodes.iterator(); it.hasNext();) {
                parser.parse(it.next());
            }
        }
    }

    private Document readFileAndGetDocument(String fileName) {
        LOG.trace(LOG_CALLED);
        try {
            try (InputStream in = new FileInputStream(fileName)) {
                return new SAXReader().read(in);
            }
        } catch (IOException | DocumentException e) {
            throw new DebsException(e);
        }
    }

    private void parseMethodNode(Map<String, String> map, Node node) {
        final String methodName = node.valueOf(XPATH_ATTRIB_NAME);
        final String cdata = node.getText()
            .replaceAll("\\r|\\n", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
        LOG.trace("Adding method: {}", methodName);
        map.put(methodName, cdata);
    }

    private List<String> getSqlFiles(Path srcDir) {
        LOG.trace(LOG_CALLED);
        List<String> filenames = new ArrayList<>();

        LOG.debug("Loading SQL files from: {}", () -> srcDir);

        try {
            Files.walkFileTree(srcDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                    if (Files.isRegularFile(file)) {
                        filenames.add(file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new DebsException(e);
        }

        return filenames;
    }
}
