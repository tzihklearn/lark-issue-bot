package io.github.tzih.larkissuebot.util;

import com.alibaba.fastjson2.JSONObject;
import io.github.tzih.larkissuebot.handler.BasicLarkRespHandler;
import io.github.tzih.larkissuebot.pojo.dto.data.Entry;
import io.github.tzih.larkissuebot.pojo.dto.result.LarkResponse;
import io.github.tzih.larkissuebot.pojo.po.ReqData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author tzih
 * @version v1.0
 * @since 2024.01.05
 */
@Component
@ConfigurationProperties("lark-robot")
@Slf4j
public class LarkBotUtil {

    @Setter
    private Boolean dev;

    @Setter
    private String url;

    private final CloseableHttpClient httpClient;

    private final BasicLarkRespHandler basicLarkRespHandler;

    @Autowired
    public LarkBotUtil(CloseableHttpClient httpClient, BasicLarkRespHandler basicLarkRespHandler) {
        this.httpClient = httpClient;
        this.basicLarkRespHandler = basicLarkRespHandler;
    }

    public static List<Object> getParameter(JoinPoint joinPoint, Method method) {

        //获取请求参数
        Object[] args = joinPoint.getArgs();
        //设置请求参数类
        List<Object> argList = new ArrayList<>();
        //获取请求参数
        int i = 0;
        for (Parameter parameter : method.getParameters()) {
            //获取@RequestBody注解的参数
            RequestBody requestBodyAnnotation = parameter.getAnnotation(RequestBody.class);
            if (requestBodyAnnotation != null) {
                argList.add(args[i]);
                ++i;
                continue;
            }

            //获取@RequestParam注解的参数
            RequestParam annotation1 = parameter.getAnnotation(RequestParam.class);
            if (annotation1 != null) {
                Map<String, Object> map = new HashMap<>();
                map.put(parameter.getName(), args[i]);
                argList.add(map);
            }
            ++i;
        }

        return argList;
    }

    public void send(JoinPoint joinPoint, Exception exception) {

        // 判断是否为开发环境, 如果是开发环境则不发送消息
        if (dev) {
            return;
        }

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        //获取请求的url
        String urlStr = request.getRequestURL().toString();

        /*
          设置请求信息
         */
        ReqData webReqData = new ReqData();
        webReqData.setMethod(request.getMethod());

        webReqData.setParameter(getParameter(joinPoint, method));
        webReqData.setUri(request.getRequestURI());

        /*
          组装消息实体类
         */
        Entry entry = new Entry("", urlStr, webReqData.toString(), exception.getMessage() +
                ": **" + exception.getClass().getName() + "**", Arrays.toString(exception.getStackTrace()));

        // 设置请求体
        HttpPost httpPost = new HttpPost(url);
        HttpEntity httpEntity = new StringEntity(JSONObject.toJSONString(entry));
        httpPost.setEntity(httpEntity);

        // 发送消息
        try {
            LarkResponse response = httpClient.execute(httpPost, basicLarkRespHandler);
            if (response != null) {
                log.info("lark issue bot send message is ok, message param is {}, response is {}", httpPost, response);
            }
        } catch (IOException e) {
            log.error("lark issue bot send message is error, message param is {}, error message is {}", httpPost, e.getMessage());
        }

    }

}
