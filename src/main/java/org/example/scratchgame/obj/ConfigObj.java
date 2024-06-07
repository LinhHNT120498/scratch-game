package org.example.scratchgame.obj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigObj {

    private Integer columns;
    private Integer rows;
    private Map<String, SymbolsObj> symbols;
    private ProbabilitiesObj probabilities;
    private Map<String, WinCombinationsObj> win_combinations;
}
