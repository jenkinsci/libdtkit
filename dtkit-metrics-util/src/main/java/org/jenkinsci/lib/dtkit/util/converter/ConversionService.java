/*******************************************************************************
 * Copyright (c) 2010 Thales Corporate Services SAS                             *
 * Author : Gregory Boissinot, Guillaume Tanier                                 *
 *                                                                              *
 * Permission is hereby granted, free of charge, to any person obtaining a copy *
 * of this software and associated documentation files (the "Software"), to deal*
 * in the Software without restriction, including without limitation the rights *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell    *
 * copies of the Software, and to permit persons to whom the Software is        *
 * furnished to do so, subject to the following conditions:                     *
 *                                                                              *
 * The above copyright notice and this permission notice shall be included in   *
 * all copies or substantial portions of the Software.                          *
 *                                                                              *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR   *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,     *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER       *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,*
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN    *
 * THE SOFTWARE.                                                                *
 *******************************************************************************/

package org.jenkinsci.lib.dtkit.util.converter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.lib.Feature;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class ConversionService implements Serializable {

    /**
     * Skip DTD Entity resolution.
     *
     * @author nikolasfalco
     */
    public static class DTKitEntityresolver implements EntityResolver {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new StringReader(""));
        }
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     *
     * @param xslFile   the xsl file
     * @param inputFile the input file
     * @param outFile   the output file
     * @param params    the parameter map
     * @throws ConversionException the convert exception
     */
    public void convert(File xslFile, File inputFile, File outFile, Map<String, Object> params) throws ConversionException {
        try (InputStream xsl = new FileInputStream(xslFile)) {
            convert(new StreamSource(xsl), inputFile, outFile, params);
        } catch (IOException e) {
            throw asConversionException(e);
        }
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     *
     * @param xslFile   the xsl file
     * @param inputFile the input file
     * @param outFile   the output file
     * @throws ConversionException the convert exception
     */
    public void convert(File xslFile, File inputFile, File outFile) throws ConversionException {
        convert(xslFile, inputFile, outFile, null);
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param xslSource the source of the xsl
     * @param inputFile the input file
     * @param outFile   the output file
     * @throws ConversionException the convert exception
     */
    public void convert(StreamSource xslSource, File inputFile, File outFile) throws ConversionException {
        convert(xslSource, inputFile, outFile, null);
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param xslSource the source of the xsl
     * @param inputFile the input file
     * @param outFile   the output file
     * @param params    the parameter map
     * @throws ConversionException the convert exception
     */
    public void convert(StreamSource xslSource, File inputFile, File outFile, Map<String, Object> params) throws ConversionException {
        try (InputStream input = new FileInputStream(inputFile)) {
            convert(xslSource, new InputSource(input), outFile, params);
        } catch (IOException e) {
            throw asConversionException(e);
        }
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     *
     * @param xslFile   the xsl file
     * @param inputFile the input file
     * @param outFile   the output file
     * @param params    the parameter map
     * @throws ConversionException the convert exception
     */
    public void convert(File xslFile, InputSource inputFile, File outFile, Map<String, Object> params) throws ConversionException {
        try (InputStream xsl = new FileInputStream(xslFile)) {
            convert(new StreamSource(xsl), inputFile, outFile, params);
        } catch (IOException e) {
            throw asConversionException(e);
        }
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param xslFile   the xsl file
     * @param inputFile the input file
     * @param params    the parameter map
     * @return the converted string
     * @throws ConversionException the convert exception
     */
    public String convertAndReturn(File xslFile, InputSource inputFile, Map<String, Object> params) throws ConversionException {
        try (InputStream xsl = new FileInputStream(xslFile)) {
            return convertAndReturn(new StreamSource(xsl), inputFile, params);
        } catch (IOException e) {
            throw asConversionException(e);
        }
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param xslFile   the xsl file
     * @param inputFile the input file
     * @param params    the parameter map
     * @return the converted string
     * @throws ConversionException the convert exception
     */
    public String convertAndReturn(File xslFile, File inputFile, Map<String, Object> params) throws ConversionException {
        try (InputStream xsl = new FileInputStream(xslFile); InputStream input = new FileInputStream(inputFile)) {
            return convertAndReturn(new StreamSource(xsl), new InputSource(input), params);
        } catch (IOException e) {
            throw asConversionException(e);
        }
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param xslSource the source of the xsl
     * @param inputFile the input file
     * @param params    the parameter map
     * @return the converted string
     * @throws ConversionException the convert exception
     */
    public String convertAndReturn(StreamSource xslSource, File inputFile, Map<String, Object> params) throws ConversionException {
        try (InputStream input = new FileInputStream(inputFile)) {
            return convertAndReturn(xslSource, new InputSource(input), params);
        } catch (IOException e) {
            throw asConversionException(e);
        }
    }

    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param xslSource the source of the xsl
     * @param inputFile the input file
     * @param params    the parameter map
     * @return the converted string
     * @throws ConversionException the convert exception
     */
    public String convertAndReturn(StreamSource xslSource, InputSource inputFile, Map<String, Object> params) throws ConversionException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            convert(xslSource, inputFile, output, params);
            return new String(output.toByteArray(), "UTF-8");
        } catch (Exception e) {
            throw asConversionException(e);
        }
    }


    /**
     * Launches an XSLT conversion from a source to an OutputStream.
     * This methods uses the net.sf.saxon packages.
     *
     * @param xslSource the source of the xsl
     * @param inputFile the input file
     * @param outFile   the output file
     * @param params    the parameter map
     * @throws ConversionException the convert exception
     */
    public void convert(StreamSource xslSource, InputSource inputFile, File outFile, Map<String, Object> params) throws ConversionException {
        try (OutputStream os = new FileOutputStream(outFile)) {
            convert(xslSource, inputFile, os, params);
        } catch (Exception e) {
            throw asConversionException(e);
        }
    }

    private void convert(StreamSource xslSource, InputSource inputFile, OutputStream output, Map<String, Object> params) throws Exception {
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false);
        javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {
                return new InputSource(new StringReader(""));
            }
        });

        // create the conversion processor with a XSLT compiler
        Processor processor = new Processor(false);
        processor.setConfigurationProperty(Feature.ENTITY_RESOLVER_CLASS, DTKitEntityresolver.class.getName());
        processor.setConfigurationProperty(Feature.DTD_VALIDATION, false);
        processor.setConfigurationProperty(Feature.DTD_VALIDATION_RECOVERABLE, true);
        XsltCompiler compiler = processor.newXsltCompiler();

        // compile and load the XSL file
        XsltExecutable stylesheet = compiler.compile(xslSource);
        Xslt30Transformer transformer = stylesheet.load30();

        // create the output with its options
        Serializer out = processor.newSerializer(output);
        out.setOutputProperty(Serializer.Property.INDENT, "yes");

        // unwrap input stream to maintain APIs back compatible
        Source source = new StreamSource(inputFile.getByteStream());
        // run the conversion
        transformer.transform(source, out);
    }

    private ConversionException asConversionException(Exception e) {
        if (e instanceof FileNotFoundException) {
            return new ConversionException(e);
        } else if (e instanceof IOException) {
            return new ConversionException("Conversion Error", e);
        } else if (e instanceof SaxonApiException) {
            throw new ConversionException("Error to convert the input XML document", e);
        } else if (e instanceof SAXException || e instanceof ParserConfigurationException) {
            // TODO verify that this kind of message agree with the exception reason
            throw new ConversionException("Error to convert - A file not found", e);
        } else {
            return new ConversionException(e);
        }
    }
}