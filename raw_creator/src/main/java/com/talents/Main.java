package com.talents;

/**
 * Created by Yannick on 18.07.2015.
 */

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

import ch.yannick.intern.Talent;
import ch.yannick.enums.Action;

public class Main {

    public static void main(String[] args) {
        Yaml yaml = new Yaml();

        Map<Integer,Integer> map = new HashMap<>();
        map.put(1,2);
        map.put(8, 10);

        Talent talent = new Talent(Action.DEFEND);

        System.out.print(talent);
    }
}
