package com.ex00.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // 매핑을 할 때 private 필드에만 접근할 수 있게 함으로써 캡슐화된 필드에만 쓸 수 있다.
                .setMatchingStrategy(MatchingStrategies.LOOSE); // 필드 매칭 전략을 설정해놓은 곳으로 LOOSE로 설정되어 있기 때문에, 느슨한 매칭 전략을 사용할 것입니다.
                // 이는 필드 이름과 유형이 일치하지 않더라도 가능한 한 많은 필드를 매핑하려고 시도할 것을 의미한다.

        return modelMapper;
    }
}
