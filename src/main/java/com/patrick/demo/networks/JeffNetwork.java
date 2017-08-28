package com.patrick.demo.networks;

/**
 * Created by patri on 20/08/2017.
 */

import com.google.gson.Gson;
import com.patrick.demo.entity.PredictionEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sun.nio.ch.Net;

import java.io.*;
import java.text.NumberFormat;
import java.util.Arrays;

/**
 * Neural Network
 * Feedforward Backpropagation Neural Network
 * Written in 2002 by Jeff Heaton(http://www.jeffheaton.com)
 *
 * This class is released under the limited GNU public
 * license (LGPL).
 *
 * @author Jeff Heaton
 * @version 1.0
 */

public class JeffNetwork extends Network {

    /**
     * The global error for the training.
     */
    protected double globalError;

    /**
     * The number of input neurons.
     */
    protected int inputCount;

    /**
     * The number of hidden neurons.
     */
    protected int hiddenCount;

    /**
     * The number of output neurons
     */
    protected int outputCount;

    /**
     * The total number of neurons in the network.
     */
    protected int neuronCount;

    /**
     * The number of weights in the network.
     */
    protected int weightCount;

    /**
     * The learning rate.
     */
    protected double learnRate;

    /**
     * The outputs from the various levels.
     */
    protected double fire[];

    /**
     * The weight matrix this, along with the thresholds can be
     * thought of as the "memory" of the neural network.
     */
    protected double matrix[];

    /**
     * The errors from the last calculation.
     */
    protected double error[];

    /**
     * Accumulates matrix delta's for training.
     */
    protected double accMatrixDelta[];

    /**
     * The thresholds, this value, along with the weight matrix
     * can be thought of as the memory of the neural network.
     */
    protected double thresholds[];

    /**
     * The changes that should be applied to the weight
     * matrix.
     */
    protected double matrixDelta[];

    /**
     * The accumulation of the threshold deltas.
     */
    protected double accThresholdDelta[];

    /**
     * The threshold deltas.
     */
    protected double thresholdDelta[];

    /**
     * The momentum for training.
     */
    protected double momentum;

    /**
     * The changes in the errors.
     */
    protected double errorDelta[];


    protected double input[][];
    protected double output[][];

    public JeffNetwork() {

    }

