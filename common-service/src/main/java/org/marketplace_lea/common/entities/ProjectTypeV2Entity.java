package org.marketplace_lea.common.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ProjectTypeV2")
@Table(name = "ce_project_type_v2")
public class ProjectTypeV2Entity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", unique = true, nullable = false)
    private String label;

    @Column(name = "description")
    private String description;
}
