package org.marketplace_lea.common.dtos;

import java.util.List;

public record CollectsDTO(boolean alreadyInCollect, List<CollectDTO> collects) {
}
