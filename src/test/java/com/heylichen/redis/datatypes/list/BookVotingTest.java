package com.heylichen.redis.datatypes.list;

import com.alibaba.fastjson.JSON;
import com.heylichen.commons.base.WithContextBase;
import com.heylichen.redis.datatypes.hash.BookVoting;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by lc on 2016/5/28.
 */
public class BookVotingTest extends WithContextBase {
  @Autowired
  private RedisTemplate<String, Object> template;

  @Test
  public void testVoting() {
    BookVoting voting = new BookVoting(template);
    String id = "Harry potter";
    voting.saveLink("Harry potter", "J. K. Rowling", "Harry Potter");
    //upvote to 10
    for (int i = 0; i < 10; i++) {
      voting.upVote(id);
    }

    Map<String, Object> infoMap = voting.getInfo(id);
    Integer score = (Integer) infoMap.get("score");
    assertTrue(score.intValue() == 10);
    baseLogger.info("info:{}", JSON.toJSONString(voting.getInfo(id)));
    //test downvote
    for (int i = 0; i < 10; i++) {
      voting.downVote(id);
    }
    infoMap = voting.getInfo(id);
    score = (Integer) infoMap.get("score");
    assertTrue(score.intValue() == 0);
    //remove test data
    voting.removeAll(id);
  }

}
