package com.heylichen.redis.datatypes.list;

import com.heylichen.commons.base.WithContextBase;
import com.heylichen.redis.datatypes.set.DealTracking;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Created by lc on 2016/5/28.
 */
public class DealTrackingTest extends WithContextBase {
  @Autowired
  private DealTracking dealTracking;
  @Test
  public void testSetOps() {
    dealTracking.markDealAsSent("deal1",  "user1");
    dealTracking.markDealAsSent("deal1",  "user2");
    dealTracking.markDealAsSent("deal2",  "user1");
    dealTracking.markDealAsSent("deal2",  "user3");

    dealTracking.markIfNotSent("deal1", "user1");
    dealTracking.markIfNotSent("deal1", "user2");
    dealTracking.markIfNotSent("deal1", "user3");

    dealTracking.showUsersReceivedAll(Arrays.asList("deal1","deal2"));
    dealTracking.showUsersReceivedAny(Arrays.asList("deal1","deal2"));
  }

}
