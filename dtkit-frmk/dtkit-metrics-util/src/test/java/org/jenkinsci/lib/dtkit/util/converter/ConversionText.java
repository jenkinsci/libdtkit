package org.jenkinsci.lib.dtkit.util.converter;

import org.junit.Test;


public class ConversionText extends AbstractTest {

    @Test
    public void convertTxt() throws Exception {
        convertAndValidate("myex-txt.xsl", "myex.xml", "myex-outtxt.txt");
    }

    @Test
    public void convertXml() throws Exception {
        convertAndValidate("myex-xml.xsl", "myex.xml", "myex-outxml.xml");
    }
}
