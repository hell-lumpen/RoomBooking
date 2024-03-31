package org.mai.roombooking.repositories;


import org.mai.roombooking.entities.RecurringException;
import org.mai.roombooking.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface RecurringExceptionRepository extends JpaRepository<RecurringException, Long>
{


}
