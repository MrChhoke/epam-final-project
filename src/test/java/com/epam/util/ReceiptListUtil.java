package com.epam.util;

import com.epam.cash.register.entity.Receipt;
import com.epam.cash.register.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ReceiptListUtil {

    public static List<Receipt> getListReceipt() {
        Receipt doneReceipt = new Receipt(
                6,
                "code-3",
                40000,
                new Date(0),
                new User(
                        1L,
                        "username2",
                        "password",
                        User.Role.ADMIN
                ),
                true,
                new User(
                        2L,
                        "username2",
                        "password",
                        User.Role.ADMIN
                ),
                new ArrayList<>()
        );
        doneReceipt.setDone(true);

        return List.of(
                new Receipt(
                        1,
                        "code-1",
                        1000,
                        new Date(0),
                        new User(
                                1L,
                                "username",
                                "password",
                                User.Role.ADMIN
                        ),
                        new ArrayList<>()
                ),
                new Receipt(
                        2,
                        "code-2",
                        5000,
                        new Date(0),
                        new User(
                                1L,
                                "username",
                                "password",
                                User.Role.ADMIN
                        ),
                        new ArrayList<>()
                ),
                new Receipt(
                        3,
                        "code-3",
                        10000,
                        new Date(0),
                        new User(
                                1L,
                                "username",
                                "password",
                                User.Role.ADMIN
                        ),
                        true,
                        new User(
                                2L,
                                "username2",
                                "password",
                                User.Role.ADMIN
                        ),
                        new ArrayList<>()
                ),
                new Receipt(
                        4,
                        "code-4",
                        20000,
                        new Date(0),
                        new User(
                                1L,
                                "username",
                                "password",
                                User.Role.ADMIN
                        ),
                        true,
                        new User(
                                2L,
                                "username2",
                                "password",
                                User.Role.ADMIN
                        ),
                        new ArrayList<>()
                ),
                doneReceipt
        );
    }

}
