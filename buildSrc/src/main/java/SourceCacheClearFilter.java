import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceCacheClearFilter extends FilterReader {
    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script.*src=\"(.+?)\".*>.*</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_PATTERN = Pattern.compile("<link(.*)href=\"(.+?)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_URL_PATTERN = Pattern.compile("url\\(\"?'?(.+?(jpg|gif|png|bmp|ico))\"?'?\\)", Pattern.CASE_INSENSITIVE);
    private static final Pattern IMG_PATTERN = Pattern.compile("<img(.*?)src=\"(.+?(jpg|gif|png|bmp|ico))\"", Pattern.CASE_INSENSITIVE);

    public static final String uq = RandomStringUtils.randomAlphanumeric(8);

    private String appendTo;
    private final String content;
    private DelayedStringReader delayedStringReader;

    /**
     * Creates a new filtered reader.
     *
     * @param in a Reader object providing the underlying stream.
     * @throws NullPointerException if <code>in</code> is <code>null</code>
     */
    public SourceCacheClearFilter(Reader in) {
        super(new DelayedStringReader());

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
                return processAppendUQ(content);
            }
        });
    }

    private String processAppendUQ(String content) {
        content = content.replaceAll("\\$", "111DOLLARSIGN111");
        content = content.replaceAll("\\{", "111BEGINVAR111");
        content = content.replaceAll("\\}", "111ENDVAR111");

        if(appendTo.contains("js")) {
            StringBuffer sb = new StringBuffer();
            Matcher matcher = SCRIPT_PATTERN.matcher(content);
            while(matcher.find()) {
                String path = matcher.group(1);
                String sep = path.contains("?") ? "&" : "?";
                String line = path + sep + "uq=" + uq;
                matcher.appendReplacement(sb, "<script src=\"" + line + "\" type=\"text/javascript\"></script>");
            }
            matcher.appendTail(sb);
            content = sb.toString();
        }

        if(appendTo.contains("linkCSS")) {
            StringBuffer sb = new StringBuffer();
            Matcher matcher = CSS_PATTERN.matcher(content);
            while(matcher.find()) {
                String head = matcher.group(1);
                String path = matcher.group(2);
                String sep = path.contains("?") ? "&" : "?";
                String line = path + sep + "uq=" + uq;
                matcher.appendReplacement(sb, "<link " + head + " href=\"" + line + "\"");
            }
            matcher.appendTail(sb);
            content = sb.toString();
        }

        if(appendTo.contains("img")) {
            StringBuffer sb = new StringBuffer();
            Matcher matcher = IMG_PATTERN.matcher(content);
            while(matcher.find()) {
                String head = matcher.group(1);
                String src = matcher.group(2);
                String sep = src.contains("?") ? "&" : "?";
                String line = src + sep + "uq=" + uq;
                matcher.appendReplacement(sb, "<img " + head + " src=\"" + line + "\"");
            }
            matcher.appendTail(sb);
            content = sb.toString();

            sb = new StringBuffer();
            matcher = CSS_URL_PATTERN.matcher(content);
            while(matcher.find()) {
                String url = matcher.group(1);
                String sep = url.contains("?") ? "&" : "?";
                String line = url + sep + "uq=" + uq;
                matcher.appendReplacement(sb, "url(" + line + ")");
            }
            matcher.appendTail(sb);
            content = sb.toString();
        }

        if(appendTo.contains("css")) {
            StringBuffer sb = new StringBuffer();
            Matcher matcher = CSS_URL_PATTERN.matcher(content);
            while(matcher.find()) {
                String url = matcher.group(1);
                String sep = url.contains("?") ? "&" : "?";
                String line = url + sep + "uq=" + uq;
                matcher.appendReplacement(sb, "url(" + line + ")");
            }
            matcher.appendTail(sb);
            content = sb.toString();
        }

        content = content.replaceAll("111BEGINVAR111", "\\{");
        content = content.replaceAll("111ENDVAR111", "\\}");
        content = content.replaceAll("111DOLLARSIGN111", "\\$");

        return content;
    }
}