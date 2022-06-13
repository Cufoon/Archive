package com.manager.form.message;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SendMessageParam {

    @Length(max = 100)
    private String msgTitle;

    @Length(max = 500)
    private String msgContent;

    @Length(max = 1)
    private String msgType;

    private String[] msgRecvIds;
}
