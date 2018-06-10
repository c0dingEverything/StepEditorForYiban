package stepeditor.yiban.hhh;

import java.util.Map;

public class ShowInfoUtils {

    public static String showTotalSteps(Map<String, String> maps, String step) {
        String total = maps.get("user_step_total_count");
        int totalStep = Integer.parseInt(total);
        int steps = Integer.parseInt(step);
        int newTotalStep = totalStep + steps;
        String newStep = Integer.toString(newTotalStep);

        return newStep;
    }
}
