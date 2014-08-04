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

package org.jenkinsci.lib.dtkit.model;

import com.sun.xml.bind.AnyTypeAdapter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.jenkinsci.lib.dtkit.util.converter.ConversionException;
import org.jenkinsci.lib.dtkit.util.validator.ValidationError;
import org.jenkinsci.lib.dtkit.util.validator.ValidationException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unused")
//-- JAXB Annotations
// @XmlJavaTypeAdapter used for class and interface enables users to not add a custom adapter
// - All implementation must be explicitly added in the JAXBContext
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class InputMetric implements Serializable {

    private String toolName;
    private String toolVersion;
    private InputMetricType inputMetricType;
    private InputType toolType;
    private OutputMetric outputFormatType;

    /**
     * The current input validation errors
     */
    private List<ValidationError> inputValidationErrors = new ArrayList<ValidationError>();

    /**
     * The current output validation errors
     */
    private List<ValidationError> outputValidationErrors = new ArrayList<ValidationError>();

    /**
     * The  name of the current tool
     *
     * @return the tool name
     */
    @XmlElement
    public String getToolName() {
        return toolName;
    }


    /**
     * The version of the current tool
     *
     * @return the tool version
     */
    @XmlElement
    public String getToolVersion() {
        return toolVersion;
    }

    @JsonIgnore
    public boolean isDefault() {
        return false;
    }

    /**
     * The label of the tool
     *
     * @return the label metric
     */
    @JsonIgnore
    public String getLabel() {
        String label;
        if (getToolVersion() == null) {
            label = getToolName();
        } else {
            label = getToolName() + "-" + getToolVersion();
        }
        if (isDefault()) {
            label = label + " (default)";
        }

        return label;
    }

    /**
     * Gives the input metric type (XSL or Other) according the subclass type
     *
     * @return the input metric type
     */
    @JsonIgnore
    public InputMetricType getInputMetricType() {
        return inputMetricType;
    }

    /**
     * Gives the metric tool type (TEST, COVERAGE, MEASURE, VIOLATION)
     *
     * @return the input object
     */
    @XmlElement
    public InputType getToolType() {
        return toolType;
    }

    /**
     * Gives the output format type (given by the format model)
     *
     * @return the Output format type (usually retrieved by the format model library as junit-model.jar or tusar-model.jar)
     */
    @XmlElement
    public OutputMetric getOutputFormatType() {
        return outputFormatType;
    }

    /**
     * Convert an input file to an output file
     * Give your conversion process
     * Input and Output files are relatives to the filesystem where the process is executed on (like Hudson agent)
     *
     * @param inputFile the input file to convert
     * @param outFile   the output file to convert
     * @throws org.jenkinsci.lib.dtkit.util.converter.ConversionException an application Exception to throw when there is an error of conversion
     *                                                                    The exception is catched by the API client (as Hudson plugin)
     */
    public void convert(File inputFile, File outFile) throws ConversionException {
        convert(inputFile, outFile, null);
    }


    /**
     * Convert an input file to an output file
     * Give your conversion process
     * Input and Output files are relatives to the filesystem where the process is executed on (like Hudson agent)
     *
     * @param inputFile the input file to convert
     * @param outFile   the output file to convert
     * @param params    the conversion parameters
     * @throws org.jenkinsci.lib.dtkit.util.converter.ConversionException an application Exception to throw when there is an error of conversion
     *                                                                    The exception is catched by the API client (as Hudson plugin)
     */
    public abstract void convert(File inputFile, File outFile, Map<String, Object> params) throws ConversionException;

    /*
    *  Gives the validation process for the input file
    *
    * @return true if the input file is valid, false otherwise
    */

    public abstract boolean validateInputFile(File inputXMLFile) throws ValidationException;

    /*
     *  Gives the validation process for the output file
     *
     * @return true if the input file is valid, false otherwise
     */

    public abstract boolean validateOutputFile(File inputXMLFile) throws ValidationException;

    /**
     * Gets all input validation errors
     *
     * @return the list of all input validation errors
     */
    @JsonIgnore
    public List<ValidationError> getInputValidationErrors() {
        return inputValidationErrors;
    }

    /**
     * Gets all output validation errors
     *
     * @return the list of all output validation errors
     */
    @JsonIgnore
    public List<ValidationError> getOutputValidationErrors() {
        return outputValidationErrors;
    }

    public void setInputValidationErrors(List<ValidationError> inputValidationErrors) {
        this.inputValidationErrors = inputValidationErrors;
    }

    public void setOutputValidationErrors(List<ValidationError> outputValidationErrors) {
        this.outputValidationErrors = outputValidationErrors;
    }

    /**
     * --------------------------------------------------------
     * <p/>
     * SETTERS for JAX-RS Layer
     * <p/>
     * --------------------------------------------------------
     */


    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    @JsonIgnore
    //Mandatory for deserialization (the associated field is present in the output serialization)
    public void setLabel(String label) {
        //nothing
    }

    public void setInputMetricType(InputMetricType inputMetricType) {
        this.inputMetricType = inputMetricType;
    }

    public void setToolType(InputType toolType) {
        this.toolType = toolType;
    }

    @JsonIgnore
    public void setOutputFormatType(OutputMetric outputFormatType) {
        this.outputFormatType = outputFormatType;
    }


    /**
     * --------------------------------------------------------
     * <p/>
     * HASHCODE() AND EQUALS() for comparaison
     * <p/>
     * --------------------------------------------------------
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InputMetric that = (InputMetric) o;

        if (getInputMetricType() != that.getInputMetricType()) return false;
        if (getToolName() != null ? !getToolName().equals(that.getToolName()) : that.getToolName() != null)
            return false;
        if (getToolType() != that.getToolType()) return false;
        if (getToolVersion() != null ? !getToolVersion().equals(that.getToolVersion()) : that.getToolVersion() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getToolName() != null ? getToolName().hashCode() : 0;
        result = 31 * result + (getToolVersion() != null ? getToolVersion().hashCode() : 0);
        result = 31 * result + (getInputMetricType() != null ? getInputMetricType().hashCode() : 0);
        result = 31 * result + (getToolType() != null ? getToolType().hashCode() : 0);
        return result;
    }
}
