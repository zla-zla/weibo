package com.example.weiboserver.modules.system.service.Imp.repository;

import com.example.weiboserver.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Integer> {
    // 根据账号删除用户
    void deleteByAccount(String account);
    //根据账号查询用户
    List<User> findByAccount(String account);

    Optional<User> findById(Integer id);

    @Modifying
    @Query("update User set password = :password where id = :id")
    void updatePwd(@Param("password")String password,@Param("id")Integer id);

    @Modifying
    @Query("update User set name = :name,introduction = :introduction,sex = :sex," +
            "phone = :phone where id = :id")
    void updateInfo(@Param("name")String name,@Param("introduction")String introduction,@Param("sex")String sex,
                    @Param("phone")String phone,@Param("id")Integer id);

    // 根据账号返回用户列表（只有一个用户）
    @Query(name = "findStuByAccount",nativeQuery = true,value = "select * from User where account=:account ")
    List<User> findStuByAccount(@Param("account") String account);
}
