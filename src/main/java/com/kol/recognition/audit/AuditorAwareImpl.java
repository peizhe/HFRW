package com.kol.recognition.audit;

import com.kol.recognition.beans.entities.User;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<User> {
    @Override
    public User getCurrentAuditor() {
        final User user = new User();
        user.setUserName("kolexandr");
        return user;
    }
}
