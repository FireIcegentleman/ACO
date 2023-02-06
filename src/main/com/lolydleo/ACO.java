package com.lolydleo;

import java.io.*;
/**
 * 蚁群优化算法，用来求解TSP问题
 * @author lolydleo
 */
public class ACO {
    /**
     * 定义蚂蚁群
     * */
    public ant []ants;
    /**
     * 蚂蚁的数量
     * */
    public int antCount;
    /**
     * 表示城市之间的距离
     * */
    public int [][]distance;
    /**
     * 信息素矩阵
     * */
    public double [][]tao;
    /**
     * 城市数量
     * */
    public int cityCount;
    /**
     * 求解的最佳路径
     * */
    public int[] bestTour;
    /**
     * 求解的最佳长度
     * */
    public int bestLength;

    /**
     * @param fileName 文件名
     * @param antNum 用到的蚂蚁数量
     * */
    public void init(String fileName,int antNum) throws FileNotFoundException, IOException{
        antCount = antNum;
        ants = new ant[antCount];
        //读取数据tsp里的数据包括第I个城市与城市的X,Y坐标
        int[] x;
        int[] y;
        String strbuff;
        BufferedReader tspdata = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        strbuff = tspdata.readLine();
        cityCount = Integer.valueOf(strbuff);
        distance = new int[cityCount][cityCount];
        x = new int[cityCount];
        y = new int[cityCount];
        for (int citys = 0; citys < cityCount; citys++) {
            strbuff = tspdata.readLine();
            String[] strcol = strbuff.split(" ");
            x[citys] = Integer.parseInt(strcol[1]);
            y[citys] = Integer.parseInt(strcol[2]);
        }
        //计算两个城市之间的距离矩阵，并更新距离矩阵
        for (int city1 = 0; city1 < cityCount - 1; city1++) {
            distance[city1][city1] = 0;
            for (int city2 = city1 + 1; city2 < cityCount; city2++) {
                distance[city1][city2] = (int) (Math.sqrt((x[city1] - x[city2]) * (x[city1] - x[city2])
                        + (y[city1] - y[city2]) * (y[city1] - y[city2])));
                distance[city2][city1] = distance[city1][city2];
            }
        }
        distance[cityCount - 1][cityCount - 1] = 0;
        //初始化信息素矩阵
        tao = new double[cityCount][cityCount];
        for (int i = 0; i < cityCount; i++) {
            for(int j = 0; j < cityCount; j++){
                tao[i][j] = 0.1;
            }
        }
        bestLength = Integer.MAX_VALUE;
        bestTour = new int[cityCount+1];
        //随机放置蚂蚁
        for (int i = 0; i < antCount; i++) {
            ants[i] = new ant();
            ants[i].randomSelectCity(cityCount);
        }
    }

    /**
     * @param maxGen ACO的最多循环次数
     * */
    public void run(int maxGen){
        for(int runtimes = 0; runtimes < maxGen; runtimes++){
            //每次迭代，所有蚂蚁都要跟新一遍，走一遍
            for(int i = 0; i < antCount; i++){
                for(int j = 1; j < cityCount; j++){
                    ants[i].selectNextCity(j, tao, distance);
                }
                //计算蚂蚁获得的路径长度
                ants[i].calTourLength(distance);
                if(ants[i].tourLength < bestLength){
                    //保留最优路径
                    bestLength = ants[i].tourLength;
                    //runtimes仅代表最大循环次数，但是只有当，有新的最优路径的时候才会显示下列语句。
                    //如果后续没有更优解（收敛），则最后直接输出。
                    System.out.println("第"+runtimes+"次迭代，发现新的最优路径长度：" + bestLength);
                    //更新路径
                    for(int j = 0; j < cityCount + 1; j++) {
                        bestTour[j] = ants[i].tour[j];
                    }
                }
            }
            //更新信息素矩阵
            updateTao();
            //重新随机设置蚂蚁
            for(int i = 0; i < antCount; i++){
                ants[i].randomSelectCity(cityCount);
            }
        }
    }

    /**
     * 更新信息素矩阵
     */
    private void updateTao(){
        double rou = 0.5;
        //信息素挥发
        for(int i = 0; i < cityCount; i++) {
            for(int j = 0; j < cityCount; j++) {
                tao[i][j] = tao[i][j] * (1 - rou);
            }
        }

        //信息素更新
        for(int i = 0; i < antCount; i++){
            for(int j = 0; j < cityCount; j++){
                tao[ants[i].tour[j]][ants[i].tour[j+1]] += 1.0 / ants[i].tourLength;
            }
        }
    }

    /**
     * 输出程序运行结果
     * */
    public void reportResult(){
        System.out.println("最优路径长度是" + bestLength);
        System.out.println("蚁群算法最优路径输出：");
        for(int j = 0; j < cityCount + 1; j++) {
            //输出最优路径
            System.out.print( bestTour[j]+">>");
        }
    }
}
