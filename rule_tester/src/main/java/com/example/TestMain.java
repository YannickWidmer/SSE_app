package com.example;

import ch.yannick.enums.Race;

public class TestMain {

    public static int ground(Race race, int weight){
        return weight/12;
    }

    public static  int mouvement(Race race, int weight){
        return weight/12;
    }


    public static void main(int[] args){
        int leger = 12, moyen = 30, lourd = 70;

        System.out.println("leger :");
        for(Race race:Race.values()){
            System.out.print(race.name()+": ground "+ground(race,leger)+" mouvement " + mouvement(race,leger));
        }



    }

}
