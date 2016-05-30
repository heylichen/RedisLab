package com.heylichen.redis.datatypes.list;

import com.heylichen.commons.base.WithContextBase;
import com.heylichen.commons.redis.TempUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by lc on 2016/5/28.
 */
public class ABookVotingTest extends WithContextBase {
 @Autowired
  private RedisTemplate<String, TempUser> template;


  @Test
  public void test1(){
    TempUser u = new TempUser();
    u.setId(1L);
    u.setAge(20);
    u.setDesc("hello, everyone！");
    u.setBirthday(new Date());
    //test
    String key = "user_"+u.getId();
    template.opsForValue().set(key, u);
    TempUser u1 = template.opsForValue().get(key);
    assertTrue(u1!=null);
  }
}
