import com.yahoo.platform.yui.compressor.CssCompressor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.Callable;

public class CompressCSSFilter extends FilterReader {
    private String file;
    private DelayedStringReader delayedStringReader;
    private final static Logger log = LoggerFactory.getLogger(CompressCSSFilter.class);

    /**
     * Creates a new filtered reader.
     *
     * @param in a Reader object providing the underlying stream.
     * @throws NullPointerException if <code>in</code> is <code>null</code>
     */
    public CompressCSSFilter(Reader in) {
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
            final CssCompressor compressor = new CssCompressor(new StringReader(content));
            compressor.compress(result,-1);
            result.flush();
            return result.toString();
        } catch (Exception e) {
            log.debug("Error compressing css (" + file + ")");
        }
        return content;
    }
}