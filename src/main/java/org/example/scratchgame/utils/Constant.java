package org.example.scratchgame.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Constant {

    @Getter
    @AllArgsConstructor
    public enum WinCombinationsGroupEnum {
        SAME_SYMBOLS("same_symbols"),
        HORIZONTALLY_LINEAR_SYMBOLS("horizontally_linear_symbols"),
        VERTICALLY_LINEAR_SYMBOLS("vertically_linear_symbols"),
        RTL_DIAGONALLY_LINEAR_SYMBOLS("rtl_diagonally_linear_symbols"),
        LTR_DIAGONALLY_LINEAR_SYMBOLS("ltr_diagonally_linear_symbols"),

        UNKNOWN("");

        private final String val;

        public static WinCombinationsGroupEnum getByVal(String val) {
            for (WinCombinationsGroupEnum group : WinCombinationsGroupEnum.values()) {
                if (group.getVal().equals(val)) {
                    return group;
                }
            }
            return UNKNOWN;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum SymbolTypeEnum {
        STANDARD("standard"),
        BONUS("bonus"),

        UNKNOWN("");

        private final String val;

        public static SymbolTypeEnum getByVal(String val) {
            for (SymbolTypeEnum type : SymbolTypeEnum.values()) {
                if (type.getVal().equals(val)) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum SymbolImpactEnum {
        MULTIPLY_REWARD("multiply_reward"),
        EXTRA_BONUS("extra_bonus"),
        MISS("miss"),

        UNKNOWN("");

        private final String val;

        public static SymbolImpactEnum getByVal(String val) {
            for (SymbolImpactEnum impact : SymbolImpactEnum.values()) {
                if (impact.getVal().equals(val)) {
                    return impact;
                }
            }
            return UNKNOWN;
        }
    }
}

