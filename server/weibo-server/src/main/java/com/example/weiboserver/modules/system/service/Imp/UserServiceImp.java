package com.example.weiboserver.modules.system.service.Imp;

import com.example.weiboserver.modules.system.domain.Vo.Login;
import com.example.weiboserver.modules.system.domain.User;
import com.example.weiboserver.modules.system.service.Imp.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class UserServiceImp {

    @Autowired
    UserDao userDao;
    //添加用户
    public void addUser(User user){ userDao.save(user);}
    //查询用户
    public User findById(Integer Id){
        return userDao.findById(Id).get();
    }

    public void updatePwd(String pwd,Integer id){
        userDao.updatePwd(pwd,id);
    }

    public void updateInfo(String name,String introduction,String sex,String phone,Integer id){
        userDao.updateInfo(name, introduction, sex, phone, id);
    }

    //根据账号删除用户
    public void deleteByAccount(String account){
        userDao.deleteByAccount(account);
    }

    //根据用户账号返回用户
    public User findByAccount(String account){
        if (userDao.findStuByAccount(account).size()==0){
            return null;
        }
        return userDao.findStuByAccount(account).get(0);
    }

    //更新用户信息（先根据账号搜索用户再保存）
    public void updateUser(User user){
        user.setId(findByAccount(user.getAccount()).getId());
        userDao.save(user);
    }

    public boolean userLogin(Login login){
        List<User> ans = userDao.findByAccount(login.getAccount());
        if(ans != null && ans.size() > 0){
            return ans.get(0).getPassword().equals(login.getPwd());
        }
        else return false;
    }





}
