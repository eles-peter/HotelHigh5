package com.progmasters.hotel.service;

import com.progmasters.hotel.domain.Room;
import com.progmasters.hotel.domain.RoomReservation;
import com.progmasters.hotel.dto.RoomReservationData;
import com.progmasters.hotel.repository.RoomReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomReservationService {

    private RoomReservationRepository roomReservationRepository;

    public RoomReservationService(RoomReservationRepository roomReservationRepository) {
        this.roomReservationRepository = roomReservationRepository;
    }

    public boolean isRoomFree(Long roomId, LocalDate startDate, LocalDate endDate) {
        List<RoomReservation> roomReservations =
                this.roomReservationRepository.findAllByRoomIdAndEndDateAfterAndStartDateBefore(roomId, startDate, endDate);
        return roomReservations.isEmpty();
    }

    public List<Room> findAllOccupiedRoomIdByDateRange(LocalDate startDate, LocalDate endDate) {
        return this.roomReservationRepository.findAllOccupiedRoomIdByDateRange(startDate, endDate);
    }


    public boolean updateRoomReservation(RoomReservationData modifiedRoomReservationData, Long roomReservationId) {
        if (isRoomReservationModifiable(modifiedRoomReservationData)) {
            Optional<RoomReservation> optionalModifiedRoomReservation = this.roomReservationRepository.findById(roomReservationId);
            if (optionalModifiedRoomReservation.isPresent()) {
                RoomReservation modifiedRoomReservation = optionalModifiedRoomReservation.get();
                modifiedRoomReservation.setStartDate(modifiedRoomReservationData.getStartDate());
                modifiedRoomReservation.setEndDate(modifiedRoomReservationData.getEndDate());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean updateAllRoomReservationInBooking(List<RoomReservationData> modifiedRoomReservations) {
        for (RoomReservationData modifiedRoomReservation : modifiedRoomReservations) {
            if (!isRoomReservationModifiable(modifiedRoomReservation)) {
                return false;
            }
        }
        for (RoomReservationData modifiedRoomReservation : modifiedRoomReservations) {
            Optional<RoomReservation> optionalRoomReservation =
                    this.roomReservationRepository.findById(modifiedRoomReservation.getRoomReservationId());
            if (optionalRoomReservation.isPresent()) {
                RoomReservation roomReservation = optionalRoomReservation.get();
                roomReservation.setStartDate(modifiedRoomReservation.getStartDate());
                roomReservation.setEndDate(modifiedRoomReservation.getEndDate());
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isRoomReservationModifiable(RoomReservationData modifiedRoomReservationData) {
        LocalDate startDate = modifiedRoomReservationData.getStartDate();
        LocalDate endDate = modifiedRoomReservationData.getEndDate();
        Optional<RoomReservation> optionalRoomReservation = this.roomReservationRepository.findById(modifiedRoomReservationData.getRoomReservationId());
        if (optionalRoomReservation.isPresent()) {
            RoomReservation roomReservation = optionalRoomReservation.get();
            Long roomId = roomReservation.getRoom().getId();
            List<RoomReservation> roomReservations =
                    this.roomReservationRepository.findAllByRoomIdAndEndDateAfterAndStartDateBefore(roomId, startDate, endDate);
            for (RoomReservation otherReservation : roomReservations) {
                if (!otherReservation.getId().equals(modifiedRoomReservationData.getRoomReservationId())) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }


    public boolean deleteRoomReservation(Long roomReservationId) {
        Optional<RoomReservation> optionalRoomReservation = this.roomReservationRepository.findById(roomReservationId);
        if (optionalRoomReservation.isPresent()) {
            RoomReservation roomReservation = optionalRoomReservation.get();
            roomReservationRepository.delete(roomReservation);
            return true;
        } else {
            return false;
        }
    }
}
