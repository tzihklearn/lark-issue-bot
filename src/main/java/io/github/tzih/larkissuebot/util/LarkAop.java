package io.github.tzih.larkissuebot.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tzih
 * @version v1.0
 * @since 2024.01.04
 */
@Aspect
@Component
@Slf4j
public class LarkAop {

    private final String pointcut = "execution(* *..controller..*.*(..))";

    private final LarkBotUtil larkBotUtil;

    @Autowired
    public LarkAop(LarkBotUtil larkBotUtil) {
        this.larkBotUtil = larkBotUtil;
        log.info("lark issue bot is ok, pointcut is: {}", pointcut);
    }

    // 切点
    @Pointcut(pointcut)
    public void lark() {
    }

    @AfterThrowing(pointcut = "lark()", throwing = "exception")
    public void larkRobot(JoinPoint point, Exception exception) {
        // 在切点位置发生异常时的处理逻辑

        log.info("lark issue bot is working, exception is happened, now send message to lark robot");
        larkBotUtil.send(point, exception);
        
    }

}
