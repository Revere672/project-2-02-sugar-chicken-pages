

public class Entry {
    String orderType;
        String side1=null;
        String protien1=null;
        String side2=null;
        String protien2=null;
        String protien3=null;
        String miscItem=null;
        double price=0;

        public Entry(String orderType,String miscItem,double price){
            this.orderType=orderType;
            this.miscItem=miscItem;
            this.price=price;
        }

        public Entry(String[] strs,double price){
            /*creates an entry from an array of strings in order [orderType,side1,protien1,protien2,side2,protien3,miscItem] this is because that is the most common way to order items. */
            orderType=strs[0];
            if(strs.length>1) side1=strs[1];
            else if (strs.length>2) protien1=strs[1];
            else if (strs.length>2) protien2=strs[2];
            else if (strs.length>3) side2=strs[3];
            else if (strs.length>4) protien3=strs[4];
            else if (strs.length>5) miscItem=strs[5];
            this.price=price;
        }

        public String formatedString(){
            if(orderType.equals("other")){
                return miscItem;
            }

            return orderType;
        }
}
