package com.manager.util;

import com.manager.constant.SessionFields;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionChecker {

    private String id;

    private final boolean success;

    public void setId(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public boolean isAuthed() {
        return success;
    }

    public SessionChecker(String id, boolean success) {
        this.id = id;
        this.success = success;
    }

    /**
     * 检查 session 是否是有效的认证过的 session
     */
    public static SessionChecker checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String id = (String) session.getAttribute(SessionFields.USERNAME);
        boolean succeed = true;
        if (id == null) {
            succeed = false;
        }
        return new SessionChecker(id, succeed);
    }
}
