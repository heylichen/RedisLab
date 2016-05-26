package com.heylichen.redis.quickstart;

import com.alibaba.fastjson.JSONArray;
import com.heylichen.commons.base.WithContextBase;
import com.heylichen.commons.redis.RedisHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by lc on 2016/5/26.
 */
public class RedisHashMemTest extends WithContextBase {
  @Autowired
  private RedisHelper redisHelper;
  @Autowired
  private RedisTemplate<String, String> template;

  @Test
  public void compareMemory(){
    int size = 10000;
    JSONArray arr = RedisTemplateTest.newData(size);

  }


  private void asJSONString(JSONArray arr){

  }

  private void asHash(){

  }



}
