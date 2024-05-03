package org.mai.roombooking.repositories;

import org.mai.roombooking.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
//    @Query("select (" +
//            "select count(distinct(e.id)) from bookings as b" +
//            " join bookings_equipment as be on b.id = be.booking_id" +
//            " join equipment as e on be.equipment_id = e.id" +
//            ") < count(eq.id) from equipment as eq;")
//    Boolean isFreeEquipment();
}
