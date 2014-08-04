/*******************************************************************************
 * Copyright (c) 2010 Thales Corporate Services SAS                             *
 * Author : Gregory Boissinot                                                   *
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

package org.jenkinsci.lib.dtkit.util.validator;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.List;


public class ValidationService implements Serializable {


    /**
     * Inner class to implement a resource resolver. This version always returns null, which
     * has the same effect as not supplying a resource resolver at all. The LSResourceResolver
     * is part of the DOM Level 3 load/save module.
     */

    protected static class Resolver implements LSResourceResolver {

        /**
         * Resolve a reference to a resource
         *
         * @param type      The type of resource, for example a schema, source XML document, or query
         * @param namespace The target namespace (in the case of a schema document)
         * @param publicId  The public ID
         * @param systemId  The system identifier (as written, possibly a relative URI)
         * @param baseURI   The base URI against which the system identifier should be resolved
         * @return an LSInput object typically containing the character stream or byte stream identified
         * by the supplied parameters; or null if the reference cannot be resolved or if the resolver chooses
         * not to resolve it.
         */

        public LSInput resolveResource(String type, String namespace, String publicId, String systemId, String baseURI) {
            return null;
        }

    }


    /**
     * Validate an input file against a XSD
     *
     * @param xsdSource the xsd source
     * @param inputXML  the input XML file
     * @return true if the validation succeeded, false otherwise
     * @throws ValidationException when there is a validation error
     */
    public List<ValidationError> processValidation(Source xsdSource, File inputXML) throws ValidationException {
        return processValidation(new Source[]{xsdSource}, inputXML);
    }

    public List<ValidationError> processValidation(File[] xsdFiles, File inputXML) throws ValidationException {
        Source[] xsdSources = new Source[xsdFiles.length];
        Reader[] readers = new Reader[xsdFiles.length];

        for (int i = 0; i < xsdFiles.length; i++) {
            try {
                readers[i] = new FileReader(xsdFiles[i]);
            } catch (FileNotFoundException fne) {
                throw new ValidationException("Validation error", fne);
            }
        }

        for (int i = 0; i < readers.length; i++) {
            xsdSources[i] = new StreamSource(readers[i]);
        }

        List<ValidationError> listErrors = processValidation(xsdSources, inputXML);

        for (int i = 0; i < readers.length; i++) {
            try {
                readers[i].close();
            } catch (IOException ioe) {
                throw new ValidationException("Validation error", ioe);
            }
        }

        return listErrors;
    }


    public List<ValidationError> processValidation(Source[] xsdSources, File inputXML) throws ValidationException {

        ValidationHandler handler = new ValidationHandler();
        try {

            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            schemaFactory.setErrorHandler(handler);
            Schema schemaGrammar = schemaFactory.newSchema(xsdSources);
            Resolver resolver = new Resolver();
            Validator schemaValidator = schemaGrammar.newValidator();
            schemaValidator.setErrorHandler(handler);
            schemaValidator.setResourceResolver(resolver);
            schemaValidator.validate(new StreamSource(inputXML));

            for (int i = 0; i < xsdSources.length; i++) {
                xsdSources[i] = null;
            }

            return handler.getErrors();
        } catch (SAXException sae) {
            List<ValidationError> errors = handler.getErrors();
            errors.add(new ValidationError(ErrorType.ERROR, -1, "-1", sae.getMessage()));
            return errors;
        } catch (IOException ioe) {
            throw new ValidationException("Validation error", ioe);
        }
    }


    /**
     * Validate an input file against a XSD
     *
     * @param xsdFile  the xsd file
     * @param inputXML the input XML file
     * @return true if the validation succeeded, false otherwise
     * @throws ValidationException when there is a validation error
     */
    public List<ValidationError> processValidation(File xsdFile, File inputXML) throws ValidationException {

        return processValidation(new StreamSource(xsdFile), inputXML);
    }
}