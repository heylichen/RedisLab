package com.heylichen.redis.quickstart;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lc on 2016/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    {
        "/test-context.xml"
    }
)
public class RedisTemplateTest {
  private Logger logger = LoggerFactory.getLogger(RedisTemplateTest.class);
  @Autowired
  private RedisTemplate<String, String> template;

  @Test
  public void testTemplate() {
    String msg = template.opsForValue().get("k1");
    System.out.println(msg);
  }

  @Test
  public void performanceCompareWithPipeline() {
    //warming up
    logger.info("warming up");
    int size = 10;
    JSONArray dataList = newData(size);
    byPipeline(dataList, 30);
    mSetByPipeline(dataList, 30);
    multiSetNoBatch(dataList, 30);
    multiSetPartBatch(dataList, 30);

    logger.info("start test");
    //start test
    Stopwatch sw = Stopwatch.createStarted();
    multiSetNoBatch(dataList, 30);
    logger.info("multiSetNoBatch, size:{}, using :{}", size, sw.stop());

    sw = Stopwatch.createStarted();
    multiSetPartBatch(dataList, 30);
    logger.info("multiSetPartBatch, size:{}, using :{}", size, sw.stop());


    sw = Stopwatch.createStarted();
    byPipeline(dataList, 30);
    logger.info("byPipeline, size:{}, using :{}", size, sw.stop());

    sw = Stopwatch.createStarted();
    mSetByPipeline(dataList, 30);
    logger.info("mSetByPipeline, size:{}, using :{}", size, sw.stop());

  }

  /**
   * test show that this is the best one
   * @param dataList
   * @param ttl
   */
  private void byPipeline(JSONArray dataList, long ttl) {
    final Expiration expiration = Expiration.seconds(ttl);
    template.executePipelined(new RedisCallback<String>() {
      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        try {
          for (int i = 0; i < dataList.size(); i++) {
            byte[] key = ("list_key" + i).getBytes("UTF-8");
            JSONObject data = dataList.getJSONObject(i);
            connection.set(key, data.toJSONString().getBytes("UTF-8"), expiration, null);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }
    });
  }

  private void mSetByPipeline(JSONArray dataList, long ttl) {
    template.executePipelined(new RedisCallback<String>() {
      @Override
      public String doInRedis(RedisConnection connection) throws DataAccessException {
        try {
          Map<byte[], byte[]> tuple = new HashMap<>();
          for (int i = 0; i < dataList.size(); i++) {
            JSONObject data = dataList.getJSONObject(i);
            tuple.put(("list_key" + i).getBytes("UTF-8"), data.toJSONString().getBytes("UTF-8"));
          }
          connection.mSet(tuple);
          tuple.keySet().forEach(key -> connection.expire(key, ttl));
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }
    });
  }

  private void multiSetNoBatch(JSONArray dataList, long ttl) {
    for (int i = 0; i < dataList.size(); i++) {
      JSONObject data = dataList.getJSONObject(i);
      String key = "list_key" + i;
      template.opsForValue().set(key, data.toJSONString(), ttl, TimeUnit.SECONDS);
    }
  }


  private void multiSetPartBatch(JSONArray dataList, long ttl) {
    Map<String, String> pairs = new HashMap<>();

    for (int i = 0; i < dataList.size(); i++) {
      JSONObject data = dataList.getJSONObject(i);
      String key = "list_key" + i;
      pairs.put(key, data.toJSONString());
    }
    template.opsForValue().multiSetIfAbsent(pairs);
    pairs.keySet().forEach(key -> {
      template.expire(key, ttl, TimeUnit.SECONDS);
    });
  }

  public static JSONArray newData(int size) {
    JSONArray result = new JSONArray();
    for (int i = 0; i < size; i++) {
      JSONObject jo = new JSONObject();
      jo.put("name", "张三" + i);
      jo.put("birthday", new Date());
      jo.put("selfDescrip", "5月26日消息，网络支付实名认证新规将于今年7月1日起执行。TechWeb" +
          "从支付宝和微信方面确认，届时未实名认证的用户发红包、转账等功能将受到限制。支付宝方面称" +
          "，只有实名且完善了更多身份信息后，才有可能享受余额宝、招财宝、蚂蚁花呗和蚂蚁借呗等金融服" +
          "务，账户的余额支付功能是需要身份验证信息完善程度达到一定等级才能使用的，而发红包需要走" +
          "余额通路，也需要实名。“沃兹负责开发设计，乔布斯负责营销”，这是苹果（电脑）公司成立之初的" +
          "状态。如果你看过官方《乔布斯传》，应该会了解到乔布斯其实对科技并不擅长。而今，另外一名苹果联" +
          "合创始人再次提到了这一点。威锋网消息，史蒂夫·沃兹尼亚克在与一名学生进行对话时直接说出了这样" +
          "的话：乔布斯并不了解科技，他只想让别人把他看得很重要。\n" +
          "VeryCloud云主机/CDN\n");
      result.add(jo);
    }
    return result;
  }
}
