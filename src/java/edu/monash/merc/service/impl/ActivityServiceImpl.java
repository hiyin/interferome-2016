/*
 * Copyright (c) 2010-2011, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.monash.merc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.monash.merc.dao.impl.ActivityDAO;
import edu.monash.merc.domain.Activity;
import edu.monash.merc.service.ActivityService;

/**
 * ActivityService Service Implementation class
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityDAO activityDao;

	/**
	 * @param activityDao
	 *            the activityDao to set
	 */
	public void setActivityDao(ActivityDAO activityDao) {
		this.activityDao = activityDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#saveActivity(edu.monash.merc.domain.Activity)
	 */
	@Override
	public void saveActivity(Activity activity) {
		this.activityDao.add(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#getActivityById(long)
	 */
	@Override
	public Activity getActivityById(long id) {
		return this.activityDao.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#deleteActivity(edu.monash.merc.domain.Activity)
	 */
	@Override
	public void deleteActivity(Activity activity) {
		this.activityDao.remove(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#updateActivity(edu.monash.merc.domain.Activity)
	 */
	@Override
	public void updateActivity(Activity activity) {
		this.activityDao.update(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#getActivityByActKey(java.lang.String)
	 */
	@Override
	public Activity getActivityByActKey(String activityKey) {
		return this.activityDao.getActivityByActKey(activityKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#getAllActivities()
	 */
	@Override
	public List<Activity> getAllActivities() {
		return this.activityDao.getAllActivities();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#getActivitiesByExpId(long)
	 */
	@Override
	public List<Activity> getActivitiesByExpId(long expId) {
		return this.activityDao.getActivitiesByExpId(expId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#deleteActivityById(long)
	 */
	@Override
	public void deleteActivityById(long id) {
		this.activityDao.deleteActivityById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.monash.merc.service.ActivityService#deleteActivityByActKey(java.lang.String)
	 */
	@Override
	public void deleteActivityByActKey(String activityKey) {
		this.activityDao.deleteActivityByActKey(activityKey);
	}
}
