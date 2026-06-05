package org.marketplace_lea.common.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class StatisticConsoItem {
    private int totalChildrenCount;
    private Map<String, List<ChildDTO>> childrenByLevel;

    public StatisticConsoItem(int count, Map<String, List<ChildDTO>> childrenByLevel) {
        this.totalChildrenCount = count;
        this.childrenByLevel = childrenByLevel;
    }
}