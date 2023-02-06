package com.lolydleo;

import java.util.Random;
/**
 * @author lolydleo
 * ant class
 * */
public class ant {
    /**
     * 蚂蚁参观城市的顺序
     */
    public int[] tour;
    /**
     * 取值是0或1，1表示没有访问过，0表示访问过
     * */
    public int[] unvisitedCity;

    /**
     * 蚂蚁获得的路径长度
     */
    public int tourLength;
    /**
     * 城市个数
     * */
    public int citys;

    /**
     * 随机分配蚂蚁到某个城市中
     * 同时完成蚂蚁包含字段的初始化工作
     * @param cityCount 总的城市数量
     */
    public void randomSelectCity(int cityCount){
        citys = cityCount;
        unvisitedCity = new int[cityCount];
        tour = new int[cityCount+1];
        tourLength = 0;
        for(int i = 0; i < cityCount; i++){
            tour[i] =- 1;
            unvisitedCity[i] = 1;
        }

        long r1 = System.currentTimeMillis();
        Random random = new Random(r1);
        //随机指定第一个城市作为起点
        int firstCity = random.nextInt(cityCount);
        unvisitedCity[firstCity] = 0;
        tour[0] = firstCity;
    }

    /**
     * 选择下一个城市
     * @param index 需要选择第index个城市了
     * @param tao   全局的信息素信息
     * @param distance  全局的距离矩阵信息
     */
    public void selectNextCity(int index,double[][]tao,int[][]distance){
        double[] p;
        // 下一步要走的城市的选中概率
        p = new double[citys];
        // 计算选中概率所需系数。
        double alpha = 1.0;
        double beta = 2.0;
        double sum = 0;
        //蚂蚁所处当前城市
        int currentCity = tour[index - 1];
        //计算公式中的分母部分（为下一步计算选中概率使用）
        for(int i = 0; i < citys; i++){
            // 没走过
            if(unvisitedCity[i]==1) {
                sum+=(Math.pow(tao[currentCity][i], alpha) *
                        Math.pow(1.0 / distance[currentCity][i], beta));
            }
        }
        //计算每个城市被选中的概率
        for(int i = 0; i < citys; i++){
            //城市走过了，选中概率就是0
            if(unvisitedCity[i] == 0){
                p[i] = 0.0;
            } else {
                //没走过，下一步要走这个城市的概率是？
                p[i] = (Math.pow(tao[currentCity][i], alpha) *
                        Math.pow(1.0 / distance[currentCity][i], beta)) / sum;
            }
        }
        long r1 = System.currentTimeMillis();
        Random random = new Random(r1);
        double selecTp = random.nextDouble();
        //轮盘赌选择一个城市；
        double sumselect = 0;
        int selectCity = -1;
        //城市选择随机，直到n个概率加起来大于随机数，则选择该城市
        for(int i = 0; i < citys; i++){
            sumselect += p[i];
            if(sumselect >= selecTp){
                selectCity = i;
                break;
            }
        }
        if (selectCity == -1) {
            System.out.println();
        }
        tour[index] = selectCity;
        unvisitedCity[selectCity] = 0;
    }

    /**
     * 计算蚂蚁获得的路径的长度
     * @param distance  全局的距离矩阵信息
     */
    public void calTourLength(int [][]distance){
        tourLength = 0;
        //第一个城市等于最后一个要到达的城市
        tour[citys] = tour[0];
        for(int i = 0; i < citys; i++){
            tourLength += distance[tour[i]][tour[i + 1]];
        }
    }
}
