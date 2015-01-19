import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CrunchBaseDumpProcessor {

    /*public static final String DATABASE_SCHEME = "scheme";
    private static final String DEFAULT_SCHEME = "crunch_base";

    public static final String MYSQL_RESTORE_SOURCE_COMMAND = "-e";
    public static final String MYSQL_PROPERTIES_SEPARATOR = " ";
    @Value("${mysql.path}") private String mysqlPath;
    @Value("${mysql.properties}") private String mysqlProperties;
    @Value("${crunchBase.dump.history}") private String pathToHistory;

    private static final int SUCCESS = 200;
    private static final String SNAPSHOT_EXPORT_TYPE = "snapshot";
    private static final String CRUNCH_BASE_SNAPSHOT_URL = "http://static.crunchbase.com/custom_exports/index_pitchbook_yjgidknxhblw.json";

    private List<String> commandLine;

    @Override
    public void execute() throws Exception {
        final String scheme = StringUtil.isEmpty(getTaskParameters().get(DATABASE_SCHEME)) ? DEFAULT_SCHEME : getTaskParameters().get(DATABASE_SCHEME);

        getHistoryLog().info("Restoring CrunchBase dump on PitchBook MySQL database");
        final Instant lastExecutionDate = getLastSuccessExecutingDate();
        commandLine = Lists.newArrayList(mysqlPath, scheme);
        commandLine.addAll(Lists.newArrayList(Splitter.on(MYSQL_PROPERTIES_SEPARATOR).trimResults().split(mysqlProperties)));

        getHistoryLog().info("Getting json from " + CRUNCH_BASE_SNAPSHOT_URL);
        final String json = getData(CRUNCH_BASE_SNAPSHOT_URL);
        final JSONObject data = new JSONArray(json).getJSONObject(0);
        getHistoryLog().info("Json was got successful");

        if(data.has("export_type") && SNAPSHOT_EXPORT_TYPE.equals(data.getString("export_type"))){
            final Instant updated_at = DateUtils.parseDate(data.getString("updated_at"), "yyyy-MM-dd HH:mm:ss Z").toInstant();
            if(null == lastExecutionDate || updated_at.isAfter(lastExecutionDate)) {
                getHistoryLog().info("Dump on CrunchBase server newer than on PitchBook");
                final String url = data.getString("url");
                getHistoryLog().info("Downloading dump file....");
                final Path pathToFile = getDumpFile(url);
                if(null != pathToFile){
                    getHistoryLog().info("Dump file was downloaded successful");
                    final Path tempDirectory = Files.createDirectory(Paths.get(pathToHistory).resolve(pathToFile.getFileName().toString() + System.currentTimeMillis()));

                    getHistoryLog().info("Extracting dump files from downloaded archive");
                    unTar(unGzip(pathToFile, tempDirectory), tempDirectory);
                    getHistoryLog().info("All files was extracted into temp directory (" + tempDirectory.toString() + ")");

                    getHistoryLog().info("Getting all *.sql files from extracted files");
                    final Collection<Path> paths = getFiles(tempDirectory, "*.sql");
                    getHistoryLog().info(paths.size() + " files were got");

                    getHistoryLog().info("Start restore each *.sql file");
                    paths.stream().forEach(this::restoreOneFile);
                    getHistoryLog().info("All files were restored successful");

                    getHistoryLog().info("Deleting temporary files");
                    deleteDirectory(tempDirectory);
                    getHistoryLog().info("All temporary files were deleted");
                } else {
                    throw new RuntimeException("Some error happens while TM downloading dump file. File was not downloaded");
                }
                getHistoryLog().info("Deleting dump files...");
                deleteOldDumps(pathToHistory);
                getHistoryLog().info("Old dump files were deleted successful");
            } else {
                getHistoryLog().info("Dump on CrunchBase server is the same as on PitchBook");
            }
        }
        getHistoryLog().info("Task was finished successful");
    }

    private Path getDumpFile(final String url) throws IOException {
        final Path pathToFile = Paths.get(pathToHistory).resolve(getFileNameFromUrl(url));
        final byte[] bytes = downloadFile(url);
        if(null != bytes) {
            Files.deleteIfExists(pathToFile);
            Files.createFile(pathToFile);
            Files.write(pathToFile, bytes);
            return pathToFile;
        } else {
            return null;
        }
    }

    /***
     * deletes all files modified in this month in {@param basePath} except one file which is newest
     */
    /*private void deleteOldDumps(final String basePath){
        final Path path = Paths.get(basePath);
        final Collection<Path> files = getFiles(path, "*");
        final List<Path> thisMonthDumps = getThisMonthDumps(files);
        final List<Path> newest = thisMonthDumps.stream().limit(1).collect(Collectors.toList());
        deleteFiles(thisMonthDumps, p -> !newest.contains(p));
    }

    private List<Path> getThisMonthDumps(final Collection<Path> files){
        final Calendar cal = new GregorianCalendar();
        final int month = cal.get(Calendar.MONTH);
        final int year = cal.get(Calendar.YEAR);

        return zipFilesAndModificationTime(files)
                .stream()
                .filter(f -> f.getFirst().get(Calendar.YEAR) == year && f.getFirst().get(Calendar.MONTH) == month)
                .sorted((o1, o2) -> o1.getFirst().compareTo(o2.getFirst()))
                .map(Pair::getSecond)
                .collect(Collectors.toList());
    }

    private List<Pair<Calendar, Path>> zipFilesAndModificationTime(final Collection<Path> files) {
        return files.stream().map(f -> {
            try {
                final FileTime lastModifiedTime = Files.getLastModifiedTime(f);
                final Calendar instance = Calendar.getInstance();
                instance.setTime(new Date(lastModifiedTime.toMillis()));
                return new Pair<>(instance, f);
            } catch (IOException e) {
                return null;
            }
        }).filter(f -> null != f).collect(Collectors.toList());
    }

    private void deleteDirectory(final Path path) {
        final Collection<Path> files = getFiles(path, "*");
        files.stream().filter(Files::isDirectory).forEach(this::deleteDirectory);
        deleteFiles(files, p -> true);
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {}
    }

    private void deleteFiles(final Collection<Path> paths, final NeedDelete del){
        paths.stream().forEach(p -> {
            if(del.apply(p) && !Files.isDirectory(p)){
                try {
                    Files.deleteIfExists(p);
                } catch (IOException ignored) {}
            }
        });
    }

    private void restoreOneFile(final Path path){
        final List<String> cmd = Lists.newArrayList(commandLine);
        cmd.add(MYSQL_RESTORE_SOURCE_COMMAND);
        cmd.add("source " + path.toString());
        try {
            getHistoryLog().info("Executing: " + cmd.stream().collect(Collectors.joining(" ")));

            final ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream(true);
            final Process start = builder.start();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(start.getInputStream()))) {
                String line;
                while (null != (line = reader.readLine())) {
                    getHistoryLog().info(line);
                }
            }
            final int waitFor = start.waitFor();
            if (waitFor == 0) {
                getHistoryLog().info("File " + path.toString() + " restored successful");
            } else {
                throw new RuntimeException("Some error happens. File " + path.toString() + " did not restore");
            }
        } catch (Exception e) {
            throw new RuntimeException("Some error happens. File " + path.toString() + " did not restore. " + e.toString());
        }
    }

    private String getFileNameFromUrl(final String url){
        if(StringUtil.isNotEmpty(url)){
            return url.substring(url.lastIndexOf("/") + 1);
        } else {
            return "";
        }
    }

    private Collection<Path> getFiles(final Path tempDirectory, final String glob) {
        final Collection<Path> sql = new ArrayList<>();
        try(DirectoryStream<Path> pathList = Files.newDirectoryStream(tempDirectory, glob)){
            pathList.forEach(sql::add);
        } catch (IOException ignored) {}
        return sql;
    }

    private Path unGzip(final Path pathToFile, final Path tempDir) throws IOException {
        final Path pathToTmpFile = Files.createTempFile(tempDir, "data", ".tar");
        try(final FileInputStream fin = new FileInputStream(pathToFile.toFile());
			final BufferedInputStream in = new BufferedInputStream(fin);
			FileOutputStream out = new FileOutputStream(pathToTmpFile.toFile())) {
            try (GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in)) {
                final byte[] buffer = new byte[1024];
                int n;
                while (-1 != (n = gzIn.read(buffer))) {
                    out.write(buffer, 0, n);
                }
            }
        }
        return pathToTmpFile;
    }

    private void unTar(final Path pathToTarFile, final Path tempDirectory) throws IOException {
        try(TarArchiveInputStream tais = new TarArchiveInputStream(Files.newInputStream(pathToTarFile))) {
            TarArchiveEntry entry = tais.getNextTarEntry();
            while (null != entry) {
                final Path destPath = Files.createFile(tempDirectory.resolve(Paths.get(entry.getName())));
                if (!entry.isDirectory()) {
                    final FileOutputStream fout = new FileOutputStream(destPath.toFile());
                    final byte[] buffer = new byte[8192];
                    int n;
                    while (-1 != (n = tais.read(buffer))) {
                        fout.write(buffer,0,n);
                    }
                    fout.close();
                }
                entry = tais.getNextTarEntry();
            }
        }
    }

    private String getData(final String url) throws IOException {
        final Response response = getResponse(url, 1000 * 10);
        if(null == response){
            return null;
        } else {
            return response.getResponseBody();
        }
    }

    private byte[] downloadFile(final String url) throws IOException {
        final Response response = getResponse(url, 1000 * 60 * 60);
        if(null == response){
            return null;
        } else {
            return response.getResponseBodyAsBytes();
        }
    }

    private Response getResponse(final String url, final int timeout){
        final AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder().setRequestTimeoutInMs(timeout);
        final AsyncHttpClient httpClient = new AsyncHttpClient(builder.build());
        try {
            final ListenableFuture<Response> initial = httpClient.prepareGet(url).execute();
            final Response response = initial.get();
            if(response.getStatusCode() == SUCCESS) {
                return response;
            } else {
                throw new RuntimeException("Error happens while server gets data from " + url + ". Error " + response.getStatusCode());
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException("Error happens while server gets data from " + url + ". " + e.toString());
        }
    }

    @FunctionalInterface
    private interface NeedDelete {
        boolean apply(Path path);
    }*/
}