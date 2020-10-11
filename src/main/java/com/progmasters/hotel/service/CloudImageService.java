package com.progmasters.hotel.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CloudImageService {

    @Autowired
    private Environment env;
    private static Cloudinary cloudinary;

    //For demo version
    //Non deletable images from cloudaniry for hotel id number 1 in demo version
    public static final List<String> NON_DELETABLE_IMAGES_FOR_HOTEL_1 = new ArrayList<>(Arrays.asList(
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625348/ghddxdo9mukw56uxsiif.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625352/xyqwnet7b0n7pfcgdzfi.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625356/fd3qjrwg1aqzwe7sinlr.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625361/jqburr9rlhookl8d3kik.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625373/qwac71rs3vj4pnkqmdvk.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625378/dpsh6ywmznyukbis3wma.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625385/rmiseyggbcufsy483bcu.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625389/lpwzpyhwgogfejuvzs0n.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1601625393/jv0l55g52mqrgbb1aokd.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584021123/sy1guk8o0mo2wcys75vq.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584021124/xhwcici966iuaiqmr07z.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584021126/g51pvqdxlksd6zdcnsl1.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584021127/dm6yxmb24u9t4atceqob.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584021129/cxsskti7j8o1smmbccgb.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584021131/lncnhnjqkkctrwqrbk9q.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584021132/tg1b4htcor9545yhhb1n.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584021135/nrm2w5l219e7o3bii6xl.jpg"
    ));

    public static final List<String> OTHER_IMAGES_ON_CLOUDINARY = new ArrayList<>(Arrays.asList(
            "http://res.cloudinary.com/doaywchwk/image/upload/v1591973849/Akarmi/background_c4qrdl.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1589268435/background.jpg",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1585830612/logo_SIGN_s14ohn.png",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1585825631/high_five_logo.png",
            "http://res.cloudinary.com/doaywchwk/image/upload/v1584571162/image_not_found.png"
    ));

    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", env.getProperty("spring.cloudinary.cloud_name"),
                "api_key", env.getProperty("spring.cloudinary.api_key"),
                "api_secret", env.getProperty("spring.cloudinary.api_secret")
        ));
    }

    public String uploadImage(MultipartFile file) {
        String url = null;

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            url = uploadResult.get("url").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public void deleteImageFromCloud(String imageURL) {
        //For demo version
        if (!NON_DELETABLE_IMAGES_FOR_HOTEL_1.contains(imageURL)) {
            if (imageURL != null && !imageURL.isEmpty() && !imageURL.isBlank()) {
                try {
                    cloudinary.uploader().destroy(getFileNameWithoutExtension(imageURL), ObjectUtils.emptyMap());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getFileNameWithoutExtension(String url) {
        int slashIndex = url.lastIndexOf('/');
        int dotIndex = url.lastIndexOf('.');
        //TODO if room images uploading is ready, to be deleted
        if (slashIndex == -1) {
            slashIndex = 0;
        }
        if (dotIndex == -1) {
            dotIndex = 1;
        }
        return url.substring(slashIndex + 1, dotIndex);
    }

    //For demo version
    public List<String> getAllCloudinaryImagesUrl() {
        List<String> cloudinaryImagesUrl = new ArrayList<>();
        try {
            boolean hasNext = true;
            String nextCursor = null;

            while (hasNext) {
                ApiResponse resources = cloudinary.api().resources(ObjectUtils.asMap("max_results", 500, "next_cursor", nextCursor));
                if (resources.containsKey("next_cursor")) {
                    nextCursor = resources.get("next_cursor").toString();
                } else {
                    hasNext = false;
                }
                List<Map> resourcesList = (List<Map>) resources.get("resources");
                for (Map map : resourcesList) {
                    cloudinaryImagesUrl.add(map.get("url").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloudinaryImagesUrl;
    }


}
