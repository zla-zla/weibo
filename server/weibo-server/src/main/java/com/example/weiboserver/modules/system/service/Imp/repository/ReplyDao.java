package com.example.weiboserver.modules.system.service.Imp.repository;

import com.example.weiboserver.modules.system.domain.Reply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReplyDao extends JpaRepository<Reply, Integer>, JpaSpecificationExecutor<Reply>,
        PagingAndSortingRepository<Reply, Integer> {
    List<Reply> findByFidIn(Pageable pageable, List<Integer> ids);
}
