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
package edu.monash.merc.dao.impl;

import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.dao.HibernateGenericDAO;
import edu.monash.merc.domain.User;
import edu.monash.merc.domain.UserType;
import edu.monash.merc.repository.IUserRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserDAO class which provides data access functionality for User domain object
 *
 * @author Simon Yu - Xiaoming.Yu@monash.edu
 * @version 2.0
 */
@Scope("prototype")
@Repository
public class UserDAO extends HibernateGenericDAO<User> implements IUserRepository {

    @Override
    public User getUserByUnigueId(String uniqueId) {
        return (User) this.session().createCriteria(this.persistClass).add(Restrictions.eq("uniqueId", uniqueId))
                .setComment("UserDAO.getByUserUnigueId").uniqueResult();
    }

    @Override
    public User getUserByEmail(String email) {
        return (User) this.session().createCriteria(this.persistClass).add(Restrictions.eq("email", email)).setComment("UserDAO.getByUserEmail")
                .uniqueResult();
    }

    @Override
    public User getUserByNameEmail(String firstName, String lastName, String email) {
        return (User) this.session().createCriteria(this.persistClass).add(Restrictions.eq("email", email))
                .add(Restrictions.eq("firstName", firstName)).add(Restrictions.eq("lastName", lastName)).setComment("UserDAO.getUserByNameEmail")
                .uniqueResult();
    }

    @Override
    public boolean checkUserUniqueIdExisted(String uniqueId) {
        long num = (Long) this.session().createCriteria(this.persistClass).setProjection(Projections.rowCount())
                .add(Restrictions.eq("uniqueId", uniqueId).ignoreCase()).uniqueResult();
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkUserDisplayNameExisted(String userDisplayName) {
        long num = (Long) this.session().createCriteria(this.persistClass).setProjection(Projections.rowCount())
                .add(Restrictions.eq("displayName", userDisplayName).ignoreCase()).uniqueResult();
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkEmailExisted(String email) {
        long num = (Long) this.session().createCriteria(this.persistClass).setProjection(Projections.rowCount())
                .add(Restrictions.eq("email", email).ignoreCase()).uniqueResult();
        if (num == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the virtual user by the user type, only use for retrieve the superadmin, all registed user and anonymous
     * virtual user.
     *
     * @param userType
     * @return User
     */
    @Override
    public User getVirtualUser(int userType) {
        if (userType == UserType.REGUSER.code()) {
            return null;
        }
        Criteria userCriteria = this.session().createCriteria(this.persistClass);
        userCriteria.setComment("UserDAO.getVirtualUser");
        userCriteria.add(Restrictions.eq("userType", userType));
        userCriteria.addOrder(Order.asc("id")).setMaxResults(1);
        return (User) userCriteria.uniqueResult();
    }

    @Override
    public User checkUserLogin(String uniqueId, String password) {
        return (User) this.session().createCriteria(this.persistClass).add(Restrictions.eq("uniqueId", uniqueId))
                .add(Restrictions.eq("password", password)).setComment("UserDAO.checkUserExisted").uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pagination<User> getAllUsers(int startPageNo, int recordsPerPage, OrderBy[] orderBys) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.add(Restrictions.and(Restrictions.ne("userType", UserType.ALLREGUSER.code()), Restrictions.ne("userType", UserType.ANONYMOUS.code())));
        criteria.setProjection(Projections.rowCount());
        int total = ((Long) criteria.uniqueResult()).intValue();

        Pagination<User> p = new Pagination<User>(startPageNo, recordsPerPage, total);
        Criteria qcriteria = this.session().createCriteria(this.persistClass);
        qcriteria.add(Restrictions.and(Restrictions.ne("userType", UserType.ALLREGUSER.code()),
                Restrictions.ne("userType", UserType.ANONYMOUS.code())));
        if (orderBys != null && orderBys.length > 0) {
            for (int i = 0; i < orderBys.length; i++) {
                Order order = orderBys[i].getOrder();
                if (order != null) {
                    qcriteria.addOrder(order);
                }
            }
        } else {
            qcriteria.addOrder(Order.desc("userType"));
        }
        qcriteria.setFirstResult(p.getFirstResult());
        // set the max results (size-per-page)
        qcriteria.setMaxResults(p.getSizePerPage());
        List<User> collist = qcriteria.list();
        p.setPageResults(collist);
        return p;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllActiveUsers() {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.setComment("UserDAO.getAllActiveUsers");
        criteria.add(Restrictions.eq("isActivated", true)).add(
                Restrictions.and(Restrictions.ne("userType", UserType.ALLREGUSER.code()), Restrictions.ne("userType", UserType.ANONYMOUS.code())));
        return criteria.addOrder(Order.desc("displayName")).list();
    }

}