    public static void main(String[] args) {

       /* double input[][] =
                {
                        {0.0,0.0},
                        {1.0,0.0},
                        {0.0,1.0},
                        {1.0,1.0}};

        double output[][] =
                {
                        {0.0},
                        {1.0},
                        {1.0},
                        {0.0}};

        System.out.println("Learn:");

        JeffNetwork network = new JeffNetwork(2,6,1,0.7,0.9);



        double testInput[] = {1.0,0.0};

        System.out.println("Test output: " + Arrays.toString(network.computeOutputs(testInput)));

        Gson gson = new Gson();
        String json = gson.toJson(network);

        try {
            System.out.println("Saving network to disk...");
            FileWriter writer = new FileWriter("src/main/resources/models/test.json");
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Retrieving network from disk...");

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("src/main/resources/models/test.json"));
            JeffNetwork savedNetwork = gson.fromJson(br, JeffNetwork.class);

            System.out.println("Test output (from json): " + Arrays.toString(savedNetwork.computeOutputs(testInput)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/


    }

    @Override
    public void load(double[][] input, double[][] output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void construct(int inputCount, int outputCount) {

        double learnRate = 0.7;
        double momentum = 0.9;
        int hiddenCount = 6;

        this.learnRate = learnRate;
        this.momentum = momentum;

        this.inputCount = inputCount;
        this.hiddenCount = hiddenCount;
        this.outputCount = outputCount;
        neuronCount = inputCount + hiddenCount + outputCount;
        weightCount = (inputCount * hiddenCount) + (hiddenCount * outputCount);

        fire    = new double[neuronCount];
        matrix   = new double[weightCount];
        matrixDelta = new double[weightCount];
        thresholds = new double[neuronCount];
        errorDelta = new double[neuronCount];
        error    = new double[neuronCount];
        accThresholdDelta = new double[neuronCount];
        accMatrixDelta = new double[weightCount];
        thresholdDelta = new double[neuronCount];

        reset();

    }



    @Override
    public void learn(){

        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(4);


        for (int i=0;i<10000;i++) {
            for (int j=0;j<input.length;j++) {
                computeOutputs(input[j]);
                calcError(output[j]);
                backProp();
            }
            System.out.println( "Trial #" + i + ",Error:" +
                    percentFormat .format(getError(input.length)));
        }

        System.out.println("Recall:");

        for (int i=0;i<input.length;i++) {

            for (int j=0;j<input[0].length;j++) {
                System.out.print( input[i][j] +":" );
            }

            double out[] = computeOutputs(input[i]);
            System.out.println("="+out[0]);
        }

    }

    @Override
    public double[] ask(double input[]) {
        int i, j;
        final int hiddenIndex = inputCount;
        final int outIndex = inputCount + hiddenCount;

        for (i = 0; i < inputCount; i++) {
            fire[i] = input[i];
        }

        // first layer
        int inx = 0;

        for (i = hiddenIndex; i < outIndex; i++) {
            double sum = thresholds[i];

            for (j = 0; j < inputCount; j++) {
                sum += fire[j] * matrix[inx++];
            }
            fire[i] = threshold(sum);
        }

        // hidden layer

        double result[] = new double[outputCount];

        for (i = outIndex; i < neuronCount; i++) {
            double sum = thresholds[i];

            for (j = hiddenIndex; j < outIndex; j++) {
                sum += fire[j] * matrix[inx++];
            }
            fire[i] = threshold(sum);
            result[i-outIndex] = fire[i];
        }

        return result;
    }

    /**
     * Reset the weight matrix and the thresholds.
     */
    @Override
    public void reset() {
        int i;

        for (i = 0; i < neuronCount; i++) {
            thresholds[i] = 0.5 - (Math.random());
            thresholdDelta[i] = 0;
            accThresholdDelta[i] = 0;
        }
        for (i = 0; i < matrix.length; i++) {
            matrix[i] = 0.5 - (Math.random());
            matrixDelta[i] = 0;
            accMatrixDelta[i] = 0;
        }
    }


    /**
     * Modify the weight matrix and thresholds based on the last call to
     * calcError.
     */

    public void backProp() {
        int i;

        // process the matrix
        for (i = 0; i < matrix.length; i++) {
            matrixDelta[i] = (learnRate * accMatrixDelta[i]) + (momentum * matrixDelta[i]);
            matrix[i] += matrixDelta[i];
            accMatrixDelta[i] = 0;
        }

        // process the thresholds
        for (i = inputCount; i < neuronCount; i++) {
            thresholdDelta[i] = learnRate * accThresholdDelta[i] + (momentum * thresholdDelta[i]);
            thresholds[i] += thresholdDelta[i];
            accThresholdDelta[i] = 0;
        }
    }

    /**
     * Returns the root mean square error for a complet training set.
     *
     * @param len The length of a complete training set.
     * @return The current error for the neural network.
     */
    public double getError(int len) {
        double err = Math.sqrt(globalError / (len * outputCount));
        globalError = 0; // clear the accumulator
        return err;

    }

    /**
     * The threshold method. You may wish to override this class to provide other
     * threshold methods.
     *
     * @param sum The activation from the neuron.
     * @return The activation applied to the threshold method.
     */
    public double threshold(double sum) {
        return 1.0 / (1 + Math.exp(-1.0 * sum));
    }

    /**
     * Compute the output for a given input to the neural network.
     *
     * @param input The input provide to the neural network.
     * @return The results from the output neurons.
     */
    public double[] computeOutputs(double input[]) {

        int i, j;
        final int hiddenIndex = inputCount;
        final int outIndex = inputCount + hiddenCount;

        for (i = 0; i < inputCount; i++) {
            fire[i] = input[i];
        }

        // first layer
        int inx = 0;

        for (i = hiddenIndex; i < outIndex; i++) {
            double sum = thresholds[i];

            for (j = 0; j < inputCount; j++) {
                sum += fire[j] * matrix[inx++];
            }
            fire[i] = threshold(sum);
        }

        // hidden layer

        double result[] = new double[outputCount];

        for (i = outIndex; i < neuronCount; i++) {
            double sum = thresholds[i];

            for (j = hiddenIndex; j < outIndex; j++) {
                sum += fire[j] * matrix[inx++];
            }
            fire[i] = threshold(sum);
            result[i-outIndex] = fire[i];
        }

        return result;

    }


    /**
     * Calculate the error for the recogntion just done.
     *
     * @param ideal What the output neurons should have yielded.
     */
    public void calcError(double ideal[]) {
        int i, j;
        final int hiddenIndex = inputCount;
        final int outputIndex = inputCount + hiddenCount;

        // clear hidden layer errors
        for (i = inputCount; i < neuronCount; i++) {
            error[i] = 0;
        }

        // layer errors and deltas for output layer
        for (i = outputIndex; i < neuronCount; i++) {
            error[i] = ideal[i - outputIndex] - fire[i];
            globalError += error[i] * error[i];
            errorDelta[i] = error[i] * fire[i] * (1 - fire[i]);
        }

        // hidden layer errors
        int winx = inputCount * hiddenCount;

        for (i = outputIndex; i < neuronCount; i++) {
            for (j = hiddenIndex; j < outputIndex; j++) {
                accMatrixDelta[winx] += errorDelta[i] * fire[j];
                error[j] += matrix[winx] * errorDelta[i];
                winx++;
            }
            accThresholdDelta[i] += errorDelta[i];
        }

        // hidden layer deltas
        for (i = hiddenIndex; i < outputIndex; i++) {
            errorDelta[i] = error[i] * fire[i] * (1 - fire[i]);
        }

        // input layer errors
        winx = 0; // offset into weight array
        for (i = hiddenIndex; i < outputIndex; i++) {
            for (j = 0; j < hiddenIndex; j++) {
                accMatrixDelta[winx] += errorDelta[i] * fire[j];
                error[j] += matrix[winx] * errorDelta[i];
                winx++;
            }
            accThresholdDelta[i] += errorDelta[i];
        }
    }
}