package com.ruichen.staybooking.service;

import com.ruichen.staybooking.exception.StayDeleteException;
import com.ruichen.staybooking.exception.StayNotExistException;
import com.ruichen.staybooking.model.*;
import com.ruichen.staybooking.repository.LocationRepository;
import com.ruichen.staybooking.repository.ReservationRepository;
import com.ruichen.staybooking.repository.StayRepository;
import com.ruichen.staybooking.repository.StayReservationDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StayService {
    private StayRepository stayRepository;
    private ImageStorageService imageStorageService;
    private LocationRepository locationRepository;
    private GeoCodingService geoCodingService;
    private ReservationRepository reservationRepository;

    private StayReservationDateRepository stayReservationDateRepository;


    @Autowired
    public StayService(StayRepository stayRepository, LocationRepository locationRepository, ReservationRepository reservationRepository, ImageStorageService imageStorageService, GeoCodingService geoCodingService, StayReservationDateRepository stayReservationDateRepository) {
        this.stayRepository = stayRepository;
        this.locationRepository = locationRepository;
        this.reservationRepository = reservationRepository;
        this.imageStorageService = imageStorageService;
        this.geoCodingService = geoCodingService;
        this.stayReservationDateRepository = stayReservationDateRepository;
    }


    public List<Stay> listByUser(String userId) {
        return stayRepository.findByHost(new User.Builder().setUsername(userId).build());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Stay stay, MultipartFile[] images) {
//        //upload file, return urls
//        List<String> imageUrls = new ArrayList<>();
//        for(MultipartFile image : images) {
//            String url = imageStorageService.save(image);
//            imageUrls.add(url);
//        }

        List<String> imageUrls = Arrays.stream(images).parallel().map(image -> imageStorageService.save(image)).collect(Collectors.toList());

        //create stay image objects, save to db
        List<StayImage> stayImages = new ArrayList<>();
        for(String url : imageUrls) {
            stayImages.add(new StayImage(url, stay));
        }
        stay.setImages(stayImages);
        stayRepository.save(stay);

        Location location = geoCodingService.getLatLong(stay.getId(), stay.getAddress());
        locationRepository.save(location);
    }

    public Stay findByIdAndHost(Long stayId, String username) {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if (stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        return stay;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long stayId, String username) {
        Stay stay = stayRepository.findByIdAndHost(stayId, new User.Builder().setUsername(username).build());
        if(stay == null) {
            throw new StayNotExistException("Stay doesn't exist");
        }
        List<Reservation> reservations = reservationRepository.findByStayAndCheckoutDateAfter(stay, LocalDate.now());
        if (reservations != null && reservations.size() > 0) {
            throw new StayDeleteException("Cannot delete stay with active reservation");
        }
        stayRepository.deleteById(stayId);
    }

}
