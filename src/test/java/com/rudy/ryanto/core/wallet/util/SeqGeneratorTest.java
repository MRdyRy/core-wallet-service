package com.rudy.ryanto.core.wallet.util;

import com.rudy.ryanto.core.wallet.entity.TblSeqRek;
import com.rudy.ryanto.core.wallet.exception.CoreWalletException;
import com.rudy.ryanto.core.wallet.repository.SeqRekRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.rudy.ryanto.core.wallet.util.WalletConstant.SEQ_CACHE_KEY;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SeqGenerator.class)
@TestPropertySource(locations = "classpath:application.properties")
@Slf4j
public class SeqGeneratorTest {

    @MockBean
    private SeqRekRepository seqRekRepository;

    @MockBean
    private RedisTemplate<String , String> redisTemplate;

    @Mock
    private HashOperations hashOperations;

    @Autowired
    private SeqGenerator seqGenerator;

    @SuppressWarnings("all")
    @Before
    public void setup(){
        when(seqRekRepository.save(any())).thenReturn(TblSeqRek.builder()
                        .seq(1)
                        .key("202301")
                .build());
        hashOperations.put(SEQ_CACHE_KEY,"NOREK20231",1);
        when(seqRekRepository.findByKey(any())).thenReturn(seqtab());
    }

    private Optional<TblSeqRek> seqtab(){
        return Optional.of(TblSeqRek.builder()
                        .key("NOREK20231")
                        .seq(1)
                .build());
    }

    @SuppressWarnings("all")
    @Test
    public void generateSequencefromCache_Test(){
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(),any())).thenReturn(1);

        var response = seqGenerator.generateSequence("1");
        log.info("response : {}",response);
        assertNotNull(response);
    }

    @SuppressWarnings("all")
    @Test
    public void generateNewSequence_Test(){
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        var response = seqGenerator.generateSequence("1");
        log.info("response : {}",response);
        assertNotNull(response);
    }

    @Test(expected = CoreWalletException.class)
    public void generateSequence_Error(){
        when(redisTemplate.opsForHash()).thenThrow(new RuntimeException());
        var response = seqGenerator.generateSequence("1");
    }


}
