package net.yogstation.api.service;


import net.yogstation.api.jpa.entity.BanEntity;
import net.yogstation.api.jpa.repository.BanRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@RunWith(MockitoJUnitRunner.class)
public class BanServiceTest {

    @Mock
    private BanRepository banRepository;

    @InjectMocks
    private BanService banService;

    @Test
    public void test_getBans() {
        banService.getBans(4, 25);

        Mockito.verify(banRepository).findAll(Mockito.anyObject(), Mockito.eq(PageRequest.of(4, 25)));
    }
}
