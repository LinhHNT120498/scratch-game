package org.example.scratchgame.obj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutputObj {

    private String[][] matrix;
    private Integer reward;
    private Map<String, List<String>> applied_winning_combinations;
    private String applied_bonus_symbol;
}
