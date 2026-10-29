package com.reedobook.repository;

import com.reedobook.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing Subscription entities.
 */
@Repository
public interface Subscription_Repository extends JpaRepository<Subscription, Long> {

    public static final String FIND_ACIVE_SUBSCRIPTION = "SELECT * FROM Subscription where user_id= :userId and end_date >= CURDATE()";
    /**
     * Find all subscriptions by the given user ID.
     *
     * @param userId the user ID
     * @return list of subscriptions for the user
     */
    List<Subscription> findByUserId(Long userId);

    @Query(value = FIND_ACIVE_SUBSCRIPTION, nativeQuery = true)
    List<Subscription> findActiveSubscriptionByUserId(@Param("userId") Long userId);


}
