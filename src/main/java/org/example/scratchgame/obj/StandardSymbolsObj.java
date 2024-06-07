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
public class StandardSymbolsObj {

    private Integer column;
    private Integer row;
    private Map<String, Double> symbols;
}
