package com.lolydleo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lolydleo
 * */
public class Main {
    public static void main(String[] args) {
        ACO aco = new ACO();

        try {
            aco.init("F:/ACO/libs/att48.tsp", 100);
            aco.run(1000);
            aco.reportResult();
        } catch (FileNotFoundException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
