package com.heylichen.redis.datatypes.list;

import com.heylichen.commons.base.WithContextBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lc on 2016/5/28.
 */
public class StringQueueTest extends WithContextBase {
  @Autowired
  private RedisTemplate<String,String> template;
  @Test
  public void testQueue(){
    StringQueue queue = new StringQueue(2, template, "testSetOps");
    //insert 10 elements
    for(int i=0; i<10; i++){
      queue.push(i+"");
    }
    //test size
    assertTrue(queue.size()==10L);
    //test pop
    for(int i=0; i<10; i++){
      String v = queue.pop();
      assertEquals(v,i+"");
    }
    //test size
    assertTrue(queue.size()==0L);
    //remove test data
    queue.removeAll();
  }

}
