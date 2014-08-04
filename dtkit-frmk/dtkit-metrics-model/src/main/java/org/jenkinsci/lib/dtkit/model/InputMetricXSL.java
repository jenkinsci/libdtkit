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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jenkinsci.lib.dtkit.util.converter.ConversionException;
import org.jenkinsci.lib.dtkit.util.converter.ConversionService;
import org.jenkinsci.lib.dtkit.util.validator.ValidationException;
import org.jenkinsci.lib.dtkit.util.validator.ValidationService;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
public abstract class InputMetricXSL extends InputMetric {


    private String xslName;
    private File xslFile;

    private String[] inputXsdNameList;
    private File[] inputXsdFileList;


    /**
     * Gets the Class (namespace) of the xsl file resource
     *
     * @return the resource class (for loading)
     */
    @JsonIgnore
    public Class getXslResourceClass() {
        return this.getClass();
    }

    /**
     * Gets the Class (namespace) of the xsd file resource
     *
     * @return the xsd class (for loading)
     */
    @JsonIgnore
    public Class getInputXsdClass() {
        return this.getClass();
    }

    /**
     * the XSL file associated to this tool
     *
     * @return the relative xsl path
     */
    @JsonIgnore
    public String getXslName() {
        return xslName;
    }


    /**
     * Overrides this method if you want to provide your own xsl (independent of its location)
     * Used for custom type where the user specifies its own xsl
     *
     * @return null by default
     */
    @JsonIgnore
    public File getXslFile() {
        return xslFile;
    }

    @JsonIgnore
    public InputStream getXslInputStream() throws IOException {

        if (getXslFile() != null) {
            return new FileInputStream(getXslFile());
        }

        if (this.getXslName() != null) {
            return this.getXslResourceClass().getResourceAsStream(this.getXslName());
        }

        return null;
    }

    /**
     * the XSD file associated to this tool result file
     *
     * @return the xsd name. Can be null if there no XSD for the input file of the current tool type
     */
    @JsonIgnore
    public String[] getInputXsdNameList() {
        return inputXsdNameList;
    }

    @JsonIgnore
    public File[] getInputXsdFileList() {
        return inputXsdFileList;
    }

    @JsonIgnore
    public InputStream[] getListXsdInputStream() throws IOException {

        File[] inputXsdFileList = getInputXsdFileList();
        if (inputXsdFileList != null) {
            InputStream[] inputStreams = new InputStream[inputXsdFileList.length];
            for (int i = 0; i < inputXsdFileList.length; i++) {
                inputStreams[i] = new FileInputStream(inputXsdFileList[i]);
            }
            return inputStreams;
        }

        String[] inputXsdNameList = getInputXsdNameList();
        if (inputXsdNameList != null) {
            InputStream[] inputStreams = new InputStream[inputXsdNameList.length];
            for (int i = 0; i < inputXsdNameList.length; i++) {
                inputStreams[i] = this.getInputXsdClass().getResourceAsStream(inputXsdNameList[i]);
            }
            return inputStreams;
        }

        return null;
    }

    /**
     * the XSD file associated to this output format
     *
     * @return the relative xsd path. Can be null if there no XSD for the output format
     */
    @JsonIgnore
    public String[] getOutputXsdNameList() {
        if (getOutputFormatType() == null) {
            return null;
        }
        return getOutputFormatType().getXsdNameList();
    }

    /**
     * All the subclasses will be of XSL
     *
     * @return XSL type
     */
    @Override
    public InputMetricType getInputMetricType() {
        return InputMetricType.XSL;
    }

    public String getUserContentXSLDirRelativePath() {
        if (getToolVersion() == null || getToolVersion().endsWith("N/A")) {
            return "xunit/" + getToolName() + "/";
        }
        return "xunit/" + getToolName() + "/" + getToolVersion();
    }

    /*
     *  Convert the input file against the current xsl of the tool and put the result in the outFile
     */
    @Override
    public void convert(File inputFile, File outFile, Map<String, Object> params) throws ConversionException {
        ConversionService conversionService = new ConversionService();
        if (getXslFile() == null) {
            conversionService.convert(new StreamSource(this.getXslResourceClass().getResourceAsStream(this.getXslName())), inputFile, outFile, params);
        } else {
            conversionService.convert(getXslFile(), inputFile, outFile, params);
        }
    }

