package com.heylichen.commons.base;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lc on 2016/5/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    {
        "/test-context.xml"
    }
)
public abstract class WithContextBase {
    protected static Logger baseLogger  = LoggerFactory.getLogger(WithContextBase.class);
}
