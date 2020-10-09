package com.progmasters.hotel.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.progmasters.hotel.domain.*;
import com.progmasters.hotel.dto.*;
import com.progmasters.hotel.repository.HotelRepository;
import com.progmasters.hotel.repository.RoomRepository;
import com.progmasters.hotel.repository.RoomReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelService {

    @Autowired
    private Environment env;

    private static Cloudinary cloudinary;

    private HotelRepository hotelRepository;
    private RoomRepository roomRepository;
    private RoomReservationRepository roomReservationRepository;

    @Autowired
    public HotelService(RoomRepository roomRepository, HotelRepository hotelRepository, RoomReservationRepository roomReservationRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.roomReservationRepository = roomReservationRepository;
    }

    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", env.getProperty("spring.cloudinary.cloud_name"),
                "api_key", env.getProperty("spring.cloudinary.api_key"),
                "api_secret", env.getProperty("spring.cloudinary.api_secret")
        ));
    }

    public List<Hotel> findAllHotel() {
        return hotelRepository.findAll();
    }

    public List<HotelListItem> getHotelListItemList() {
        return findAllHotel().stream().map(HotelListItem::new).collect(Collectors.toList());
    }

    public List<HotelFeatureTypeOption> getHotelFeatureTypeOptionList() {
        return Arrays.stream(HotelFeatureType.values()).map(HotelFeatureTypeOption::new).collect(Collectors.toList());
    }

    public List<HotelTypeOption> getHotelTypeOptionList() {
        return Arrays.stream(HotelType.values()).map(HotelTypeOption::new).collect(Collectors.toList());
    }

    public HotelListItemSubList getPageOfHotelListOrderByBestPrice(Integer listPageNumber, Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(listPageNumber, numOfElementsPerPage);
        Page<HotelRepository.HotelFilterResult> queryResults = hotelRepository.findAllOrderByBestPrice(pageable);
        return getHotelListItemSubList(queryResults);
    }

    public HotelListItemSubList getPageOfHotelListFilteredByDateAndPerson
            (LocalDate startDate, LocalDate endDate, long numberOfGuests, Integer listPageNumber, Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(listPageNumber, numOfElementsPerPage);
        Page<HotelRepository.HotelFilterResult> queryResults =
                this.hotelRepository.findAllByDateAndPersonFilterOrderByBestPrice(startDate, endDate, numberOfGuests, pageable);
        return getHotelListItemSubList(queryResults);
    }

    public HotelListItemSubList getPageOfHotelListFilteredByDatePersonAndFeatures
            (LocalDate startDate, LocalDate endDate, long numberOfGuests, List<HotelFeatureType> hotelFeatures, Integer listPageNumber, Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(listPageNumber, numOfElementsPerPage);
        Page<HotelRepository.HotelFilterResult> queryResults =
                this.hotelRepository.findAllByDatePersonAndFeaturesFilterOrderByBestPrice
                        (startDate, endDate, numberOfGuests, hotelFeatures, (long) hotelFeatures.size(), pageable);
        return getHotelListItemSubList(queryResults);
    }

    private HotelListItemSubList getHotelListItemSubList(Page<HotelRepository.HotelFilterResult> queryResults) {
        if (!queryResults.isEmpty()) {
            List<HotelListItem> hotelList = new ArrayList<>();
            for (HotelRepository.HotelFilterResult queryResult : queryResults) {
                hotelList.add(new HotelListItem(queryResult.getFilteredHotel(), queryResult.getBestPrice().intValue()));
            }
            return new HotelListItemSubList(hotelList, queryResults.getNumber(), queryResults.getTotalPages());
        } else return null;
    }

    public List<HotelItemForHomePage> getHotelListTheBestPriceForHomePage(Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(0, numOfElementsPerPage);
        Page<HotelRepository.HotelFilterResult> queryResults = this.hotelRepository.findAllOrderByBestPrice(pageable);
        return queryResults.stream()
                .map(result -> new HotelItemForHomePage(result.getFilteredHotel(), result.getBestPrice().intValue()))
                .collect(Collectors.toList());
    }

    public List<HotelItemForHomePage> getHotelListTheBestAvgRateForHomePage(Integer numOfElementsPerPage) {
        Pageable pageable = PageRequest.of(0, numOfElementsPerPage);
        Page<HotelRepository.HotelFilterResult> queryResults = this.hotelRepository.findAllOrderByBestAvgRateForHomePage(pageable);
        return queryResults.stream()
                .map(result -> new HotelItemForHomePage(result.getFilteredHotel(), result.getBestPrice().intValue()))
                .collect(Collectors.toList());
    }

    public List<HotelItemForHomePage> getRandomHotelListForHomePage(Integer numOfElementsPerPage) {
        List<Long> hotelIdList = this.hotelRepository.findAllHotelId();
        Collections.shuffle(hotelIdList);
        List<HotelItemForHomePage> result = new ArrayList<>();
        for (int i = 0; i < numOfElementsPerPage; i++) {
            HotelRepository.HotelFilterResult queryResult = this.hotelRepository.findByIdWithBestPrice(hotelIdList.get(i));
            result.add(new HotelItemForHomePage(queryResult.getFilteredHotel(), queryResult.getBestPrice().intValue()));
        }
        return result;
    }

    public Long saveHotel(HotelCreateItem hotelCreateItem) {
        Hotel hotel = new Hotel(hotelCreateItem);
        this.hotelRepository.save(hotel);
        return hotel.getId();
    }

    public HotelDetailItem getHotelDetailItem(Long hotelId) {
        HotelDetailItem hotelDetailItem = null;
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        if (hotelOptional.isPresent()) {
            hotelDetailItem = new HotelDetailItem(hotelOptional.get());
            List<RoomListItem> rooms = roomRepository.findAllByHotelId(hotelId)
                    .stream()
                    .map(RoomListItem::new)
                    .collect(Collectors.toList());
            hotelDetailItem.setRooms(rooms);
        }
        return hotelDetailItem;
    }

    public HotelDetailItem getFilteredHotelDetailItem(Long hotelId, LocalDate startDate, LocalDate endDate) {
        HotelDetailItem hotelDetailItem = null;
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        if (hotelOptional.isPresent()) {
            hotelDetailItem = new HotelDetailItem(hotelOptional.get());
            List<RoomListItem> rooms = roomRepository.findAllByHotelId(hotelId)
                    .stream()
                    .filter(room -> isRoomFree(room, startDate, endDate))
                    .map(RoomListItem::new)
                    .collect(Collectors.toList());
            hotelDetailItem.setRooms(rooms);
        }
        return hotelDetailItem;
    }

    public HotelShortItem getHotelShortItem(Long id) {
        HotelShortItem hotelShortItem = null;
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            hotelShortItem = new HotelShortItem(hotelOptional.get());
        }
        return hotelShortItem;
    }

    public HotelCreateItem getHotelCreateItem(Long id) {
        HotelCreateItem hotelCreateItem = null;
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            hotelCreateItem = new HotelCreateItem(hotelOptional.get());
        }
        return hotelCreateItem;
    }

    public boolean updateHotel(HotelCreateItem hotelCreateItem, Long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            List<String> hotelImageUrls = hotelOptional.get().getHotelImageUrls();
            Hotel hotel = new Hotel(hotelCreateItem);
            hotel.setHotelImageUrls(hotelImageUrls);
            hotel.setId(id);
            this.hotelRepository.save(hotel);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteHotel(Long id) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
        if (hotelOptional.isPresent()) {
            Hotel hotel = hotelOptional.get();
            List<Room> deletedRooms = hotel.getRooms();
            for (Room deletedRoom : deletedRooms) {
                this.roomRepository.delete(deletedRoom);
            }
            for (String hotelImageUrl : hotel.getHotelImageUrls()) {
                deleteImageFromCloud(hotelImageUrl);
            }
            hotelRepository.delete(hotel);
            return true;
        } else {
            return false;
        }
    }

    public String saveHotelImage(MultipartFile file, Long id) {
        String imageURL = uploadImage(file);
        if (hotelRepository.findById(id).isPresent() && imageURL != null) {
            hotelRepository.findById(id).get().getHotelImageUrls().add(imageURL);
        }
        return imageURL;
    }

    private String uploadImage(MultipartFile file) {
        String url = null;

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            url = uploadResult.get("url").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public void deleteHotelImage(String imageURL, Long id) {
        if (hotelRepository.findById(id).isPresent()) {
            hotelRepository.findById(id).get().getHotelImageUrls().remove(imageURL);
            deleteImageFromCloud(imageURL);
        }
    }

    public void deleteAllImageFromCloudWhereHotelIdMoreThan(Long hotelId) {
        List<Hotel> hotelList = hotelRepository.findByIdMoreThan(hotelId);
        if (!hotelList.isEmpty()) {
            for (Hotel hotel : hotelList) {
                List<String> hotelImageUrls = hotel.getHotelImageUrls();
                if (!hotelImageUrls.isEmpty()) {
                    for (String imageURL : hotelImageUrls) {
                        deleteImageFromCloud(imageURL);
                    }
                }
            }
        }
     }

    private void deleteImageFromCloud(String imageURL) {
        try {
            //TODO megírni több fájl típusra!!! pl.: .webp, .tiff
            cloudinary.uploader().destroy(imageURL.substring(61, imageURL.length() - 4), ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHotelImages(Long id) {
        List<String> hotelImageUrls = null;
        if (hotelRepository.findById(id).isPresent()) {
            hotelImageUrls = hotelRepository.findById(id).get().getHotelImageUrls();
        }
        return hotelImageUrls;
    }

    private boolean isRoomFree(Room room, LocalDate startDate, LocalDate enDate) {
        List<RoomReservation> roomReservations =
                this.roomReservationRepository.findAllByRoomAndEndDateAfterAndStartDateBefore(room, startDate, enDate);
        return roomReservations.isEmpty();
    }

    public void deleteUnusedImagesFromCloud() {
        List<String> usedImagesUrl = getUsedImagesUrl();
        List<String> cloudinaryImagesUrl = getCloudinaryImagesUrl();
        List<String> unusedImagesUrl = new ArrayList<>(cloudinaryImagesUrl);
        unusedImagesUrl.removeAll(usedImagesUrl);

        List<String> otherUsedImagesUrl = new ArrayList<>();
        otherUsedImagesUrl.add("http://res.cloudinary.com/doaywchwk/image/upload/v1591973849/Akarmi/background_c4qrdl.jpg");
        otherUsedImagesUrl.add("http://res.cloudinary.com/doaywchwk/image/upload/v1589268435/background.jpg");
        otherUsedImagesUrl.add("http://res.cloudinary.com/doaywchwk/image/upload/v1585830612/logo_SIGN_s14ohn.png");
        otherUsedImagesUrl.add("http://res.cloudinary.com/doaywchwk/image/upload/v1585825631/high_five_logo.png");
        otherUsedImagesUrl.add("http://res.cloudinary.com/doaywchwk/image/upload/v1584571162/image_not_found.png");
        unusedImagesUrl.removeAll(otherUsedImagesUrl);

        for (String unusedImageUrl : unusedImagesUrl) {
            deleteImageFromCloud(unusedImageUrl);
        }
    }

    public List<String> getCloudinaryImagesUrl() {
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

    public List<String> getUsedImagesUrl() {
        List<Hotel> hotelList = hotelRepository.findAll();
        List<String> usedImages = new ArrayList<>();
        for (Hotel hotel : hotelList) {
            List<String> hotelImageUrls = hotel.getHotelImageUrls();
            usedImages.addAll(hotelImageUrls);
            for (Room room : hotel.getRooms()) {
                usedImages.add(room.getRoomImageUrl());
            }
        }
        return usedImages;
    }
}
