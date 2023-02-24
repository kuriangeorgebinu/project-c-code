package com.coderofjava8888.helpers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Processor_HashMap_Synchronizer {

    HashMap<String, Double> portfolioMap = new HashMap<>();
    

    public TreeMap<String, Double> synchMethod(Map<String, Double> unsortedIncomingMap) {
        unsortedIncomingMap.size();
        System.out.println("unsortedIncomingMap.size= " + unsortedIncomingMap.size());
        System.out.println();
        System.out.println("unsortedIncomingMap HashMap= " + unsortedIncomingMap.size());
        System.out.println();


        portfolioMap.put("NZD", 0.0);
        portfolioMap.put("RUR", 0.0);
        portfolioMap.put("ZAR", 0.0);
        portfolioMap.put("MXP", 0.0);
        portfolioMap.put("AUD", 0.0);
        portfolioMap.put("CAD", 0.0);
        portfolioMap.put("CHF", 0.0);
        portfolioMap.put("EUR", 0.0);
        portfolioMap.put("GBP", 0.0);
        portfolioMap.put("JPY", 0.0);
        


        

        Set<Map.Entry<String, Double>> entrySet = unsortedIncomingMap.entrySet();
        for (Map.Entry<String, Double> e : entrySet) {
            if (unsortedIncomingMap.containsKey(e.getKey())) {
                portfolioMap.put(e.getKey(), e.getValue());
            } else {
                portfolioMap.put(e.getKey(), e.getValue());
            }
        }

        System.out.println("Before Sorting:");
        Set set = portfolioMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry me = (Map.Entry) iterator.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }

        TreeMap<String, Double> map = new TreeMap<>(portfolioMap);
        System.out.println("After Sorting:");
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator2.next();
            System.out.print(me2.getKey() + ": ");
            System.out.println(me2.getValue());
        }


        System.out.println();
        System.out.println();
        System.out.println("Here's Merged map = :" + map);
        System.out.println();
        System.out.println();

        System.out.println("Here's AFTER -- Taking out 0.0 positions:");
        System.out.println();
        System.out.println();

        Map<String, Double> result = map.entrySet()
                .stream()
                .filter(e -> Math.abs(e.getValue()) != 0.0)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        TreeMap<String, Double> treeMapDONE = new TreeMap<>();
//        // Pass the hashMap to putAll() method
        treeMapDONE.putAll(result);

        return treeMapDONE;

    }

//    //https://www.geeksforgeeks.org/program-to-convert-hashmap-to-treemap-in-java/
//    public static <K, V> Map<K, V> convertToTreeMap(Map<K, V> hashMap) {
//        // Create a new TreeMap
//        Map<K, V> treeMap = new TreeMap<>();
//        // Pass the hashMap to putAll() method
//        treeMap.putAll(hashMap);
//        // Return the TreeMap
//        return treeMap;
//    }

    /*
    public Map<String, Integer> synchMethod_StrInt(HashMap<String, Integer> unsortedIncomingMap) {
        unsortedIncomingMap.size();
        System.out.println("unsortedIncomingMap.size= " + unsortedIncomingMap.size());
        System.out.println();
        System.out.println("unsortedIncomingMap HashMap= " + unsortedIncomingMap.size());
        System.out.println();

        unsortedMapi = unsortedIncomingMap;

        portfolioMapi.put("AUD", 0);
        portfolioMapi.put("CAD", 0);
        portfolioMapi.put("CHF", 0);
        portfolioMapi.put("EUR", 0);
        portfolioMapi.put("GBP", 0);
        portfolioMapi.put("JPY", 0);
        portfolioMapi.put("MXP", 0);
        portfolioMapi.put("NZD", 0);
        portfolioMapi.put("RUR", 0);
        portfolioMapi.put("ZAR", 0);
        
        
        Processor_HashMap_Synchronizer HM_S = new Processor_HashMap_Synchronizer();

        Set<Map.Entry<String, Integer>> entrySet = unsortedMapi.entrySet();
        for (Map.Entry<String, Integer> e : entrySet) {
            if (unsortedMapi.containsKey(e.getKey())) {
                portfolioMapi.put(e.getKey(), e.getValue());
            } else {
                portfolioMapi.put(e.getKey(), e.getValue());
            }
        }

        System.out.println("Before Sorting:");
        Set set = portfolioMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry me_i = (Map.Entry) iterator.next();
            System.out.print(me_i.getKey() + ": ");
            System.out.println(me_i.getValue());
        }

        TreeMap<String, Integer> map_i = new TreeMap<>(portfolioMapi);
        System.out.println("After Sorting:");
        Set set2_i = map_i.entrySet();
        Iterator iterator2_i = set2_i.iterator();
        while (iterator2_i.hasNext()) {
            Map.Entry me2_i = (Map.Entry) iterator2_i.next();
            System.out.print(me2_i.getKey() + ": ");
            System.out.println(me2_i.getValue());
        }

//        TreeMap<String, Double> mergedMap = HM_S.merge_maps_before_java8();
//        for (Map.Entry<String, Double> e : mergedMap.entrySet()) {
//            System.out.println("Key : " + e.getKey() + "  Value : " + e.getValue());
//        }
        System.out.println();
        System.out.println();
        System.out.println("Here's Merged map = :" + map_i);
        System.out.println();
        System.out.println();

   //https://www.geeksforgeeks.org/program-to-convert-hashmap-to-treemap-in-java/     
       Map<String, Integer> result = map_i.entrySet()
      .stream()
      .filter(map -> map.getValue() != 0)
      .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
        
       
   
       Map<String, Integer> treeMap = new TreeMap<>();
        // Pass the hashMap to putAll() method
        treeMap.putAll(result);
         
         
        return treeMap;

    }
     */
}
