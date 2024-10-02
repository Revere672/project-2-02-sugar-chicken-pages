

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
            if (strs.length>2) protien1=strs[2];
            if (strs.length>3) protien2=strs[3];
            if (strs.length>4) side2=strs[4];
            if (strs.length>5) protien3=strs[5];
            if (strs.length>6) miscItem=strs[6];
            this.price=price;
        }

        public String getPrice() {
            String text=DisplayReceipt.df.format(price) +"\n";
            if(orderType.equals("other")){
                return text;
            }

            if(side1!=null){
                if(DisplayReceipt.extraCostName.contains(side1)){
                    text+="+"+DisplayReceipt.df.format(DisplayReceipt.extraCostPrice.get(DisplayReceipt.extraCostName.indexOf(side1)))+"\n";
                }
                else text+="+$0.00\n";
            }
            if(side2!=null){
                if(DisplayReceipt.extraCostName.contains(side2)){
                    text+="+"+DisplayReceipt.df.format(DisplayReceipt.extraCostPrice.get(DisplayReceipt.extraCostName.indexOf(side2)))+"\n";
                }
                else text+="+$0.00\n";
            }
            if(protien1!=null){
                if(DisplayReceipt.extraCostName.contains(protien1)){
                    text+="+"+DisplayReceipt.df.format(DisplayReceipt.extraCostPrice.get(DisplayReceipt.extraCostName.indexOf(protien1)))+"\n";
                }
                else text+="+$0.00\n";
            }
            if(protien2!=null){
                if(DisplayReceipt.extraCostName.contains(protien2)){
                    text+="+"+DisplayReceipt.df.format(DisplayReceipt.extraCostPrice.get(DisplayReceipt.extraCostName.indexOf(protien2)))+"\n";
                }
                else text+="+$0.00\n";
            }
            if(protien3!=null){
                if(DisplayReceipt.extraCostName.contains(protien3)){
                    text+="+"+DisplayReceipt.df.format(DisplayReceipt.extraCostPrice.get(DisplayReceipt.extraCostName.indexOf(protien3)))+"\n";
                }
                else text+="+$0.00\n";
            }
            if(miscItem!=null){
                if(DisplayReceipt.extraCostName.contains(miscItem)){
                    text+="+"+DisplayReceipt.df.format(DisplayReceipt.extraCostPrice.get(DisplayReceipt.extraCostName.indexOf(miscItem)))+"\n";
                }
                else text+="+$0.00\n";
            }
            return text;
        }

        public String formatedString(){
            if(orderType.equals("other")){
                return miscItem+"\n";
            }

            String text=orderType+"\n";

            if(side1!=null){
                text+="    "+side1+"\n";
            }
            if(side2!=null){
                text+="    "+side2+"\n";
            }
            if(protien1!=null){
                text+="    "+protien1+"\n";
            }
            if(protien2!=null){
                text+="    "+protien2+"\n";
            }
            if(protien3!=null){
                text+="    "+protien3+"\n";
            }
            if(miscItem!=null){
                text+="    "+miscItem+"\n";
            }

            return text;
        }
}
