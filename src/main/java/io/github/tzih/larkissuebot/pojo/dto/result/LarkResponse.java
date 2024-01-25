package io.github.tzih.larkissuebot.pojo.dto.result;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tzih
 * @version v1.0
 * @since 2023.05.04
 */
@Setter
@Getter
public class LarkResponse {

    private Integer code;

    private String msg;

    private Object data;

    @JsonAlias("Extra")
    private Object extra;

    @JsonAlias("StatusCode")
    private Integer statusCode;

    @JsonAlias("StatusMessage")
    private String statusMessage;

}
