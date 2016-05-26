package com.heylichen.commons.redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lc on 2016/5/26.
 */
@Component
public class RedisHelper {
  public static final String ENCODING = "UTF-8";
  @Autowired
  private RedisTemplate<String, String> template;

  public void mSet(JSONArray dataList, long ttl) {
    final Expiration expiration = Expiration.seconds(ttl);
    template.executePipelined(new RedisCallback<String>() {
      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        try {
          for (int i = 0; i < dataList.size(); i++) {
            byte[] key = ("list_key" + i).getBytes(ENCODING);
            JSONObject data = dataList.getJSONObject(i);
            connection.set(key, data.toJSONString().getBytes(ENCODING), expiration, null);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }
    });
  }


  public void delByKeys(List<String> keys) {
    template.delete(keys);
  }


}
