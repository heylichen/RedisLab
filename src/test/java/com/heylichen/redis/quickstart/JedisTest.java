package com.heylichen.redis.quickstart;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class JedisTest {
  private Jedis jedis;

  @Before
  public void setup() {
    //连接redis服务器，虚拟机的ip地址192.168.20.128:6479
    jedis = new Jedis("192.168.192.128", 6479);
    //权限认证
  }

  /**
   * redis存储字符串
   */
  @Test
  public void testString() {
    String msg = jedis.get("k1");
    System.out.println(msg);
  }
}