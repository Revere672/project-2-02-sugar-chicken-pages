/**
 * Functional interface for running a function on an entry.
 */
interface EntryFunction {
    void run(Object str, String data);
}

/**
 * A class representing an entry in an order.
 */
public class Entry {

    String orderType;
    String side1 = null;
    String protien1 = null;
    String side2 = null;
    String protien2 = null;
    String protien3 = null;
    String miscItem = null;
    double price = 0;

    /**
     * Constructs an Entry with the specified order type, miscellaneous item, and price.
     *
     * @param orderType the type of the order
     * @param miscItem the miscellaneous item
     * @param price the price of the entry
     */
    public Entry(String orderType, String miscItem, double price) {
        this.orderType = orderType;
        this.miscItem = miscItem;
        this.price = price;
    }

    /**
     * Constructs an Entry from an array of strings and a price.
     * The array should be in the order: [orderType, side1, protien1, protien2, side2, protien3, miscItem].
     *
     * @param strs the array of strings representing the entry details
     * @param price the price of the entry
     */
    public Entry(String[] strs, double price) {
        /*creates an entry from an array of strings in order [orderType,side1,protien1,protien2,side2,protien3,miscItem] this is because that is the most common way to order items. */
        orderType = strs[0];
        if (strs.length > 1) {
            side1 = strs[1];
        }
        if (strs.length > 2) {
            protien1 = strs[2];
        }
        if (strs.length > 3) {
            protien2 = strs[3];
        }
        if (strs.length > 4) {
            side2 = strs[4];
        }
        if (strs.length > 5) {
            protien3 = strs[5];
        }
        if (strs.length > 6) {
            miscItem = strs[6];
        }
        this.price = price;
    }

    /**
     * Checks if the entry has a half side.
     *
     * @return true if the entry has a half side, false otherwise
     */
    public boolean isHalfSide() {
        int sides = DisplayReceipt.numberOfSides.get(orderType);
        return sides == 1 && side2 != null;
    }

    /**
     * Adds a side to the entry.
     *
     * @param side the side to add
     * @return true if the side was added, false otherwise
     */
    public boolean addSide(String side) {
        if (DisplayReceipt.extraCostName.contains(side)) {
            int index = DisplayReceipt.extraCostName.indexOf(side);
            price += DisplayReceipt.extraCostPrice.get(index);
            DisplayReceipt.subtotal += DisplayReceipt.extraCostPrice.get(index);
        }
        if (side1 == null) {
            side1 = side;
            return true;
        }
        if (side2 == null) {
            side2 = side;
            return true;
        }
        return false;
    }

    /**
     * Removes a side from the entry.
     *
     * @param side the side to remove
     * @return true if the side was removed, false otherwise
     */
    public boolean removeSide(String side) {
        if (DisplayReceipt.extraCostName.contains(side)) {
            int index = DisplayReceipt.extraCostName.indexOf(side);
            price -= DisplayReceipt.extraCostPrice.get(index);
            DisplayReceipt.subtotal -= DisplayReceipt.extraCostPrice.get(index);
        }
        if (side1.equals(side)) {
            side1 = null;
            return true;
        }
        if (side2.equals(side)) {
            side2 = null;
            return true;
        }
        return false;
    }

    /**
     * Adds a protein to the entry.
     *
     * @param protein the protein to add
     * @return true if the protein was added, false otherwise
     */
    public boolean addProtein(String protein) {
        if (DisplayReceipt.extraCostName.contains(protein)) {
            int index = DisplayReceipt.extraCostName.indexOf(protein);
            price += DisplayReceipt.extraCostPrice.get(index);
            DisplayReceipt.subtotal += DisplayReceipt.extraCostPrice.get(index);
        }
        if (protien1 == null) {
            protien1 = protein;
            return true;
        }
        int count = DisplayReceipt.numberOfProteins.get(orderType);
        if (protien2 == null && count > 1) {
            protien2 = protein;
            return true;
        }
        if (protien3 == null && count > 2) {
            protien3 = protein;
            return true;
        }
        return false;
    }

    /**
     * Removes a protein from the entry.
     *
     * @param protein the protein to remove
     * @return true if the protein was removed, false otherwise
     */
    public boolean removeProtein(String protein) {
        if (DisplayReceipt.extraCostName.contains(protein)) {
            int index = DisplayReceipt.extraCostName.indexOf(protein);
            price -= DisplayReceipt.extraCostPrice.get(index);
            DisplayReceipt.subtotal -= DisplayReceipt.extraCostPrice.get(index);
        }
        if (protien1.equals(protein)) {
            protien1 = null;
            return true;
        }
        if (protien2 != null && protien2.equals(protein)) {
            protien2 = null;
            return true;
        }
        if (protien3 != null && protien3.equals(protein)) {
            protien3 = null;
            return true;
        }
        return false;
    }

    /**
     * Gets the price of the entry as a formatted string.
     *
     * @return the formatted price string
     */
    public String getPrice() {
        StringBuilder text = new StringBuilder(DisplayReceipt.df.format(price) + "\n");
        if (orderType.equals("other")) {
            return text.toString();
        }

        EntryFunction function = (in, data) -> {
            ((StringBuilder) in).append("+");
            ((StringBuilder) in).append(DisplayReceipt.df.format(DisplayReceipt.extraCostPrice.get(DisplayReceipt.extraCostName.indexOf(data))));
            ((StringBuilder) in).append("\n");
        };

        forEachNotNull(function, text);

        return text.toString();
    }

    /**
     * Gets the formatted string representation of the entry.
     *
     * @return the formatted string
     */
    public String formatedString() {
        if (orderType.equals("other")) {
            return miscItem + "\n";
        }

        StringBuilder text = new StringBuilder(orderType + "\n");
        EntryFunction function = (in, data) -> ((StringBuilder) in).append("    " + data + "\n");

        forEachNotNull(function, text);

        return text.toString();
    }

    /**
     * Updates the information of the entry for database insertion.
     *
     * @param orderId the order ID
     * @param orderNum the order number
     * @return the formatted string for database insertion
     */
    public String updateInfo(int orderId, int orderNum) {
        return "(" + orderId + "," + orderNum + ",'" + orderType + "','" + side1 + "','" + side2 + "','" + protien1 + "','" + protien2 + "','" + protien3 + "','" + miscItem + "'," + price + ")";
    }

    /**
     * Executes a function on each non-null field of the entry.
     *
     * @param function the function to execute
     * @param obj the object to pass to the function
     * @return the modified object
     */
    public Object forEachNotNull(EntryFunction function, Object obj) {
        if (side1 != null) {
            function.run(obj, side1);
        }
        if (side2 != null) {
            function.run(obj, side2);
        }
        if (protien1 != null) {
            function.run(obj, protien1);
        }
        if (protien2 != null) {
            function.run(obj, protien2);
        }
        if (protien3 != null) {
            function.run(obj, protien3);
        }
        if (miscItem != null) {
            function.run(obj, miscItem);
        }

        return obj;
    }
}
