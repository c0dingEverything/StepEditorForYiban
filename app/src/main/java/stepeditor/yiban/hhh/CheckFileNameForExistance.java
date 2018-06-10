package stepeditor.yiban.hhh;

import android.os.Environment;

import java.io.File;

public class CheckFileNameForExistance {
    static String path = Environment.getExternalStorageDirectory() + File.separator + "Yiban" + File.separator + "step" + File.separator;

    public static boolean itExits(String id) {
        String filename = "step" + id;
        File file = new File(path);
        File[] files = file.listFiles();
        for (File sigleFile : files) {
            System.out.println(sigleFile.getName());
            if (sigleFile.getName().contains("step")) {
                if (sigleFile.getName().equals(filename)) {
                    return true;
                }
            }
        }
        return false;
    }
}
