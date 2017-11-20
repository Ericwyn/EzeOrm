package com.ericwyn.ezeorm.expection;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class EzeExpection extends Exception{
    private String retCd; //异常对应的返回码
    private String msgDes; //异常对应的描述信息

    public EzeExpection() {
        super();
    }

    public EzeExpection(String message) {
        super(message);
        msgDes = message;
    }

    public EzeExpection(String retCd, String msgDes) {
        super();
        this.retCd = retCd;
        this.msgDes = msgDes;
    }

    public String getRetCd() {
        return retCd;
    }

    public String getMsgDes() {
        return msgDes;
    }
}
