import java.util.Random;

public class DNA {
    public final static String PATH = "file1.txt";
    private TransportData suppliers = new TransportData(PATH, "Suppliers");
    private TransportData deliveries = new TransportData(PATH, "Delivers");
    private TransportMatrixData costMatrix = new TransportMatrixData(PATH, "CostMatrix", deliveries.length(), suppliers.length());

    int[] dostawy = suppliers.getData();
    int[] odbiory = deliveries.getData();
    int[][] koszty = costMatrix.getData();
    int[][] genes = new int[odbiory.length][dostawy.length];

    int fitness;
    float mutationRate = (float) 0.03;

    //DNA (chromosom) służy do inicjalizacji początkowych tabel dostaw czyli genów
    DNA() {
        Random generator = new Random();
        int[] tempOdbiory = odbiory.clone();
        int[] tempDostawy = dostawy.clone();
        for (int i=0; i<odbiory.length; i++) {
            for( int j=0; j<dostawy.length; j++) {
                int bound = Math.min(tempOdbiory[i], tempDostawy[j]);
                genes[i][j] = generator.nextInt(bound);
                tempDostawy[j] = tempDostawy[j] - genes[i][j];
                tempOdbiory[i] = tempOdbiory[i] - genes[i][j];
            }
        }

        //Uzupełnienie dostaw
        for (int i=dostawy.length-1; i>=0; i--) {
            for( int j=0; j<odbiory.length; j++) {
                int value = Math.min(tempOdbiory[j], tempDostawy[i]);
                genes[j][i] += value;
                tempDostawy[i] = Math.max(0, tempDostawy[i] - value);
                tempOdbiory[j] = Math.max(0, tempOdbiory[j] - value);
            }
        }


    }

    void fitness() {
        int kosztyCalkowite = 0;
        for (int i = 0; i < odbiory.length ; i++) {
            for (int j = 0; j < dostawy.length; j++) {
                kosztyCalkowite = kosztyCalkowite + (genes[i][j] * koszty[i][j]);
            }
        }
        fitness = kosztyCalkowite;

    }

    DNA crossover(DNA partner) {
        DNA child = new DNA();
        Random generator = new Random();
        int midpoint = (int)(generator.nextInt(odbiory.length-2) + 1);

        ////////////////////////////////////////////////////////////
        //Tylko 2 geny od partnera
        for( int i=0; i<odbiory.length; i++) {
            if(i<midpoint) {
                for (int j = 0; j < dostawy.length; j++) {
                    child.genes[i][j] = genes[i][j];
                }
            } else if (i >= midpoint && i < midpoint + 2 ) {
                for (int j = 0; j < dostawy.length ; j++) {
                    child.genes[i][j] = partner.genes[i][j];
                }
            } else {
                for (int j = 0; j < dostawy.length; j++) {
                    child.genes[i][j] = genes[i][j];
                }
            }
        }
        /////////////////////!!!!!!!!!!!!!!!!!//////////////////////
        ////////////////////naprawianie genu///////////////////////
        //Ma polegać na przerzucaniu towaru o jedną jednostkę do kolejnego
        //  wiersza, gdy suma w danej kolumnie jest za duża
        int[] sumCol = child.ColSums();
        int[] tempDostawy = child.dostawy.clone();
        int k = 0;
        while(k<tempDostawy.length) {
            for (int j = 0; j < tempDostawy.length; j++) {
                while (sumCol[j] > tempDostawy[j]) {
                    int temp = 1;//sumCol[j] - tempDostawy[j];
                    if(child.genes[midpoint][j]==0){
                        //Sprawdzenie czy nie jest to ostatni gen
                        if(child.genes[midpoint+1][j]>0) {
                            child.genes[midpoint + 1][j] -= temp;
                            if (j == tempDostawy.length - 1) {
                                child.genes[midpoint + 1][0] += temp;
                            } else {
                                child.genes[midpoint + 1][j + 1] += temp;
                                // System.out.println("....");
                            }
                        } else {
                            break;
                        }
                    } else {
                        child.genes[midpoint][j] -= temp;
                        if (j == tempDostawy.length - 1) {
                            child.genes[midpoint][0] += temp;
                        } else {
                            child.genes[midpoint][j + 1] += temp;
                        }
                    }
                    sumCol = child.ColSums();
                }
            }
            k++;
        }
        return child;
    }

    void mutate() {
        Random generator = new Random();
        if(Math.random() < mutationRate) {
            int gen = generator.nextInt(odbiory.length);
            int komorka = generator.nextInt(dostawy.length);
            for (int j = 0; j < dostawy.length - 1 ; j++) {
                int temp = genes[gen][j];
                genes[gen][j] = genes[gen][j+1];
                genes[gen][j+1] = temp;
            }
        }

    }

    public void PrintGen() {
        for(int i = 0; i < genes.length; i++) {
            int sum = 0;
            for(int j = 0; j < genes[i].length; j++) {
                sum += genes[i][j];
                System.out.print(genes[i][j] + " ");
            }
            System.out.print(" -> " + sum + " [" + odbiory[i] + "]");
            System.out.print('\n');
        }
    }

    public int[] ColSums() {
        int[] ret = new int[dostawy.length];
        for (int i=0; i<dostawy.length; i++) {
            int sum = 0;
            for( int j=0; j<odbiory.length; j++) {
                sum += genes[j][i];
            }
            ret[i] = sum;
        }
        return ret;
    }


}
