/*******************************************************************************
 * Copyright (c) 2011 Thales Corporate Services SAS                             *
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

package org.jenkinsci.lib.dtkit.type;

import hudson.ExtensionPoint;
import org.jenkinsci.lib.dtkit.model.InputMetric;

import java.io.Serializable;


public abstract class MetricsType implements ExtensionPoint, Serializable {

    private final String pattern;

    private transient Boolean faildedIfNotNew;

    private Boolean skipNoTestFiles;

    private Boolean failIfNotNew;

    private Boolean deleteOutputFiles;

    private Boolean stopProcessingIfError;

    protected MetricsType(String pattern, Boolean skipNoTestFiles, Boolean failIfNotNew, Boolean deleteOutputFiles, Boolean stopProcessingIfError) {
        this.pattern = pattern;
        this.skipNoTestFiles = skipNoTestFiles;
        this.failIfNotNew = failIfNotNew;
        this.deleteOutputFiles = deleteOutputFiles;
        this.stopProcessingIfError = stopProcessingIfError;
    }

    protected MetricsType(String pattern, Boolean failIfNotNew, Boolean deleteOutputFiles, Boolean stopProcessingIfError) {
        this.pattern = pattern;
        this.failIfNotNew = failIfNotNew;
        this.deleteOutputFiles = deleteOutputFiles;
        this.stopProcessingIfError = stopProcessingIfError;
    }

    protected MetricsType(String pattern, boolean failIfNotNew, boolean deleteOutputFiles) {
        this.pattern = pattern;
        this.failIfNotNew = failIfNotNew;
        this.deleteOutputFiles = deleteOutputFiles;
        this.stopProcessingIfError = true;
    }

    protected MetricsType(String pattern) {
        this.pattern = pattern;
        this.failIfNotNew = false;
        this.deleteOutputFiles = false;
        this.stopProcessingIfError = true;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isSkipNoTestFiles() {
        return (skipNoTestFiles == null) ? false : skipNoTestFiles.booleanValue();
    }

    public boolean isFailIfNotNew() {
        return (failIfNotNew == null ? true : failIfNotNew.booleanValue());
    }

    @SuppressWarnings("unused")
    @Deprecated
    public boolean isFaildedIfNotNew() {
        return (faildedIfNotNew == null ? true : faildedIfNotNew);
    }

    public boolean isDeleteOutputFiles() {
        return (deleteOutputFiles == null ? true : deleteOutputFiles);
    }

    public boolean isStopProcessingIfError() {
        return stopProcessingIfError;
    }

    public abstract InputMetric getInputMetric();

    public Object readResolve() {

        if (stopProcessingIfError == null) {
            stopProcessingIfError = true;
        }

        if (failIfNotNew == null) {
            failIfNotNew = (faildedIfNotNew == null) ? false : faildedIfNotNew.booleanValue();
        }

        if (skipNoTestFiles == null) {
            skipNoTestFiles = false;
        }

        return this;
    }
}

