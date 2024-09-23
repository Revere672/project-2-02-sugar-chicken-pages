import csv
import random
import datetime

with open("order_items.csv", "w", newline="", encoding="utf-8") as item_file:
    item_writer = csv.writer(item_file)
    item_field = ["Order_ID", "Item_Type", "Side_1", "Side_2", "Protein_1", "Protein_2", "Protein_3", "Misc_Item", "Item_Cost", "Item_Number"]
    item_writer.writerow(item_field)

    options_reader = open("panda_orders.csv", "r")
    option_list = []
    for row in options_reader:
        option_list.append(row)

    history_writer = csv.writer(open("order_history.csv", "w", newline="", encoding="utf-8"))
    history_field = ["Order_ID", "Order_Time", "Total_Price", "Employee_ID"]
    history_writer.writerow(history_field)

    total_income = 0
    order_time = datetime.datetime(2024, 1, 1)
    for order_ID in range(120000):
        total_cost = 0
        num_items = random.randint(1, 2)
        for item_num in range(1, num_items+1):
            option_num = random.randint(1, len(option_list)-1)
            item_list = option_list[option_num].split(",")
            item_list.insert(0, order_ID)
            item_list.append(item_num)
            item_list[8] = item_list[8].replace("\n", "")
            item_writer.writerow(item_list)
            total_cost += float(item_list[8])
        history_list = [order_ID, order_time, total_cost, "383493"]
        history_writer.writerow(history_list)

        total_income += total_cost

        time_change = datetime.timedelta(seconds=270+random.randint(-20,20))
        order_time = order_time + time_change