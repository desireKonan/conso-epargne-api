package org.marketplace_lea.common.entities.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.marketplace_lea.common.entities.BaseEntity;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TransactionNotificationTemplate")
@Table(name = "ce_transaction_notification_templates", indexes = {
        @Index(name = "idx_key", columnList = "t_key"),
        @Index(name = "idx_type", columnList = "type")
})
public class TransactionNotificationTemplateEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "t_key", nullable = false)
    private String key;

    // Type de transaction lié
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType transactionType;

    // Titre par langue.
    @Column(name = "titles", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> titles = new HashMap<>();

    // Messages par langue.
    @Column(name = "messages", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> messages = new HashMap<>();

    // Activation
    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    private boolean active;
}