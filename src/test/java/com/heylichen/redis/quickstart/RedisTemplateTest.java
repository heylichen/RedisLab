package com.heylichen.redis.quickstart;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lc on 2016/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    {
        "/test-context.xml"
    }
)
public class RedisTemplateTest {
  @Autowired
  private RedisTemplate<String, String> template;

  @Test
  public void testTemplate() {
    String msg = template.opsForValue().get("k1");
    System.out.println(msg);
  }

  @Test
  public void testPipeline() {
    long start = System.currentTimeMillis();
    template.executePipelined(new RedisCallback<String>() {
      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        try {
          RedisConnection stringRedisConn = (RedisConnection) connection;
          for (int i = 0; i < 10; i++) {
            byte[] key = ("key" + i).getBytes("UTF-8");
            stringRedisConn.set(key, ("value" + i).getBytes("UTF-8"));
            stringRedisConn.expire(key, 15);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

        return null;
      }
    });

    long end = System.currentTimeMillis();
    System.out.println(end-start);

   /* template.executePipelined(new RedisCallback<String>() {
      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        try {
          RedisConnection stringRedisConn = (RedisConnection) connection;
          for (int i = 0; i < 10; i++) {
            stringRedisConn.set(("key" + i).getBytes("UTF-8"), ("value" + i).getBytes("UTF-8"));
            stringRedisConn.get(("key" + i).getBytes("UTF-8"));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

        return null;
      }
    });*/
  }
}
