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
package org.jenkinsci.lib.dtkit.util.validator;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ValidationError implements Serializable {

    private ErrorType type;

    private int line;

    private String errorId;

    private String message;

    public ValidationError() {
    }

    public ValidationError(ErrorType errorType, int line, String errorId, String message) {
        this.type = errorType;
        this.line = line;
        this.errorId = errorId;
        this.message = message;
    }

    public String getErrorId() {
        return errorId;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorType getType() {
        return type;
    }

    public void setType(ErrorType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (line != -1) {
            sb.append("At line ").append(line).append(" of ").append(errorId).append(":").append((message));
        } else {
            sb.append("Technical validation:").append(message);
        }
        return sb.toString();
    }
}
