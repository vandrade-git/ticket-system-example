package org.vandrade.support.security;

import org.springframework.security.core.Authentication;

/**
 * Author: Vitor Andrade
 * Date: 10/12/18
 * Time: 1:04 PM
 */
public interface IAuthenticationFacade {
    Authentication getAuthentication();

    CustomUserDetails geCustomUserDetails();
}
