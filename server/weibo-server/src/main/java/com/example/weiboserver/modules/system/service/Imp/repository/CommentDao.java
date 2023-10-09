package com.example.weiboserver.modules.system.service.Imp.repository;

import com.example.weiboserver.modules.system.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentDao extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment>,
        PagingAndSortingRepository<Comment, Integer> {
    Page<Comment> findByPid(Pageable pageable, Integer pid);
}
