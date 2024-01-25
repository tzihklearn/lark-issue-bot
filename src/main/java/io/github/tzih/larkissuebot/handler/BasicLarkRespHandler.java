package io.github.tzih.larkissuebot.handler;

import com.alibaba.fastjson2.JSON;
import io.github.tzih.larkissuebot.pojo.dto.result.LarkResponse;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author tzih
 * @version v1.0
 * @since 2024.01.05
 */
@Component
public class BasicLarkRespHandler extends AbstractHttpClientResponseHandler<LarkResponse> {

    /**
     * 重写handleEntity方法，将返回的json字符串转换为LarkResponse对象
     *
     * @param entity HttpEntity @NotNull
     * @return LarkResponse 对象
     * @throws IOException IOException
     */
    @Override
    public LarkResponse handleEntity(HttpEntity entity) throws IOException {

        InputStream content = entity.getContent();

        Object o = JSON.parseObject(content, LarkResponse.class);
        if (o instanceof LarkResponse) {
            return (LarkResponse) o;
        }
        return null;
    }
}
