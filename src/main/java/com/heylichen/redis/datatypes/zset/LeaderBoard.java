package com.heylichen.redis.datatypes.zset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heylichen.redis.datatypes.set.DealTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by lc on 2016/5/29.
 */
@Component
public class LeaderBoard {
  private final Logger logger = LoggerFactory.getLogger(DealTracking.class);
  @Resource(name = "redisTemplate")
  private ZSetOperations<String, String> ops;
  private String key;

  public LeaderBoard() {
  }

  public LeaderBoard(String key) {
    this.key = key;
  }

  public void addUser(String user, Integer score) {
    ops.add(key, user, score);
  }

  public void removeUser(String user) {
    ops.remove(key, user);
  }

  public JSONObject getUserDetails(String user) {
    Double score = ops.score(key, user);
    Long rank = ops.reverseRank(key, user) + 1;
    JSONObject result = new JSONObject();
    result.put("user", user);
    result.put("score", score);
    result.put("rank", rank);
    return result;
  }

  public JSONArray getTopUsers(int quantity) {
    Set<ZSetOperations.TypedTuple<String>> temp = ops.reverseRangeWithScores(key, 0, quantity - 1);
    return toJSONArray(temp);
  }

  private JSONArray toJSONArray(Set<ZSetOperations.TypedTuple<String>> temp) {
    JSONArray result = new JSONArray();
    temp.forEach(tuple -> {
      JSONObject jo = new JSONObject();
      jo.put("user", tuple.getValue());
      jo.put("score", tuple.getScore());
      result.add(jo);
    });
    return result;
  }

  public JSONArray getUsersAroundUser(String user, int quantity) {
    Long rank = ops.reverseRank(key, user);
    long startOffset = rank - Math.round(quantity / 2);
    long end = startOffset + quantity - 1;
    Set<ZSetOperations.TypedTuple<String>> temp = ops.reverseRangeWithScores(key, startOffset, end);
    return toJSONArray(temp);
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
