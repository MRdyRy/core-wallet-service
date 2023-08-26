package com.rudy.ryanto.core.wallet.util;

import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.repository.SeqRekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import java.util.Calendar;
import java.util.Date;

@Component
public class SeqGenerator {

    @Autowired
    private SeqRekRepository seqRekRepository;

    private final Date date;

    public SeqGenerator(Date date) {
        this.date = date;
    }

    @Lock(LockModeType.WRITE)
    public String generateSequence(String userId) {
        String norek = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            String id = WalletConstant.GENERATE_TYPE.NOREK.name() + year + userId;

            var temp = seqRekRepository.findById(id);
            if (temp.isPresent()) {
                var tblSeq = temp.get();
                var sequence = tblSeq.getSeq() + 1;
                norek = year + sequence + userId;
                tblSeq.setSeq(sequence);
                seqRekRepository.save(tblSeq);
            }

        } catch (Exception e) {
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.FAILED_GENERATE_NOREK.getDescription());
        }
        return norek;
    }
}
