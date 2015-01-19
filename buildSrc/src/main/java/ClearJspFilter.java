import org.apache.commons.io.IOUtils;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.Callable;

public class ClearJspFilter extends FilterReader {
    private DelayedStringReader delayedStringReader;

    /**
     * Creates a new filtered reader.
     *
     * @param in a Reader object providing the underlying stream.
     * @throws NullPointerException if <code>in</code> is <code>null</code>
     */
    public ClearJspFilter(Reader in) {
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
        String tmpContent;
        do {
            tmpContent = content;
            content = content.replaceAll("\t", " ");
        } while (!tmpContent.equals(content));

        do {
            tmpContent = content;
            content = content.replaceAll("  ", " ");
        } while (!tmpContent.equals(content));
        return content;
    }
}