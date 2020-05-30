package net.yogstation.api.configuration;

import net.yogstation.api.bean.AuthorizationRequestInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@RunWith(MockitoJUnitRunner.class)
public class WebMvcConfigurationTest {
    @Mock
    private AuthorizationRequestInterceptor authorizationRequestInterceptor;

    @Mock
    private InterceptorRegistry interceptorRegistry;

    @InjectMocks
    private WebMvcConfiguration out;

    @Test
    public void test_addInterceptors_AddsAuthorizationInterceptor() {
        out.addInterceptors(interceptorRegistry);

        Mockito.verify(interceptorRegistry).addInterceptor(authorizationRequestInterceptor);
    }
}
