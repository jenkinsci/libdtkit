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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ValidationHandler implements ErrorHandler, Serializable {


    private List<ValidationError> errors = new ArrayList<ValidationError>();

    /**
     * Report a non-fatal error
     *
     * @param ex the error condition
     */
    public void error(SAXParseException ex) {

        errors.add(new ValidationError(ErrorType.ERROR, ex.getLineNumber(), ex.getSystemId(), ex.getMessage()));
    }

    /**
     * Report a fatal error
     *
     * @param ex the error condition
     */

    public void fatalError(SAXParseException ex) {
        System.err.println("At line " + ex.getLineNumber() + " of " + ex.getSystemId() + ':');
        System.err.println(ex.getMessage());
        errors.add(new ValidationError(ErrorType.FATAL_ERROR, ex.getLineNumber(), ex.getSystemId(), ex.getMessage()));
    }

    /**
     * Report a warning
     *
     * @param ex the warning condition
     */
    public void warning(org.xml.sax.SAXParseException ex) {
        System.err.println("At line " + ex.getLineNumber() + " of " + ex.getSystemId() + ':');
        System.err.println(ex.getMessage());
        errors.add(new ValidationError(ErrorType.WARNING, ex.getLineNumber(), ex.getSystemId(), ex.getMessage()));
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
