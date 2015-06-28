package io.protostuff.compiler.parser;

import com.google.common.base.Preconditions;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Kostiantyn Shchepanovskyi
 */
@ParametersAreNonnullByDefault
public class Util {

    // path delimiters
    public static final char WINDOWS_DELIMITER = '\\';
    public static final char LINUX_DELIMITER = '/';
    public static final char QUOTE = '"';

    /**
     * Remove first and last character from given string and return result.
     *
     * @param text given string
     * @return substring of given string - without first and last characters
     */
    public static String removeFirstAndLastChar(String text) {
        Preconditions.checkNotNull(text, "text can not be null");
        int n = text.length();
        return text.substring(1, n - 1);
    }

    /**
     * Returns file name by given absolute or relative file location.
     */
    public static String getFileName(String fullPath) {
        Preconditions.checkNotNull(fullPath, "path can not be null");
        int winDelimiterPos = fullPath.lastIndexOf(WINDOWS_DELIMITER);
        int linDelimiterPos = fullPath.lastIndexOf(LINUX_DELIMITER);
        int pos = Math.max(winDelimiterPos, linDelimiterPos);
        return fullPath.substring(pos + 1, fullPath.length());
    }
}
