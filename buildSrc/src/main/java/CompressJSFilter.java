import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.Callable;

public class CompressJSFilter extends FilterReader {
    private String file;
    private DelayedStringReader delayedStringReader;
    private final static Logger log = LoggerFactory.getLogger(CompressJSFilter.class);

    /**
     * Creates a new filtered reader.
     *
     * @param in a Reader object providing the underlying stream.
     * @throws NullPointerException if <code>in</code> is <code>null</code>
     */
    public CompressJSFilter(Reader in) {
        super(new DelayedStringReader());

        final String content;
        try {
            content = IOUtils.toString(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        delayedStringReader = (DelayedStringReader) this.in;
        delayedStringReader.setLoader(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return processContent(content);
            }
        });
    }

    private String processContent(String content) {
        if (null == content || content.isEmpty()) {
            return content;
        }
        try {
            final StringWriter result = new StringWriter();
            final JavaScriptCompressor compressor = new JavaScriptCompressor(new StringReader(content), new ErrorReporter() {
                @Override
                public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    log.debug(getMessage("WARNING", sourceName, message, line, lineOffset));
                }

                @Override
                public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    log.debug(getMessage("ERROR", sourceName, message, line, lineOffset));
                }

                @Override
                public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    return new EvaluatorException(message);
                }
            });
            compressor.compress(result, -1, true, false, false, false);
            result.flush();
            return result.toString();
        } catch (Exception e) {
            log.debug("Error compressing javascript (" + file + ")");
        }
        return content;
    }

    private String getMessage(final String type, final String source, final String message, final int line, final int column){
        if (line < 0) {
            return "[" + type + "]: " + "file: " + file + " -> " + ((source != null) ? source + ": " : "" + message);
        } else {
            return "[" + type + "]: " + "file: " + file + ", line: " + line + ", column: " + column + " -> " + ((source != null) ? source + ": " : "" + message);
        }
    }
}