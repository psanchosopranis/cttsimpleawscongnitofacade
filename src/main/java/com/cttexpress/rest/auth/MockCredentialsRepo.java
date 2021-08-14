package com.cttexpress.rest.auth;

import com.cttexpress.rest.representations.MgmtUser;
import com.cttexpress.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class MockCredentialsRepo {

    protected HashMap<String, MgmtUser> mgmtUsersList;

    public MockCredentialsRepo() {

        this.mgmtUsersList = new HashMap<>();

        // Usuarios con roles de Administradores

        this.mgmtUsersList.put (
                "admin1", new MgmtUser(
                    "admin1",
                     MgmtUser.bcryptPassword("!Uvb75kcb9tetBB"),
                    "ADMIN,SYSTEM",
                    "ACTIVE",
                     DateUtils.getFullTimeStampFormattedWithMillis())
        );

        this.mgmtUsersList.put (
                "admin2", new MgmtUser(
                        "admin2",
                        MgmtUser.bcryptPassword("j6z8mpjFnx9h9XNZ.."),
                        "ADMIN,SYSTEM",
                        "ACTIVE",
                        DateUtils.getFullTimeStampFormattedWithMillis())
        );

        // Usuarios "regulares" "QRYONLY"

        this.mgmtUsersList.put (
                "qryonly1", new MgmtUser(
                        "qryonly1",
                        MgmtUser.bcryptPassword("VRrF3jpALFeTqxBT!"),
                        "QRYONLY",
                        "ACTIVE",
                        DateUtils.getFullTimeStampFormattedWithMillis())
        );

        this.mgmtUsersList.put (
                "qryonly2", new MgmtUser(
                        "qryonly2",
                        MgmtUser.bcryptPassword("WjnyW2ugrs2vkMJk$"),
                        "QRYONLY",
                        "ACTIVE",
                        DateUtils.getFullTimeStampFormattedWithMillis())
        );

    }

    public HashMap<String, MgmtUser> getMgmtUsersList() {
        return mgmtUsersList;
    }
}
