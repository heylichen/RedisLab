package com.heylichen.redis.datatypes.list;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by lc on 2016/5/28.
 */
public class StringQueue {
  private RedisTemplate<String,String> template;
  private String queueName;
  private long timeout;

  public StringQueue(long timeout, RedisTemplate<String, String> template, String queueName) {
    this.timeout = timeout;
    this.template = template;
    this.queueName = queueName;
  }

  public void push(String value){
    template.opsForList().leftPush(queueName,value);
  }

  public String pop(){
    return template.opsForList().rightPop(queueName,timeout, TimeUnit.SECONDS);
  }
  public Long size(){
    return template.opsForList().size(queueName);
  }

  public void removeAll(){
    template.delete(queueName);
  }

}
