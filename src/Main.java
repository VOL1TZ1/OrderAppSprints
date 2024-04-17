import java.util.Scanner;
public class Main {
    private static Menu userMenu;
    private static String pageDivider;
    private static Order userOrder;
    public static void main(String[] args) {
        /*Initializations*/
        userMenu = new Menu();
        //Page divider
        char character = '*';
        int repeatCount = 73;
        pageDivider = String.valueOf(character).repeat(repeatCount);
        boolean isNewOrder = true;
        boolean sessionEnded = false;
        char userChoice;
        /*Start of program*/
        System.out.print("\t\t\t\t\t\t\t\t\t\t");
        System.out.println("!!!! Hello and welcome to Sprints Ordering App in Java !!!!");
        while (!sessionEnded) {
            if(isNewOrder){
                userOrder = new Order();
                isNewOrder = false;
            }
            /************************** Main menu **************************/
            System.out.println(pageDivider);
            System.out.println("""
                    \t\t\t\t\t\t\tMain Menu\s
                    =========================================================================""");
            System.out.println("""
                    Let's start ordering. Please choose an option:
                    (1) Browse available menus manually
                    (2) Search for a specific dish
                    (e) No thanks, I am satisfied :)""");
            Scanner sc = new Scanner(System.in);
            userChoice = sc.next().charAt(0);
            switch (userChoice) {
                case '1':
                    browseOption();
                    break;
                case '2':
                    break;
                case 'e':
                    sessionEnded = true;
                    break;
                default:
                    System.out.println("Oops! Looks like you entered an invalid input. " +
                            "Please choose one of the numbers in parentheses or (e) to exit.");
            }
            if('e' != userChoice){
                System.out.println("Do you want to make a new order? (y/n)");
                userChoice = sc.next().toLowerCase().charAt(0);
                if('y' == userChoice){
                    isNewOrder = true;
                } else {
                    sessionEnded = true;
                }
            }
        }
    }
    private static void browseOption(){
        //local variables
        boolean stillBrowsing = true;
        //start of browsing loop
        while (stillBrowsing) {
            System.out.println("""
                    Choose one of the following menus to start browsing:\s
                    (1) Appetizers
                    (2) Main Course Dishes
                    (3) Desserts
                    (0) To return to the main menu""");
            Scanner sMenu = new Scanner(System.in);
            //keeping track of which menu to use
            int categoryNum = sMenu.nextInt();
            //a loop to make sure the user choose a valid menu number
            while (categoryNum > 3 || categoryNum < 0) {
                System.out.print("Oops! Invalid Input detected! Please enter one of the numbers between parentheses: ");
                categoryNum = sMenu.nextInt();
            }
            // to return to the main menu
            if (0 == categoryNum) {
                break;
            }
            /************************** Dishes menus **************************/
            System.out.println(pageDivider);
            switch (categoryNum) {
                case 1 -> System.out.println("\t\t\t\t\t\t\tAppetizers Menu");
                case 2 -> System.out.println("\t\t\t\t\t\t\tMain Course Menu");
                case 3 -> System.out.println("\t\t\t\t\t\t\tDesserts Menu");
            }
            //a method to show all the dishes in the menu
            userMenu.getDishMenu(categoryNum);
            System.out.print("Please enter dish number: ");
            //keeping track of dish number
            int dishNum = sMenu.nextInt();
            //a loop to make sure the user choose a valid dish number
            while (dishNum > 8 || dishNum < 1) {
                System.out.print("Invalid Input detected! Please enter a valid dish number: ");
                dishNum = sMenu.nextInt();
            }
            //get the dish the user ordered
            Dish userDish = userMenu.getDishFromMenu(categoryNum, dishNum - 1);
            if (0 == userDish.getNumInStock()) {
                System.out.println("Oops! The dish selected is \u001B[31mout of stock\u001B[0m. Try another dish.");
            } else {
                //show the dish info
                userMenu.getDishInfo(categoryNum, dishNum);
                System.out.print("do you like to add the dish to cart? (y/n): ");
                Scanner addChoice = new Scanner(System.in);
                char choice = addChoice.next().toLowerCase().charAt(0);
                switch (choice) {
                    case 'y':
                        userOrder.addToCart(userDish);
                        System.out.print("Do you want to keep browsing? (y/n): ");
                        char cartChoice = addChoice.next().toLowerCase().charAt(0);
                        if ('n' == cartChoice) {
                            stillBrowsing = false;
                        }
                        break;
                    case 'n':
                        break;
                    default: {
                        System.out.print("Invalid input detected! Please try again..");
                        break;
                    }
                }
            }
        }
        if(!stillBrowsing){
            /************************** Checkout menu **************************/
            System.out.println(pageDivider);
            System.out.println("""
                    \t\t\t\t\t\t\tCheckout Menu\s
                    =========================================================================""");
        }
        //checkout loop:
        while (!stillBrowsing) {
            userOrder.showCart();
            System.out.print("Do you want to make any changes to cart (y/n): ");
            Scanner addChoice = new Scanner(System.in);
            char choice = addChoice.next().toLowerCase().charAt(0);
            switch (choice) {
                case 'y':
                    boolean checkoutFlag = true;
                    //edit the cart loop:
                    while (checkoutFlag) {
                        System.out.println("--------------Cart Modifications--------------");
                        System.out.print("""
                                    (1) Modify item quantities
                                    (2) Remove item
                                    (3) Confirm order
                                    """);
                        Scanner cartChoice = new Scanner(System.in);
                        int cChoice = cartChoice.nextInt();
                        switch (cChoice) {
                            case 1: {
                                userOrder.increaseItemQuantities();
                                break;
                            }
                            case 2: {
                                userOrder.removeFromCart();
                                break;
                            }
                            case 3: {
                                checkoutFlag = false;
                                break;
                            }
                            default: {
                                System.out.print("Invalid Input detected! Please enter one of the numbers between parentheses: ");
                                break;
                            }
                        }
                    }
                case 'n':
                    stillBrowsing = true;
                    break;
                default:
                    System.out.print("Invalid input detected! Please try again..");
            }
            System.out.println("Would you like to add any special requirements? (y/n)");
            Scanner instructionsChoice = new Scanner(System.in);
            choice = instructionsChoice.next().toLowerCase().charAt(0);
            instructionsChoice.nextLine();
            if('y' == choice){
                System.out.println("Please enter instructions:");
                userOrder.setSpecialRequirements(instructionsChoice.nextLine());
            }
            System.out.println("Do you have a valid promo code? (y/n)");
            choice = instructionsChoice.next().toLowerCase().charAt(0);
            if('y' == choice){
                userOrder.applyCoupon();
            }
            userOrder.finalCart();
            /************************** Payment menu **************************/
            System.out.println(pageDivider);
            System.out.println("""
                    \t\t\t\t\t\t\tPayment Menu\s
                    =========================================================================""");
            userOrder.confirmPayment();
            System.out.println("Order confirmed successfully!");
        }
    }

    private static void searchOption(){

    }
}
