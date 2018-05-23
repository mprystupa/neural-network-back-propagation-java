package main.java.NeuralNetwork;

import main.java.Neuron.LinearNeuron;
import main.java.Neuron.Neuron;
import main.java.Neuron.SigmoidalNeuron;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NeuralNetwork {

    private List<NeuronLayer> layers;

    private int layersAmount;

    private List<List<Float>> inputData;
    private List<List<Float>> targetData;

    public NeuralNetwork() {
        this.layers = new ArrayList<>();
        this.inputData = new ArrayList<>();
        this.targetData = new ArrayList<>();

        this.layersAmount = 0;
    }

    public void createNewNetwork(List<List<Float>> inputSet, List<List<Float>> targetSet, Float learningRate, Float beta) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("How much layers?");
        int layersAmount = scanner.nextInt();
        for(int i = 0; i < layersAmount - 1; i++) {
            System.out.println();
            System.out.println("How much neurons in layer #" + (i + 1) + "?");
            int neuronsAmount = scanner.nextInt();

            List<Neuron> tempNeurons = new ArrayList<>();
            for(int j = 0; j < neuronsAmount; j++) {
                tempNeurons.add(new SigmoidalNeuron(learningRate, beta));
                if (i == 0)
                    tempNeurons.get(j).initialiseWeights(inputSet.get(0).size());
                else
                    tempNeurons.get(j).initialiseWeights(layers.get(i - 1).getNeuronAmount());
            }
            this.addLayer(tempNeurons);
        }
        //Add final linear neurons
        System.out.println();
        System.out.println("How much neurons in layer #" + layersAmount + "?");
        int neuronsAmount = scanner.nextInt();

        List<Neuron> tempNeurons = new ArrayList<>();
        for(int j = 0; j < neuronsAmount; j++) {
            tempNeurons.add(new LinearNeuron(learningRate));
            tempNeurons.get(j).initialiseWeights(layers.get(layers.size() - 1).getNeuronAmount());
        }
        this.addLayer(tempNeurons);

        this.setData(inputSet, targetSet);
    }


    public void addLayer(List<Neuron> neurons) {
        if(layers.size() == 0) {
            layers.add(new NeuronLayer(neurons, null, null));
        } else {
            layers.add(layers.size(), new NeuronLayer(neurons, layers.get(layers.size() - 1), null));
            //Ustaw dodana warstwe jako nastepna po poprzedniej warstwie
            layers.get(layers.size() - 2).setNextLayer(layers.get(layers.size() - 1));
        }
    }

    public void testNetwork() {
        for (NeuronLayer layer : this.getLayers()) {
            System.out.println("Previous layer: " + layer.getPrevLayer());
            System.out.println("Current layer: " + layer);
            System.out.println("Next layer: " + layer.getNextLayer());
            System.out.println();
            System.out.println("Input amount: " + layer.getNumberOfInputs());
            System.out.println("Output amount: " + layer.getNumberOfOutputs());
            System.out.println();
        }
    }

    public void setData(List<List<Float>> inputData, List<List<Float>> targetData) {
        this.inputData = inputData;
        this.targetData = targetData;

        //Inicjalizacja ilosci wejsc dla warstwy wejsciowej
        layers.get(0).setNumberOfInputs(inputData.get(0).size());
    }

    public NeuronLayer getOutputLayer() {
        return this.getLayers().get(this.getLayers().size() - 1);
    }

    public List<NeuronLayer> getLayers() {
        return layers;
    }

    public List<List<Float>> getInputData() {
        return inputData;
    }

    public List<List<Float>> getTargetData() {
        return targetData;
    }
}
