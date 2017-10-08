package com.alpatchino;

import java.util.Scanner;

/**
 * Created by patri on 24/09/2017.
 */
public class Solution {

    static int[] solve(int a0, int a1, int a2, int b0, int b1, int b2){

        int ALICE = 0;
        int BOB = 1;

        int[] scores = {0, 0};

        if(a0 > b0){
            scores[ALICE]++;
        }else if(a0 < b0){
            scores[BOB]++;
        }

        if(a1 > b1){
            scores[ALICE]++;
        }else if(a1 < b1){
            scores[BOB]++;
        }

        if(a2 > b2){
            scores[ALICE]++;
        }else if(a2 < b2){
            scores[BOB]++;
        }

        return scores;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int a0 = in.nextInt();
        int a1 = in.nextInt();
        int a2 = in.nextInt();
        int b0 = in.nextInt();
        int b1 = in.nextInt();
        int b2 = in.nextInt();
        int[] result = solve(a0, a1, a2, b0, b1, b2);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + (i != result.length - 1 ? " " : ""));
        }
        System.out.println("");


    }
}

