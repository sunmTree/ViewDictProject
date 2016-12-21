package com.sunm.widght;

import java.util.Calendar;

/**
 * Created by sunmeng on 2016/12/13.
 */

public interface IDigitalClock {
    int POSITION_LEFT = 0;
    int POSITION_CENTER = 1;
    int POSITION_RIGHT = 2;

    void updateTime(Calendar calendar, String date);

    void updateTheme();

    void updatePosition(int position);
}
