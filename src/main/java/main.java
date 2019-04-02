import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class main {
    public static void main(String[] args) {
        DNA[] population = new DNA[60];
        for (int i = 0; i < population.length; i++) {
            population[i] = new DNA();

            //System.out.println("Population: " + i);
            //population[i].showGene();
            population[i].fitness();
            //System.out.println(Arrays.toString(population[i].ColSums()));
            //System.out.println();
            //population[i].PrintGen();
            System.out.println("Koszt osobnika: " + population[i].fitness);

        }
        int minimum = 999999999;
        for (int i = 0; i < population.length - 1; i++) {
            int x = Math.min(population[i].fitness, population[i + 1].fitness);
            if (x < minimum) {
                minimum = x;
            }
        }

        ///////////////////////GENERACJE//////////////////////////
        int generacje = 150;
        for (int k = 0; k < generacje; k++) {

            System.out.println("Generacja: " + (k + 1));
            //////////////////////////MATING POOL//////////////////////////////////
            ArrayList<DNA> matingPool = new ArrayList<DNA>();
            int kosztyPopulacji = 0;
            for (int i = 0; i < population.length; i++) {
                kosztyPopulacji += population[i].fitness;
            }
            for (int i = 0; i < population.length; i++) {
                int n = ((kosztyPopulacji / (population[i].fitness)) * 10);
                for (int j = 0; j < n; j++) {
                    matingPool.add(population[i]);
                }
            }


            //System.out.println(kosztyPopulacji);

            ///////////////////CROSSOVER//////////////////////////////////////////
            for (int i = 0; i < population.length; i++) {
                Random generator = new Random();
                int a = (int) (generator.nextInt(matingPool.size()));
                int b = (int) (generator.nextInt(matingPool.size()));
                DNA parentA = matingPool.get(a);
                DNA parentB = matingPool.get(b);

                DNA child = parentA.crossover(parentB);
                child.mutate();
                population[i] = child;
            }
            //////////////////////FITNESS////////////////////////////////////
            for (int i = 0; i < population.length; i++) {
                population[i].fitness();
            }

            //////////////////////TOTAL COST///////////////////////////////
            int kosztyPopulacji2 = 0;
            for (int i = 0; i < population.length; i++) {
                kosztyPopulacji2 += (int) population[i].fitness;
                int n = (int) ((kosztyPopulacji2 - population[i].fitness) / 10);
                for (int j = 0; j < n; j++) {
                    matingPool.add(population[i]);
                }
            }
            System.out.println(kosztyPopulacji2);
        }
        for (int i = 0; i < population.length; i++) {
            System.out.println("Koszt osobnika: " + population[i].fitness);
        }

        int min = 99999999;
        for (int i = 0; i < population.length - 1; i++) {
            int x = Math.min(population[i].fitness, population[i + 1].fitness);
            if (x < min) {
                min = x;
            }
        }




        System.out.println("\n Najmniejsze rozwiązanie na początku: " + minimum);
        System.out.println("\n Najmniejsze rozwiązanie na końcu:    " + min);

    }

    public static int min(DNA[] tab){
        int min = tab[0].fitness;
        for (int i = 0; i < tab.length - 1 ; i++) {
            if(tab[i].fitness < tab[i+1].fitness){
                min = tab[i].fitness;
            }
        }
        return min;
    }

}



