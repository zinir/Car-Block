package com.projects.nir.myapplication.database;

public class DataBaseConstants {

    private DataBaseConstants() {
    }

    public final static String DB_NAME = "myCars";
    public final static int DB_VERSION = 2;
    public final static int PROFILE_TBL_POSITION = 0;
    public final static int USERS_TBL_POSITION = 1;
    public final static int BLOCKING_TBL_POSITION = 2;
    public final static int Err = -1;

    public class UsersTable {
        public final static String TABLE_NAME = "usersTable";
        public final static String USER_ID_COL = "_userId";
        public final static String USER_NAME_COL = "userName";
        public final static String FIRST_NAME_COL = "firstName";
        public final static String USER_PASSWORD_COL = "userPassword";
        public final static String USER_IMAGE_URI_COL = "userUri";
        public final static int USER_ID_COL_POS = 0;
        public final static int USER_NAME_COL_POS = 1;
        public final static int FIRST_NAME_COL_POS = 2;
        public final static int USER_PASSWORD_COL_POS = 3;
        public final static int USER_IMAGE_URI_COL_POS = 4;
    }

    public class ProfileTable {
        public final static String TABLE_NAME = "profileTable";
        public final static String PROFIlE_ID_COL = "_profileId";
        public final static String USER_ID_COL = "userId";
        public final static String ATTRIB_COL = "attrib";
        public final static String ATTRIB_TYPE_COL = "attribType";
        public final static int PROFIlE_ID_COL_POS = 0;
        public final static int USER_ID_COL_POS = 1;
        public final static int ATTRIB_COL_POS = 2;
        public final static int ATTRIB_TYPE_COL_POS = 3;

        public class AttribType
        {
            public final static int CAR_NUM = 0;
            public final static int PHONE_NUM = 1;
        }
    }

    public class BlockingRelation {
        public final static String TABLE_NAME = "BlockingRelationTable";
        public final static String RELATION_ID_COL = "_relationId";
        public final static String BLOCKING_USER_ID_COL = "blockingUserId";
        public final static String BLOCKED_USER_ID_COL = "blockedUserId";
        public final static String BLOCKED_PROFILE_ID_COL = "blockedProfileId";
        public final static int RELATION_ID_COL_POS = 0;
        public final static int BLOCKING_USER_ID_POS = 1;
        public final static int BLOCKED_USER_ID_POS = 2;
        public final static int BLOCKED_PROFILE_ID_POS = 3;
    }


}
