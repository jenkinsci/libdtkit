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
