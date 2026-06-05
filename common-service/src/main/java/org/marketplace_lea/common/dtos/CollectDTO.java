package org.marketplace_lea.common.dtos;

import org.marketplace_lea.common.entities.collect.CollectStatus;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.marketplace_lea.common.common.utils.MediaUtils.getCollectImageUrl;

@Data
@ToString
public class CollectDTO {
    private String id;

    private String label;

    private String code;

    private float amount;

    private int duration;

    private float balance;

    private String currency;

    private Integer minMembersCount;

    private CollectStatus status;

    private float counterparty;

    private String image;

    private String videoUrl;

    private ProjectTypeDTO projectType;

    private String description;

    private double expectedAmountPerMonth;

    private List<CollectRecipientDTO> collectRecipients = new ArrayList<>();

    private List<CollectMemberDTO> members = new ArrayList<>();

    public String getProjectType() {
        return Optional.ofNullable(projectType)
                .map(ProjectTypeDTO::getLabel)
                .orElse("UNKNOWN Project type !");
    }

    public void addMember(CollectMemberDTO memberDTO) {
        this.members.add(memberDTO);
    }

    public String getStatusLabel() {
        return Optional.ofNullable(status)
                .map(CollectStatus::getLabel)
                .orElse("UNKNOWN Status Label !");
    }

    public String getImage() {
        return getCollectImageUrl(image);
    }

    public String getId() {
        return id;
    }

    public void calculateExpectedAmountPerMonth() {
        this.expectedAmountPerMonth = calculateExpectedAmount();
    }


    /// Méthodes privées.
    private double calculateExpectedAmount() {
        return this.members.stream()
                .map(CollectMemberDTO::getExpectedAmountPerMonth)
                .reduce(0D, Double::sum);
    }
}
