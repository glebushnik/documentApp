package config.mapper;

import com.api.documentApp.domain.mapper.user.UserResponseMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperBean {
    @Bean
    public UserResponseMapper userResponseMapper() {
        return Mappers.getMapper(UserResponseMapper.class);
    }
}
