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


import net.sf.saxon.s9api.*;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.CharBuffer;
import java.util.Map;

public class ConversionService implements Serializable {


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
        try {
            Reader reader = new FileReader(xslFile);
            String result = convertAndReturn(new StreamSource(reader), inputFile, params);
            reader.close();
            return result;
        } catch (IOException ioe) {
            throw new ConversionException("Conversion Error", ioe);
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
        FileInputStream fis = null;
        try {
            Reader reader = new FileReader(xslFile);
            fis = new FileInputStream(inputFile);
            String result = convertAndReturn(new StreamSource(reader), new InputSource(fis), params);
            reader.close();
            return result;
        } catch (FileNotFoundException fne) {
            throw new ConversionException(fne);

        } catch (IOException ioe) {
            throw new ConversionException("Conversion Error", ioe);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    throw new ConversionException(ioe);
                }
            }
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
        try {
            Reader reader = new FileReader(xslFile);
            convert(new StreamSource(reader), inputFile, outFile, params);
            reader.close();
        } catch (IOException ioe) {
            throw new ConversionException("Conversion Error", ioe);
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
     * @param params    the parameter map
     * @return the converted string
     * @throws ConversionException the convert exception
     */
    public String convertAndReturn(StreamSource xslSource, File inputFile, Map<String, Object> params) throws ConversionException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inputFile);
            return convertAndReturn(xslSource, new InputSource(fis), params);
        } catch (FileNotFoundException fne) {
            throw new ConversionException(fne);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    throw new ConversionException(ioe);
                }
            }
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
    public void convert(StreamSource xslSource, File inputFile, File outFile, Map<String, Object> params) throws ConversionException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inputFile);
            convert(xslSource, new InputSource(fis), outFile, params);
        } catch (FileNotFoundException fne) {
            throw new ConversionException(fne);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    throw new ConversionException(ioe);
                }
            }
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
        try {
            Reader reader = new FileReader(xslFile);
            convert(new StreamSource(reader), inputFile, outFile, params);
            reader.close();
        } catch (IOException ioe) {
            throw new ConversionException("Conversion Error", ioe);
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

        try {

            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId)
                        throws SAXException, IOException {
                    return new InputSource(new StringReader(""));
                }
            });

            Document document = builder.parse(inputFile);
            Source source = new DOMSource(document.getDocumentElement());

            // create the conversion processor with a Xslt compiler
            Processor processor = new Processor(false);
            XsltCompiler compiler = processor.newXsltCompiler();

            // compile and load the XSL file
            XsltExecutable xsltExecutable = compiler.compile(xslSource);
            XsltTransformer xsltTransformer = xsltExecutable.load();

            // create the input
            DocumentBuilder documentBuilder = processor.newDocumentBuilder();
            documentBuilder.setDTDValidation(false);

            XdmNode xdmNode = documentBuilder.build(source);

            // create the output with its options
            Serializer out = new Serializer();
            out.setOutputProperty(Serializer.Property.INDENT, "yes");

            File fileOut = File.createTempFile("serializer", "convert");
            FileOutputStream fos = new FileOutputStream(fileOut);
            out.setOutputStream(fos);

            // run the conversion
            xsltTransformer.setInitialContextNode(xdmNode);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    QName paramName = new QName(entry.getKey());
                    XdmAtomicValue paramValue = new XdmAtomicValue(String.valueOf(entry.getValue()));
                    xsltTransformer.setParameter(paramName, paramValue);
                }
            }

            xsltTransformer.setDestination(out);
            xsltTransformer.transform();

            StringBuffer sb = new StringBuffer();
            CharBuffer buffer = CharBuffer.allocate(4096);
            int bufferLength = 0;
            Reader reader = new FileReader(fileOut);
            while (bufferLength != -1) {
                bufferLength = reader.read(buffer);
                if (bufferLength > 0) {
                    sb.append(buffer.flip());
                }
            }

            reader.close();
            fos.close();

            return sb.toString();
        } catch (IOException ioe) {
            throw new ConversionException("Error to convert - A file not found", ioe);
        } catch (SaxonApiException sae) {
            throw new ConversionException("Error to convert the input XML document", sae);
        } catch (SAXException sae) {
            throw new ConversionException("Error to convert - A file not found", sae);
        } catch (ParserConfigurationException pe) {
            throw new ConversionException("Error to convert - A file not found", pe);
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

        try {

            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolver() {
                public InputSource resolveEntity(String publicId, String systemId)
                        throws SAXException, IOException {
                    return new InputSource(new StringReader(""));
                }
            });

            Document document = builder.parse(inputFile);
            Source source = new DOMSource(document.getDocumentElement());

            // create the conversion processor with a Xslt compiler
            Processor processor = new Processor(false);
            XsltCompiler compiler = processor.newXsltCompiler();

            // compile and load the XSL file
            XsltExecutable xsltExecutable = compiler.compile(xslSource);
            XsltTransformer xsltTransformer = xsltExecutable.load();

            // create the input
            DocumentBuilder documentBuilder = processor.newDocumentBuilder();
            documentBuilder.setDTDValidation(false);

            XdmNode xdmNode = documentBuilder.build(source);

            // create the output with its options
            Serializer out = new Serializer();
            out.setOutputProperty(Serializer.Property.INDENT, "yes");
            FileOutputStream fos = new FileOutputStream(outFile);
            out.setOutputStream(fos);

            // run the conversion
            xsltTransformer.setInitialContextNode(xdmNode);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    QName paramName = new QName(entry.getKey());
                    XdmAtomicValue paramValue = new XdmAtomicValue(String.valueOf(entry.getValue()));
                    xsltTransformer.setParameter(paramName, paramValue);
                }
            }

            xsltTransformer.setDestination(out);
            xsltTransformer.transform();

            fos.close();
        } catch (IOException ioe) {
            throw new ConversionException("Error to convert - A file not found", ioe);
        } catch (SaxonApiException sae) {
            throw new ConversionException("Error to convert the input XML document", sae);
        } catch (SAXException sae) {
            throw new ConversionException("Error to convert - A file not found", sae);
        } catch (ParserConfigurationException pe) {
            throw new ConversionException("Error to convert - A file not found", pe);
        }
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

}