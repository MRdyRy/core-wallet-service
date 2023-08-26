package com.rudy.ryanto.core.wallet.exception;

public class CoreWalletException extends RuntimeException{

    public CoreWalletException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreWalletException(String message) {
        super(message);
    }
}
