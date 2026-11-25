package com.example.hua.config;

import com.example.hua.constant.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebPicConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/img/avatorImages/**")
                .addResourceLocations(Constants.AVATAR_IMAGES_PATH)
                .setCachePeriod(3600);

        registry.addResourceHandler("/img/singerPic/**")
                .addResourceLocations(Constants.SINGER_PIC_PATH)
                .setCachePeriod(3600);

        registry.addResourceHandler("/img/songPic/**")
                .addResourceLocations(Constants.SONG_PIC_PATH)
                .setCachePeriod(3600);

        registry.addResourceHandler("/song/**")
                .addResourceLocations(Constants.SONG_PATH)
                .setCachePeriod(3600);

        registry.addResourceHandler("/img/songListPic/**")
                .addResourceLocations(Constants.SONGLIST_PIC_PATH)
                .setCachePeriod(3600);

        registry.addResourceHandler("/img/swiper/**")
                .addResourceLocations(Constants.BANNER_PIC_PATH)
                .setCachePeriod(3600);

    }

}
