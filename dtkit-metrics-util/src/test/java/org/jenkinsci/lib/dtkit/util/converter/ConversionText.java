/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2010, Thales Corporate Services SAS, Gregory Boissinot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.lib.dtkit.util.converter;

import java.io.File;
import java.io.InputStream;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.InputSource;

import static org.junit.Assert.*;

public class ConversionText {

    @Rule
    public TemporaryFolder fileRule = new TemporaryFolder();

    private void convertAndValidate(String inputXSLPath, String inputXMLPath, String expectedResultPath) throws Exception {

        ConversionService conversionService = new ConversionService();

        File outputXMLFile = fileRule.newFile();

        // The input file must be valid
        try (InputStream xsl = this.getClass().getResourceAsStream(inputXSLPath);
                InputStream input = this.getClass().getResourceAsStream(inputXMLPath);
                InputStream expectedOutput = this.getClass().getResourceAsStream(expectedResultPath)) {

            conversionService.convert(new StreamSource(xsl), new InputSource(input), outputXMLFile, null);
            assertThat("XSL transformation did not work", //
                    FileUtils.readFileToString(outputXMLFile, "UTF-8"), //
                    CoreMatchers.equalTo(IOUtils.toString(expectedOutput, "UTF-8")));
        }
    }

    @Test
    public void convertTxt() throws Exception {
        convertAndValidate("myex-txt.xsl", "myex.xml", "myex-outtxt.txt");
    }

    @Test
    public void convertXml() throws Exception {
        convertAndValidate("myex-xml.xsl", "myex.xml", "myex-outxml.xml");
    }
}
