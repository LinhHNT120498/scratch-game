package org.example.scratchgame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.scratchgame.obj.ConfigObj;
import org.example.scratchgame.obj.OutputObj;
import org.example.scratchgame.obj.StandardSymbolsObj;
import org.example.scratchgame.obj.WinCombinationsObj;
import org.example.scratchgame.utils.Constant.WinCombinationsGroupEnum;
import org.example.scratchgame.utils.DataUtils;

import java.io.FileReader;
import java.util.*;
import java.util.Map.Entry;

public class App {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Random random = new Random();

    private static Integer betAmount;
    private static ConfigObj configObj;
    private static String[][] board;
    private static final Map<String, Integer> mapSymbolOnBoard = new HashMap<>();
    private static final OutputObj outputObj = OutputObj.builder()
            .reward(0)
            .applied_winning_combinations(new HashMap<>())
            .build();

    public static void main(String[] args) {
        try {
//      args = new String[]{"D:\\source\\untitled\\take-home-assignment\\config.json", "100"};
            System.out.println("Input arguments: " + Arrays.toString(args));
            if (args.length == 1) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter bet amount: ");
                betAmount = scanner.nextInt();
            } else if (args.length == 2) {
                betAmount = Integer.parseInt(args[1]);
                System.out.println("Bet amount: " + betAmount);
            } else {
                throw new RuntimeException("Invalid number of input arguments");
            }
            configObj = gson.fromJson(new FileReader(args[0]), ConfigObj.class);

            initializeBoard();
            printBoard();
            checkWinCombinations();
            calculateReward();

            System.out.println("Return: " + gson.toJson(outputObj));
        } catch (Exception e) {
            System.out.println(e.getClass() + ": " + e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                System.out.println(stackTraceElement.toString());
            }
        }
    }

    private static void calculateReward() {
        if (outputObj.getApplied_winning_combinations().isEmpty()) {
            outputObj.setApplied_winning_combinations(null);
            outputObj.setApplied_bonus_symbol(null);
            return;
        }

        for (Entry<String, List<String>> entry : outputObj.getApplied_winning_combinations().entrySet()) {
            int symbolReward = DataUtils.calculateSymbolReward(betAmount, configObj.getSymbols().get(entry.getKey()));
            for (String winCombination : entry.getValue()) {
                symbolReward = Double.valueOf(symbolReward * configObj.getWin_combinations().get(winCombination).getReward_multiplier()).intValue();
            }
            outputObj.setReward(outputObj.getReward() + symbolReward);
        }

        outputObj.setReward(DataUtils.calculateSymbolReward(outputObj.getReward(), configObj.getSymbols().get(outputObj.getApplied_bonus_symbol())));
    }

    private static void checkWinCombinations() {
        Map<String, Map<String, WinCombinationsObj>> groupWinCombinationsMap = new HashMap<>();

        Map<String, WinCombinationsObj> winCombinations = configObj.getWin_combinations();
        for (Entry<String, WinCombinationsObj> entry : winCombinations.entrySet()) {
            String group = entry.getValue().getGroup();
            if (!groupWinCombinationsMap.containsKey(group)) {
                groupWinCombinationsMap.put(group, new HashMap<>());
            }
            groupWinCombinationsMap.get(group).put(entry.getKey(), entry.getValue());
        }

        for (Entry<String, Map<String, WinCombinationsObj>> entryGroupWinCombinations : groupWinCombinationsMap.entrySet()) {
            switch (WinCombinationsGroupEnum.getByVal(entryGroupWinCombinations.getKey())) {
                case SAME_SYMBOLS:
                    for (Entry<String, Integer> entrySymbol : mapSymbolOnBoard.entrySet()) {
                        entryGroupWinCombinations.getValue().entrySet().stream()
                                .filter(entry -> entrySymbol.getValue().equals(entry.getValue().getCount()))
                                .findFirst()
                                .ifPresent(entry -> {
                                    if (!outputObj.getApplied_winning_combinations().containsKey(entrySymbol.getKey())) {
                                        outputObj.getApplied_winning_combinations().put(entrySymbol.getKey(), new ArrayList<>());
                                    }
                                    outputObj.getApplied_winning_combinations().get(entrySymbol.getKey()).add(entry.getKey());
                                });
                    }
                    break;
                case HORIZONTALLY_LINEAR_SYMBOLS:
                case VERTICALLY_LINEAR_SYMBOLS:
                case RTL_DIAGONALLY_LINEAR_SYMBOLS:
                case LTR_DIAGONALLY_LINEAR_SYMBOLS:
                    for (Entry<String, WinCombinationsObj> entry : entryGroupWinCombinations.getValue().entrySet()) {
                        checkWinCombination:
                        for (List<String> coveredAreas : entry.getValue().getCovered_areas()) {
                            String symbol = "";
                            for (String coveredArea : coveredAreas) {
                                String[] pos = coveredArea.split(":");
                                if (symbol.isEmpty()) {
                                    symbol = board[Integer.parseInt(pos[0])][Integer.parseInt(pos[1])];
                                } else if (!symbol.equals(board[Integer.parseInt(pos[0])][Integer.parseInt(pos[1])])) {
                                    continue checkWinCombination;
                                }
                            }

                            if (!outputObj.getApplied_winning_combinations().containsKey(symbol)) {
                                outputObj.getApplied_winning_combinations().put(symbol, new ArrayList<>());
                            }
                            outputObj.getApplied_winning_combinations().get(symbol).add(entry.getKey());
                            break;
                        }
                    }
                    break;
                default:
                    throw new RuntimeException("Unknown win combination group: " + entryGroupWinCombinations.getKey());
            }
        }
    }

    private static void printBoard() {
        System.out.println("Board:");
        System.out.println("-------------------------");
        for (int i = 0; i < configObj.getRows(); i++) {
            System.out.print("|");
            for (int j = 0; j < configObj.getColumns(); j++) {
                System.out.printf(" %-5s |", board[i][j]);
            }
            System.out.println();
            System.out.println("-------------------------");
        }
    }

    private static void initializeBoard() {
        board = new String[configObj.getRows()][configObj.getColumns()];

        Map<String, Double> bonusSymbols = configObj.getProbabilities().getBonus_symbols().getSymbols();
        double total = bonusSymbols.values().stream().mapToDouble(Double::doubleValue).sum();
        for (Entry<String, Double> symbol : bonusSymbols.entrySet()) {
            symbol.setValue(symbol.getValue() / total * 100.0);
        }
        String randomItem = DataUtils.getRandomItem(bonusSymbols);
        board[random.nextInt(3)][random.nextInt(3)] = randomItem;
        outputObj.setApplied_bonus_symbol(randomItem);

        List<StandardSymbolsObj> standardSymbols = configObj.getProbabilities().getStandard_symbols();
        for (StandardSymbolsObj standardSymbol : standardSymbols) {
            if (board[standardSymbol.getRow()][standardSymbol.getColumn()] != null) {
                continue;
            }

            Map<String, Double> symbols = standardSymbol.getSymbols();
            total = symbols.values().stream().mapToDouble(Double::doubleValue).sum();
            for (Entry<String, Double> symbol : symbols.entrySet()) {
                symbol.setValue(symbol.getValue() / total * 100.0);
            }
            randomItem = DataUtils.getRandomItem(standardSymbol.getSymbols());
            board[standardSymbol.getRow()][standardSymbol.getColumn()] = randomItem;
            if (!mapSymbolOnBoard.containsKey(randomItem)) {
                mapSymbolOnBoard.put(randomItem, 1);
            } else {
                mapSymbolOnBoard.put(randomItem, mapSymbolOnBoard.get(randomItem) + 1);
            }
        }

        outputObj.setMatrix(board);
    }
}
