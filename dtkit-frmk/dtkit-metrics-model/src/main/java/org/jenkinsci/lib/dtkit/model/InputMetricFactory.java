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

package org.jenkinsci.lib.dtkit.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class InputMetricFactory {

    public static InputMetric getInstance(Class<? extends InputMetric> classInputMetric) throws InputMetricException {

        try {
            return classInputMetric.newInstance();
        } catch (InstantiationException ie) {
            throw new InputMetricException(ie);
        } catch (IllegalAccessException iae) {
            throw new InputMetricException(iae);
        }
    }

    public static InputMetric getInstanceWithNoDefaultConstructor(Class<? extends InputMetric> classInputMetric, Class<?>[] parameterTypes, Object[] parameters) throws InputMetricException {
        try {
            Constructor<? extends InputMetric> constructor = classInputMetric.getDeclaredConstructor(parameterTypes);
            return constructor.newInstance(parameters);
        } catch (InstantiationException ie) {
            throw new InputMetricException(ie);
        } catch (IllegalAccessException iae) {
            throw new InputMetricException(iae);
        } catch (NoSuchMethodException e) {
            throw new InputMetricException(e);
        } catch (InvocationTargetException e) {
            throw new InputMetricException(e);
        }
    }

}