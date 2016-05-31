package com.heylichen.redis.timeseries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lichen2 on 2016/5/31.
 */
public class TimeUnits {

  private static final int second = 1;
  private static final int miniute = 60;
  private static final int hour = 60 * 60;
  public static final TimeGranularity SECOND = new TimeGranularity("1sec", hour * 2, second, miniute * 5);
  private static final int day = 24 * 60 * 60;
  public static final TimeGranularity MINIUTE = new TimeGranularity("1min", day * 7, miniute, hour * 8);
  public static final TimeGranularity HOUR = new TimeGranularity("1hour", day * 60, hour, day * 10);
  public static final TimeGranularity DAY = new TimeGranularity("1day", 0, day, day * 30);

  private Map<String, TimeGranularity> granularityMap;

  public TimeUnits() {
    this.granularityMap = new HashMap<>();
    List<TimeGranularity> list = Arrays.asList(
        SECOND,
        MINIUTE,
        HOUR,
        DAY
    );
    list.forEach(timeGranularity -> {
      granularityMap.put(timeGranularity.getName(), timeGranularity);
    });
  }

  public int getSecond() {
    return second;
  }

  public int getMiniute() {
    return miniute;
  }

  public int getHour() {
    return hour;
  }

  public int getDay() {
    return day;
  }

  public Map<String, TimeGranularity> getGranularityMap() {
    return granularityMap;
  }
}
