package com.runto.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig { // yml에서도 설정가능하긴한데, Pageable 처리에 대한 공부를 위해 빈 등록방식으로 했습니다.

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {

        // void customize(PageableHandlerMethodArgumentResolver pageableResolver);
        return pageableResolver -> {
            pageableResolver.setOneIndexedParameters(true);
            pageableResolver.setMaxPageSize(20);
        };

    }
}
