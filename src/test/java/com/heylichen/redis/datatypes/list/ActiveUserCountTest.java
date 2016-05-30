package com.heylichen.redis.datatypes.list;

import com.alibaba.fastjson.JSON;
import com.heylichen.commons.base.WithContextBase;
import com.heylichen.redis.datatypes.bitmap.ActiveUserCount;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by lc on 2016/5/30.
 */
public class ActiveUserCountTest extends WithContextBase {
  @Autowired
  private ActiveUserCount activeUserCount;

  @Test
  public void testBit() {
    String date = "2016-06-01";
    String action = "active";
    activeUserCount.addCount(action, date, 1000L);
    activeUserCount.addCount(action, date, 1001L);
    activeUserCount.addCount(action, date, 1831L);
    Long count = activeUserCount.uniqueCount(action, date);
    baseLogger.info("count:{}", count);
    List<Long> users = activeUserCount.getUsers(action, date);
    baseLogger.info("users:{}", JSON.toJSONString(users));

    String date2 = "2016-06-03";
    activeUserCount.addCount(action, date2, 1831L);
    activeUserCount.addCount(action, date, 1001L);
    int count2 = activeUserCount.uniqueCount(action, date, date2);
    baseLogger.info("count:{}", count2);

  }

}
