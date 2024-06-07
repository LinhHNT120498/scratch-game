package org.example.scratchgame.utils;

import org.example.scratchgame.obj.SymbolsObj;
import org.example.scratchgame.utils.Constant.SymbolImpactEnum;
import org.example.scratchgame.utils.Constant.SymbolTypeEnum;

import java.util.Map;
import java.util.Random;

public class DataUtils {

    public static <T> T getRandomItem(Map<T, Double> chances) {
        double chance = new Random().nextDouble() * 100.0;
        double cumulative = 0.0;
        for (T item : chances.keySet()) {
            cumulative += chances.get(item);
            if (chance < cumulative) {
                return item;
            }
        }
        throw new RuntimeException("Chances don't sum to 100");
    }

    public static Integer calculateSymbolReward(int preReward, SymbolsObj symbolsObj) {
        switch (SymbolTypeEnum.getByVal(symbolsObj.getType())) {
            case STANDARD:
                preReward = Double.valueOf(preReward * symbolsObj.getReward_multiplier()).intValue();
                break;
            case BONUS:
                switch (SymbolImpactEnum.getByVal(symbolsObj.getImpact())) {
                    case MULTIPLY_REWARD:
                        preReward = Double.valueOf(preReward * symbolsObj.getReward_multiplier()).intValue();
                        break;
                    case EXTRA_BONUS:
                        preReward = preReward + symbolsObj.getExtra();
                        break;
                    case MISS:
                        break;
                    default:
                        throw new RuntimeException("Unknown symbol impact: " + symbolsObj.getImpact());
                }
                break;
            default:
                throw new RuntimeException("Unknown symbol type: " + symbolsObj.getType());
        }
        return preReward;
    }
}
