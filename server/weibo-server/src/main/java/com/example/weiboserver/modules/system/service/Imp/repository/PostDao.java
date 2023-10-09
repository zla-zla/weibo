package com.example.weiboserver.modules.system.service.Imp.repository;

import com.example.weiboserver.modules.system.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface PostDao extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post>,
        PagingAndSortingRepository<Post, Integer> {
    @Query(value = "SELECT * FROM post", nativeQuery = true)
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByUid(Pageable pageable, Integer uid);
    Page<Post> findByTextLikeOrNameLike(Pageable pageable, String info1, String info2);
//    void deleteById(Integer id);

}
