package pos.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PosMachine {
    private List<ItemInfo> itemInfoList = ItemDataLoader.loadAllItemInfos();

    public String printReceipt(List<String> barcodes) {
        List<ItemInfo> items = convertBarcodeToItems(barcodes);
        Map<String, Integer> itemQuantities = getItemQuantity(items);

        System.out.println();

        return calculateReceipt(itemQuantities);
    }

    public List<ItemInfo> convertBarcodeToItems (List<String> barcodes) {
        List<ItemInfo> itemsWithDetails = new ArrayList<>();

        for (String barcode : barcodes) {
            for (ItemInfo itemInfo : itemInfoList) {
                if (barcode.equals(itemInfo.getBarcode())) {
                    itemsWithDetails.add(itemInfo);
                    break;
                }
            }
        }

        return itemsWithDetails;
    }

    public Map<String, Integer> getItemQuantity (List<ItemInfo> itemsWithDetails) {
        Map<String, Integer> itemQuantityMap = new HashMap<>();

        for(ItemInfo itemInfo : itemsWithDetails){
            if(itemQuantityMap.containsKey(itemInfo.getName())){
                itemQuantityMap.replace(itemInfo.getName(), itemQuantityMap.get(itemInfo.getName()) + 1);
            }else{
                itemQuantityMap.put(itemInfo.getName(), 1);
            }
        }

        return itemQuantityMap;
    }

    public String calculateReceipt (Map<String, Integer> itemQuantityMap) {
        int subTotal, total=0;
        StringBuilder allItemString = new StringBuilder();
        String receipt = "";

        for(ItemInfo itemInfo : itemInfoList){
            subTotal = itemInfo.getPrice() * itemQuantityMap.get(itemInfo.getName());
            String itemString = "Name: " + itemInfo.getName() + ", Quantity: " + itemQuantityMap.get(itemInfo.getName()) +
                    ", Unit price: " + itemInfo.getPrice() + " (yuan), Subtotal: " + subTotal + " (yuan)\n";

            allItemString.append(itemString);
            total += subTotal;
        }

        receipt = generateReceipt(total, allItemString.toString());

        return receipt;
    }

    private String generateReceipt(int total, String allItemString) {
        String header = "***<store earning no money>Receipt***\n";
        String divider = "----------------------\n";
        String footer = "**********************";
        return header + allItemString + divider + "Total: " + total + " (yuan)\n" + footer;
    }
}
