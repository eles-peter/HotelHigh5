package com.progmasters.hotel.service.integration;

import com.progmasters.hotel.dto.HotelCreateItem;
import com.progmasters.hotel.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureTestDatabase
class HotelServiceIT_Test {

    @Autowired
    private HotelService hotelService;

//    @Test
//    void findAllHotel() {
//    }
//
//    @Test
//    void getHotelListItemList() {
//    }
//
//    @Test
//    void getHotelFeatureTypeOptionList() {
//    }
//
//    @Test
//    void getHotelTypeOptionList() {
//    }

    @Test
    void saveHotel() {
        HotelCreateItem hotelCreateItem = new HotelCreateItem();
        hotelCreateItem.setName("Hilton");
        hotelCreateItem.setHotelType("SZALLODA");
        hotelCreateItem.setPostalCode("1022");
        hotelCreateItem.setCity("Budapest");
        hotelCreateItem.setStreetAddress("Fő tér 1.");
        hotelCreateItem.setDescription("Ez egy fasza hotel");
        hotelCreateItem.setHotelFeatures(List.of("ETTEREM", "INGYENESWIFI"));
        this.hotelService.saveHotel(hotelCreateItem);

        assertEquals(1, this.hotelService.getHotelListItemList().size());
    }

//    @Test
//    void getHotelDetailItem() {
//    }
//
//    @Test
//    void getHotelShortItem() {
//    }
//
//    @Test
//    void getHotelCreateItem() {
//    }
}
