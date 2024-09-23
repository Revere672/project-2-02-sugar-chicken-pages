psql -c '\copy Order_Items from \"order_items.csv\" CSV HEADER' -h csce-315-db.engr.tamu.edu -U csce331_02 -d csce331_02

psql -c '\copy Order_History from \"order_history.csv\" CSV HEADER' -h csce-315-db.engr.tamu.edu -U csce331_02 -d csce331_02