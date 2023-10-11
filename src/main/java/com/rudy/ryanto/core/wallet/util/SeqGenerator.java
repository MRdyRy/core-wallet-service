package com.rudy.ryanto.core.wallet.util;

import com.rudy.ryanto.core.wallet.entity.TblSeqRek;
import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.repository.SeqRekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Calendar;
import java.util.Date;

import static com.rudy.ryanto.core.wallet.util.WalletConstant.SEQ_CACHE_KEY;

@Component
public class SeqGenerator {

    private final Date date;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SeqRekRepository seqRekRepository;

    public SeqGenerator(Date date) {
        this.date = date;
    }

    @SuppressWarnings("all")
    @Lock(LockModeType.WRITE)
    @Transactional
    public synchronized String generateSequence(String userId) {
        String norek = "";
        try {
            generateKey result = getGenerateKey(userId);
            Integer seq = (Integer) redisTemplate.opsForHash().get(SEQ_CACHE_KEY, result.id);
            if (null == seq) {
                var temp = seqRekRepository.findByKey(result.id);
                temp.ifPresentOrElse(s -> s.setSeq(s.getSeq() + 1), () -> new TblSeqRek(result.id, 1));
                norek = result.year + temp.get().getSeq() + userId;
                seqRekRepository.save(temp.get());
                putSequenceToCache(result, temp.get().getSeq());
            } else {
                var sequence = seq + 1;
                var temp = seqRekRepository.findByKey(result.id);
                temp.ifPresent(s -> s.setSeq(sequence));
                norek = result.year + sequence + userId;
                seqRekRepository.save(temp.get());
                putSequenceToCache(result, sequence);
            }
        } catch (Exception e) {
            throw new CoreWalletException(WalletConstant.ERROR_DESCRIPTION.FAILED_GENERATE_NOREK.getDescription());
        }
        return norek;
    }

    private void putSequenceToCache(generateKey result, Integer sequence) {
        redisTemplate.opsForHash().put(SEQ_CACHE_KEY, result.id, sequence);
    }

    private generateKey getGenerateKey(String userId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        String id = WalletConstant.GENERATE_TYPE.NOREK.name() + year + userId;
        return new generateKey(year, id);
    }

    private static class generateKey {
        public final int year;
        public final String id;

        public generateKey(int year, String id) {
            this.year = year;
            this.id = id;
        }
    }
}
