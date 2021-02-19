import csv
import json

with open('champions.json') as json_file, open("champions_whole.csv", "w") as output:
    raw_data = json.load(json_file)
    for champion in raw_data["data"]:
        line = ""
        line += str(raw_data["data"][champion]["key"]) + ","
        line += str(raw_data["data"][champion]["name"]) + ","
        line += str(raw_data["data"][champion]["tags"][0]) + ","
        line += str(raw_data["data"][champion]["image"]["full"]) + ","
        line += str(raw_data["data"][champion]["info"]["attack"]) + ","
        line += str(raw_data["data"][champion]["info"]["defense"]) + ","
        line += str(raw_data["data"][champion]["info"]["magic"]) + ","
        line += str(raw_data["data"][champion]["info"]["difficulty"]) + "\n"

        output.write(line)