    @Override
    public void convert(File inputFile, File outFile) throws ConversionException {
        convert(inputFile, outFile, null);
    }

    /**
     * Convert an input file to an output file
     * Give your conversion process
     * Input and Output files are relatives to the filesystem where the process is executed on (like Hudson agent)
     *
     * @param inputFile   the input file to convert
     * @param outFile     the output file to convert
     * @param externalXsl an external xsl file such as a user xsl
     * @param params      the conversion parameters
     * @throws org.jenkinsci.lib.dtkit.util.converter.ConversionException an application Exception to throw when there is an error of conversion
     *                                                                    The exception is catched by the API client (as Hudson plugin)
     */
    public void convert(File inputFile, File outFile, File externalXsl, Map<String, Object> params) throws ConversionException {
        ConversionService conversionService = new ConversionService();
        conversionService.convert(externalXsl, inputFile, outFile, params);
    }

    /**
     * Convert an input file to an output file
     * Give your conversion process
     * Input and Output files are relatives to the filesystem where the process is executed on (like Hudson agent)
     *
     * @param inputFile          the input file to convert
     * @param outFile            the output file to convert
     * @param externalXslContent an external xsl content such as a user xsl
     * @param params             the conversion parameters
     * @throws org.jenkinsci.lib.dtkit.util.converter.ConversionException an application Exception to throw when there is an error of conversion
     *                                                                    The exception is catched by the API client (as Hudson plugin)
     */
    public void convert(File inputFile, File outFile, String externalXslContent, Map<String, Object> params) throws ConversionException {
        ConversionService conversionService = new ConversionService();
        conversionService.convert(new StreamSource(new StringReader(externalXslContent)), inputFile, outFile, params);
    }

    /*
    *  Validates the input file against the current grammar of the tool
    */
    @Override
    public boolean validateInputFile(File inputXMLFile) throws ValidationException {

        ValidationService validationService = new ValidationService();

        if ((this.getInputXsdNameList() == null) && (this.getInputXsdFileList() == null)) {
            return true;
        }

        if (this.getInputXsdFileList() != null) {
            setInputValidationErrors(validationService.processValidation(getInputXsdFileList(), inputXMLFile));
        }

        if (getInputXsdNameList() != null) {
            StreamSource[] streamSources = new StreamSource[getInputXsdNameList().length];
            for (int i = 0; i < streamSources.length; i++) {
                streamSources[i] = new StreamSource(this.getInputXsdClass().getResourceAsStream(getInputXsdNameList()[i]));
            }

            setInputValidationErrors(validationService.processValidation(streamSources, inputXMLFile));
        }


        return getInputValidationErrors().size() == 0;
    }

    /*
     *  Validates the output file against the current grammar of the format
     */

    @Override
    public boolean validateOutputFile(File inputXMLFile) throws ValidationException {

        //If no format is specified, exit validation and returns true
        if (this.getOutputFormatType() == null) {
            return true;
        }

        //If there no given xsd, exit validation and returns true
        if (this.getOutputXsdNameList() == null) {
            return true;
        }

        //Validate given XSD
        Source[] sources = new Source[getOutputXsdNameList().length];
        for (int i = 0; i < sources.length; i++) {
            sources[i] = new StreamSource(this.getOutputFormatType().getClass().getResourceAsStream(getOutputXsdNameList()[i]));
        }

        ValidationService validationService = new ValidationService();
        setOutputValidationErrors(validationService.processValidation(sources, inputXMLFile));
        return getOutputValidationErrors().size() == 0;
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
        if (!super.equals(o)) return false;

        InputMetricXSL that = (InputMetricXSL) o;

        if (!Arrays.equals(inputXsdFileList, that.inputXsdFileList)) return false;
        if (!Arrays.equals(inputXsdNameList, that.inputXsdNameList)) return false;
        if (xslFile != null ? !xslFile.equals(that.xslFile) : that.xslFile != null) return false;
        if (xslName != null ? !xslName.equals(that.xslName) : that.xslName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (xslName != null ? xslName.hashCode() : 0);
        result = 31 * result + (xslFile != null ? xslFile.hashCode() : 0);
        result = 31 * result + (inputXsdNameList != null ? Arrays.hashCode(inputXsdNameList) : 0);
        result = 31 * result + (inputXsdFileList != null ? Arrays.hashCode(inputXsdFileList) : 0);
        return result;
    }
}
