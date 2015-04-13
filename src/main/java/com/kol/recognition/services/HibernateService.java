package com.kol.recognition.services;

public class HibernateService {

    /*public <T extends HistoryObject> void save(final Collection<T> beans) {
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
    }*/
}
