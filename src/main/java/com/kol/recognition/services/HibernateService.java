package com.kol.recognition.services;

import com.kol.recognition.beans.entities.HistoryObject;
import com.kol.recognition.beans.entities.SupportObject;
import com.kol.recognition.beans.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class HibernateService {

    @Autowired private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public <T extends HistoryObject> void save(final T bean) {
        getCurrentSession().saveOrUpdate(bean);
    }

    public <T extends HistoryObject> void save(final Collection<T> beans) {
        beans.forEach(this::save);
    }

    public <T extends HistoryObject> T get(final Class<T> clazz, final Integer id) {
        return (T) getCurrentSession().get(clazz, id);
    }

    public <T extends SupportObject> T get(final Class<T> clazz, final String code) {
        return (T) getCurrentSession().get(clazz, code);
    }

    public <T extends HistoryObject> List<T> get(final Class<T> clazz, final Collection<Integer> ids) {
        return (List<T>) getCurrentSession().createCriteria(clazz).add(Restrictions.in("id", ids)).list();
    }

    public <T> T getByCode(final Class<T> clazz, final String code) {
        return (T) getCurrentSession().createCriteria(clazz).add(Restrictions.eq("code", code)).uniqueResult();
    }

    public User getUser(final String username) {
        return (User) getCurrentSession().createCriteria(User.class).add(Restrictions.eq("userName", username)).uniqueResult();
    }
}
