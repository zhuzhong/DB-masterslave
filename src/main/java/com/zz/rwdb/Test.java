/**
 * 
 */
package com.zz.rwdb;

import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 *
 */
public class Test {

    
    public static void main(String args[]){
        Random r=new Random();
        while(true){
            System.out.println(r.nextInt(0));
        }
    }
    public S tt(List<S> list) {

        /*
         * 　假设有一组服务器S = {S0, S1, …, Sn-1}，W(Si)表示服务器Si的权值，
         * 一个指示变量i表示上一次选择的服务器，指示变量cw表示当前调度的权值， max(S)表示集合S中所有服务器的最大权值，
         * gcd(S)表示集合S中所有服务器权值的最大公约数。 变量i初始化为-1，cw初始化为零。其算法如下：
         */
        /*
         * while (true) { i = (i + 1) mod n; if (i == 0) { cw = cw - gcd(S); if
         * (cw <= 0) { cw = max(S); if (cw == 0) return NULL; } } if (W(Si) >=
         * cw) return Si; }
         */
        int i = -1;
        int machineNo = list.size();
        int cw = 0;
        int gcd = gcd(list);
        int maxWeight = maxWeight(list);

        while (true) {
            i = (i + 1) % machineNo;
            if (i == 0) {
                cw = cw - gcd;
                if (cw < 0) {
                    cw = maxWeight;
                    if (cw == 0) {
                        return null;
                    }
                }
            }
            S si = list.get(i);
            if (si.getWeight() >= cw) {
                return si;
            }
        }

    }
    
    private int gcd(List<S> list) {
        return 0;
    }

    private int minWeight(List<S> list) {
        return 0;
    }

    private int maxWeight(List<S> list) {
        return 0;
    }

    private static class S {
        private int weight;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

    }
}
