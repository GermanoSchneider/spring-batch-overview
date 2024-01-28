package com.example.springbatchoverview;

import org.springframework.batch.item.ItemProcessor;

import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.String.join;
import static java.util.Arrays.stream;

public class OrderNumberProcessor implements ItemProcessor<Map<Integer, Integer>, Map<Integer, Integer>> {

    @Override
    public Map<Integer, Integer> process(Map<Integer, Integer> item) throws Exception {

        final Map<Integer, Integer> newMap = new HashMap<>();

        for (Map.Entry<Integer, Integer> mapValue : item.entrySet()) {

            Integer key = mapValue.getKey();
            Integer value = mapValue.getValue();

            List<String> numbers = new ArrayList<>(stream(value.toString()
                    .split(""))
                    .toList());

            numbers.sort(Comparator.naturalOrder());

            Integer newValue = parseInt(join("", numbers));

            newMap.put(key, newValue);
        }

        return newMap;
    }
}
