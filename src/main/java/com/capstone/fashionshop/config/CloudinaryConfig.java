package com.capstone.fashionshop.config;

import com.capstone.fashionshop.utils.ImageUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public CommonsMultipartResolver commonsMultipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmlt1eshx",
                "api_key", "762325771148589",
                "api_secret", "qGwPAxMLYcFE6J3SgM2_nZ0n0nc",
                "secure",true
        ));
    }

    public String getPublicId(String urlImage){
        int temp1 = urlImage.lastIndexOf(".");
        int temp2 = urlImage.lastIndexOf("/");
        return urlImage.substring(temp2+1,temp1);
    }

    public String uploadImage(MultipartFile file, String urlDestroy) throws IOException {
        Map params = ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "fashion"
        );
        Map map = cloudinary().uploader().upload(ImageUtils.convertMultiPartToFile(file),params);
        deleteImage(urlDestroy);
        ImageUtils.deleteMultipartFile(ImageUtils.convertMultiPartToFile(file));
        return map.get("secure_url").toString();
    }

    public void deleteImage(String urlImage) throws IOException {
        if (urlImage != null && urlImage.startsWith("https://res.cloudinary.com/dmlt1eshx/image/upload")) {
            cloudinary().uploader().destroy("fashion/" + getPublicId(urlImage)
                    , ObjectUtils.asMap("resource_type", "image"));
        }
    }
}
