package com.manager.constant;

public final class MessageConstants {

    private MessageConstants() {
    }

    public static final String ADMIN_NAME = "管理员";

    public static final String TYPE_ALL = "0";

    public static final String TYPE_STUDENT = "1";

    public static final String TYPE_TEACHER = "2";

    public static final String TYPE_SPECIFIC = "3"; // 指定部分用户，结合用户列表使用

    public static final String ALL_TAG = "全体成员";

    public static final String STUDENT_TAG = "全体学生";

    public static final String TEACHER_TAG = "全体教师";

    public static final int STATE_UNREAD = 0;

    public static final int STATE_READ = 1;

    public static final int STATE_ANNOUNCE = 2;
}
