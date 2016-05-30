package com.heylichen.redis.datatypes.bitmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by lc on 2016/5/30.
 */
@Component
public class ActiveUserCount {
  @Autowired
  private Jedis jedis;


  public void addCount(String action, String date, Long userId) {
    jedis.setbit(getKey(action, date), userId, true);
  }

  public Long uniqueCount(String action, String date) {
    return jedis.bitcount(getKey(action, date));
  }

  public int uniqueCount(String action, String... dates) {
    if (dates == null || dates.length < 2) {
      throw new IllegalArgumentException(
          "requires at least two arguments, to query with one key, refer to uniqueCount(String action, String date)!");
    }
    String first = getKey(action, dates[0]);
    String[] left = new String[dates.length - 1];
    for (int i = 1; i < dates.length; i++) {
      left[i - 1] = getKey(action, dates[i]);
    }
    BitSet bitSet = BitSet.valueOf(new long[]{jedis.bitop(BitOP.OR, first, left)});

    return bitSet.cardinality();
  }

  public List<Long> getUsers(String action, String date) {

    byte[] bytes = jedis.get(getKey(action, date).getBytes(UTF_8));
    if (bytes == null || bytes.length == 0) {
      return Collections.emptyList();
    }
    List<Long> userIds = new ArrayList<>();
    for (int i = 0; i < bytes.length; i++) {
      long base = i * 8;
      byte currentByte = bytes[i];

      for (int j = 7; j >= 0; j--) {
        int set = (currentByte >>> j) & 1;
        if (set == 1) {
          long userId = base + 7 - j;
          userIds.add(userId);
        }
      }
    }
    return userIds;
  }

  private String getKey(String action, String date) {
    return action + ":" + date;
  }
}
