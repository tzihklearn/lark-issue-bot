package io.github.tzih.larkissuebot.pojo.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @author tzih
 * @version v1.0
 * @since 2023.05.04
 */
@Setter
@Getter
public class ReqData {
    private String uri;
    private String method;
    private Object parameter;

}
