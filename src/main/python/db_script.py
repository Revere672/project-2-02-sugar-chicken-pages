import csv
import random
import datetime

with open("./resources/csv/order_items.csv", "w", newline="", encoding="utf-8") as item_file:
    item_writer = csv.writer(item_file)
    item_field = ["Order_ID", "Item_Number", "Item_Type", "Side_1", "Side_2", "Protein_1", "Protein_2", "Protein_3", "Misc_Item", "Item_Cost"]
    item_writer.writerow(item_field)

    options_reader = open("./resources/csv/panda_orders.csv", "r")
    option_list = []
    for row in options_reader:
        option_list.append(row)

    history_writer = csv.writer(open("./resources/csv/order_history.csv", "w", newline="", encoding="utf-8"))
    history_field = ["Order_ID", "Order_Time", "Total_Price", "Employee_ID"]
    history_writer.writerow(history_field)

    
    
    total_income = 0
    order_time = datetime.datetime(2023, 8, 1)
    order_ID = 0

    while(order_time < datetime.datetime.now()):
        total_cost = 0
        num_items = random.randint(1, 2)


        for item_num in range(1, num_items+1):
            option_num = random.randint(1, len(option_list)-1)
            item_list = option_list[option_num].split(",")
            item_list.insert(0, order_ID)
            item_list.insert(1, item_num)
            item_list[9] = item_list[9].replace("\n", "")
            item_writer.writerow(item_list)
            total_cost += float(item_list[9])
        
        history_list = [order_ID, order_time, total_cost, "100001"]
        history_writer.writerow(history_list)

        total_income += total_cost

        if((order_time > datetime.datetime(2024, 8, 20) and order_time < datetime.datetime(2024, 8, 21)) or (order_time > datetime.datetime(2024, 1, 12) and order_time < datetime.datetime(2024, 1, 13))):
            time_change = datetime.timedelta(seconds=200+random.randint(-20,20))
            order_time = order_time + time_change
        
        else:
            time_change = datetime.timedelta(seconds=270+random.randint(-20,20))
            order_time = order_time + time_change

        order_ID += 1

    item_file.close()
    options_reader.close()

print("Data Generated")