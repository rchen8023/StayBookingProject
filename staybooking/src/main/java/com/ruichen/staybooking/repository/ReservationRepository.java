package com.ruichen.staybooking.repository;

import com.ruichen.staybooking.model.Reservation;
import com.ruichen.staybooking.model.Stay;
import com.ruichen.staybooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuest(User guest);

    List<Reservation> findByStay(Stay stay);

    Reservation findByIdAndGuest(Long id, User guest); // for deletion
    List<Reservation> findByStayAndCheckoutDateAfter(Stay stay, LocalDate date);


}
