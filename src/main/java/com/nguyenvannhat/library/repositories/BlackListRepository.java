package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
}
