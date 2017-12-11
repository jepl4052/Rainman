package trainset;

import fullyconnectednetwork.NetworkTools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class TrainSet {

    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;

    //double[][] <- index1: 0 = input, 1 = output || index2: index of element
    private ArrayList<double[][]> data = new ArrayList<>();

    public TrainSet(int INPUT_SIZE, int OUTPUT_SIZE) {
        this.INPUT_SIZE = INPUT_SIZE;
        this.OUTPUT_SIZE = OUTPUT_SIZE;
    }

    public void addData(double[] in, double[] expected) {
        if(in.length != INPUT_SIZE || expected.length != OUTPUT_SIZE) return;
        data.add(new double[][]{in, expected});
    }

    public TrainSet extractBatch(int size) {
        if(size > 0 && size <= this.size()) {
            TrainSet set = new TrainSet(INPUT_SIZE, OUTPUT_SIZE);
            Integer[] ids = NetworkTools.randomValues(0,this.size() - 1, size);
            for(Integer i:ids) {
                set.addData(this.getInput(i),this.getOutput(i));
            }
            return set;
        }else return this;
    }

    public String toString() {
        String s = "TrainSet ["+INPUT_SIZE+ " ; "+OUTPUT_SIZE+"]\n";
        int index = 0;
        for(double[][] r:data) {
            s += index +":   "+Arrays.toString(r[0]) +"  >-||-<  "+Arrays.toString(r[1]) +"\n";
            index++;
        }
        return s;
    }

    public int size() {
        return data.size();
    }

    public double[] getInput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[0];
        else return null;
    }

    public double[] getOutput(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[1];
        else return null;
    }

    public int getINPUT_SIZE() {
        return INPUT_SIZE;
    }

    public int getOUTPUT_SIZE() {
        return OUTPUT_SIZE;
    }

    public void readCSV(String file) {
        BufferedReader br = null;
        String line = "";
        String splitter = ";";

        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {

                String[] measurement = line.split(splitter);

                double[] input =
                        {humidity(Double.parseDouble(measurement[3])),
                                airpressure(Double.parseDouble(measurement[4])),
                                temperature(Double.parseDouble(measurement[5])),
                                winddirection(Double.parseDouble(measurement[6])),
                                windspeed(Double.parseDouble(measurement[8]))};

                double[] output = {rainamount(Double.parseDouble(measurement[2]))};

                this.addData(input, output);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static double humidity(double humidity) {
        return humidity / 98.0;
    }

    public static double airpressure(double airp) {
        return airp / 1042.9;
    }

    public static double temperature(double temp) {
        return kelvinize(temp) / kelvinize(15.5);
    }

    public static double winddirection(double winddir) {
        return winddir / 360;
    }

    public static double windspeed(double wind) {
        return wind / 6.1;
    }

    public static double rainamount(double rain) {
        return rain / 3.3;
    }

    public static double kelvinize(double celsius) {
        return (celsius+173);
    }
}