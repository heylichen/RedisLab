package com.heylichen.redis.timeseries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by lichen2 on 2016/5/31.
 */
public class TimeSeriesByString {

  private final Logger logger = LoggerFactory.getLogger(TimeSeriesByString.class);
  private RedisTemplate<String, String> template;
  private String namespace;
  private TimeUnits timeUnits = new TimeUnits();

  public TimeSeriesByString(
      RedisTemplate<String, String> template, String namespace) {
    this.template = template;
    this.namespace = namespace;
  }

  public void insert(int timestampInSeconds) {
    timeUnits.getGranularityMap().entrySet().forEach(entry -> {
      TimeGranularity gran = entry.getValue();
      String key = getKeyName(gran, timestampInSeconds);
      template.opsForValue().increment(key, 1);
      if (gran.getTtl() > 0) {
        template.expire(key, gran.getTtl(), TimeUnit.SECONDS);
      }
    });
  }

  private String getKeyName(TimeGranularity gran, int timestampInSeconds) {
    String roundedTimestamp = getRoundedTimestamp(timestampInSeconds, gran.getDuration());
    return StringUtils.join(namespace, gran.getName(), roundedTimestamp, ":");
  }

  private String getRoundedTimestamp(int timestampInSeconds, int duration) {
    return String.valueOf(Math.round(timestampInSeconds / duration) * duration);
  }

  public JSONArray fetch(TimeGranularity gran, int beginTimestamp, int endTimestamp) {
    int begin = Integer.valueOf(getRoundedTimestamp(beginTimestamp, gran.getDuration()));
    int end = Integer.valueOf(getRoundedTimestamp(endTimestamp, gran.getDuration()));
    List<String> keys = new ArrayList<>();
    for (int current = begin; current <= end; current += gran.getDuration()) {
      keys.add(StringUtils.join(namespace, gran.getName(), String.valueOf(current), ":"));
    }
    List<String> values = template.opsForValue().multiGet(keys);
    JSONArray result = new JSONArray();
    for (int i = 0; i < keys.size(); i++) {
      JSONObject jo = new JSONObject();
      String stamp = String.valueOf(begin + i * gran.getDuration());
      jo.put("timestamp", stamp);
      jo.put("value", values.get(i));
      result.add(jo);
    }
    logger.info("values:{}", JSON.toJSONString(values));
    return result;

  }

}
