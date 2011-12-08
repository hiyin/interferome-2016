package edu.monash.merc.utest.interferome;

import java.util.Calendar;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import edu.monash.merc.domain.Profile;
import edu.monash.merc.domain.User;

import edu.monash.merc.service.UserService;

/**
 * @author Simon Yu (Xiaoming.Yu@monash.edu)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UserService userService;

	@BeforeTransaction
	public void beforeTransaction() {

		System.out.println("start to setup test data ...");
		User u = new User();
		u.setUniqueId("12adsfasdfd111");
		u.setEmail("SimonYu@its.monash.edu.au");
		u.setRegistedDate(Calendar.getInstance().getTime());

		Profile pro = new Profile();

		pro.setGender("male");

		pro.setUser(u);
		u.setProfile(pro);
		this.userService.saveUser(u);
		long persistUId = u.getId();

		System.out.println("persistUId : " + persistUId);
	}

	@Test
	public void addUserTest() {
		User user = new User();
		user.setUniqueId("simon");
		user.setEmail("simonyu2000@hotmail.com");
		user.setRegistedDate(Calendar.getInstance().getTime());
		Profile pro = new Profile();

		pro.setGender("male");
		user.setProfile(pro);

		pro.setUser(user);
		userService.saveUser(user);

		System.out.println("User Id: " + user.getId());

		Assert.assertNotNull(user.getId());
	}

}
