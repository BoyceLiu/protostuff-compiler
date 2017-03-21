package io.protostuff.compiler.model;

import java.io.Serializable;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class SourceCodeLocation implements Serializable {

    private static final long serialVersionUID = 0L;

    public static final SourceCodeLocation UNKNOWN = new SourceCodeLocation() {
        @Override
        public String toString() {
            return "unknown";
        }
    };

    private final String file;
    private final int line;

    private SourceCodeLocation() {
        file = "unknown";
        line = 0;
    }

    public SourceCodeLocation(String file, int line) {
        this.file = file;
        this.line = line;
    }

    public String getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return file + ":" + line;
    }
}
