package com.heylichen.redis.datatypes.set;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lc on 2016/5/29.
 */
@Component
public class DealTracking {
  private final Logger logger = LoggerFactory.getLogger(DealTracking.class);
  @Resource(name="redisTemplate")
  private SetOperations<String, String> opsForSet;
  private String prefix = "DealTracking_";


  public void markDealAsSent(String dealId, String userId) {
    opsForSet.add(getKey(dealId), userId);
  }

  public void markIfNotSent(String dealId, String userId) {
    String key = getKey(dealId);
    Boolean isSent = opsForSet.isMember(key, userId);
    if (isSent == null || !isSent) {
      markDealAsSent(key, userId);
    }
  }

  public void showUsersReceivedAll(List<String> dealIds) {
    if (dealIds == null || dealIds.size() < 2) {
      return;
    }
    List<String> keys = getKeys(dealIds);
    Set<String> userIds = opsForSet.intersect(keys.get(0), keys.subList(1, keys.size()));
    logger.info("showUsersReceivedAll:{}", JSON.toJSONString(userIds));
  }

  private List<String> getKeys(List<String> dealIds){
    List<String> keys = new ArrayList<>();
    dealIds.forEach(id-> keys.add(getKey(id)));
    return keys;
  }
  public void showUsersReceivedAny(List<String> dealIds) {
    if (dealIds == null || dealIds.size() < 2) {
      return;
    }
    List<String> keys = getKeys(dealIds);
    Set<String> userIds = opsForSet.union(keys.get(0), keys.subList(1, keys.size()));
    logger.info("showUsersReceivedAny:{}", JSON.toJSONString(userIds));
  }


  public String getKey(String dealId) {
    return prefix + dealId;
  }
}
