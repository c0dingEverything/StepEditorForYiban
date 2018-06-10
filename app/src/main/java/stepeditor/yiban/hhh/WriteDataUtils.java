package stepeditor.yiban.hhh;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WriteDataUtils {

    static String path = Environment.getExternalStorageDirectory() + File.separator + "Yiban" + File.separator + "step" + File.separator;
//    static String path = "/storage/emulated/0/Yiban/step/";

    public static Map<String, String> readData(String ID) {

        String filename = "step" + ID;
        File file = new File(path, filename);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String rawdata1 = br.readLine();
            String rawdata2 = rawdata1.replace("\"", "").replace("{", "").replace("}", "");
            if (rawdata2.contains(",")) {
                String[] data = rawdata2.split(",");
                Map<String, String> maps = new HashMap<String, String>();
                for (int i = 0; i < data.length; i++) {
                    String[] finaldata = data[i].split(":");
                    maps.put(finaldata[0], finaldata[1]);
                }
                fis.close();
                return maps;
            } else {
                String[] data = rawdata2.split(":");
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("user_step_date", GetCurrentTimeUtils.getData());
                maps.put("user_step_total_count", "0");
                maps.put("user_step_count", "0");
                maps.put(data[0], data[1]);
                fis.close();
                return maps;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean writeStepintoFile(String ID, String step) {
        Map<String, String> maps = readData(ID);

        maps.put("user_step_count", step);

        String data1, data2, data3;
        data1 = "{\"user_step_date\"" + ":" + "\"" + maps.get("user_step_date") + "\",";
        data2 = "\"user_step_total_count\"" + ":" + "\"" + maps.get("user_step_total_count") + "\",";
        data3 = "\"user_step_count\"" + ":" + "\"" + maps.get("user_step_count") + "\"}";
        String data = data1 + data2 + data3;
        System.out.println(data);

        String filename = "step" + ID;
        File file = new File(path, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveID(Context context, String id) {
        try {
            FileOutputStream fos = context.openFileOutput("id.data", Context.MODE_PRIVATE);
            fos.write(id.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String readID(Context context) {
        try {
            FileInputStream fis = context.openFileInput("id.data");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String string = br.readLine();
            fis.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
