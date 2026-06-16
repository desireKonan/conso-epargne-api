package org.marketplace_lea.common.common.dtos;

import org.marketplace_lea.common.entities.account.AccountV2Entity;
import lombok.Getter;
import org.marketplace_lea.common.services.account.DefaultAccountV2Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implémentation de {@link UserDetails} pour le modèle V2 ({@link AccountV2Entity}).
 *
 * <p>Équivalent de {@link UserPrincipalV2} mais basé sur l'entité {@code AccountV2Entity}
 * (table {@code ce_account_v2}) au lieu de l'entité {@code Account} legacy (table {@code ce_account}).
 * Utilisé par {@link DefaultAccountV2Service}
 * pour l'authentification Spring Security dans le module prometheus-service.</p>
 *
 * <p>Règles d'état du compte :</p>
 * <ul>
 *   <li>{@code isAccountNonLocked()} → {@code !blacklisted}</li>
 *   <li>{@code isEnabled()} → compte non supprimé ({@code deletedAt == null})</li>
 *   <li>{@code isAccountNonExpired()} et {@code isCredentialsNonExpired()} → toujours {@code true}</li>
 * </ul>
 */
@Getter
public class UserPrincipalV2 implements UserDetails {

    private final AccountV2Entity account;

    public UserPrincipalV2(AccountV2Entity account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("CUSTOMER"));
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getLogin();
    }

    /**
     * Compte non verrouillé = non blacklisté.
     */
    @Override
    public boolean isAccountNonLocked() {
        return !account.isBlacklisted();
    }

    /**
     * Compte actif = jamais supprimé (soft delete via {@code deletedAt}).
     */
    @Override
    public boolean isEnabled() {
        return account.getDeletedAt() == null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
