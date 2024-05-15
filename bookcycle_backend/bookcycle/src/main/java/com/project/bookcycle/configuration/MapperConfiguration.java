package com.project.shopapp.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    /*
    việc tạo ra một bean là quá trình định nghĩa một đối tượng hoặc một thành phần cụ thể để quản lý và sử dụng trong
    ứng dụng của bạn. Khi một bean được tạo, Spring sẽ quản lý vòng đời của nó, bao gồm cấp phát, khởi tạo, và phá hủy.
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
