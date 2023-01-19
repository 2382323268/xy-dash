package com.xy.dash.utli.exception;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.xy.dash.utli.api.IResultCode;
import com.xy.dash.utli.api.ResultCode;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;
    private final IResultCode resultCode;

    public ServiceException(String message) {
        super(message);
        this.resultCode = ResultCode.FAILURE;
    }

    public ServiceException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ServiceException(IResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }

    public IResultCode getResultCode() {
        return this.resultCode;
    }
}
