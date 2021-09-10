package com.ian.ch04.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Stores information to identify the user.
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    private Long id;
    private String alias;

    public User(final String userAlias) {
        this(null, userAlias);
    }
}
