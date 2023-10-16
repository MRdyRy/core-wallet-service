package com.rudy.ryanto.core.wallet.util;

import com.rudy.ryanto.core.wallet.domain.AccountingDto;
import com.rudy.ryanto.core.wallet.entity.WalletHistory;
import com.rudy.ryanto.core.wallet.entity.WalletHistoryDetail;
import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.repository.WalletHistoryDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class HistoryGenerator {

    public HistoryGenerator() {
        clearDebitCredit();
    }

    @Autowired
    private WalletHistoryDetailRepository walletHistoryDetailRepository;

    HashMap<String, BigDecimal> hashMapDebit = new HashMap<>();
    HashMap<String, BigDecimal> hashMapCredit = new HashMap<>();
    HashMap<String, AccountingDto> hAll = new HashMap<>();

    public void doAccountingCredit(String norek, Boolean credit, BigDecimal amount, WalletHistory walletHistory) {
        try {
            if (!checkNull(norek) || !checkNull(credit) || !checkNull(walletHistory))
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        } catch (Exception e) {
            log.error("error accounting : ", e);
            clearDebitCredit();
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        }

        try {
            hashMapCredit.put(norek, amount);
            hAll.put(norek, AccountingDto.builder()
                    .type('C')
                    .amount(amount)
                    .norek(norek)
                    .idMaster(walletHistory.getId())
                    .build());
        } catch (Exception e) {
            log.error("error processing : ", e);
            clearDebitCredit();
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        }
    }

    public void doAccountingDebit(String norek, Boolean debit, BigDecimal amount, WalletHistory walletHistory) {
        try {
            if (!checkNull(norek) || !checkNull(debit) || !checkNull(walletHistory))
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        } catch (Exception e) {
            log.error("error accounting : ", e);
            clearDebitCredit();
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        }

        try {
            hashMapDebit.put(norek, amount);
            hAll.put(norek, AccountingDto.builder()
                    .norek(norek)
                    .amount(amount)
                    .type('D')
                    .idMaster(walletHistory.getId())
                    .build());
        } catch (Exception e) {
            log.error("error processing : ", e);
            clearDebitCredit();
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        }
    }

    @SuppressWarnings("all")
    public Boolean doPost() {
        log.info("do posting !");
        AtomicReference<Boolean> isSuccess = new AtomicReference<>(Boolean.FALSE);
        try {
            final BigDecimal[] nominalCredit = {new BigDecimal(0)};
            final BigDecimal[] nominalDebit = {new BigDecimal(0)};
            hashMapCredit.values().forEach(i->{
                nominalCredit[0] = nominalCredit[0].add(i);
            });
            hashMapDebit.values().forEach(i-> {
                nominalDebit[0] = nominalDebit[0].add(i);
            });
            if (nominalCredit[0].compareTo(nominalDebit[0]) != 0) {
                clearDebitCredit();
                throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.UNBALANCE.getDescription());
            }
            AtomicInteger i = new AtomicInteger();
            hAll.values().forEach(x -> {
                var result = walletHistoryDetailRepository.save(constructDetail(x, i.getAndIncrement()));
                if(null!=result)
                    isSuccess.set(Boolean.TRUE);
            });
        } catch (Exception e) {
            isSuccess.set(Boolean.FALSE);
            log.error("failed to post accounting, caused  : ",e);
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.GENERAL_ERROR.getDescription());
        } finally {
            clearDebitCredit();
        }
        return isSuccess.get();
    }

    private WalletHistoryDetail constructDetail(AccountingDto x, int i) {
        WalletHistoryDetail walletHistoryDetail = new WalletHistoryDetail();
        walletHistoryDetail.setNorek(x.getNorek());
        walletHistoryDetail.setSequence(i);
        if (x.getType() == 'C')
            walletHistoryDetail.setCredit(x.getAmount());
        if (x.getType() == 'D')
            walletHistoryDetail.setDebet(x.getAmount());
        walletHistoryDetail.setIdMaster(x.getIdMaster());
        return walletHistoryDetail;
    }

    private void clearDebitCredit() {
        hashMapDebit.clear();
        hashMapCredit.clear();
        hAll.clear();
    }

    private <T> Boolean checkNull(T n) {
        if (null == n || "".equals(n))
            return Boolean.FALSE;
        else
            return Boolean.TRUE;
    }
}
