package com.showroom.Repository;

import com.showroom.Entity.ShowRoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowroomRepository extends JpaRepository<ShowRoomDetails,Integer> {
}
