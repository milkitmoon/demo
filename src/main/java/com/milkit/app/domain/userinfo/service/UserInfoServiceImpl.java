package com.milkit.app.domain.userinfo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.milkit.app.domain.userinfo.UserInfo;
import com.milkit.app.domain.userinfo.dao.UserInfoDao;

@Component
public class UserInfoServiceImpl implements UserDetailsService {

	@Autowired
	private UserInfoDao userInfoDao;

	public List<UserInfo> selectAll() throws Exception {
		return userInfoDao.findAll();
	}

	public UserInfo select(final Long id) throws Exception {
	    UserInfo userInfo = null;
	
	    Optional<UserInfo> optionalPost = userInfoDao.findById(id);
	    if(optionalPost.isPresent()) {
	
	    	userInfo = optionalPost.get();
	    }
	
	    return userInfo;
	}
	
	public UserInfo select(final String userID) throws UsernameNotFoundException {
	    UserInfo userInfo = null;
	
	    Optional<UserInfo> optionalPost = userInfoDao.findByUserID(userID);
	    if(optionalPost.isPresent()) {
	    	userInfo = optionalPost.get();
	    } else {
	    	throw new UsernameNotFoundException("사용자정보 없음");
	    }
	
	    return userInfo;
	}

    public Long insert(final UserInfo member) throws Exception {
    	return userInfoDao.save(member).getId();
    }

	@Override
	public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
		UserInfo userInfo = select(userID);

        return userInfo;
	}
    
}