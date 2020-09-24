package com.progmasters.hotel.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/fill-database")
public class DataController {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    private final static String DUMP_SQL_FILE = "hotel-2020_06_03-dump.sql";

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Autowired
    public DataController(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/databaserestore")
    public ResponseEntity<Void> restoreDatabase() {
        return restoreDatabaseFromBackupWithJDBC() ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping
    public ResponseEntity<Void> fillDatabase() {
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

        return new ResponseEntity<>(HttpStatus.CREATED);
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

    @PostMapping
    public ResponseEntity fillDB_BySendingJsonFile(@RequestBody DataBaseFillerCommand hotelList) {

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
        return new ResponseEntity(HttpStatus.CREATED);
    }


    public boolean restoreDatabaseFromBackupWithCommandLine() {
        System.out.println("futok");
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

    @Scheduled(cron = "0 0 0 * * *")
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

}
