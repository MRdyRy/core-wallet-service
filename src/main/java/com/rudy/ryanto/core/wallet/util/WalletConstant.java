package com.rudy.ryanto.core.wallet.util;

public class WalletConstant {

    public enum STATUS {
        APPROVE, REJECT, ACTIVE, DORMANT, BLOCK, DISABLE
    }

    public enum HISTORY_STATUS {
        NORMAL, REVERSAL
    }

    public enum ACTIVITY {
        INQUIRY, GET_HISTORY, PRINT
    }

    public enum STAGES {
        INQUIRY, SUBMIT, EXECUTE
    }


    public enum ERROR_DESCRIPTION {
        SALDO_TIDA_MENCUKUPI("X1", "Saldo tidak mencukupi!"),
        GENERAL_ERROR("01", "Terjadi kesalahan, General Error!"),
        FAILED_GENERATE_NOREK("02", "Gagal Generate Norek!!"),
        DATA_NOT_FOUND("03", "Data tidak ditemukan!"),
        INVALID_AMOUNT("04", "Nominal Amount tidak valid!"),
        PARAMETER_NOT_FOUND("05", "Parameter Not Found!"),
        UNBALANCE("06", "Credit Debit Unbalance!");

        private String codeOps = null, description = null;

        private ERROR_DESCRIPTION(String code, String description) {
            this.codeOps = code;
            this.description = description;
        }

        public String getCode() {
            return this.codeOps;
        }

        public String getDescription() {
            return this.description;
        }
    }

    public enum CURRENCY_CODE {
        IDR("360", "RUPIAH");

        private String code = null;
        private String desc = null;

        private CURRENCY_CODE(String code, String desc) {
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


    public enum CACHES_WALLET {
        MIN_SISA_SALDO("MINIMAL_SISA_SALDO", "Minimal sisa saldo rek dalam rupiah"),
        MAX_TRANSACTION_TIER_A("MAX_TRX_TIER_A", "Maximum amount transaksi untuk tier A"),
        MAX_TRANSACTION_TIER_B("MAX_TRX_TIER_B", "Maximum amount transaksi untuk tier B");


        private String cacheName = null;
        private String desc = null;

        private CACHES_WALLET(String cacheName, String desc) {
            this.cacheName = cacheName;
            this.desc = desc;
        }

        public String getCacheName() {
            return this.cacheName;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    public enum GENERATE_TYPE {
        NOREK, USER_ID
    }

    public static final String TITLE_REK = "REKENING_WALLET : ";
    public static final String AUDIT_TOPIC = "audit-topic";


    public enum FLOW_WALLET {
        CREATE_NEW("CREATE_NEW", "CREATE NEW WALLET"),
        GET_HISTORY("GET_HISTORY", "GET HISTORY"),
        UPDATE_BALANCE("UPDATE_BALANCE", "UPDATE BALANCE"),
        INQUIRY("INQUIRY", "INQUIRY");

        private String code = null;
        private String desc = null;

        private FLOW_WALLET(String code, String desc) {
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

}
