package com.progmasters.hotel.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progmasters.hotel.domain.Hotel;
import com.progmasters.hotel.domain.HotelFeatureType;
import com.progmasters.hotel.domain.HotelType;
import com.progmasters.hotel.domain.Room;
import com.progmasters.hotel.dto.DataBaseFillerCommand;
import com.progmasters.hotel.dto.HotelDataCreatorItem;
import com.progmasters.hotel.dto.RoomCreateItem;
import com.progmasters.hotel.repository.HotelRepository;
import com.progmasters.hotel.repository.RoomRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


@Service
@Transactional
public class DataService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final CloudImageService cloudImageService;
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    //For demo version
    private final static String DUMP_SQL_FILE = "hotel-2020_10_08-dump.sql";
    private final static Long MAX_HOTEL_ID = 50L;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    public DataService(HotelRepository hotelRepository, RoomRepository roomRepository, CloudImageService cloudImageService) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.cloudImageService = cloudImageService;
    }

    public void fillDatabaseFromJson() {
        File file = loadFileFromResources("hotelsAvg.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Hotel> hotelList = objectMapper.readValue(file, new TypeReference<>() {
            });
            for (Hotel hotel : hotelList) {
                if (hotelRepository.findByHotelName(hotel.getName()).isEmpty()) {
                    hotelRepository.save(hotel);
                    for (Room room : hotel.getRooms()) {
                        room.setHotel(hotel);
                        roomRepository.save(room);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillDatabaseBySendingJsonFile(@RequestBody DataBaseFillerCommand hotelList) {
        for (HotelDataCreatorItem hotelData : hotelList.getDataListOfHotel()) {
            if (hotelRepository.findByHotelName(hotelData.getName()).isEmpty()) {
                List<Room> roomList = new ArrayList<>();
                Hotel tempHotel = new Hotel(hotelData.getName(), hotelData.getPostalCode(),
                        hotelData.getCity(), hotelData.getStreetAddress(),
                        HotelType.valueOf(hotelData.getHotelType()), roomList,
                        hotelData.getHotelImageUrls(), hotelData.getDescription(),
                        hotelData.getHotelFeatures().stream().map(HotelFeatureType::valueOf).collect(Collectors.toList()),
                        hotelData.getLongitude(), hotelData.getLatitude());

                hotelRepository.save(tempHotel);

                for (RoomCreateItem room : hotelData.getRoomList()) {
                    Room room1 = new Room(room);
                    room1.setHotel(tempHotel);
                    roomRepository.save(room1);
                }
            }
        }
    }

    public static File loadFileFromResources(String pathInResources) {
        Resource resource = new ClassPathResource(pathInResources);
        File tempFile = null;
        try (InputStream inputStream = resource.getInputStream()) {
            tempFile = File.createTempFile("test", ".txt");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    public boolean restoreDatabaseFromBackupWithCommandLine() {
        try {
            File resourceFile = loadFileFromResources(DUMP_SQL_FILE);
            String source = resourceFile.getPath();
            String executableSQL = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql";
            String dbName = "hotel";
            String[] executeCmd = new String[]{executableSQL, "--user=" + user, "--password=" + password, dbName, "-e", " source " + source};

            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            return processComplete == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    //For demo version
    @Scheduled(cron = "0 0 0 * * *")
    public boolean restoreData() {
        int countOfDeletedNewHotelImages = deleteAllImageFromCloudWhereHotelIdMoreThan(MAX_HOTEL_ID);
        logger.info( "{} pcs image deleted from newly added hotels", countOfDeletedNewHotelImages);
        int countOfDeletedImagesOfHotel1 = deleteNonOriginalImagesUrlFromCloudAtHotel1();
        logger.info("{} pcs newly added image deleted from hotel 1", countOfDeletedImagesOfHotel1);
        boolean restoreIsSuccessful =  restoreDatabaseFromBackupWithJDBC();
        if (restoreIsSuccessful) {
            logger.info("DataBase is restored from " + DUMP_SQL_FILE);
        } else {
            logger.info("There was a problem restoring the database.");
        }
        return restoreIsSuccessful;
    }

    public boolean restoreDatabaseFromBackupWithJDBC() {
        File resourceFile = loadFileFromResources(DUMP_SQL_FILE);
        String[] commands = readSQLCommandsFromFile(resourceFile);
        boolean result = false;
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (Statement statement = connection.createStatement()) {
                for (String command : commands) {
                    statement.execute(command);
                }
                result = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private String[] readSQLCommandsFromFile(File resourceFile) {
        StringBuilder queryString = new StringBuilder();
        try (Scanner scanner = new Scanner(resourceFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                queryString.append(line).append("\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return queryString.toString().split(";\n");
    }

    public int deleteAllImageFromCloudWhereHotelIdMoreThan(Long hotelId) {
        List<String> imageUrls = getImageUrlWhereHotelIdMoreThan(hotelId);
        for (String imageUrl : imageUrls) {
            cloudImageService.deleteImageFromCloud(imageUrl);
        }
        return imageUrls.size();
    }

    public List<String> getImageUrlWhereHotelIdMoreThan(Long hotelId) {
        List<String> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (Statement statement = connection.createStatement()) {
                String sqlSelectQuery = "SELECT hotel_image_urls FROM hotel_hotel_image_urls WHERE hotel_id > ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectQuery);
                preparedStatement.setLong(1, hotelId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result.add(resultSet.getString("hotel_image_urls"));
                }

                sqlSelectQuery = "SELECT room_image_url FROM room WHERE hotel_id > ?";
                preparedStatement = connection.prepareStatement(sqlSelectQuery);
                preparedStatement.setLong(1, hotelId);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result.add(resultSet.getString("room_image_url"));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }


//    //For demo version
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public int deleteAllImageFromCloudWhereHotelIdMoreThan(Long hotelId) {
//        int counter = 0;
//        List<Hotel> hotelList = hotelRepository.findByIdMoreThan(hotelId);
//        if (!hotelList.isEmpty()) {
//            for (Hotel hotel : hotelList) {
//                List<String> hotelImageUrls = hotel.getHotelImageUrls();
//                if (!hotelImageUrls.isEmpty()) {
//                    for (String imageURL : hotelImageUrls) {
//                        deleteImageFromCloud(imageURL);
//                        counter++;
//                    }
//                }
//            }
//        }
//        return counter;
//    }

    //For demo version
    public int deleteNonOriginalImagesUrlFromCloudAtHotel1() {
        List<String> imagesUrlFromHotel1 = getImagesUrlFromHotel1();
        List<String> nonOriginalImagesUrlFromHotel1 = new ArrayList<>(imagesUrlFromHotel1);
        nonOriginalImagesUrlFromHotel1.removeAll(CloudImageService.NON_DELETABLE_IMAGES_FOR_HOTEL_1);
        for (String imageURL : nonOriginalImagesUrlFromHotel1) {
            cloudImageService.deleteImageFromCloud(imageURL);
        }
        return nonOriginalImagesUrlFromHotel1.size();
    }

//    //For demo version
//    public List<String> getNonOriginalImagesUrlFromHotel1() {
//        List<String> imagesUrlInHotel1 = hotelService.getHotelImages(1L);
//        List<Room> roomsInHotel1 = roomRepository.findAllByHotelId(1L);
//        if (roomsInHotel1 != null) {
//            for (Room room : roomsInHotel1) {
//                String roomImageUrl = room.getRoomImageUrl();
//                if (roomImageUrl != null) {
//                    imagesUrlInHotel1.add(roomImageUrl);
//                }
//            }
//        }
//        imagesUrlInHotel1.removeAll(NON_DELETABLE_IMAGES_FOR_HOTEL_1);
//        return imagesUrlInHotel1;
//    }

    public List<String> getImagesUrlFromHotel1() {
        List<String> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (Statement statement = connection.createStatement()) {
                String sqlSelectQuery = "SELECT hotel_image_urls FROM hotel_hotel_image_urls WHERE hotel_id = 1";
                ResultSet resultSet = statement.executeQuery(sqlSelectQuery);
                while (resultSet.next()) {
                    result.add(resultSet.getString("hotel_image_urls"));
                }

                sqlSelectQuery = "SELECT room_image_url FROM room WHERE hotel_id = 1";
                resultSet = statement.executeQuery(sqlSelectQuery);
                while (resultSet.next()) {
                    result.add(resultSet.getString("room_image_url"));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public String getDumpSqlFile() {
        return DUMP_SQL_FILE;
    }


}
