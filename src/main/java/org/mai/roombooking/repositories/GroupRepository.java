package org.mai.roombooking.repositories;

import org.mai.roombooking.entities.Booking;
import org.mai.roombooking.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
