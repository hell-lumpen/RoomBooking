package org.mai.roombooking.repositories;

import org.mai.roombooking.entities.Room;
import org.mai.roombooking.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
