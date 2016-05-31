package com.heylichen.redis.datatypes.list;

import com.alibaba.fastjson.JSONArray;
import com.heylichen.commons.base.WithContextBase;
import com.heylichen.redis.timeseries.TimeSeriesByHash;
import com.heylichen.redis.timeseries.TimeSeriesByString;
import com.heylichen.redis.timeseries.TimeUnits;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by lichen2 on 2016/5/31.
 */
public class TimeSeriesByStringTest extends WithContextBase {
  @Autowired
  private RedisTemplate<String, String> template;

  @Test
  public void testString(){
    TimeSeriesByString ts = new TimeSeriesByString(template,"act");
    int beginTimestamp = 0;

    ts.insert(beginTimestamp);
    ts.insert(beginTimestamp+1);
    ts.insert(beginTimestamp+1);
    ts.insert(beginTimestamp+3);
    ts.insert(beginTimestamp+61);


    JSONArray ja1 = ts.fetch(TimeUnits.SECOND, beginTimestamp, beginTimestamp + 4);
    JSONArray ja2 = ts.fetch(TimeUnits.MINIUTE, beginTimestamp, beginTimestamp+120);
    baseLogger.info("{}\n{}", ja1.toJSONString(), ja2.toJSONString());
  }
  @Test
  public void testByHash(){
    TimeSeriesByHash ts = new TimeSeriesByHash(template,"act");
    int beginTimestamp = 0;

    ts.insert(beginTimestamp);
    ts.insert(beginTimestamp+1);
    ts.insert(beginTimestamp+1);
    ts.insert(beginTimestamp+3);
    ts.insert(beginTimestamp+61);


    JSONArray ja1 = ts.fetch(TimeUnits.SECOND, beginTimestamp, beginTimestamp + 4);
    JSONArray ja2 = ts.fetch(TimeUnits.MINIUTE, beginTimestamp, beginTimestamp+120);
    baseLogger.info("{}\n{}", ja1.toJSONString(), ja2.toJSONString());
  }


}
