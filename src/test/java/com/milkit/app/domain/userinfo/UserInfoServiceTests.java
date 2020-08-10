package com.milkit.app.domain.userinfo;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.milkit.app.common.exception.handler.RestResponseEntityExceptionHandler;
import com.milkit.app.domain.userinfo.UserInfo;
import com.milkit.app.domain.userinfo.service.UserInfoServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@Slf4j
class UserInfoServiceTests {

	@Autowired
    private UserInfoServiceImpl userInfoServie;


	@Test
	public void ID별조회_테스트() throws Exception {

		등록_테스트();

		UserInfo userInfo = userInfoServie.select(1l);

		log.debug(userInfo.toString());
	}
	
	@Test
	public void UserID별조회_테스트() throws Exception {

		등록_테스트();

		UserInfo userInfo = userInfoServie.select("test");

		log.debug(userInfo.toString());
	}




	@Test
	public void 전체조회_테스트() throws Exception {

		List<UserInfo> list = userInfoServie.selectAll();

		log.debug("size:"+list.size());
	}

	@Test
	public void 등록_테스트() throws Exception {

//		String password = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";	//sha256
		String password = "$2a$10$U.0zo/wtkAiqBfxoaeiHmuzUT7pjf5hJmmn/Hg5iFyuEMNUvA.FpW";		//bcrypt
		
		UserInfo userInfo = new UserInfo("test", "");
		Long id = userInfoServie.insert(userInfo);

		log.debug(id.toString());
	}

}
