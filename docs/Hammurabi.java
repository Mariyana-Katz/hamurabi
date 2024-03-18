package hammurabi.docs; // package declaration

import java.util.InputMismatchException;
import java.util.OptionalInt;
import java.util.Random;         // imports go here
import java.util.Scanner;

public class Hammurabi {         // must save in a file named Hammurabi.java
    Random rand = new Random();  // this is an instance variable
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) { // required in every Java program
        new Hammurabi().playGame();
    }

    public void playGame() {
        int price = 0;
        int bushels = 2800;
        int acresOwned = 1000;
        int population = 100;
        boolean isUprising = false;
        int numberStarveToDeath = 0;
        System.out.println("Welcome to the Hammurabi game!");

        for(int year= 1; year<=10; year++){
           // Get random number for the price each year.
            price = newCostOfLand();
            int acresToBuy = askHowManyAcresToBuy(price, bushels);
            if(acresToBuy>0){
                bushels = bushels - acresToBuy * price;
                acresOwned = acresOwned + acresToBuy;
            }
            else {
               int acresToSell =  askHowManyAcresToSell(acresOwned);
               bushels = bushels + acresToSell* price;
               acresOwned = acresOwned - acresToSell;

            }
           int bushelsToFeed =  askHowMuchGrainToFeedPeople( bushels);
            bushels = bushels - bushelsToFeed;


           int acresToPlant =  askHowManyAcresToPlant(acresOwned,  population, bushels);
           bushels = bushels - acresToPlant*2;

           numberStarveToDeath = starvationDeaths(population, bushelsToFeed);

           isUprising = uprising(population, numberStarveToDeath);
           if(isUprising){
               break;
           }
           int numImmigrants = immigrants(population,acresOwned,bushels);
           population = population + numImmigrants;

           int yield = rand.nextInt(6) +1;
           int harvestAmount = harvest(acresOwned, yield);
           bushels = bushels + harvestAmount;

           printSummary(year, numberStarveToDeath, numImmigrants, population, harvestAmount,yield, acresOwned, price);
        }

        //final summary
        if(isUprising){ //the game exited earlier
            System.out.println("Hammurabi, this is not a job for you. You starved " +
                    numberStarveToDeath + " people in one year! Good buy!");
        }
        else { //finished the game
            System.out.println("Congratulations, you finished your ten- year term of office! " +
                    "You are so great! Come back again!");
        }
    }

    public void printSummary(int year, int numberStarveToDeath, int numImmigrants, int population,
                        int harvestAmount, int yield, int acresOwned, int price){

        System.out.println(" great Hammurabi!\n" +
                " You are in year " + year + " of your 10 year rule.\n" +
                " In the previous year " + numberStarveToDeath + " people starved to death. \n" +
                "In the previous year " + numImmigrants + "  people enter the kingdom." + " " + "\n " +
                "The population is now " + population + "\n " +
                "We harvest "  +  harvestAmount  + " bushels at " + yield + " bushels per acre. \n" +
                //" Rats destroyed 2000 bushels, leaving 2800 bushels in storage. \n " +
                "The city owned "  + acresOwned + " acres of land. \n" +
                " Land is currently worth " + price + " bushels per acre.\n ");


    }

    //Asks the player how many acres of land to buy, and returns that number.
    // You must have enough grain to pay for your purchase.
    public int askHowManyAcresToBuy(int price, int bushels) {
        int maxAcres = bushels/price;
        int acres;
        do {
             acres = getNumber("How many Acres of land to buy?");
             if(acres > maxAcres){
                 System.out.println("You don't have enough bushels to buy");
             }
             if(acres<= 0){
              return 0;
             }
        } while(acres > maxAcres );

        return acres;
    }

    // Asks the player how many acres of land to sell, and returns that number. You can't sell more than you have.
    //Do not ask this question if the player is buying lEmployee[] staff = new Employee[...];
    //staff[0] = new Employee(...)and; it doesn't make sense to do both in one turn.
    public int askHowManyAcresToSell(int acresOwned) {
        int acres;
        do {
             acres = getNumber("How many acres of land to sell?");
            if (acres > acresOwned) {
                System.out.println("You can not sell.");
              }
             if(acres<=0){
                return 0;
             }
        }  while (acres> acresOwned);
        return acres;
    }
    //Ask the player how much grain to feed people, and returns that number.
    //You can't feed them more grain than you have. You can feed them more than they need to survive.
    public int askHowMuchGrainToFeedPeople(int bushels) {
        int bushelsToFeed;
        do{
             bushelsToFeed = getNumber("How much grain to feed people? (Up to " + bushels + ")");
             if(bushelsToFeed> bushels) {
                 System.out.println("You do not have enough grain to feed the people");
             }
            if(bushelsToFeed<=0){
                return 0;
            }
        } while(bushelsToFeed> bushels);
        return bushelsToFeed;
    }

    // Ask the player how many acres to plant with grain, and returns that number.
    // You must have enough acres, enough grain, and enough people to do the planting.
    // Any grain left over goes into storage for next year.
    public int askHowManyAcresToPlant(int acresOwned, int population, int bushels) {
        int acresToPlant;
        do {
            acresToPlant = getNumber("How many acres to plant with grain?");
            if(acresToPlant<= 0)
                return 0;
            if(acresToPlant> acresOwned){
                System.out.println("You only own " + acresOwned);
            }
            if(acresOwned> bushels/2){
                System.out.println("You do not have enough bushels of grain - " + bushels);
            }
            if(acresToPlant > population){
                System.out.println("You do not have enough people - " + population);
            }

        } while (acresToPlant> acresOwned || acresToPlant > bushels / 2 || acresToPlant > population * 10);
        return acresToPlant;
    }

