package com.heylichen.commons.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * Created by lc on 2016/5/28.
 */
@Component
public class RedisMonitor {
  private static Logger logger = LoggerFactory.getLogger(RedisMonitor.class);
  @Autowired
  private RedisTemplate<String, String> template;

  /**
   * 查看内存占用信息
   */
  public void reportMemory() {
    String result = template.execute(new RedisCallback<String>() {
      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        try {
          //can exec redis commands like this
          Object obj = connection.execute("info", "memory".getBytes("UTF-8"));
          return new String((byte[]) obj);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
          return null;
        }
      }
    });


    logger.info("result:{}", result);
  }
}
