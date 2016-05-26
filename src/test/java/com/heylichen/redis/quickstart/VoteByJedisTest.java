package com.heylichen.redis.quickstart;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

/**
 * Created by lc on 2016/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    {
      /*  "/spring/root-context.xml"*/
        "/test-context.xml"
    }
)
public class VoteByJedisTest {
  @Autowired
  private Jedis jedis;
  @Test
  public void testJedis(){
    String msg = jedis.get("k1");
    System.out.println(msg);
  }
}
