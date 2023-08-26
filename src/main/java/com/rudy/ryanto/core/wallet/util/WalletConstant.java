package com.rudy.ryanto.core.wallet.util;

public class WalletConstant {

    public enum STATUS{
        APPROVE, REJECT, ACTIVE, DORMANT, BLOCK, DISABLE
    }
    public enum HISTORY_STATUS{
        NORMAL,REVERSAL
    }

    public enum ACTIVITY{
        INQUIRY, GET_HISTORY, PRINT
    }

    public enum ERROR_DESCRIPTION{
        SALDO_TIDA_MENCUKUPI("X1","Saldo tidak mencukupi!"),
        GENERAL_ERROR("01","Terjadi kesalahan, General Error!"),
        FAILED_GENERATE_NOREK("02","Gagal Generate Norek!!"),
        DATA_NOT_FOUND("03","Data tidak ditemukan!");

        private String codeOps=null,description=null;

        private ERROR_DESCRIPTION(String code,String description){
            this.codeOps=code;
            this.description=description;
        }

        public String getCode(){
            return this.codeOps;
        }

        public String getDescription(){
            return this.description;
        }
    }

    public enum CURRENCY_CODE{
        IDR("360","RUPIAH");

        private String code=null;
        private String desc=null;

        private CURRENCY_CODE(String code, String desc){
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    public enum GENERATE_TYPE{
        NOREK, USER_ID
    }

    public static final String TITLE_REK = "REKENING_WALLET : ";


}