//    public int plagueDeaths(int i) {
//        return 0;
//    }


    //        Each person needs 20 bushels of grain to survive. If you feed them more than this, they are happy, but the grain is still gone.
//        You don't get any benefit from having happy subjects. Return the number of deaths from starvation (possibly zero).

    public int starvationDeaths(int population, int bushelsFedToPeople) {
        int starvationDeaths = 0;
        int fullPeople = bushelsFedToPeople/20; //Show how many are not starved.

        if(population > fullPeople){
            starvationDeaths = population - fullPeople;
        };
        return starvationDeaths;
    }
    // Return true if more than 45% of the people starve.
    // (This will cause you to be immediately thrown out of office, ending the game.)

    public boolean uprising(int population, int howManyPeopleStarved) {

        if(howManyPeopleStarved > 0.45* population){
            return true;
        }
        else {
            return false;
        }
    }

    public int immigrants(int population, int acresOwned, int grainInStorage){
        return (20 * acresOwned + grainInStorage)/ (100 * population) + 1;
    }

    // Choose a random integer between 1 and 6, inclusive.
    // Each acre that was planted with seed will yield this many bushels of grain.
    // (Example: if you planted 50 acres, and your number is 3, you harvest 150 bushels of grain).
    // Return the number of bushels harvested.
    public int harvest(int acres, int bushelsUsedAsSeed) {

        return acres * bushelsUsedAsSeed;
    }

    // There is a 40% chance that you will have a rat infestation. When this happens, rats will eat somewhere between 10% and 30% of your grain.
    // Return the amount of grain eaten by rats (possibly zero).
    public int grainEatenByRats(int i) {
        return 0;
    }
    // The price of land is random, and ranges from 17 to 23 bushels per acre.
    // Return the new price for the next set of decisions the player has to make.
    // (The player will need this information in order to buy or sell land.)

    public int newCostOfLand() {
        int min = 17;
        int max = 23;
        return rand.nextInt(max - min + 1) + min;
    }
    /**
     * Prints the given message (which should ask the user for some integral
     * quantity), and returns the number entered by the user. If the user's
     * response isn't an integer, the question is repeated until the user
     * does give a integer response.
     *
     * @param message The request to present to the user.
     * @return The user's numeric response.
     */
    public int getNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                return scanner.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    }


}

