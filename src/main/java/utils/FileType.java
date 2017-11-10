package utils;

/**
 * Created by Andrea De Castri on 08/11/2017.
 *
 */
public enum FileType {

    CSV, ARFF;

    public static FileType getFileTypeFromExtension(String fileName){
        int indexExtensionDot = fileName.lastIndexOf(".") + 1;
        String extension = fileName.toLowerCase().substring(indexExtensionDot);
        switch(extension){
            case "csv":
                return CSV;
            case "arff":
                return ARFF;
            default:
                return null;
        }
    }

}
