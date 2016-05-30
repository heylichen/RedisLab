package com.heylichen.commons.redis;

import com.heylichen.commons.base.WithContextBase;
import com.heylichen.commons.redis.serializer.FastJsonRedisSerializer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by lc on 2016/5/28.
 */
public class FastJsonRedisSerializerTest extends WithContextBase{
  @Autowired
  private JedisConnectionFactory connectionFactory;


  @Test
  public void test(){
    forBean();
  }

  public void forBean(){
    TempUser u = new TempUser();
    u.setId(1L);
    u.setAge(20);
    u.setDesc("hello, everyone！");
    u.setBirthday(new Date());
    //setup
    FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
    RedisTemplate<String,TempUser> redisTemplate = new RedisTemplate<>();
    redisTemplate.setKeySerializer(serializer);
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.afterPropertiesSet();
    //test
    String key = "user_"+u.getId();
    redisTemplate.opsForValue().set(key, u);
    TempUser u1 = redisTemplate.opsForValue().get(key);
    assertTrue(u1!=null);

  }



}
