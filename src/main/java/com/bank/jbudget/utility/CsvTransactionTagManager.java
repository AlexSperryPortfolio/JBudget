package com.bank.jbudget.utility;

import com.bank.jbudget.constants.CommonConstants;
import com.bank.jbudget.domain.CsvTransactionTag;
import com.bank.jbudget.services.CsvTransactionTagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CsvTransactionTagManager {

    private CsvTransactionTagService csvTransactionTagService;

    public static Map<String, CsvTransactionTag> csvTransactionTagCache = new HashMap<>();

    public CsvTransactionTagManager(CsvTransactionTagService csvTransactionTagService) {
        this.csvTransactionTagService = csvTransactionTagService;
    }

    public void seedBaseTags() {
        List<CsvTransactionTag> tagList = new ArrayList<>();

        tagList.add(new CsvTransactionTag(CommonConstants.ESSENTIALS));
        tagList.add(new CsvTransactionTag(CommonConstants.SECURITY));
        tagList.add(new CsvTransactionTag(CommonConstants.GOALS));
        tagList.add(new CsvTransactionTag(CommonConstants.LIFESTYLE));
        tagList.add(new CsvTransactionTag(CommonConstants.DISCRETIONARY));

        tagList.add(new CsvTransactionTag(CommonConstants.YEARLY_EXPENSE));

        tagList.add(new CsvTransactionTag(CommonConstants.BILLS));
        tagList.add(new CsvTransactionTag(CommonConstants.DEBT));

        tagList.add(new CsvTransactionTag("ATM Surcharge", "ATM SURCHARGE"));

        //student loans
        tagList.add(new CsvTransactionTag(CommonConstants.STUDENT_LOANS));
        tagList.add(new CsvTransactionTag("Discover Loans", "DISCOVER BANK"));
        tagList.add(new CsvTransactionTag("Discover Loans", "THE STUDENT LOAN DSLCPYMNTS"));
        tagList.add(new CsvTransactionTag("Firstmark Loans", "FIRSTMARK PAYMENTS"));
        tagList.add(new CsvTransactionTag("Firstmark Loans", "CITIBANK"));
        tagList.add(new CsvTransactionTag("EdFinancial Loans", "DEPT EDUCATION STUDENT LN"));
        tagList.add(new CsvTransactionTag("Aspire Loans", "ISLLC WEB DISB"));

        //car
        tagList.add(new CsvTransactionTag(CommonConstants.CAR_INSURANCE));
        tagList.add(new CsvTransactionTag("Liberty Mutual", "LIBERTY MUTUAL PAYMENT"));
        tagList.add(new CsvTransactionTag(CommonConstants.CAR_REPAIR));
        tagList.add(new CsvTransactionTag("Tuan Auto Repair", "TUAN AUTO REPAIR"));
        tagList.add(new CsvTransactionTag("Metric Auto Work", "METRIC AUTO WORK"));

        //gas stations
        tagList.add(new CsvTransactionTag(CommonConstants.GAS_STATION));
        tagList.add(new CsvTransactionTag("BP", "BP#"));
        tagList.add(new CsvTransactionTag("Super America", "SUPERAMERICA"));
        tagList.add(new CsvTransactionTag("Holiday", "HOLIDAY STNSTORE"));

        //food
        tagList.add(new CsvTransactionTag(CommonConstants.FAST_FOOD));
        tagList.add(new CsvTransactionTag("Groceries"));

        //alcohol
        tagList.add(new CsvTransactionTag("Alcohol"));

        //entertainment
        tagList.add(new CsvTransactionTag(CommonConstants.ENTERTAINMENT));
        tagList.add(new CsvTransactionTag("Streaming Video"));
        tagList.add(new CsvTransactionTag("Steam"));
        tagList.add(new CsvTransactionTag("Xbox"));
        tagList.add(new CsvTransactionTag("Amazon"));

        //unemployment
        tagList.add(new CsvTransactionTag("Unemployment"));

        //account credits
        tagList.add(new CsvTransactionTag(CommonConstants.PAYCHECK));
        tagList.add(new CsvTransactionTag("Aston Technologies"));
        tagList.add(new CsvTransactionTag("Backbone Consultants"));
        tagList.add(new CsvTransactionTag("Bank Rewards"));
        tagList.add(new CsvTransactionTag("Cash Deposit"));

        //not categorized
        tagList.add(new CsvTransactionTag("Not Categorized"));

        this.warmUpCsvTransactionTagCache(csvTransactionTagService.saveCsvTransactionTagList(tagList));
    }

    public List<CsvTransactionTag> addDefaultTags(String descriptionContent) {
        String descUpper = descriptionContent.toUpperCase();
        List<CsvTransactionTag> typeList = new ArrayList<>();

        //fast food
        String[] fastFoodArray = {
                "MCDONALD'S", "BURGER KING", "CULVERS", "WENDY", "TACO BELL", "HARDEE'S", "PEI WEI", "SUBWAY",
                "NAF NAF", "WHICH WICH", "JIMMY JOHNS", "ARBY'S", "CHIPOTLE", "MIM'S CAFE", "ERBERT AND GERBE",
                "PIZZA LUCE", "POTBELLY", "NEW ASIA", "PAPA JOHN'S", "TOPPERS", "DAVANNI'S", "MY BURGER", "PANERA",
                "GOOD TO GO DELI", "TACO JOHN'S", "AU BON PAIN", "BRUEGGERS", "DAVE'S DOWNTOWN", "THE BROTHERS DEL",
                "HEN HOUSE", "SPORTY'S PUB"};

        //Fast Food
        for (String entry : fastFoodArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(CommonConstants.FAST_FOOD));
                typeList.add(csvTransactionTagCache.get(CommonConstants.DISCRETIONARY));
            }
        }

        //Student Loans
        String[] studentLoanArray = {
                "DISCOVER BANK", "THE STUDENT LOAN DSLCPYMNTS", "FIRSTMARK PAYMENTS", "CITIBANK",
                "DEPT EDUCATION STUDENT LN", "ISLLC WEB DISB", "student loan dslcpymnts"};

        for (String entry : studentLoanArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(entry));
                typeList.add(csvTransactionTagCache.get(CommonConstants.DEBT));
                typeList.add(csvTransactionTagCache.get(CommonConstants.SECURITY));
                typeList.add(csvTransactionTagCache.get(CommonConstants.STUDENT_LOANS));
            }
        }

        //Car Insurance
        String[] carInsuranceArray = {"LIBERTY MUTUAL PAYMENT"};

        for (String entry : carInsuranceArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(entry));
                typeList.add(csvTransactionTagCache.get(CommonConstants.BILLS));
                typeList.add(csvTransactionTagCache.get(CommonConstants.ESSENTIALS));
            }
        }

        //Car Repair
        String[] carRepairArray = {"TUAN AUTO REPAIR", "METRIC AUTO WORK"};

        for (String entry : carRepairArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(entry));
                typeList.add(csvTransactionTagCache.get(CommonConstants.CAR_REPAIR));
                typeList.add(csvTransactionTagCache.get(CommonConstants.ESSENTIALS));
            }
        }

        //Car Registration
        String[] carRegistrationArray = {"MN DVS"};

        for (String entry : carRegistrationArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(entry));
                typeList.add(csvTransactionTagCache.get(CommonConstants.CAR_REGISTRATION));
                typeList.add(csvTransactionTagCache.get(CommonConstants.ESSENTIALS));
                typeList.add(csvTransactionTagCache.get(CommonConstants.YEARLY_EXPENSE));
            }
        }

        //Gas Station
        String[] gasStationArray = {"BP#", "HOLIDAY STNSTORE", "WINNER", "SUPERAMERICA", "CASEYS"};

        for (String entry : gasStationArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(entry));
                typeList.add(csvTransactionTagCache.get(CommonConstants.GAS_STATION));
                typeList.add(csvTransactionTagCache.get(CommonConstants.ESSENTIALS));
            }
        }

        //Groceries
        String[] groceriesArray = {"CUB FO", "TIM AND TOM'S", "SUPER ONE", "RAINBOW"};
        for (String entry : groceriesArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(entry));
                typeList.add(csvTransactionTagCache.get(CommonConstants.GROCERIES));
                typeList.add(csvTransactionTagCache.get(CommonConstants.ESSENTIALS));
            }
        }

        //Target
        if (descUpper.contains("TARGET")) {
            typeList.add(csvTransactionTagCache.get("Target"));
            typeList.add(csvTransactionTagCache.get(CommonConstants.BIG_BOX));
            typeList.add(csvTransactionTagCache.get(CommonConstants.LIFESTYLE));
        }

        //Wal-Mart
        if (descUpper.contains("WAL-MART")) {
            typeList.add(csvTransactionTagCache.get("Wal-Mart"));
            typeList.add(csvTransactionTagCache.get(CommonConstants.BIG_BOX));
            typeList.add(csvTransactionTagCache.get(CommonConstants.LIFESTYLE));
        }

        //Software
        if (descUpper.contains("LAST PASS")) {
            typeList.add(csvTransactionTagCache.get("Software"));
            typeList.add(csvTransactionTagCache.get("Last Pass"));
            typeList.add(csvTransactionTagCache.get(CommonConstants.DISCRETIONARY));
        }

        if (descUpper.contains("ATM SURCHARGE")) {
            typeList.add(csvTransactionTagCache.get("ATM Surcharge"));
            typeList.add(csvTransactionTagCache.get(CommonConstants.DISCRETIONARY));
        }

        //Entertainment
        //Alcohol
        String[] alcoholArray = {"TOTAL WINE", "MGM WINE", "LIQUOR BOY", "PARK LIQUOR STOR", "EDINA LIQUOR", "CUB LI",
                "BARSTOCK LIQUORS", "THE LITTLE WINE SAINT PAUL", "SHARRETTS LIQUOR", "MA AND PA LIQUOR", "MERWIN LIQUOR",
                "CUB LI"
        };
        for (String entry : alcoholArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(entry));
                typeList.add(csvTransactionTagCache.get("Alcohol"));
                typeList.add(csvTransactionTagCache.get(CommonConstants.DISCRETIONARY));
            }
        }

        //Streaming
        String[] streamingArray = {"NETFLIX", "HBO DIG"};
        for (String entry : streamingArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(entry));
                typeList.add(csvTransactionTagCache.get(CommonConstants.ENTERTAINMENT));
                typeList.add(csvTransactionTagCache.get("Streaming Video"));
                typeList.add(csvTransactionTagCache.get(CommonConstants.DISCRETIONARY));
            }
        }

        //Games
        String[] gamesArray = {"VALVE BELLVUE", "STEAMGAMES", "STEAMPOWERED"};
        for (String entry : gamesArray) {
            if (descUpper.contains(entry)) {
                typeList.add(csvTransactionTagCache.get(CommonConstants.ENTERTAINMENT));
                typeList.add(csvTransactionTagCache.get("Steam"));
                typeList.add(csvTransactionTagCache.get(CommonConstants.DISCRETIONARY));
            }
        }

        //Xbox
        if (descUpper.contains("MICROSOFT *XBO")) {
            typeList.add(csvTransactionTagCache.get(CommonConstants.ENTERTAINMENT));
            typeList.add(csvTransactionTagCache.get("Xbox"));
            typeList.add(csvTransactionTagCache.get(CommonConstants.DISCRETIONARY));
        }

        //Amazon
        if (descUpper.contains("AMAZON")) {
            typeList.add(csvTransactionTagCache.get(CommonConstants.ENTERTAINMENT));
            typeList.add(csvTransactionTagCache.get("Amazon"));
            typeList.add(csvTransactionTagCache.get(CommonConstants.DISCRETIONARY));
        }

        //Credits
        if (descUpper.contains("MN DEPT OF DEED")) {
            typeList.add(csvTransactionTagCache.get("Unemployment"));
        }

        if (descUpper.contains("ASTON TECHNOLOGI")) {
            typeList.add(csvTransactionTagCache.get(CommonConstants.PAYCHECK));
            typeList.add(csvTransactionTagCache.get("Aston Technologies"));
        }

        if (descUpper.contains("PAYROLL")) {
            typeList.add(csvTransactionTagCache.get(CommonConstants.PAYCHECK));
            typeList.add(csvTransactionTagCache.get("Backbone Consultants"));
        }

        if (descUpper.contains("KASASA TUNES")) {
            typeList.add(csvTransactionTagCache.get("Bank Rewards"));
        }

        if (descUpper.contains("DEPOSIT")) {
            typeList.add(csvTransactionTagCache.get("Cash Deposit"));
        }

        if (typeList.isEmpty()) {
            typeList.add(csvTransactionTagCache.get(CommonConstants.UNCATEGORIZED));
        }

        return typeList;
    }

    private void warmUpCsvTransactionTagCache(List<CsvTransactionTag> csvTransactionTagList) {
        for (CsvTransactionTag csvTransactionTag : csvTransactionTagList) {
            if (csvTransactionTag.getMatchString() != null) {
                csvTransactionTagCache.put(csvTransactionTag.getMatchString(), csvTransactionTag);
            } else {
                csvTransactionTagCache.put(csvTransactionTag.getTypeName(), csvTransactionTag);
            }
        }
    }

    public CsvTransactionTag getTagFromCache(String key) {
        if(csvTransactionTagCache.size() == 0) { //todo: handle this better
            System.out.println("Warm up csvTransactionTagCache");
            resetCsvTransactionTagCacheWithDbTags();
        }
        return csvTransactionTagCache.get(key);
    }

    public void resetCsvTransactionTagCacheWithDbTags() {
        csvTransactionTagCache.clear();
        warmUpCsvTransactionTagCache(csvTransactionTagService.getAllCsvTransactionTags());
    }

    public List<CsvTransactionTag> getAllCsvTransactionTags() {
        if(csvTransactionTagCache.size() == 0) { //todo: handle this better
            System.out.println("Warm up csvTransactionTagCache");
            resetCsvTransactionTagCacheWithDbTags();
        }
        return csvTransactionTagCache.entrySet().parallelStream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
