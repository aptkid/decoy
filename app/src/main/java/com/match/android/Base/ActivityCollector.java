package com.match.android.Base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 花花不花花 on 2017/5/7.
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }


}
