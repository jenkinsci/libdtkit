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

import hudson.ExtensionList;
import hudson.model.Describable;
import hudson.model.Hudson;
import org.jenkinsci.lib.dtkit.descriptor.MeasureTypeDescriptor;
import org.jenkinsci.lib.dtkit.model.InputMetric;

@SuppressWarnings("unused")
public abstract class MeasureType extends MetricsType implements Describable<MeasureType> {

    protected MeasureType(String pattern, boolean skipNoTestFiles, boolean failIfNotNew, boolean deleteOutputFiles, boolean stopProcessingIfError) {
        super(pattern, skipNoTestFiles, failIfNotNew, deleteOutputFiles, stopProcessingIfError);
    }

    protected MeasureType(String pattern, boolean failIfNotNew, boolean deleteOutputFiles, boolean stopProcessingIfError) {
        super(pattern, failIfNotNew, deleteOutputFiles, stopProcessingIfError);
    }

    protected MeasureType(String pattern, boolean failIfNotNew, boolean deleteOutputFiles) {
        super(pattern, failIfNotNew, deleteOutputFiles);
    }

    protected MeasureType(String pattern) {
        super(pattern);
    }

    public static ExtensionList<MeasureType> all() {
        return Hudson.getInstance().getExtensionList(MeasureType.class);
    }

    @SuppressWarnings("unchecked")
    public MeasureTypeDescriptor<? extends MeasureType> getDescriptor() {
        return (MeasureTypeDescriptor<? extends MeasureType>) Hudson.getInstance().getDescriptor(getClass());
    }

    @SuppressWarnings("unused")
    @Override
    public InputMetric getInputMetric() {
        return getDescriptor().getInputMetric();
    }
}

