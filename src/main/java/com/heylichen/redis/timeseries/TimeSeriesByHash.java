package com.heylichen.redis.timeseries;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lichen2 on 2016/5/31.
 */
public class TimeSeriesByHash {

  private final Logger logger = LoggerFactory.getLogger(TimeSeriesByHash.class);
  private RedisTemplate<String, String> template;
  private String namespace;
  private TimeUnits timeUnits = new TimeUnits();

  public TimeSeriesByHash(
      RedisTemplate<String, String> template, String namespace) {
    this.template = template;
    this.namespace = namespace;
  }

  public void insert(int timestampInSeconds) {
    timeUnits.getGranularityMap().entrySet().forEach(entry -> {
      TimeGranularity gran = entry.getValue();
      String key = getKeyName(gran, timestampInSeconds);
      String fieldName = getRoundedTimestamp(timestampInSeconds, gran.getDuration());
      template.opsForHash().increment(key, fieldName, 1);
      if (gran.getTtl() > 0) {
        template.expire(key, gran.getTtl(), TimeUnit.SECONDS);
      }
    });
  }

  private String getKeyName(TimeGranularity gran, int timestampInSeconds) {
    String roundedTimestamp = getRoundedTimestamp(timestampInSeconds, gran.getQuantity());
    return StringUtils.join(namespace, gran.getName(), roundedTimestamp, ":");
  }

  private String getRoundedTimestamp(int timestampInSeconds, int duration) {
    return String.valueOf(Math.round(timestampInSeconds / duration) * duration);
  }

  public JSONArray fetch(TimeGranularity gran, int beginTimestamp, int endTimestamp) {
    JSONArray result = new JSONArray();
    int begin = Integer.valueOf(getRoundedTimestamp(beginTimestamp, gran.getDuration()));
    int end = Integer.valueOf(getRoundedTimestamp(endTimestamp, gran.getDuration()));
    List<String> keys = new ArrayList<>();
    for (int current = begin; current <= end; current += gran.getDuration()) {
      String key = getKeyName(gran, current);
      Object v = template.opsForHash().get(key, current);
      JSONObject jo = new JSONObject();
      jo.put("timestamp", current);
      jo.put("value", v );
      result.add(jo);
    }
    return result;

  }

}
