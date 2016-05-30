package com.heylichen.redis.datatypes.list;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heylichen.commons.base.WithContextBase;
import com.heylichen.redis.datatypes.zset.LeaderBoard;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lc on 2016/5/29.
 */
public class LeaderBoardTest extends WithContextBase {
  @Autowired
  private LeaderBoard leaderBoard;

  @Test
  public void test1() {
    leaderBoard.setKey("game-score");
    leaderBoard.addUser("Arthur", 70);
    leaderBoard.addUser("KC", 20);
    leaderBoard.addUser("Maxwell", 10);
    leaderBoard.addUser("Patrik", 30);
    leaderBoard.addUser("Ana", 60);
    leaderBoard.addUser("Felipe", 40);
    leaderBoard.addUser("Renata", 50);
    leaderBoard.addUser("Hugo", 80);
    leaderBoard.removeUser("Arthur");

    JSONArray top = leaderBoard.getTopUsers(5);
    JSONArray around = leaderBoard.getUsersAroundUser("Felipe", 4);
    JSONObject userDetail = leaderBoard.getUserDetails("Felipe");
    baseLogger.info("top:{}\n around:{}\nuserDetail:{}", new Object[]{
        top.toJSONString(), around.toJSONString(), userDetail.toJSONString()
    });

  }
}
