package org.example.scratchgame.obj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProbabilitiesObj {

    private List<StandardSymbolsObj> standard_symbols;
    private BonusSymbolsObj bonus_symbols;
}
