package org.example.scratchgame.obj;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinCombinationsObj {

    private Double reward_multiplier;
    private String when;
    private Integer count;
    private String group;
    private List<List<String>> covered_areas;
}
