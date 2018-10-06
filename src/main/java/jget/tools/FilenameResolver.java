package jget.tools;

import java.io.File;
import java.util.Date;

public class FilenameResolver {
    public static String resolveFileName(String path) {
        // generate downloaded file name, the default value is sub string of url.if file exist,
        if (path.lastIndexOf(File.separator) != path.length() - 1) {
            var fileName = path.substring(path.lastIndexOf(File.separator) + 1);
            File tmp = new File("./" + fileName);
            if (tmp.exists()) {
                fileName = fileName.concat("--"+ new Date().toString().replace(" ", "_"));
            }
            return fileName;
        }
        return new Date().toString().replace(" ", "_");
    }
}
