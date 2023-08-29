package com.jan1ooo.apicep.repository;

import com.jan1ooo.apicep.model.AddressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressStatusRepository extends JpaRepository<AddressStatus, Long> {
}
