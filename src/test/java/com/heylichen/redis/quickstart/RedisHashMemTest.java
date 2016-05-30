package com.heylichen.redis.quickstart;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heylichen.commons.base.WithContextBase;
import com.heylichen.commons.redis.RedisHelper;
import com.heylichen.commons.redis.RedisMonitor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lc on 2016/5/26.
 */
public class RedisHashMemTest extends WithContextBase {
  @Autowired
  private RedisHelper redisHelper;
  @Autowired
  private RedisTemplate<String, String> template;
  @Autowired
  private RedisMonitor monitor;


  @Test
  public void compareMemoryUsingJSONString() {
    int size = 10000;
    int ttl = 2;
    JSONArray arr = RedisTemplateTest.newData(size);
    monitor.reportMemory();
    asJSONString(arr, ttl);
    monitor.reportMemory();

  }
  @Test
  public void compareMemoryUsingHash() {
    int size = 10000;
    int ttl = 2;
    JSONArray arr = RedisTemplateTest.newData(size);
    monitor.reportMemory();

    //sleep a while
    asHash(arr, ttl);
    monitor.reportMemory();
  }

  private void asJSONString(JSONArray arr, int ttl) {
    redisHelper.mSet(arr, ttl);
  }

  private void asHash(JSONArray dataList, int ttl) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    template.executePipelined(new RedisCallback<String>() {
      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        try {
          for (int i = 0; i < dataList.size(); i++) {
            byte[] key = ("list_key" + i).getBytes(StandardCharsets.UTF_8);
            JSONObject data = dataList.getJSONObject(i);
            Map<byte[], byte[]> fields = new HashMap<>();
            fields.put("name".getBytes(StandardCharsets.UTF_8), data.getString("name").getBytes(StandardCharsets.UTF_8));
            fields.put("selfDescrip".getBytes(StandardCharsets.UTF_8), data.getString("selfDescrip").getBytes(StandardCharsets.UTF_8));
            fields.put("birthday".getBytes(StandardCharsets.UTF_8), df.format(data.getDate("birthday")).getBytes(StandardCharsets.UTF_8));
            connection.hMSet(key, fields);
            connection.expire(key, ttl);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }
    });
  }


}
