package com.progmasters.hotel.controller;

import com.progmasters.hotel.dto.DataBaseFillerCommand;
import com.progmasters.hotel.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/QoPJDSnd/data")
public class DataController {

    private DataService dataService;


    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/restoredatabase")
    public ResponseEntity<Void> restoreDatabase() {
        return dataService.restoreData() ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/filldatabase")
    public ResponseEntity<Void> fillDatabase() {
        dataService.fillDatabaseFromJson();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/filldatabase")
    public ResponseEntity fillDB_BySendingJsonFile(@RequestBody DataBaseFillerCommand hotelList) {
        dataService.fillDatabaseBySendingJsonFile(hotelList);
        return new ResponseEntity(HttpStatus.CREATED);
    }



}
